package com.example.ics108_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GameClass extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Final Part (At the end)

    }

    public static void main(String[] args) {
        launch();
    }

    private static Scene mainMenuScene()
    {
        //Ziad's Part
        //Create Scene and return it to the start function
        Pane mainPane = new Pane();
        Scene menuScene = new Scene(mainPane);

        return menuScene;

    }
    private static Scene gameScene()
    {
        //Al Aqsa's Part

        Pane pane = new Pane();
        Scene gameScene = new Scene(pane);

        return gameScene;
    }
}