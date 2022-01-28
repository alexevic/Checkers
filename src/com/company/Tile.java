package com.company;

import javafx.scene.canvas.GraphicsContext;

public class Tile extends Shape {
    private TileType type;
    private boolean isOccupied;

    public Tile(double graphicPosX, double graphicPosY, double width, double height, boolean occupied) {
        super(graphicPosX, graphicPosY, width, height);
        this.isOccupied = occupied;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(type.getTileColor());
        gc.fillRect(getGraphicPosX(), getGraphicPosY(), getWidth(), getHeight());
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }
}
