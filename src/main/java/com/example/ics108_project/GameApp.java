package com.example.ics108_project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
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

/**
 * The main class to handle gameplay elements.
 */
public class GameApp {
    public static ArrayList<FallingEntity> apples = new ArrayList<>();
    public static Pane pane = new Pane();
    public static Timeline timeline;
    private static final double width = Screen.getPrimary().getBounds().getWidth();
    private static final double height = Screen.getPrimary().getBounds().getHeight();
    private static final long initialGenerationSpeed = 2;
    private static final double initialFallSpeed = 5;
    private static final int pointsPerApple = 5;
    private static final double fallAcceleration = 1.02;
    private static final double generationAcceleration = 1.05;
    private static double fallSpeed = initialFallSpeed;
    private static Rectangle floor;
    private static Rectangle opacityRectangle;
    private static final double rectangleHeight = 20;
    private static Label scoreLabel;
    private static Button resetButton;
    private static final MediaPlayer backGroundMusic = MainMenu.mediaPlayer;
    private static final MediaPlayer losingMusic = MainMenu.getMediaPlayer("PacManDeath.mp3");
    private static final ImageView musicImage = MainMenu.backGroundMusic();
    static Label scoreTracker;

    /**
     * Method to get the game's main scene. The scene contains a pane with a floor.
     * Apples are added to the scene as the game progresses. The gameplay is not
     * initiated until {@code initiate()} is called.
     * @return the main {@code Scene} for the game
     */
    public static Scene gameScene() {
        floor = new Rectangle();
        floor.setWidth(width);
        floor.setHeight(rectangleHeight);
        floor.setY(height);
        floor.setX(0);
        floor.setFill(Color.TRANSPARENT);

        pane.getChildren().add(floor);

        Image image = new Image("Background.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(1.0,1.0,true,true,false,false);
        BackgroundImage backgroundImage = new BackgroundImage(
                image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        pane.setBackground(background);



        musicImage.setX(width - musicImage.getFitWidth());
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

        return new Scene(pane);
    }

    /**
     * Method to initiate the game. A {@code Timeline} is created for dropping
     * apples.
     */
    public static void initiate() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(initialGenerationSpeed), event -> {
            addApple();
            timeline.setRate(timeline.getRate() * generationAcceleration);
            fallSpeed *= fallAcceleration;
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
     * Method that renders the game over screen. It provides a label of the final
     * score and an option to reset and play again. The method plays the losing music and stops the game music
     */
    public static void gameOver() {

        backGroundMusic.stop();
        losingMusic.seek(Duration.ZERO);
        losingMusic.play();

        opacityRectangle = new Rectangle();
        opacityRectangle.setFill(Color.BLACK);
        opacityRectangle.setOpacity(0.5);
        opacityRectangle.setHeight(height);
        opacityRectangle.setWidth(width);
        opacityRectangle.setX(0);
        opacityRectangle.setY(0);
        pane.getChildren().add(opacityRectangle);


        scoreLabel = new Label("Score: " + Player.getScore());
        scoreLabel.setFont(Font.font("Rockwell Extra Bold",40));
        Rectangle scoreBox =
                new Rectangle(350, 100, Paint.valueOf("#EBA709"));
        scoreBox.setArcHeight(10);
        scoreBox.setArcWidth(10);

        StackPane boxAndScore = new StackPane(scoreBox,scoreLabel);
        boxAndScore.layoutXProperty().bind(pane.widthProperty().subtract(boxAndScore.widthProperty()).divide(2));
        boxAndScore.layoutYProperty().bind(pane.heightProperty().subtract(boxAndScore.heightProperty()).divide(4));
        pane.getChildren().add(boxAndScore);



        resetButton = MainMenu.createButton("reset.png",200,100);
        Button menuButton = MainMenu.createButton("menu.png", 200, 100);



        VBox buttons = new VBox(resetButton, menuButton);
        buttons.layoutXProperty().bind(pane.widthProperty().subtract(buttons.widthProperty()).divide(2));
        buttons.layoutYProperty().bind(pane.heightProperty().subtract(buttons.heightProperty()).divide(2));
        pane.getChildren().add(buttons);

        resetButton.setOnAction(e -> {
            opacityRectangle.setOpacity(0);
            pane.getChildren().removeAll(opacityRectangle,boxAndScore,buttons);
            scoreLabel = null;
            resetButton = null;
            opacityRectangle = null;
            scoreTracker.setText("Score: 0");
            clear();
            initiate();
        });
        menuButton.setOnAction(e ->
                {
                    GameClass.stage.setScene(MainMenu.mainMenuScene());
                    GameClass.stage.setFullScreen(true);
                    MainMenu.mediaPlayer.seek(Duration.ZERO);
                    MainMenu.mediaPlayer.play();
                    pane.getChildren().clear();
                    pane.getScene().setRoot(new Pane());
                }
        );
    }


    /**
     * Method to clear all elements on screen. Main purpose is to call when player
     * chooses to play again at the game over screen.
     */
    private static void clear() {
        while (apples.size() != 0) {
            apples.get(0).kill();
        }
        fallSpeed = initialFallSpeed;
    }

    /**
     * A private method to add an apple to the {@code ArrayList} of apples
     * and drop it using {@code FallingEntity.fall()}
     */
    private static void addApple() {
        FallingEntity fallingEntity = new FallingEntity(pointsPerApple,fallSpeed);
        apples.add(fallingEntity);
        fallingEntity.setPosition((int)(Math.random() * (width - FallingEntity.getSize())), -250);
        System.out.println(fallingEntity.getX());
        pane.getChildren().add(fallingEntity);
        fallingEntity.fall(floor);
    }

}