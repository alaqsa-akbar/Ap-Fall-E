package com.example.ics108_project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main class to handle gameplay elements.
 */
public class GameApp {
    public static Pane pane;
    private static final int NUMBER_OF_APPLES_TO_SPAWN = 50;
    private static final double GOLDEN_APPLE_RARITY = 0.20;

    // Screen Dimensions
    private static final double WIDTH = Screen.getPrimary().getBounds().getWidth();
    private static final double HEIGHT = Screen.getPrimary().getBounds().getHeight();

    // Trackers
    public static Timeline timeline;
    public static ArrayList<FallingEntity> apples = new ArrayList<>();
    public static Label scoreTracker;
    private static int numberOfApplesSpawned = 0;

    // Generation speed
    private static final long INITIAL_GENERATION_SPEED = 2;
    private static final double GENERATION_ACCELERATION = 1.05;

    // Falling speed
    private static final double INITIAL_FALL_SPEED = 5;
    private static double fallSpeed = INITIAL_FALL_SPEED;
    private static final double FALL_ACCELERATION = 1.03;

    // Floor
    private static Rectangle floor;
    private static final double floorHeight = 20;

    private static Rectangle gameOverScreen;
    private static Rectangle box;
    private static Button resetButton;
    private static final MediaPlayer backGroundMusic = MainMenu.mediaPlayer;
    private static final MediaPlayer losingMusic = MainMenu.getMediaPlayer("PacManDeath.mp3");
    private static final ImageView musicImage = MainMenu.backGroundMusic();


    /**
     * Method to get the game's main scene. The scene contains a pane with a floor.
     * Apples are added to the scene as the game progresses. The gameplay is not
     * initiated until {@code initiate()} is called.
     * @return the main {@code Scene} for the game
     */
    public static Pane gameScene() {
        pane = new Pane();
        floor = new Rectangle();
        floor.setWidth(WIDTH);
        floor.setHeight(floorHeight);
        floor.setY(HEIGHT + 2 * Screen.getPrimary().getBounds().getWidth() / 15);
        floor.setX(0);
        floor.setFill(Color.TRANSPARENT);

        pane.getChildren().add(floor);

        Image image = new Image("Background.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(1.0,1.0,true,true,false,false);
        BackgroundImage backgroundImage = new BackgroundImage(
                image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        pane.setBackground(background);

        musicImage.setX(WIDTH - musicImage.getFitWidth());
        pane.getChildren().add(musicImage);

        StackPane scoreTrackPane = new StackPane();
        Rectangle scoreTrackBox = new Rectangle(80,50,Paint.valueOf("#36D1A3"));
        scoreTracker = new Label("Score : " + Player.getScore());
        scoreTracker.setFont(Font.font("Rockwell Extra Bold",10));
        scoreTracker.setTextFill(Paint.valueOf("black"));
        scoreTrackPane.getChildren().addAll(scoreTrackBox,scoreTracker);
        scoreTrackPane.setLayoutX(0);
        scoreTrackPane.setLayoutY(0);
        pane.getChildren().add(scoreTrackPane);

        return pane;
    }

    /**
     * Method to initiate the game. A {@code Timeline} is created for dropping
     * apples.
     */
    public static void initiate() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(INITIAL_GENERATION_SPEED), event -> {
            addApple();
            timeline.setRate(timeline.getRate() * GENERATION_ACCELERATION);
            fallSpeed *= FALL_ACCELERATION;
        }));
        addApple();

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        musicImage.setImage(new Image("musicNoBG.png"));
        backGroundMusic.play();
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
     * Method that renders the game over screen. It provides a label of the top 5
     * scores and an option to reset and play again. The method plays the losing music and stops the game music
     */
    public static void gameOver() {
        // Stopping Background Processes
        backGroundMusic.stop();
        losingMusic.seek(Duration.ZERO);
        losingMusic.play();

        timeline.setCycleCount(0);
        timeline.stop();
        stopAll();
        numberOfApplesSpawned = 0;

        // Creating Game Over Screen
        gameOverScreen = new Rectangle();
        gameOverScreen.setFill(Color.BLACK);
        gameOverScreen.setOpacity(0.5);
        gameOverScreen.setHeight(HEIGHT);
        gameOverScreen.setWidth(WIDTH);
        gameOverScreen.setX(0);
        gameOverScreen.setY(0);
        pane.getChildren().add(gameOverScreen);

        // Displaying Score
        Player.updateFinalScore();
        Scanner scoreScanner = Player.scoreFileScanner();
        assert scoreScanner != null;

        Label topScore = new Label("Top Score: " + scoreScanner.next() + "\n");
        Label secondScore = new Label("Second Top Score: " + scoreScanner.next() +"\n");
        Label thirdScore = new Label("Third Top Score: " + scoreScanner.next() + "\n");
        Label fourthScore = new Label("Fourth Top Score: " + scoreScanner.next() + "\n");
        Label fifthScore = new Label("Fifth Top Score: " + scoreScanner.next() + "\n");

        // Display features
        VBox scoresBox = new VBox(topScore, secondScore, thirdScore, fourthScore, fifthScore);
        scoresBox.setAlignment(Pos.CENTER);

        for(Node node : scoresBox.getChildren())
            ((Label) node).setFont(Font.font("Rockwell Extra Bold",20));

        box = new Rectangle(350, 200, Paint.valueOf("#EBA709"));
        box.setArcHeight(10);
        box.setArcWidth(10);

        StackPane boxAndScore = new StackPane(box, scoresBox);
        boxAndScore.layoutXProperty().bind(pane.widthProperty().subtract(boxAndScore.widthProperty()).divide(2));
        boxAndScore.layoutYProperty().bind(pane.heightProperty().subtract(boxAndScore.heightProperty()).divide(4));
        pane.getChildren().add(boxAndScore);

        resetButton = MainMenu.createButton("reset.png",200,100);
        Button menuButton = MainMenu.createButton("menu.png", 200, 100);

        VBox buttons = new VBox(resetButton, menuButton);
        buttons.layoutXProperty().bind(pane.widthProperty().subtract(buttons.widthProperty()).divide(2));
        buttons.layoutYProperty().bind(pane.heightProperty().subtract(buttons.heightProperty()).divide(1.5));
        pane.getChildren().add(buttons);

        // Even Handlers for Buttons
        resetButton.setOnAction(e -> {
            gameOverScreen.setOpacity(0);
            pane.getChildren().removeAll(gameOverScreen,boxAndScore,buttons);
            box = null;
            resetButton = null;
            gameOverScreen = null;
            scoreTracker.setText("Score: 0");
            clear();
            initiate();
        });

        menuButton.setOnAction(e -> {
            clear();
            pane = null;
            GameClass.stage.getScene().setRoot(MainMenu.mainMenuPane());
            MainMenu.mediaPlayer.seek(Duration.ZERO);
            MainMenu.mediaPlayer.play();
        });

        Player.updateFinalScore();
        Player.resetScore();
    }


    /**
     * Method to clear all elements on screen. Main purpose is to call when player
     * chooses to play again at the game over screen.
     */
    private static void clear() {
        while (apples.size() != 0) {
            apples.get(0).kill();
        }
        fallSpeed = INITIAL_FALL_SPEED;
    }

    /**
     * A private method to add an apple to the {@code ArrayList} of apples
     * and drop it using {@code FallingEntity.fall()}
     */
    private static void addApple() {
        if (numberOfApplesSpawned == NUMBER_OF_APPLES_TO_SPAWN)
            return;

        boolean isGoldenApple = Math.random() <= GOLDEN_APPLE_RARITY;
        FallingEntity fallingEntity = new FallingEntity(fallSpeed, isGoldenApple);

        apples.add(fallingEntity);
        numberOfApplesSpawned++;
        pane.getChildren().add(fallingEntity);

        fallingEntity.setPosition((int)(Math.random() * (WIDTH - FallingEntity.getSize())), -250);

        fallingEntity.fall(floor);
    }

    public static void checkForGameOver(){
        if (numberOfApplesSpawned == NUMBER_OF_APPLES_TO_SPAWN && apples.size() == 0){
            gameOver();
        }
    }
}