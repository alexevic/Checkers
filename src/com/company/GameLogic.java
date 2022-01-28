package com.company;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GameLogic {
    final private Board board;
    final private ArrayList<Point> allExistingMovesUpLeft = new ArrayList<>();
    final private ArrayList<Point> allExistingMovesUpRight = new ArrayList<>();
    final private ArrayList<Point> allExistingMovesDownLeft = new ArrayList<>();
    final private ArrayList<Point> allExistingMovesDownRight = new ArrayList<>();
    final private ArrayList<Point> accessibleTilesCoordinates = new ArrayList<>();
    final private ArrayList<Point> movableTilesCoordinates = new ArrayList<>();
    private int currentClickPosX, currentClickPosY, lastClickPosX, lastClickPosY;
    private boolean activeTileOn = false, tileHasStrike = false, strikeAvailable = false, turn = true, movesAvailable = false; //true = player1/black, false = player2/white

    GameLogic(GraphicsContext gc, Player player1, Player player2) {
        this.board = Board.getInstance(50,50, 50,5,8,8, gc, player1, player2);
    }

    /**
     * MAIN FUNCTION
     * */

    public void clickOnBoard(double mouseX, double mouseY, GraphicsContext gc) {
        currentClickPos(mouseX, mouseY);
        if(activeTileOn) {
            legalMovesFinder(getLastClickPosY(), getLastClickPosX(), board.getPiecesMatrix());
            if(strikeAvailable) {
                checksForSameClick(gc);
                allExistingMovesWhenStrikeAvailable(allExistingMovesUpLeft, allExistingMovesUpRight);
                allExistingMovesWhenStrikeAvailable(allExistingMovesDownLeft, allExistingMovesDownRight);
                resetMovableTiles(movableTilesCoordinates);
                activeTileOn = false;
                strikeAvailable = false;
                movesAvailable = false;
                resetLegalMovesArrays();
                findTilesWithMovablePieces(gc);
                lastClickPos();
            } else {
                checksForSameClick(gc);
                allExistingMovesStrikeNotAvailable(allExistingMovesUpLeft, allExistingMovesUpRight);
                allExistingMovesStrikeNotAvailable(allExistingMovesDownLeft, allExistingMovesDownRight);
                resetMovableTiles(movableTilesCoordinates);
                activeTileOn = false;
                strikeAvailable = false;
                movesAvailable = false;
                resetLegalMovesArrays();
                findTilesWithMovablePieces(gc);
                lastClickPos();
            }
        } else {
            for (Point temp : movableTilesCoordinates) {
                if (getCurrentClickPosX() == temp.x && getCurrentClickPosY() == temp.y) {
                    resetLegalMovesArrays();
                    accessibleTilesCoordinates.clear();
                    legalMovesFinder(temp.y, temp.x, board.getPiecesMatrix());
                    showLegalMoves();
                    activeTileOn = true;
                } else {
                    board.getTilesMatrix()[temp.y][temp.x].setType(TileType.Dark);
                }
            }
        }
        board.updateBoard(gc, turn, strikeAvailable);
        lastClickPos();
        resetMovableTiles(movableTilesCoordinates);
    }

    private void allExistingMovesStrikeNotAvailable(ArrayList<Point> allExistingMovesUpLeft, ArrayList<Point> allExistingMovesUpRight) {
        if(!allExistingMovesUpLeft.isEmpty()) {
            for (Point temp : allExistingMovesUpLeft) {
                if (getCurrentClickPosX() == temp.x && getCurrentClickPosY() == temp.y) {
                    makeMove(temp.x, temp.y, getLastClickPosX(), getLastClickPosY());
                    turn = !turn;
                }
            }
        }
        if(!allExistingMovesUpRight.isEmpty()) {
            for (Point temp : allExistingMovesUpRight) {
                if (getCurrentClickPosX() == temp.x && getCurrentClickPosY() == temp.y) {
                    makeMove(temp.x, temp.y, getLastClickPosX(), getLastClickPosY());
                    turn = !turn;
                }
            }
        }
    }

    private void allExistingMovesWhenStrikeAvailable(ArrayList<Point> allExistingMovesUpLeft, ArrayList<Point> allExistingMovesUpRight) {
        allExistingMovesWhenStrikeAvailableDirectional(allExistingMovesUpLeft);
        allExistingMovesWhenStrikeAvailableDirectional(allExistingMovesUpRight);
    }

    private void allExistingMovesWhenStrikeAvailableDirectional(ArrayList<Point> allExistingMovesUpLeft) {
        if(!allExistingMovesUpLeft.isEmpty()) {
            for (int i = 1; i < allExistingMovesUpLeft.size(); i++) {
                Point temp = allExistingMovesUpLeft.get(i);
                if (getCurrentClickPosX() == temp.x && getCurrentClickPosY() == temp.y) {
                    strikeAvailable = false;
                    makeMove(temp.x, temp.y, getLastClickPosX(), getLastClickPosY());
                    temp = allExistingMovesUpLeft.get(0);
                    board.getPiecesMatrix()[temp.y][temp.x].getOwner().setPieceNumber(board.getPiecesMatrix()[temp.y][temp.x].getOwner().getPieceNumber() - 1);
                    board.getPiecesMatrix()[temp.y][temp.x] = null;
                    board.getTilesMatrix()[temp.y][temp.x].setOccupied(false);
                    resetLegalMovesArrays();
                    legalMovesFinder(getCurrentClickPosY(), getCurrentClickPosX(), board.getPiecesMatrix());
                    if(!tileHasStrike) {
                        turn = !turn;
                    }
                }
            }
        }
    }

    private void checksForSameClick(GraphicsContext gc) {
        if(isItSameClickPos(getCurrentClickPosX(), getCurrentClickPosY(), getLastClickPosX(), getLastClickPosY())) {
            resetMovableTiles(movableTilesCoordinates);
            activeTileOn = false;
            strikeAvailable = false;
            accessibleTilesCoordinates.clear();
            resetLegalMovesArrays();
            movesAvailable = false;
            findTilesWithMovablePieces(gc);
            lastClickPos();
        }
    }

    public void clickOfBoard(GraphicsContext gc) {
        resetMovableTiles(movableTilesCoordinates);
        activeTileOn = false;
        strikeAvailable = false;
        resetLegalMovesArrays();
        movesAvailable = false;
        findTilesWithMovablePieces(gc);
        lastClickPos();
    }

    /**
     * CLICK REGISTERING
     * */

    public boolean isItClickOnBoard(double mouseX, double mouseY) {
        return mouseX >= 50 && mouseY >= 50 && mouseX <= 450 && mouseY <= 450;
    }

    private void currentClickPos(double mouseX, double mouseY) {
        if(isItClickOnBoard(mouseX, mouseY)) {
            setCurrentClickPosX(mouseX);
            setCurrentClickPosY(mouseY);
        }
    }

    private void lastClickPos() {
        setLastClickPosX(getCurrentClickPosX());
        setLastClickPosY(getCurrentClickPosY());
    }

    private boolean isItSameClickPos(int x1, int y1, int x2, int y2) {
        return x1 == x2 && y1 == y2;
    }

    /**
     * MOVE FINDER
     * */

    public void findTilesWithMovablePieces(GraphicsContext gc) {
        boolean flag = false;
        for (int y = 0; y < board.getTilesMatrix().length; y++) {
            for (int x = 0; x < board.getTilesMatrix()[y].length; x++) {
                if(board.getTilesMatrix()[y][x].isOccupied() && board.getPiecesMatrix()[y][x].getOwner().isGoingFirst() == turn) {
                    legalMovesFinder(y, x, board.getPiecesMatrix());
                    if(legalMovesExist()) {
                        movesAvailable = true;
                        if(strikeAvailable) {
                            if(!flag) {
                                flag = true;
                                resetMovableTiles(movableTilesCoordinates);
                            }
                            if(tileHasStrike) {
                                movableTilesCoordinates.add(new Point(x, y));
                            }
                        }
                        if(!strikeAvailable) {
                            movableTilesCoordinates.add(new Point(x, y));
                        }
                    }
                    resetLegalMovesArrays();
                }
            }
        }
        drawMovableTiles(movableTilesCoordinates);
        board.updateBoard(gc, turn, strikeAvailable);
    }

    private void legalMovesFinder(int y, int x, Piece[][] piece) {
        findAllExistingTiles(y, x, piece);

        for (ArrayList<Point> arrayList : Arrays.asList(allExistingMovesUpLeft, allExistingMovesDownLeft, allExistingMovesUpRight, allExistingMovesDownRight)) {
            removeIllegalTilesFromAllExistingTiles(y, x, arrayList);
        }

        for (ArrayList<Point> arrayList : Arrays.asList(allExistingMovesUpLeft, allExistingMovesDownLeft, allExistingMovesUpRight, allExistingMovesDownRight)) {
            removeDoubleStrike(arrayList);
        }

        if(hasPiecesToStrike(allExistingMovesUpLeft) || hasPiecesToStrike(allExistingMovesDownLeft) || hasPiecesToStrike(allExistingMovesUpRight) || hasPiecesToStrike(allExistingMovesDownRight)) {
            tileHasStrike = true;
            strikeAvailable = true;
            if(!hasPiecesToStrike(allExistingMovesUpLeft)) {
                allExistingMovesUpLeft.clear();
            } else {
                strikingMoveTilesFilter(allExistingMovesUpLeft);
            }
            if(!hasPiecesToStrike(allExistingMovesDownLeft)) {
                allExistingMovesDownLeft.clear();
            } else {
                strikingMoveTilesFilter(allExistingMovesDownLeft);
            }
            if(!hasPiecesToStrike(allExistingMovesUpRight)) {
                allExistingMovesUpRight.clear();
            } else {
                strikingMoveTilesFilter(allExistingMovesUpRight);
            }
            if(!hasPiecesToStrike(allExistingMovesDownRight)) {
                allExistingMovesDownRight.clear();
            } else {
                strikingMoveTilesFilter(allExistingMovesDownRight);
            }
        } else {
            tileHasStrike = false;
        }
    }

    private void findAllExistingTiles(int y, int x, Piece[][] piece) {
        if(piece[y][x].getType().isKing()) {
            allExistingTilesFinder(y, x, piece[y][x].getType().getMaxMove(), -1, true);
            allExistingTilesFinder(y, x, piece[y][x].getType().getMaxMove(), 1, false);
        } else {
            allExistingTilesFinder(y, x, piece[y][x].getType().getMaxMove(), piece[y][x].getType().getMoveDir(), piece[y][x].getType().getMoveDir() == -1);
        }
    }

    private void allExistingTilesFinder(int y, int x, int maxMove, int direction, boolean positive) {
        int tempLX = x, tempRX = x, tempY = y, tempMoves = maxMove;
        while(tempMoves > 0) {
            tempMoves--;
            tempY = tempY + direction;
            /**
             * Finds all undergo possible moves on the left side.
             */
            tempLX--;
            int axisBorderMinValue = -1;
            int axisBorderMaxValue = 8;
            if(tempLX > axisBorderMinValue && tempY > axisBorderMinValue && tempY < axisBorderMaxValue) {
                if(positive) {
                    allExistingMovesUpLeft.add(new Point(tempLX, tempY));
                } else {
                    allExistingMovesDownLeft.add(new Point(tempLX, tempY));
                }
            }
            /**
             * Finds all undergo possible moves on the right side.
             */
            tempRX++;
            if(tempRX < axisBorderMaxValue && tempY > axisBorderMinValue && tempY < axisBorderMaxValue) {
                if(positive) {
                    allExistingMovesUpRight.add(new Point(tempRX, tempY));
                } else {
                    allExistingMovesDownRight.add(new Point(tempRX, tempY));
                }
            }
        }
    }

    private void removeIllegalTilesFromAllExistingTiles(int y, int x, ArrayList<Point> arrayList) {
        /**
         * Friendly pieces, double enemy pieces, if last tile has enemy piece
         */
        ArrayList<Point> delete = new ArrayList<>();

        if(!board.getPiecesMatrix()[y][x].getType().isKing() && arrayList.size() == 2) {
            Point temp = arrayList.get(0);
            if(!board.getTilesMatrix()[temp.y][temp.x].isOccupied()) {
                temp = arrayList.get(1);
                delete.add(new Point(temp.x, temp.y));
            }
        }

        int tilesInARow = 0;
        boolean flagDoublePiece = false;
        boolean flagFriendly = false;
        int lastX = 0, lastY = 0;
        for(int i = 0; i < arrayList.size(); i++) {
            Point temp = arrayList.get(i);

            // For removing friendly pieces
            if(board.getTilesMatrix()[temp.y][temp.x].isOccupied() && (board.getPiecesMatrix()[temp.y][temp.x].getOwner() == board.getPiecesMatrix()[y][x].getOwner())) {
                flagFriendly = true;
            }
            if(flagFriendly) {
                delete.add(new Point(temp.x, temp.y));
            }

            // For removing double pieces
            if(board.getTilesMatrix()[temp.y][temp.x].isOccupied()) {
                tilesInARow++;
                if(tilesInARow >= 2) {
                    if(tilesInARow == 2) {
                        delete.add(new Point(lastX, lastY));
                    }
                    flagDoublePiece = true;
                    delete.add(new Point(temp.x, temp.y));
                }
                lastX = temp.x;
                lastY = temp.y;
            } else {
                if(!flagDoublePiece) {
                    tilesInARow = 0;
                }
            }

            // For removing enemy piece if it's last in an array
            if(board.getTilesMatrix()[temp.y][temp.x].isOccupied() && board.getPiecesMatrix()[temp.y][temp.x].getOwner() != board.getPiecesMatrix()[y][x].getOwner() && (i == arrayList.size() - 1)) {
                delete.add(new Point(temp.x, temp.y));
            }

        }

        for (Point temp : delete) {
            arrayList.remove(temp);
        }
    }

    private void removeDoubleStrike(ArrayList<Point> arrayList) {
        ArrayList<Point> delete = new ArrayList<>();
        int enemyPieces = 0;
        for (Point temp : arrayList) {
            if (board.getTilesMatrix()[temp.y][temp.x].isOccupied()) {
                enemyPieces++;
            }
            if (enemyPieces >= 2) {
                delete.add(new Point(temp.x, temp.y));
            }
        }

        if(enemyPieces > 0) {
            for (Point temp : delete) {
                arrayList.remove(temp);
            }
        }
    }

    private void strikingMoveTilesFilter(ArrayList<Point> arrayList) {
        ArrayList<Point> delete = new ArrayList<>();
        boolean flag = false;
        for (Point temp : arrayList) {
            if (board.getTilesMatrix()[temp.y][temp.x].isOccupied()) {
                flag = true;
            }
            if(!flag) {
                delete.add(new Point(temp.x, temp.y));
            }
        }

        for (Point temp : delete) {
            arrayList.remove(temp);
        }
    }

    private boolean hasPiecesToStrike(ArrayList<Point> arrayList) {
        for (Point temp : arrayList) {
            if (board.getTilesMatrix()[temp.y][temp.x].isOccupied()) {
                return true;
            }
        }
        return false;
    }

    private boolean legalMovesExist() {
        return !allExistingMovesUpLeft.isEmpty() || !allExistingMovesDownLeft.isEmpty() || !allExistingMovesUpRight.isEmpty() || !allExistingMovesDownRight.isEmpty();
    }

    private void resetLegalMovesArrays() {
        resetLegalMovesForDirection(allExistingMovesUpLeft, allExistingMovesUpRight);
        resetLegalMovesForDirection(allExistingMovesDownLeft, allExistingMovesDownRight);
    }

    private void resetLegalMovesForDirection(ArrayList<Point> left, ArrayList<Point> right) {
        for (Point temp : left) {
            board.getTilesMatrix()[temp.y][temp.x].setType(TileType.Dark);
        }
        left.clear();
        for (Point temp : right) {
            board.getTilesMatrix()[temp.y][temp.x].setType(TileType.Dark);
        }
        right.clear();
    }

    /**
     * MAKING MOVES
     * */

    private void makeMove(int newX, int newY, int oldX, int oldY) {
        board.getPiecesMatrix()[newY][newX] = board.getPiecesMatrix()[oldY][oldX];
        board.getPiecesMatrix()[newY][newX].setGraphicPosX(board.getTilesMatrix()[newY][newX].getGraphicPosX() + board.getPiecePadding());
        board.getPiecesMatrix()[newY][newX].setGraphicPosY(board.getTilesMatrix()[newY][newX].getGraphicPosY() + board.getPiecePadding());
        board.getTilesMatrix()[newY][newX].setOccupied(true);
        board.getTilesMatrix()[oldY][oldX].setOccupied(false);
        board.getPiecesMatrix()[oldY][oldX] = null;
        activeTileOn = false;
        if(board.getPiecesMatrix()[newY][newX].getType() == PieceType.BlackDefault && newY == 0) {
            board.getPiecesMatrix()[newY][newX].setType(PieceType.BlackKing);
        }
        if(board.getPiecesMatrix()[newY][newX].getType() == PieceType.WhiteDefault && newY == 7) {
            board.getPiecesMatrix()[newY][newX].setType(PieceType.WhiteKing);
        }
    }

    /**
     * DRAWING TO SCREEN
     * */

    private void showLegalMoves() {
        showLegalMovesForGivenDirection(allExistingMovesUpLeft, allExistingMovesUpRight);
        showLegalMovesForGivenDirection(allExistingMovesDownLeft, allExistingMovesDownRight);
    }

    private void showLegalMovesForGivenDirection(ArrayList<Point> left, ArrayList<Point> right) {
        for (Point temp : left) {
            if(board.getTilesMatrix()[temp.y][temp.x].isOccupied()) {
                board.getTilesMatrix()[temp.y][temp.x].setType(TileType.Kill);
            } else {
                board.getTilesMatrix()[temp.y][temp.x].setType(TileType.Accessible);
            }
        }

        for (Point temp : right) {
            if(board.getTilesMatrix()[temp.y][temp.x].isOccupied()) {
                board.getTilesMatrix()[temp.y][temp.x].setType(TileType.Kill);
            } else {
                board.getTilesMatrix()[temp.y][temp.x].setType(TileType.Accessible);
            }
        }
    }

    private void drawMovableTiles(ArrayList<Point> arrayList) {
        for (Point temp : arrayList) {
            board.getTilesMatrix()[temp.y][temp.x].setType(TileType.Active);
        }
    }

    private void resetMovableTiles(ArrayList<Point> arrayList) {
        for (Point temp : arrayList) {
            board.getTilesMatrix()[temp.y][temp.x].setType(TileType.Dark);
        }
        arrayList.clear();
    }

    /**
     * GETTERS AND SETTERS
     */

    public boolean isTurn() {
        return turn;
    }

    public boolean isMovesAvailable() {
        return movesAvailable;
    }

    public Board getBoard() {
        return board;
    }

    public int getCurrentClickPosX() {
        return currentClickPosX;
    }

    public void setCurrentClickPosX(double mouseX) {
        for (int x = 0; x < board.getTilesMatrix().length; x++) {
            if((board.getTilesMatrix()[0][x].getGraphicPosX() <= mouseX) && (board.getTilesMatrix()[0][x].getGraphicPosX() + board.getTilesMatrix()[0][x].getWidth()) >= mouseX) {
                this.currentClickPosX = x;
            }
        }
    }

    public int getCurrentClickPosY() {
        return currentClickPosY;
    }

    public void setCurrentClickPosY(double mouseY) {
        for (int y = 0; y < board.getTilesMatrix().length; y++) {
            if((board.getTilesMatrix()[y][0].getGraphicPosY() <= mouseY) && (board.getTilesMatrix()[y][0].getGraphicPosY() + board.getTilesMatrix()[y][0].getHeight() >= mouseY)) {
                this.currentClickPosY = y;
            }
        }
    }

    public int getLastClickPosX() {
        return lastClickPosX;
    }

    public void setLastClickPosX(int lastClickPosX) {
        this.lastClickPosX = lastClickPosX;
    }

    public int getLastClickPosY() {
        return lastClickPosY;
    }

    public void setLastClickPosY(int lastClickPosY) {
        this.lastClickPosY = lastClickPosY;
    }
}