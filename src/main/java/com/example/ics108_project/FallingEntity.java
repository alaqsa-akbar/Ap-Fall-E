package com.example.ics108_project;

import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Class for objects that fall during the game. Objects can be
 * clicked and clicking them rewards the player with points
 * depending on the score that the object has. The higher the
 * score the faster the object falling will be. The class is a
 * subclass of {@code Circle}.
 */
public class FallingEntity extends Circle {
    private final int score;
    private final int speed;

    /**
     * Constructs a {@code FallingEntity} with a circle shape
     * and a radius of 65, score of {@code score}, and a speed
     * of {@code 2 * score}
     * @param score the number of points the player will receive
     *              if he clicks on the entity
     */
    public FallingEntity(int score) {
        super();
        this.score = score;
        this.speed = score * 2;
        double radius = 65;
        setRadius(radius);
        setOnMouseClicked(new EntityClickedEventHandler());
    }

    /**
     * Sets the center position of the object
     * @param x the position of the object horizontally
     * @param y the position of the object vertically
     */
    public void setCenter(int x, int y) {
        setCenterX(x);
        setCenterY(y);
    }

    /**
     * Initiates the falling animation for entity
     */
    public void fall() {
        TranslateTransition animation = new TranslateTransition(Duration.seconds(3));
        animation.setNode(this);
        animation.setByY(1920);
        animation.setCycleCount(TranslateTransition.INDEFINITE);
        animation.setRate(speed / 10.0);
        animation.play();
    }

    private class EntityClickedEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent e) {
            setVisible(false);
            //MainApp.player.addScore(score);
            //System.out.println("Score: " + MainApp.player.getScore());
        }
    }
}
