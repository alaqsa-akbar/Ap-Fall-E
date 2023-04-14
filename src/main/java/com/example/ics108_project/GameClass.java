
package com.example.ics108_project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GameClass extends Application {
    @Override
    public void start(Stage stage) {

        Scene menuScene = MainMenu.mainMenuScene(stage);
        stage.setScene(menuScene);

        //Set the title as the name of the game application
        stage.setTitle("Ap-FALL-E");

        //Set the icon of the game
        Image iconImage = new Image("AppleLogoNoBG.PNG");
        stage.getIcons().add(iconImage);

        //Change dimensions
        stage.setFullScreen(true);

        //Show the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}