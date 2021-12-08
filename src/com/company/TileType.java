package com.company;

import javafx.scene.paint.Color;

public enum TileType {
    Light(Color.WHITE, false, false),
    Dark(Color.GRAY, true, false),
    Active(Color.ORANGE, false, true),
    Accessible(Color.LIMEGREEN, true, false),
    Strikable(Color.RED, false, false);

    private Color tileColor;
    private boolean isAccessible;
    private boolean isActive;

    TileType(Color tileColor, boolean isAccessible, boolean isActive) {
        this.tileColor = tileColor;
        this.isAccessible = isAccessible;
        this.isActive = isActive;
    }

    public Color getTileColor() {
        return tileColor;
    }

    public void setTileColor(Color tileColor) {
        this.tileColor = tileColor;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
