package com.company;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Piece implements DrawableElement {
    final private Player owner;
    private double graphicPosX, graphicPosY;
    final private double diameter;
    private PieceType type;

    public Piece(Player owner, double graphicPosX, double graphicPosY, double diameter) {
        this.owner = owner;
        this.graphicPosX = graphicPosX;
        this.graphicPosY = graphicPosY;
        this.diameter = diameter;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(type.getPieceColor());
        gc.fillOval(graphicPosX, graphicPosY, diameter, diameter);
    }

    public void draw(GraphicsContext gc, double padding) {
        gc.setFill(type.getPieceColor());
        gc.fillOval(graphicPosX, graphicPosY, diameter, diameter);
        gc.setFill(Color.DARKGREY);
        gc.fillOval(graphicPosX + padding, graphicPosY + padding, diameter - padding * 2, diameter - padding * 2);
        gc.setFill(type.getPieceColor());
        gc.fillOval(graphicPosX + padding * 2, graphicPosY + padding * 2, diameter - padding * 4, diameter - padding * 4);
    }

    // TODO: 2022-01-27 inheritance nauja square class 
    // TODO: 2022-01-27 perziureti metodu encapsulation
    // TODO: 2022-01-27 sukurti move finder klase?
    // TODO: 2022-01-27 3 unit testai javoje (ctrl+shift+t)
    // TODO: 2022-01-27 creational design ir behavioural design patternai
    // TODO: 2022-01-27 fix one mistake
    // TODO: 2022-01-28 sukurti matrixgrid klase, kuria paveldes tilematrix ir piecematrix ir tada padaryti polimorfizma draw metodui :)

    public Player getOwner() {
        return owner;
    }

    public void setGraphicPosX(double graphicPosX) {
        this.graphicPosX = graphicPosX;
    }

    public void setGraphicPosY(double graphicPosY) {
        this.graphicPosY = graphicPosY;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }
}
