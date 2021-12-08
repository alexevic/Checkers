package com.company;

import javafx.scene.canvas.GraphicsContext;

public class Tile {
    public int matrixPosX, matrixPosY;
    public double graphicPosX, graphicPosY, width, height;
    public TileType type;
    public boolean isOccupied;

    public Tile(int matrixPosX, int matrixPosY, double graphicPosX, double graphicPosY, double width, double height, boolean occupied) {
        this.matrixPosX = matrixPosX;
        this.matrixPosY = matrixPosY;
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

    public int getMatrixPosX() {
        return matrixPosX;
    }

    public void setMatrixPosX(int matrixPosX) {
        this.matrixPosX = matrixPosX;
    }

    public int getMatrixPosY() {
        return matrixPosY;
    }

    public void setMatrixPosY(int matrixPosY) {
        this.matrixPosY = matrixPosY;
    }

    public double getGraphicPosX() {
        return graphicPosX;
    }

    public void setGraphicPosX(double graphicPosX) {
        this.graphicPosX = graphicPosX;
    }

    public double getGraphicPosY() {
        return graphicPosY;
    }

    public void setGraphicPosY(double graphicPosY) {
        this.graphicPosY = graphicPosY;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public TileType getType() {
        return type;
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
