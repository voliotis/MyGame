package com.voliotis.boxs;

import com.voliotis.fileutils.HighScore;
import com.voliotis.fileutils.SLFile;
import com.voliotis.fileutils.SaveHScore;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class BoxAlert{
    private static final Logger LOGGER = Logger.getLogger(SLFile.class.getName());
    private BoxAlert(){}
    public GridPane mainGridPane;

    public static void display(String title, String message) {
        Stage window = new Stage();
        Label label = new Label(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        VBox layoutV = new VBox(10);
        HBox layoutH = new HBox(5);
        layoutH.getChildren().add(closeButton);
        layoutV.getChildren().addAll(label, layoutH);
        layoutV.setAlignment(Pos.CENTER);
        layoutH.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layoutV);

        setWindowSettings(window,scene,title);
    }

    public static void displayGameOver(String title, String  round, String  score) {
        int r = Integer.parseInt(round);
        int s = Integer.parseInt(score);
        Stage window = new Stage();
        Label label = new Label();
        VBox layoutV = new VBox(10);
        HBox layoutH = new HBox(5);

        SaveHScore load = SLFile.loadHighScoreFromFile();

        if (load.isInTopTen(s)) {
            label.setText(
                    "The game is over.\n" +
                            "You finish the game in " + round + " rounds.\n" +
                            "Your Score is " + score + ".\n" +
                            "Your Score is enter in top ten Scores!!!"
            );

            TextField nickname = new TextField("Enter your Nickname...");
            nickname.setMaxWidth(185);

            Button saveScoreButton = new Button("Save Score");
            saveScoreButton.setOnAction(event -> {
                HighScore highScore = new HighScore(nickname.getText(), s, r);
                load.updateHighScore(highScore);
                window.close();
                try {
                    displayHighScore(load);
                } catch (IOException e) {
                    LOGGER.warning("Failed to run displayHighScore. " + e);
                }
            });

            layoutH.getChildren().add(saveScoreButton);
            layoutV.getChildren().addAll(label, nickname, layoutH);
        } else {
            label.setText(
                    "The game is over.\n" +
                            "You finish the game in " + round + " rounds.\n" +
                            "Your Score is " + score + ".\n" +
                            "Your score isn't enter in high score.\n" +
                            "The Limit is " + SaveHScore.getLimit() + ". Tray again!!!"
            );
            layoutV.getChildren().addAll(label, layoutH);
        }

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        layoutH.getChildren().add(closeButton);
        layoutV.setAlignment(Pos.CENTER);
        layoutH.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layoutV);

        setWindowSettings(window,scene,title);
    }

    public static void displayHighScore(SaveHScore saveMode) throws IOException{
        List<HighScore> highScores = saveMode.getHighScores();
        String title = "High Scores...";
        Stage window = new Stage();

        FXMLLoader loader = new FXMLLoader(BoxAlert.class.getResource("/HighScoreStage.fxml"));
        Parent root = loader.load();
        BoxAlert controller = loader.getController();

        for (int i = 0; i < highScores.size() && i < 10; i++) {
            Label leftLabel = new Label(highScores.get(i).getName());
            Label centerLabel = new Label(Integer.toString(highScores.get(i).getScore()));
            Label rightLabel = new Label(Integer.toString(highScores.get(i).getRound()));
            controller.mainGridPane.add(leftLabel, 0, i+1);
            GridPane.setHalignment(leftLabel, HPos.CENTER);
            controller.mainGridPane.add(centerLabel,1,i+1);
            GridPane.setHalignment(centerLabel, HPos.CENTER);
            controller.mainGridPane.add(rightLabel,2,i+1);
            GridPane.setHalignment(rightLabel, HPos.CENTER);
        }

        Scene scene = new Scene(root, 300, 490);

        setWindowSettings(window,scene,title);
    }

    private static void setWindowSettings(Stage window, Scene scene, String title){
        window.setScene(scene);
        window.initModality(Modality.APPLICATION_MODAL);
        window.resizableProperty().setValue(Boolean.FALSE);
        window.setTitle(title);
        window.setMinWidth(250);
        window.showAndWait();
    }
}