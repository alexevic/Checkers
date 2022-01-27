package com.company;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GameLogic {
    public Board board;
    public ArrayList<Point> allExistingMovesUpLeft = new ArrayList<>();
    public ArrayList<Point> allExistingMovesUpRight = new ArrayList<>();
    public ArrayList<Point> allExistingMovesDownLeft = new ArrayList<>();
    public ArrayList<Point> allExistingMovesDownRight = new ArrayList<>();
    public ArrayList<Point> accessibleTilesCoordinates = new ArrayList<>();
    public ArrayList<Point> movableTilesCoordinates = new ArrayList<>();
    public int currentClickPosX, currentClickPosY, lastClickPosX, lastClickPosY, axisBorderMaxValue = 8, axisBorderMinValue = -1;
    public boolean activeTileOn = false, tileHasStrike = false, strikeAvailable = false, turn = true, movesAvailable = false; //true = player1/black, false = player2/white

    GameLogic(GraphicsContext gc, Player player1, Player player2) {
        this.board = new Board(50,50, 50,5,8,8, gc, player1, player2);
    }

    /**
     * MAIN FUNCTION
     * */

    public void clickOnBoard(double mouseX, double mouseY, GraphicsContext gc) {
        currentClickPos(mouseX, mouseY);
        if(activeTileOn) {
            legalMovesFinder(getLastClickPosY(), getLastClickPosX(), board.tilesMatrix, board.piecesMatrix);
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
                    legalMovesFinder(temp.y, temp.x, board.tilesMatrix, board.piecesMatrix);
                    showLegalMoves();
                    activeTileOn = true;
                } else {
                    board.tilesMatrix[temp.y][temp.x].setType(TileType.Dark);
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
                    board.piecesMatrix[temp.y][temp.x].getOwner().setPieceNumber(board.piecesMatrix[temp.y][temp.x].getOwner().getPieceNumber() - 1);
                    board.piecesMatrix[temp.y][temp.x] = null;
                    board.tilesMatrix[temp.y][temp.x].setOccupied(false);
                    resetLegalMovesArrays();
                    legalMovesFinder(getCurrentClickPosY(), getCurrentClickPosX(), board.tilesMatrix, board.piecesMatrix);
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

    public void currentClickPos(double mouseX, double mouseY) {
        if(isItClickOnBoard(mouseX, mouseY)) {
            setCurrentClickPosX(mouseX);
            setCurrentClickPosY(mouseY);
        }
    }

    public void lastClickPos() {
        setLastClickPosX(getCurrentClickPosX());
        setLastClickPosY(getCurrentClickPosY());
    }

    public boolean isItSameClickPos(int x1, int y1, int x2, int y2) {
        return x1 == x2 && y1 == y2;
    }

    /**
     * MOVE FINDER
     * */

    public void findTilesWithMovablePieces(GraphicsContext gc) {
        boolean flag = false;
        for (int y = 0; y < board.tilesMatrix.length; y++) {
            for (int x = 0; x < board.tilesMatrix[y].length; x++) {
                if(board.tilesMatrix[y][x].isOccupied && board.piecesMatrix[y][x].getOwner().isGoingFirst() == turn) {
                    legalMovesFinder(y, x, board.tilesMatrix, board.piecesMatrix);
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

    public void legalMovesFinder(int y, int x, Tile[][] tile, Piece[][] piece) {
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

    public void findAllExistingTiles(int y, int x, Piece[][] piece) {
        if(piece[y][x].getType().isKing()) {
            allExistingTilesFinder(y, x, piece[y][x].getType().getMaxMove(), -1, true);
            allExistingTilesFinder(y, x, piece[y][x].getType().getMaxMove(), 1, false);
        } else {
            allExistingTilesFinder(y, x, piece[y][x].getType().getMaxMove(), piece[y][x].getType().getMoveDir(), piece[y][x].getType().getMoveDir() == -1);
        }
    }

    public void allExistingTilesFinder(int y, int x, int maxMove, int direction, boolean positive) {
        int tempLX = x, tempRX = x, tempY = y, tempMoves = maxMove;
        while(tempMoves > 0) {
            tempMoves--;
            tempY = tempY + direction;
            /**
             * Finds all undergo possible moves on the left side.
             */
            tempLX--;
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

    public void removeIllegalTilesFromAllExistingTiles(int y, int x, ArrayList<Point> arrayList) {
        /**
         * Friendly pieces, double enemy pieces, if last tile has enemy piece
         */
        ArrayList<Point> delete = new ArrayList<>();

        if(!board.piecesMatrix[y][x].getType().isKing() && arrayList.size() == 2) {
            Point temp = arrayList.get(0);
            if(!board.tilesMatrix[temp.y][temp.x].isOccupied) {
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
            if(board.tilesMatrix[temp.y][temp.x].isOccupied && (board.piecesMatrix[temp.y][temp.x].getOwner() == board.piecesMatrix[y][x].getOwner())) {
                flagFriendly = true;
            }
            if(flagFriendly) {
                delete.add(new Point(temp.x, temp.y));
            }

            // For removing double pieces
            if(board.tilesMatrix[temp.y][temp.x].isOccupied) {
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
            if(board.tilesMatrix[temp.y][temp.x].isOccupied && board.piecesMatrix[temp.y][temp.x].getOwner() != board.piecesMatrix[y][x].getOwner() && (i == arrayList.size() - 1)) {
                delete.add(new Point(temp.x, temp.y));
            }

        }

        for (Point temp : delete) {
            arrayList.remove(temp);
        }
    }

    public void removeDoubleStrike(ArrayList<Point> arrayList) {
        ArrayList<Point> delete = new ArrayList<>();
        int enemyPieces = 0;
        for (Point temp : arrayList) {
            if (board.tilesMatrix[temp.y][temp.x].isOccupied) {
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

    public void strikingMoveTilesFilter(ArrayList<Point> arrayList) {
        ArrayList<Point> delete = new ArrayList<>();
        boolean flag = false;
        for (Point temp : arrayList) {
            if (board.tilesMatrix[temp.y][temp.x].isOccupied) {
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

    public boolean hasPiecesToStrike(ArrayList<Point> arrayList) {
        for (Point temp : arrayList) {
            if (board.tilesMatrix[temp.y][temp.x].isOccupied) {
                return true;
            }
        }
        return false;
    }

    public boolean legalMovesExist() {
        return !allExistingMovesUpLeft.isEmpty() || !allExistingMovesDownLeft.isEmpty() || !allExistingMovesUpRight.isEmpty() || !allExistingMovesDownRight.isEmpty();
    }

    public void resetLegalMovesArrays() {
        resetLegalMovesForDirection(allExistingMovesUpLeft, allExistingMovesUpRight);
        resetLegalMovesForDirection(allExistingMovesDownLeft, allExistingMovesDownRight);
    }

    private void resetLegalMovesForDirection(ArrayList<Point> left, ArrayList<Point> right) {
        for (Point temp : left) {
            board.tilesMatrix[temp.y][temp.x].setType(TileType.Dark);
        }
        left.clear();
        for (Point temp : right) {
            board.tilesMatrix[temp.y][temp.x].setType(TileType.Dark);
        }
        right.clear();
    }

    /**
     * MAKING MOVES
     * */

    public void makeMove(int newX, int newY, int oldX, int oldY) {
        board.piecesMatrix[newY][newX] = board.piecesMatrix[oldY][oldX];
        board.piecesMatrix[newY][newX].setGraphicPosX(board.tilesMatrix[newY][newX].getGraphicPosX() + board.piecePadding);
        board.piecesMatrix[newY][newX].setGraphicPosY(board.tilesMatrix[newY][newX].getGraphicPosY() + board.piecePadding);
        board.tilesMatrix[newY][newX].setOccupied(true);
        board.tilesMatrix[oldY][oldX].setOccupied(false);
        board.piecesMatrix[oldY][oldX] = null;
        activeTileOn = false;
        if(board.piecesMatrix[newY][newX].getType() == PieceType.BlackDefault && newY == 0) {
            board.piecesMatrix[newY][newX].setType(PieceType.BlackKing);
        }
        if(board.piecesMatrix[newY][newX].getType() == PieceType.WhiteDefault && newY == 7) {
            board.piecesMatrix[newY][newX].setType(PieceType.WhiteKing);
        }
    }

    /**
     * DRAWING TO SCREEN
     * */

    public void showLegalMoves() {
        showLegalMovesForGivenDirection(allExistingMovesUpLeft, allExistingMovesUpRight);
        showLegalMovesForGivenDirection(allExistingMovesDownLeft, allExistingMovesDownRight);
    }

    private void showLegalMovesForGivenDirection(ArrayList<Point> left, ArrayList<Point> right) {
        for (Point temp : left) {
            if(board.tilesMatrix[temp.y][temp.x].isOccupied) {
                board.tilesMatrix[temp.y][temp.x].setType(TileType.Strikable);
            } else {
                board.tilesMatrix[temp.y][temp.x].setType(TileType.Accessible);
            }
        }

        for (Point temp : right) {
            if(board.tilesMatrix[temp.y][temp.x].isOccupied) {
                board.tilesMatrix[temp.y][temp.x].setType(TileType.Strikable);
            } else {
                board.tilesMatrix[temp.y][temp.x].setType(TileType.Accessible);
            }
        }
    }

    public void drawMovableTiles(ArrayList<Point> arrayList) {
        for (Point temp : arrayList) {
            board.tilesMatrix[temp.y][temp.x].setType(TileType.Active);
        }
    }

    public void resetMovableTiles(ArrayList<Point> arrayList) {
        for (Point temp : arrayList) {
            board.tilesMatrix[temp.y][temp.x].setType(TileType.Dark);
        }
        arrayList.clear();
    }

    /**
     * GETTERS AND SETTERS
     */

    public int getCurrentClickPosX() {
        return currentClickPosX;
    }

    public void setCurrentClickPosX(double mouseX) {
        for (int x = 0; x < board.tilesMatrix.length; x++) {
            if((board.tilesMatrix[0][x].getGraphicPosX() <= mouseX) && (board.tilesMatrix[0][x].getGraphicPosX() + board.tilesMatrix[0][x].getWidth()) >= mouseX) {
                this.currentClickPosX = x;
            }
        }
    }

    public int getCurrentClickPosY() {
        return currentClickPosY;
    }

    public void setCurrentClickPosY(double mouseY) {
        for (int y = 0; y < board.tilesMatrix.length; y++) {
            if((board.tilesMatrix[y][0].getGraphicPosY() <= mouseY) && (board.tilesMatrix[y][0].getGraphicPosY() + board.tilesMatrix[y][0].getHeight() >= mouseY)) {
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

    public boolean isActiveTileOn() {
        return activeTileOn;
    }

    public void setActiveTileOn(boolean activeTileOn) {
        this.activeTileOn = activeTileOn;
    }

}