package com.voliotis.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage window) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Options.MAIN_FXML_FILE_NAME));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setWindow(window);

        Scene scene = new Scene(root, 570, 490);
        scene.getStylesheets().add(getClass().getResource(Options.MAIN_CSS_FILE_NAME).toExternalForm());

        window.setTitle("Ball GameField!!!");
        window.setScene(scene);
        window.show();
        controller.controlCloseProgram();
    }

    public static void main(String [] args) {
        launch(args);
    }
}