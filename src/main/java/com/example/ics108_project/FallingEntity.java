package com.example.ics108_project;

import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 * Class for objects that fall during the game. Objects can be
 * clicked and clicking them rewards the player with points
 * depending on the score that the object has. The higher the
 * score the faster the object falling will be. The class is a
 * subclass of {@code ImageView}.
 */
public class FallingEntity extends ImageView {
    private final int score;
    private final double speed;
    private static final Image apple = new Image("Applelogo.png");
    private TranslateTransition translateTransition;
    private RotateTransition rotateTransition;
    private AnimationTimer collisionTimer;
    private static final double height = Screen.getPrimary().getBounds().getHeight();


    /**
     * Constructs a {@code FallingEntity} with a circle shape
     * and a radius of 65, score of {@code score}, and a speed
     * of {@code speed}
     * @param score the number of points the player will receive
     *              if he clicks on the entity
     */
    public FallingEntity(int score, double speed) {
        super(apple);
        this.score = score;
        this.speed = speed;
        double size = 200;
        setPreserveRatio(true);
        setFitWidth(size);
        setOnMouseClicked(new EntityClickedEventHandler());
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
        translateTransition.setByY(height + 400);
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setRate(speed / 10.0);

        // Rotation while falling
        rotateTransition = new RotateTransition(Duration.seconds(3));
        rotateTransition.setNode(this);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);

        // Detects for collision with the floor
        collisionTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (checkForCollision(floor)) {
                    System.out.println("Collision");
                    System.out.println("Final Score: " + Player.getScore());

                    GameApp.gameOver();

                    Player.resetScore();

                    GameApp.timeline.setCycleCount(0);
                    GameApp.timeline.stop();
                    GameApp.stopAll();
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
            Player.addScore(score);
            System.out.println("Score: " + Player.getScore());
            kill();
        }
    }
}
