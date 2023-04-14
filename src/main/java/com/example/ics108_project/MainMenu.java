package com.example.ics108_project;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

public class MainMenu {

    /**
     * This method creates the main menu scene with all the components found
     * The method returns the scene with the title , game buttons , music buttons and the app creator name
     * @param stage is the main stage on which the application will run
     * @return the main menu scene that contains all the components
     */
    static Scene mainMenuScene(Stage stage)
    {
        //Create the main pane
        BorderPane mainPane = new BorderPane();

        //Making small panes to add to the big pane at the end
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.TOP_RIGHT);
        topBox.setSpacing(420);

        //Add music and game name to top horizontal pane
        ImageView mainImage = new ImageView("ZiadAppleLogo.png");
        mainImage.setFitHeight(350);
        mainImage.setFitWidth(350);
        ImageView musicImageView = backGroundMusic();

        HBox.setHgrow(musicImageView, Priority.ALWAYS);
        HBox.setHgrow(mainImage, Priority.ALWAYS);
        topBox.getChildren().addAll(mainImage,musicImageView);

        //Add top pane to main pane
        mainPane.setTop(topBox);



        //Set background image
        Image image = new Image("Background.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(1.0,1.0,true,true,false,false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        mainPane.setBackground(background);



        //Start Button
        ImageView startImage = new ImageView("StartButton.png");
        startImage.setFitHeight(110);
        startImage.setFitWidth(200);
        Button startButton = new Button("",startImage);
        startButton.setStyle("-fx-background-color: transparent;-fx-cursor: hand;");//Transparent
        startButton.setOnAction(e -> {
            musicImageView.get
            stage.setScene(GameApp.gameScene());
            GameApp.initiate();
            stage.setFullScreen(true);
        });

        //Quit Button
        ImageView quitImage = new ImageView("QuitButton.png");
        quitImage.setFitHeight(90);
        quitImage.setFitWidth(180);
        Button quitButton = new Button("",quitImage);
        quitButton.setStyle("-fx-background-color: transparent;-fx-cursor: hand;");//Transparent
        quitButton.setOnAction(e -> Platform.exit());//Exit program when clicked

        //Add Buttons To Pane
        VBox buttons = new VBox(startButton,quitButton);
        mainPane.setCenter(buttons);
        buttons.setAlignment(Pos.CENTER);

        Text aboutText = new Text("Ap-FALL-E ICS 108 Project by Al Aqsa Akbar and Ziad Al-Alami");
        aboutText.setFill(Paint.valueOf("blue"));
        aboutText.setFont(Font.font("Rockwell Extra Bold"));
        mainPane.setBottom(aboutText);
        BorderPane.setAlignment(aboutText,Pos.BOTTOM_CENTER);


        return new Scene(mainPane);
    }

    /**
     * Creates background music for the main menu scene of the app
     * @return an ImageView object that can turn on and off the music when clicked on
     */
    static ImageView backGroundMusic()
    {
        //Create a MediaPlayer object that plays the music and set auto play to true with infinite repitition
        File file = new File("src\\main\\resources\\SuperMario.mp3");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(Timeline.INDEFINITE);

        //Adding music icon to turn the music on and off
        Image musicImage = new Image("musicNoBG.png");
        Image musicOff = new Image("muteMusicNoBG.png");

        //Create an ImageView object that displays the image and the current status of the background music
        ImageView musicImageView = new ImageView(musicImage);
        musicImageView.setFitWidth(70);
        musicImageView.setFitHeight(70);
        musicImageView.setStyle("-fx-cursor: hand;");
        mediaPlayer.setVolume(0.1);

        //Turn on and off the music when clicked on
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
