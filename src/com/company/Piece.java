package com.company;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Piece extends Shape {
    final private Player owner;
    private PieceType type;

    public Piece(Player owner, double graphicPosX, double graphicPosY, double diameter) {
        super(graphicPosX, graphicPosY, diameter);
        this.owner = owner;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(type.getPieceColor());
        gc.fillOval(getGraphicPosX(), getGraphicPosY(), getDiameter(), getDiameter());
    }

    public void draw(GraphicsContext gc, double padding) {
        gc.setFill(type.getPieceColor());
        gc.fillOval(getGraphicPosX(), getGraphicPosY(), getDiameter(), getDiameter());
        gc.setFill(Color.DARKGREY);
        gc.fillOval(getGraphicPosX() + padding, getGraphicPosY() + padding, getDiameter() - padding * 2, getDiameter() - padding * 2);
        gc.setFill(type.getPieceColor());
        gc.fillOval(getGraphicPosX() + padding * 2, getGraphicPosY() + padding * 2, getDiameter() - padding * 4, getDiameter() - padding * 4);
    }

    // TODO: 2022-01-27 3 unit testai javoje (ctrl+shift+t)

    public Player getOwner() {
        return owner;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }
}
