package com.company;

import javafx.scene.canvas.GraphicsContext;

public abstract class Shape {
    private double graphicPosX, graphicPosY, width, height, diameter;

    public Shape(double graphicPosX, double graphicPosY, double diameter) {
        this.graphicPosX = graphicPosX;
        this.graphicPosY = graphicPosY;
        this.diameter = diameter;
    }

    public Shape(double graphicPosX, double graphicPosY, double width, double height) {
        this.graphicPosX = graphicPosX;
        this.graphicPosY = graphicPosY;
        this.width = width;
        this.height = height;
    }

    abstract void draw(GraphicsContext gc);

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

    public double getHeight() {
        return height;
    }

    public double getDiameter() {
        return diameter;
    }
}
