package com.example.ics108_project;

import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.File;
import java.util.Random;


/**
 * Class for objects that fall during the game. Objects can be
 * clicked and clicking them rewards the player with points
 * depending on the score that the object has. The higher the
 * score the faster the object falling will be. The class is a
 * subclass of {@code ImageView}.
 */
public class FallingEntity extends ImageView {
    // Instance variables

    // The score of a golden apple is variable every time
    // so there is no instance variables that resemble its scores
    //The golden apple score ranges from 10 to 20 based on a normal distribution
    private final int score = 5;
    private final double speed;
    private final boolean isGoldenApple;

    private static final double GOLDEN_APPLE_RARITY = 0.075;


    // Screen Dimensions
    private static final double HEIGHT = Screen.getPrimary().getBounds().getHeight();
    private static final double WIDTH = Screen.getPrimary().getBounds().getWidth();

    // Animations
    private TranslateTransition translateTransition;
    private RotateTransition rotateTransition;
    private AnimationTimer collisionTimer;

    // Display features
    private static final MediaPlayer soundEffect = MainMenu.getMediaPlayer("AppleClickSound.mp3");
    private static final Image regularAppleImage = new Image("Applelogo.png");
    private static final Image goldenAppleImage = new Image(new File("GoldenApple.png").toURI().toString());
    private double size = WIDTH / 15;



    /**
     * Constructs a {@code FallingEntity} with a circle shape
     * and a radius of 65, score of , and a speed
     * of {@code speed}. The apple can also be golden to get a score
     * of 15.
     * @param speed the speed of falling for the apple
     */
    public FallingEntity(double speed) {
        super();
        isGoldenApple = Math.random() <= GOLDEN_APPLE_RARITY;

        if (isGoldenApple)
            setImage(goldenAppleImage);

        else
            setImage(regularAppleImage);

        this.speed = speed;
        setPreserveRatio(true);
        if(isGoldenApple)
            size /= 2;
        setFitWidth(size);
        setOnMouseMoved(e -> this.setStyle("-fx-cursor: hand;"));
        setOnMouseClicked(new EntityClickedEventHandler());
    }

    /**
     * Method to get the size (width of the {@code FallingEntity})
     * @return the width of the {@code FallingEntity}
     */
    public double getSize() {
        return size;
    }

    /**
     * Sets the position of the object
     * @param x the position of the object horizontally
     * @param y the position of the object vertically
     */
    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Initiates the falling animation for entity. The falling animation is composed
     * of a translation and rotation. This method also initiates the call to check
     * for collision with the floor.
     *
     * @param floor rectangle at the bottom of the screen to check for collisions
     */
    public void fall(Rectangle floor) {
        // Translation (the falling animation)
        translateTransition = new TranslateTransition(Duration.seconds(3));
        translateTransition.setNode(this);
        translateTransition.setByY(HEIGHT + 400);
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setRate(speed / 10.0);

        // Rotation while falling
        rotateTransition = new RotateTransition(Duration.seconds(6));
        rotateTransition.setNode(this);
        rotateTransition.setByAngle(720);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        translateTransition.setRate(speed / 10.0);

        // Detects for collision with the floor
        collisionTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (checkForCollision(floor)) {
                    kill();
                    GameApp.checkForGameOver();
                }
            }
        };

        // Initiating the animations
        translateTransition.play();
        rotateTransition.play();
        collisionTimer.start();
    }

    /**
     * Method to stop all processes. These include the translation, the rotation,
     * collision checker, and click detection.
     */
    public void stopProcesses() {
        translateTransition.stop();
        rotateTransition.stop();
        collisionTimer.stop();
        setOnMouseClicked(null);
    }

    /**
     * When called, the object is set to null and removed from the scene.
     * All processes are stopped to make it eligible for garbage collection.
     */
    public void kill() {
        stopProcesses();

        // Removes all instances of the object and sets it to null for garbage collection
        int index = GameApp.apples.indexOf(this);
        GameApp.apples.set(index, null);
        GameApp.apples.remove(index);
        GameApp.pane.getChildren().remove(this);
    }

    /**
     * Checks for collision with the floor (bottom of the screen)
     * @param floor rectangle at the bottom of the screen to check for collisions
     * @return {@code true} if collided with the floor, {@code false} otherwise
     */
    private boolean checkForCollision(Rectangle floor) {
        return getBoundsInParent().intersects(floor.getBoundsInParent());
    }

    /**
     * Private class to handle clicking on a {@code FallingEntity}. Score
     * will be added to the player and the object will be killed.
     */
    private class EntityClickedEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent e) {
            setVisible(false);
            if(isGoldenApple)
                Player.addScore(Math.min((int) Math.abs(new Random().nextGaussian() * 5) + 10,20));
            else Player.addScore(score);
            GameApp.scoreTracker.setText("Score: " + Player.getScore());
            soundEffect.play();
            soundEffect.setOnEndOfMedia(soundEffect::stop);
            kill();

            GameApp.checkForGameOver();
        }
    }
}