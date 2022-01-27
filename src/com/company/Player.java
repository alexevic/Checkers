package com.company;

public class Player {
    final private String name;
    private int pieceNumber;
    final private boolean isGoingFirst;

    public Player(String name, boolean isGoingFirst) {
        this.name = name;
        this.pieceNumber = 12;
        this.isGoingFirst = isGoingFirst;
    }

    public String getName() {
        return name;
    }

    public int getPieceNumber() {
        return pieceNumber;
    }

    public void setPieceNumber(int pieceNumber) {
        this.pieceNumber = pieceNumber;
    }

    public boolean isGoingFirst() {
        return isGoingFirst;
    }
}
