package com.example.ics108_project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * The main class to handle gameplay elements.
 */
public class GameApp {
    public static ArrayList<FallingEntity> apples = new ArrayList<>();
    public static Pane pane = new Pane();
    public static Timeline timeline;
    private static final long generationSpeed = 2;
    private static double fallSpeed = 10;
    private static final int pointsPerApple = 5;
    private static Rectangle floor;

    /**
     * Method to get the game's main scene. The scene contains a pane with a floor.
     * Apples are added to the scene as the game progresses. The gameplay is not
     * initiated until {@code initiate()} is called.
     * @return the main {@code Scene} for the game
     */
    public static Scene gameScene() {
        floor = new Rectangle();

        floor.setWidth(2560);
        floor.setHeight(500);
        floor.setY(1280);
        floor.setX(0);
        floor.setFill(Color.RED);

        pane.getChildren().add(floor);

        return new Scene(pane);
    }

    /**
     * Method to initiate the game. A {@code Timeline} is created for dropping
     * apples.
     */
    public static void initiate() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(generationSpeed), event -> {
            addApple();
            timeline.setRate(timeline.getRate() * 1.01);
            fallSpeed *= 1.001;
        }));
        addApple();

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * A method that stops all instances of apples using
     * {@code FallingEntity.stopProcesses()}
     */
    public static void stopAll() {
        for (FallingEntity apple: apples) {
            apple.stopProcesses();
        }
    }

    /**
     * A private method to add an apple to the {@code ArrayList} of apples
     * and drop it using {@code FallingEntity.fall()}
     */
    private static void addApple() {
        apples.add(new FallingEntity(pointsPerApple, fallSpeed));
        apples.get(apples.size() - 1).setPosition((int)(Math.random() * 2460), -250);
        pane.getChildren().add(apples.get(apples.size() - 1));
        apples.get(apples.size() - 1).fall(floor);
    }
}