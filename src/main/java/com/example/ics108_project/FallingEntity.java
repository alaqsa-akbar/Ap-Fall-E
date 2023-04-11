package com.example.ics108_project;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Paint;

public class FallingEntity {
    private final int score;
    private final int speed;
    private final double radius;
    Circle circle;

    public FallingEntity(int score) {
        this.score = score;
        this.speed = score * 2;
        this.radius = 200;
        createCircle();
    }

    public Circle getCircle() {
        return circle;
    }


    private void createCircle() {
        circle = new Circle(radius);
        circle.setOnMouseClicked(new EntityClickedEventHandler());
    }


    public void setCenter(int x, int y) {
        circle.setCenterX(x);
        circle.setCenterY(y);
    }

    public void setColor(Paint color) {
        circle.setFill(color);
    }

    class EntityClickedEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            circle.setVisible(false);
        }
    }
}
