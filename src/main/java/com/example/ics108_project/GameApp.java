package com.example.ics108_project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * The main class to handle gameplay elements.
 */
public class GameApp {
    public static ArrayList<FallingEntity> apples = new ArrayList<>();
    public static Pane pane = new Pane();
    public static Timeline timeline;

    private static final double width = Screen.getPrimary().getBounds().getWidth();
    private static final double height = Screen.getPrimary().getBounds().getHeight();
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

        floor.setWidth(width);
        floor.setHeight(500);
        floor.setY(height - 200);
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
            timeline.setRate(timeline.getRate() * 1.03);
            fallSpeed *= 1.005;
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

    public static void gameOver() {
        Rectangle opacityRectangle = new Rectangle();
        opacityRectangle.setFill(Color.BLACK);
        opacityRectangle.setOpacity(0.5);
        opacityRectangle.setHeight(height);
        opacityRectangle.setWidth(width);
        opacityRectangle.setX(0);
        opacityRectangle.setY(0);
        pane.getChildren().add(opacityRectangle);

        Label scoreLabel = new Label("Score: " + Player.getScore());
        scoreLabel.setFont(Font.font(40));
        scoreLabel.layoutXProperty().bind(pane.widthProperty().subtract(scoreLabel.widthProperty()).divide(2));
        scoreLabel.layoutYProperty().bind(pane.heightProperty().subtract(scoreLabel.heightProperty()).divide(2));
        pane.getChildren().add(scoreLabel);
    }

    /**
     * A private method to add an apple to the {@code ArrayList} of apples
     * and drop it using {@code FallingEntity.fall()}
     */
    private static void addApple() {
        apples.add(new FallingEntity(pointsPerApple, fallSpeed));
        apples.get(apples.size() - 1).setPosition((int)(Math.random() * width), -250);
        pane.getChildren().add(apples.get(apples.size() - 1));
        apples.get(apples.size() - 1).fall(floor);
    }
}