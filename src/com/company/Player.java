package com.company;

import javafx.scene.paint.Color;

public class Player {
    public String name;
    public int pieceNumber;
    public boolean isGoingFirst;
    public Color color;

    public Player(String name, boolean isGoingFirst) {
        this.name = name;
        this.pieceNumber = 12;
        this.isGoingFirst = isGoingFirst;
        if(isGoingFirst) {
            this.color = Color.BLACK;
        } else {
            this.color = Color.WHITE;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setGoingFirst(boolean goingFirst) {
        isGoingFirst = goingFirst;
    }
}
