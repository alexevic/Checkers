package com.company;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Random;
import java.util.Scanner;

public class Main extends Application {

    public String name;

    private void drawMessage(GraphicsContext gc, String str, double x, double y, int size) {
        gc.setFont(new Font("Arial", size));
        gc.setFill(Color.BLACK);
        gc.fillText(str, x, y);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Checkers");
        Group root = new Group();
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        Canvas canvas = new Canvas(500,600);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        System.out.println("Enter Player 1 name:");
        Scanner input = new Scanner(System.in);
        name = input.nextLine();
        Random rand = new Random();
        Player player1, player2;
        if(rand.nextBoolean()) {
            player1 = new Player(name, true);
            System.out.println("Enter Player 2 name:");
            name = input.nextLine();
            player2 = new Player(name, false);
        }
        else {
            player2 = new Player(name, false);
            System.out.println("Enter Player 2 name:");
            name = input.nextLine();
            player1 = new Player(name, true);
        }

        GameLogic logic = new GameLogic(gc, player1, player2);
        drawMessage(gc, "Click anywhere to start!", logic.board.graphicPosX, 40, 22);
        mainScene.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent e)
                    {
                        if(player1.getPieceNumber() == 0) {
                            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                            drawMessage(gc, player1.getName() + " lost!", 150,300,40);
                            drawMessage(gc, "Lost all pieces!", 150,350,40);
                        }

                        if(player2.getPieceNumber() == 0) {
                            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                            drawMessage(gc, player2.getName() + " lost!", 150,300, 40);
                            drawMessage(gc, "Lost all pieces!", 150,350, 40);
                        }

                        logic.findTilesWithMovablePieces(gc);
                        if(logic.movesAvailable) {
                            if(logic.isItClickOnBoard(e.getX(), e.getY())) {
                                logic.clickOnBoard(e.getX(), e.getY(), gc);
                            } else {
                                logic.clickOfBoard(gc);
                            }
                        } else {
                            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                            if(player1.isGoingFirst() == logic.turn) {
                                drawMessage(gc, player1.getName() + " lost!", 150,300, 40);
                            } else {
                                drawMessage(gc, player2.getName() + " lost!", 150,300, 40);
                            }
                            drawMessage(gc, "Can't move!", 150,350, 40);
                        }
                    }
                });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
