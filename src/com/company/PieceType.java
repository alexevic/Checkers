package com.company;

import javafx.scene.paint.Color;

public enum PieceType {
    WhiteDefault(false, Color.WHITE, 1, 2),
    WhiteKing(true, Color.WHITE, 0, 8),
    BlackDefault(false, Color.BLACK, -1, 2),
    BlackKing(true, Color.BLACK, 0, 8);

    private boolean isKing;
    private Color pieceColor;
    public int moveDir, maxMove;

    PieceType(boolean isKing, Color pieceColor, int moveDir, int maxMove) {
        this.isKing = isKing;
        this.pieceColor = pieceColor;
        this.moveDir = moveDir;
        this.maxMove = maxMove;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        isKing = king;
    }

    public Color getPieceColor() {
        return pieceColor;
    }

    public void setPieceColor(Color pieceColor) {
        this.pieceColor = pieceColor;
    }

    public int getMoveDir() {
        return moveDir;
    }

    public void setMoveDir(int moveDir) {
        this.moveDir = moveDir;
    }
}
