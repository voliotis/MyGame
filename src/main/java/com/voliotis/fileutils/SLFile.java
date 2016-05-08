package com.voliotis.fileutils;

import com.google.gson.Gson;
import javafx.stage.Stage;
import java.io.*;
import java.util.logging.Logger;
import com.voliotis.game.Options;

public class SLFile {
    private static final Logger LOGGER = Logger.getLogger(SLFile.class.getName());
    private static Gson gson = new Gson();
    private SLFile(){
    }

    public static File saveGameToFile(SaveGame settings, File file, Stage stage, Options.SOrSAs saveOrSaveAs){
        File returnFile = file;
        String json = gson.toJson(settings);

        if (saveOrSaveAs.equals(Options.SOrSAs.SAVE_AS))
            returnFile = ChooseFile.display(stage, Options.SOrL.SAVE);
        if (returnFile == null)
            return null;
        try {
            FileWriter writer = new FileWriter(returnFile);
            writer.write(json);
            writer.close();
        } catch (IOException e){
            LOGGER.warning("Failed to save the game. " + e);
        }
        return returnFile;
    }

    public static SaveGame loadGameFromFile(File file){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return gson.fromJson(reader, SaveGame.class);
        } catch (IOException e) {
            LOGGER.warning("Failed to load the game. " + e);
            return  null;
        }
    }

    public static void saveHighScoreToFile(SaveHScore highScores){
        String json = gson.toJson(highScores);
        File file = new File(
                System.getProperty("user.dir") +
                 Options.HIGH_SCORE_FILE);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e){
            LOGGER.warning("Failed to save high score. " + e);
        }
    }

    public static SaveHScore loadHighScoreFromFile(){
        SaveHScore highScores = new SaveHScore();
        File file = new File(
                System.getProperty("user.dir") +
                        Options.HIGH_SCORE_FILE);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            highScores = gson.fromJson(reader, SaveHScore.class);
        }catch (IOException e) {
            LOGGER.warning("Failed to load high score from file. " + e);
        }
        return highScores;
    }

}
