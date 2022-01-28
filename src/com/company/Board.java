package com.company;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class Board {
    private Tile[][] tilesMatrix;
    private Piece[][] piecesMatrix;
    final private Player player1, player2;
    final private double graphicPosX, graphicPosY, tileEdge, piecePadding;
    final private int horizontalTileAmount, verticalTileAmount;

    public Board(double graphicPosX, double graphicPosY, double tileEdge, double piecePadding, int horizontalTileAmount, int verticalTileAmount, GraphicsContext gc, Player player1, Player player2) {
        this.graphicPosX = graphicPosX;
        this.graphicPosY = graphicPosY;
        this.horizontalTileAmount = horizontalTileAmount;
        this.verticalTileAmount = verticalTileAmount;
        this.tileEdge = tileEdge;
        this.piecePadding = piecePadding;
        this.player1 = player1;
        this.player2 = player2;
        createTileMatrix(gc);
        createPieces(gc, player1, player2);
    }

    private void createTileMatrix(GraphicsContext gc) {
        tilesMatrix = new Tile[horizontalTileAmount][verticalTileAmount];
        for (int y = 0; y < tilesMatrix.length; y++) {
            for (int x = 0; x < tilesMatrix[y].length; x++) {
                tilesMatrix[y][x] = new Tile(graphicPosX + (x * tileEdge), graphicPosY + (y * tileEdge), tileEdge,tileEdge, false);
                drawTile(gc, y, x);
            }
        }
        drawBoarder(gc);
    }

    private void drawTile(GraphicsContext gc, int y, int x) {
        if((x + y) % 2 == 0) {
            tilesMatrix[y][x].setType(TileType.Light);
        } else {
            tilesMatrix[y][x].setType(TileType.Dark);
        }
        tilesMatrix[y][x].draw(gc);
    }

    private void createPieces(GraphicsContext gc, Player player1, Player player2) {
        piecesMatrix = new Piece[horizontalTileAmount][verticalTileAmount];
        for (int y = 0; y < piecesMatrix.length; y++) {
            for (int x = 0; x < piecesMatrix[y].length; x++) {
                if(((x + y) % 2 != 0) && y < 3) {
                    drawPieces(gc, player2, y, x, PieceType.WhiteDefault);
                }
                if(((x + y) % 2 != 0) && y > 4) {
                    drawPieces(gc, player1, y, x, PieceType.BlackDefault);
                }
            }
        }
    }

    private void drawPieces(GraphicsContext gc, Player player, int y, int x, PieceType type) {
        tilesMatrix[y][x].setOccupied(true);
        piecesMatrix[y][x] = new Piece(player, (tilesMatrix[y][x].getGraphicPosX() + piecePadding), (tilesMatrix[y][x].getGraphicPosY() + piecePadding), (tilesMatrix[y][x].getWidth() - piecePadding * 2));
        piecesMatrix[y][x].setType(type);
        piecesMatrix[y][x].draw(gc);
    }

    private void drawBoarder(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        gc.strokeRect(graphicPosX - ((double) 4 / 2), graphicPosY - ((double) 4 / 2), (horizontalTileAmount * tileEdge) + (double) 4, (verticalTileAmount * tileEdge) + (double) 4);
        gc.setFont(new Font("Arial", 13));
        gc.setFill(Color.BLACK);
        for(int i = 65, x = 0; i < 73; i++, x++){
            gc.fillText(String.valueOf((char) i), graphicPosX + (graphicPosX / 2) + (graphicPosX * x) - 3, 470);
            gc.fillText(String.valueOf(x + 1), graphicPosX - 20, graphicPosY - (graphicPosY / 2) + (graphicPosY * (73 - i)) + 3);
        }
    }

    private void drawMessage(GraphicsContext gc, String str, double x, double y) {
        gc.setFont(new Font("Arial", 22));
        gc.setFill(Color.BLACK);
        gc.fillText(str, x, y);
    }

    public void updateBoard(GraphicsContext gc, boolean turn, boolean strikeAvailable) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for (int y = 0; y < verticalTileAmount; y++) {
            for (int x = 0; x < horizontalTileAmount; x++) {
                tilesMatrix[y][x].draw(gc);
            }
        }
        for (int y = 0; y < verticalTileAmount; y++) {
            for (int x = 0; x < horizontalTileAmount; x++) {
                if(tilesMatrix[y][x].isOccupied()) {
                    if(piecesMatrix[y][x].getType().isKing()) {
                        piecesMatrix[y][x].draw(gc, 3);
                    } else {
                        piecesMatrix[y][x].draw(gc);
                    }
                }
            }
        }
        if(turn) {
            drawMessage(gc, "Turn: (B) " + player1.getName() + "! (" + player1.getPieceNumber() + ")", graphicPosX, 40);
        } else {
            drawMessage(gc, "Turn: (W) " + player2.getName() + "! (" + player2.getPieceNumber() + ")", graphicPosX, 40);
        }

        if(strikeAvailable) {
            drawMessage(gc, "Strike available!", graphicPosX, 500);
        }

        drawBoarder(gc);

    }

    public Tile[][] getTilesMatrix() {
        return tilesMatrix;
    }

    public Piece[][] getPiecesMatrix() {
        return piecesMatrix;
    }

    public double getGraphicPosX() {
        return graphicPosX;
    }

    public double getPiecePadding() {
        return piecePadding;
    }
}


