package com.company;

import javafx.scene.paint.Color;

public enum TileType {
    Light(Color.WHITE),
    Dark(Color.GRAY),
    Active(Color.ORANGE),
    Accessible(Color.LIMEGREEN),
    Strikable(Color.RED);

    final private Color tileColor;

    TileType(Color tileColor) {
        this.tileColor = tileColor;
    }

    public Color getTileColor() {
        return tileColor;
    }
}
