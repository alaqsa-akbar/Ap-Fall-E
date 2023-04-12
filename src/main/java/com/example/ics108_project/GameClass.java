package com.example.ics108_project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
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

        //Change dimensions
        stage.setWidth(1000);
        stage.setHeight(600);

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
        BorderPane mainPane = new BorderPane();

        //Making small panes to add to the big pane at the end
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.TOP_RIGHT);
        topBox.setSpacing(500);

        //Add music and game name to top horizontal pane
        ImageView mainImage = new ImageView("AppleLogoNoBG.png");
        mainImage.setFitHeight(50);
        mainImage.setFitWidth(50);
        ImageView musicImageView = backGroundMusic();
        HBox.setHgrow(musicImageView,Priority.ALWAYS);
        topBox.getChildren().addAll(mainImage,musicImageView);

        //Set background image
        Image image = new Image("Background.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(1.0,1.0,true,true,false,false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        mainPane.setBackground(background);

        //
        

        //Add top pane to main pane
        mainPane.setTop(topBox);

        return new Scene(mainPane);

    }


    private static Scene gameScene()
    {
        //Al Aqsa's Part

        Pane pane = new Pane();
        Scene gameScene = new Scene(pane);

        return gameScene;
    }

    private static ImageView backGroundMusic()
    {
        //Add Background music
        File file = new File("src\\main\\resources\\SuperMario.mp3");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

        //Adding music icon to turn the music on and off
        Image musicImage = new Image("musicNoBG.png");
        Image musicOff = new Image("muteMusicNoBG.png");

        ImageView musicImageView = new ImageView(musicImage);
        musicImageView.setFitWidth(50);
        musicImageView.setFitHeight(50);
        mediaPlayer.setVolume(0.1);
        musicImageView.setOnMouseClicked(event -> {
            if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
            {
                mediaPlayer.pause();
                musicImageView.setImage(musicOff);
            }
            else
            {
                mediaPlayer.play();
                musicImageView.setImage(musicImage);
            }
        });
        return musicImageView;
    }
}