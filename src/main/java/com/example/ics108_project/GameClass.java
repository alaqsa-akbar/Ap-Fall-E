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
        stage.setHeight(500);

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

        //Add game name
        Text gameName = new Text("Ap-FALL-E");
        topBox.getChildren().add(gameName);

        //Add music to top horizontal pane
        ImageView musicImageView = backGroundMusic();
        HBox.setHgrow(musicImageView,Priority.ALWAYS);
        topBox.getChildren().add(musicImageView);

        //Add top pane to main pane
        mainPane.setTop(topBox);
        mainPane.setStyle("color: red");
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

    private static ImageView backGroundMusic()
    {
        //Add Background music
        File file = new File("src\\main\\resources\\PinkPanther.mp3");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

        //Adding music icon to turn the music on and off
        Image musicImage = new Image("music.png");
        Image musicOff = new Image("MuteMusic.png");
        ImageView musicImageView = new ImageView(musicImage);
        musicImageView.setFitWidth(100);
        musicImageView.setFitHeight(100);
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