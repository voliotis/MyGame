package com.voliotis.file.controler;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import com.voliotis.game.Options;

public class ChooseFile {

    private static final FileChooser fileChooser = new FileChooser();

    static {
        String title = "Open Resource File";
        FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON file","*.json");
        File userDir = new File(System.getProperty("user.dir"));
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(jsonFilter);
        fileChooser.setInitialDirectory(userDir);
    }

    private ChooseFile(){
    }

    public static File display(Stage window, Options.SOrL saveOrLoad){
        if (saveOrLoad == Options.SOrL.LOAD)
            return fileChooser.showOpenDialog(window);
        else
            return fileChooser.showSaveDialog(window);
    }

}