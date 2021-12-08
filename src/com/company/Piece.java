package com.company;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Piece {
    public Player owner;
    public double graphicPosX, graphicPosY, diameter;
    public PieceType type;

    public Piece(Player owner, double graphicPosX, double graphicPosY, double diameter) {
        this.owner = owner;
        this.graphicPosX = graphicPosX;
        this.graphicPosY = graphicPosY;
        this.diameter = diameter;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(type.getPieceColor());
        gc.fillOval(graphicPosX, graphicPosY, diameter, diameter);
        if(type.isKing()) {
            gc.setFill(Color.DARKGREY);
            gc.fillOval(graphicPosX + 3, graphicPosY + 3, diameter - 6, diameter - 6);
            gc.setFill(type.getPieceColor());
            gc.fillOval(graphicPosX + 6, graphicPosY + 6, diameter - 12, diameter - 12);
        }
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
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

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }
}
