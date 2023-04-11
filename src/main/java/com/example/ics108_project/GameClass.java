package com.example.ics108_project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GameClass extends Application {
    @Override
    public void start(Stage stage) throws IOException,URISyntaxException {
        //Final Part (At the end)
        Scene menuScene = mainMenuScene();
        Scene gameScene = gameScene();

        stage.setScene(menuScene);

        //Decide the name of the game??
        stage.setTitle("Ap-FALL-E");

        //Set the icon of the game
        Image iconImage = new Image("com/example/ics108_project/AppleLogo.PNG");
        stage.getIcons().add(iconImage);

        //Show the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


    private static Scene mainMenuScene() throws URISyntaxException
    {
        //Ziad's Part
        //Create Scene and return it to the start function
        Pane mainPane = new Pane();
        File file = new File("src\\main\\resources\\PinkPanther.mp3");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

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