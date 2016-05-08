package com.voliotis.boxs;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.voliotis.game.Options;

public class BoxConfirm {
    private static Options.YNC answer;
    private BoxConfirm(){}

    public static Options.YNC display(String title, String message) {
        Stage window = new Stage();
        Label label = new Label(message);

        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            answer = Options.YNC.YES;
            window.close();
        });
        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            answer = Options.YNC.NO;
            window.close();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            answer = Options.YNC.CANCEL;
            window.close();
        });

        VBox vBox = new VBox(10);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(yesButton, noButton, cancelButton);
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label, hBox);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);

        window.setScene(scene);
        window.setTitle(title);
        window.setMinWidth(350);
        window.initModality(Modality.APPLICATION_MODAL);
        window.resizableProperty().setValue(Boolean.FALSE);
        window.setOnCloseRequest(Event::consume);
        window.showAndWait();

        return answer;
    }

}