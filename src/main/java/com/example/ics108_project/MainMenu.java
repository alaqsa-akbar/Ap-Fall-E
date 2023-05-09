package com.example.ics108_project;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;
import java.io.File;
import java.util.Objects;
import java.util.Scanner;


/**
 * This class displays the main menu and all of its components such as the start, scores, and quit options
 * This class also offers static objects and methods to help other classes such as {@code  GameApp} in reusing the code
 * by entering specific parameters
 */
public class MainMenu {
    final static MediaPlayer mediaPlayer = getMediaPlayer("SuperMario.mp3");
    private static ImageView musicImageView;
    private static final double height = Screen.getPrimary().getBounds().getHeight();
    private static final double width = Screen.getPrimary().getBounds().getWidth();
    final static Background backGround = createBackGround();

    /**
     * Creates the main menu pane with all the components found
     * The method returns the pane with the title , game buttons , music buttons and the app creator name
     * @return the main menu pane that contains all the components
     */
    static Pane mainMenuPane()
    {
        //Creating main pane
        BorderPane mainPane = new BorderPane();

        //Creating top box
        HBox topBox = new HBox();

        //Add music and game name to top horizontal pane
        ImageView mainImage = new ImageView("ZiadAppleLogo.png");
        mainImage.setPreserveRatio(true);
        mainImage.setFitWidth(width / 4.3);
        ImageView musicImageView = backGroundMusic();

        //Making small panes to add to the big pane at the end
        topBox.setAlignment(Pos.TOP_RIGHT);
        topBox.setSpacing((width - mainImage.getFitWidth()) / 2 - musicImageView.getFitWidth());

        topBox.getChildren().addAll(mainImage,musicImageView);

        //Add top pane to main pane
        mainPane.setTop(topBox);



        //Set background image
        mainPane.setBackground(backGround);



        //Start Button
        Button startButton = createButton("StartButton.png",200, 110);
        startButton.setOnAction(e -> {
            mediaPlayer.seek(Duration.ZERO);
            GameClass.stage.getScene().setRoot(GameApp.gameScene());
            GameApp.initiate();
        });

        //Scores Button
        Button scoreButton = createButton("Score.png",200,95);
        scoreButton.setOnMouseClicked(e ->
                GameClass.stage.getScene().setRoot(scorePane())
        );

        //How To Play Button
        Button helpButton = createButton("HelpNoBG.jpg", 220, 140);
        helpButton.setOnMouseClicked(e ->
                GameClass.stage.getScene().setRoot(guidePane()));


        //Quit Button
        Button quitButton = createButton("QuitButton.png",180,85);
        quitButton.setOnAction(e -> Platform.exit());//Exit program when clicked

        //Add Buttons To Pane
        VBox buttons = new VBox(startButton,scoreButton,helpButton,quitButton);
        mainPane.setCenter(buttons);
        buttons.setAlignment(Pos.CENTER);

        Text aboutText = new Text("Ap-FALL-E ICS 108 Project by Al Aqsa Akbar and Ziad Al-Alami");
        aboutText.setFill(Paint.valueOf("cyan"));
        aboutText.setFont(Font.font("Rockwell Extra Bold",30));
        mainPane.setBottom(aboutText);
        BorderPane.setAlignment(aboutText,Pos.BOTTOM_CENTER);


        return mainPane;
    }

    /**
     * Creates background music for the main menu pane of the app
     * @return an ImageView object that can turn on and off the music when clicked on
     */
    static ImageView backGroundMusic()
    {
        //Plays the music and set autoplay to true with infinite repetition
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(Timeline.INDEFINITE);

        //Adding music icon to turn the music on and off
        Image musicImage = new Image("musicNoBG.png");
        Image musicOff = new Image("muteMusicNoBG.png");

        //Create an ImageView object that displays the image and the current status of the background music
        musicImageView = new ImageView(musicImage);
        musicImageView.setPreserveRatio(true);
        musicImageView.setFitWidth(width / 15.4);
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
    /**
     * Creates a MediaPlayer object that for any given resource in the directory
     * @return the MediaPlayer object
     */
    static MediaPlayer getMediaPlayer(String mediaName)
    {
        Media media = new Media(Objects.requireNonNull(MainMenu.class.getClassLoader().getResource(mediaName)).toExternalForm());
        return new MediaPlayer(media);
    }

    /**
     * Creates the scene in which the top 5 scores will be displayed
     * and offering the player a chance to reset all scores
     * The menu button returns the player to the main menu without any extra action
     * The YES button clears all the top 5 scores data of the player, allowing the player to start from the beginning
     * @return the scene in which the top 5 scores are displayed along with the objects mentioned
     */
    private static Pane scorePane()
    {
        Pane pane = new Pane();
        pane.setBackground(backGround);
        Rectangle opacityRectangle = new Rectangle();
        opacityRectangle.setFill(Color.BLACK);
        opacityRectangle.setOpacity(0.5);
        opacityRectangle.setHeight(height);
        opacityRectangle.setWidth(width);
        opacityRectangle.setX(0);
        opacityRectangle.setY(0);
        pane.getChildren().add(opacityRectangle);

        StackPane scoresBoard = new StackPane();
        Rectangle scoreRectangle = new Rectangle(650,650,Paint.valueOf("#FCBA03"));
        Scanner scoreScanner = Player.scoreFileScanner();
        assert scoreScanner != null;
        Label topScore = new Label("Top Score: " + scoreScanner.next() + "\n");
        Label secondScore = new Label("Second Top Score: " + scoreScanner.next() +"\n");
        Label thirdScore = new Label("Third Top Score: " + scoreScanner.next() + "\n");
        Label fourthScore = new Label("Fourth Top Score: " + scoreScanner.next() + "\n");
        Label fifthScore = new Label("Fifth Top Score: " + scoreScanner.next() + "\n");
        Label clearScore = new Label("Clear the scores?");
        VBox scoresBox = new VBox(topScore,secondScore,thirdScore,fourthScore,fifthScore,clearScore);
        scoresBox.setAlignment(Pos.CENTER);
        for(Node node : scoresBox.getChildren())
            ((Label) node).setFont(Font.font("Rockwell Extra Bold",40));

        scoresBoard.getChildren().addAll(scoreRectangle,scoresBox);

        Button menuButton = createButton("menu.png",200,100);
        menuButton.setOnMouseClicked(e ->
                GameClass.stage.getScene().setRoot(mainMenuPane()));
        Button yesClear = createButton("yes.png",200,100);
        yesClear.setOnMouseClicked(e ->
        {
            Player.clearData();
            topScore.setText("Top Score: 0");
            secondScore.setText("Second Top Score: 0");
            thirdScore.setText("Third Top Score: 0");
            fourthScore.setText("Fourth Top Score: 0");
            fifthScore.setText("Fifth Top Score: 0");
        });
        HBox buttonsBox = new HBox();
        buttonsBox.getChildren().addAll(yesClear,menuButton);
        buttonsBox.setAlignment(Pos.BASELINE_CENTER);
        buttonsBox.setSpacing(200);

        VBox allNodes = new VBox(scoresBoard,buttonsBox);
        allNodes.layoutXProperty().bind(pane.widthProperty().subtract(allNodes.widthProperty()).divide(2));
        allNodes.layoutYProperty().bind(pane.heightProperty().subtract(allNodes.heightProperty()).divide(2));
        pane.getChildren().add(allNodes);

        return pane;
    }

    private static Pane guidePane()
    {
        ImageView goldenApple = new ImageView(new File("GoldenApple.png").toURI().toString());
        goldenApple.setFitWidth(Screen.getPrimary().getBounds().getWidth() / 18);
        goldenApple.setFitHeight(Screen.getPrimary().getBounds().getHeight()/9);

        ImageView apple = new ImageView("Applelogo.png");
        apple.setFitWidth(Screen.getPrimary().getBounds().getWidth() / 15);
        apple.setFitHeight(Screen.getPrimary().getBounds().getHeight()/7);

        Pane pane = new Pane();
        pane.setBackground(backGround);
        Rectangle opacityRectangle = new Rectangle();
        opacityRectangle.setFill(Color.BLACK);
        opacityRectangle.setOpacity(0.5);
        opacityRectangle.setHeight(height);
        opacityRectangle.setWidth(width);
        opacityRectangle.setX(0);
        opacityRectangle.setY(0);
        pane.getChildren().add(opacityRectangle);

        Label instructions = new Label("Instructions");
        instructions.setTextFill(Paint.valueOf("#096AE0"));
        Label mainIdea = new Label("You will have 50 falling apples from the top, try to click them all");
        Label apples = new Label("There are two types of apples with different scores: Normal and Golden Apples");
        Label normalApples = new Label("Normal apples are large red apples with 5 points each",apple);
        Label goldenApples = new Label("Golden apples are small golden apples with 15 points each",goldenApple);
        Label goldenPoints = new Label(
                "Golden apples points are normally distributed with a mean of 10 and STD of 5");
        VBox textBox = new VBox(instructions,mainIdea,apples,normalApples,goldenApples,goldenPoints);
        textBox.setAlignment(Pos.CENTER);

        Rectangle rectangle = new Rectangle(1000,550,Paint.valueOf("#FCBA03"));

        for(Node node : textBox.getChildren())
            ((Label) node).setFont(Font.font("Rockwell Extra Bold",20));

        StackPane stackPane = new StackPane(rectangle,textBox);
        stackPane.setAlignment(Pos.CENTER);

        Button menu = createButton("menu.png",200,100);
        menu.setOnMouseClicked(e -> GameClass.stage.getScene().setRoot(mainMenuPane()));
        VBox allPane = new VBox(stackPane,menu);
        allPane.layoutXProperty().bind(pane.widthProperty().subtract(allPane.widthProperty()).divide(2));
        allPane.layoutYProperty().bind(pane.heightProperty().subtract(allPane.heightProperty()).divide(2));
        pane.getChildren().add(allPane);

        return pane;
    }

    /**
     * Generates a button from the image and gives it some styling
     * @param imageName the name of the image which the button is created from as in the directory
     * @param width the desired width of the button
     * @param height the desired height of the button
     * @return a Button instance with the given image , width and height properties with CSS styling
     */
    static Button createButton(String imageName, int width, int height)
    {
        ImageView imageView;
        if(imageName.equals("HelpNoBG.jpg")) {
            imageView = new ImageView(new File(imageName).toURI().toString());
            imageView.setImage(new Image(new File(imageName).toURI().toString()));
        }

        else imageView = new ImageView(imageName);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        Button button = new Button("",imageView);
        button.setStyle("-fx-background-color: transparent;-fx-cursor: hand;");//Transparent
        return button;
    }

    /**
     * Creates the same background image for any scene, this method is supposed to reduce the number lines of code
     * @return the BackGround instance that displays the background image
     */
    static Background createBackGround()
    {
        Image image = new Image("Background.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(1.0,1.0,true,true,false,false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        return new Background(backgroundImage);
    }

}
