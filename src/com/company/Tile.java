package com.company;

import javafx.scene.canvas.GraphicsContext;

public class Tile {
    final private double graphicPosX, graphicPosY, width, height;
    private TileType type;
    private boolean isOccupied;

    public Tile(double graphicPosX, double graphicPosY, double width, double height, boolean occupied) {
        this.graphicPosX = graphicPosX;
        this.graphicPosY = graphicPosY;
        this.width = width;
        this.height = height;
        this.isOccupied = occupied;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(type.getTileColor());
        gc.fillRect(graphicPosX, graphicPosY, width, height);
    }

    public double getGraphicPosX() {
        return graphicPosX;
    }

    public double getGraphicPosY() {
        return graphicPosY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
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
