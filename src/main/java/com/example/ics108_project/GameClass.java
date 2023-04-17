package com.example.ics108_project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This class is the main class that runs the program and the application
 * This class sets the scene and the title along with the icon and other stage controls
 */
public class GameClass extends Application {
    //Set the stage as a static variable to allow other classes to access the stage and modify the  code
    static Stage stage;

    /**
     * The primary start method from the application class where we set the scene and icons and the title
     * @param primaryStage is the primary stage in which the program will be displayed from the Application class
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        Scene menuScene = new Scene(MainMenu.mainMenuPane());
        stage.setScene(menuScene);

        //Set the title as the name of the game application
        stage.setTitle("Ap-FALL-E");

        //Set the icon of the game
        Image iconImage = new Image("AppleLogoNoBG.PNG");
        stage.getIcons().add(iconImage);

        //Change dimensions
        stage.setMaximized(true);
        stage.setFullScreen(true);

        //Show the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}