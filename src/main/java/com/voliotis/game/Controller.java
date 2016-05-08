package com.voliotis.game;

import com.voliotis.fileutils.ChooseFile;
import com.voliotis.fileutils.SLFile;
import com.voliotis.fileutils.SaveGame;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.File;
import com.voliotis.boxs.*;

public class Controller {
    public GridPane maimGridPane, nextBallGridPane;
    public Label score, round;

    private Stage window;
    private GameImageViews imageV;
    private Game game;
    private boolean isTheGameSaved = false;
    private File saveGameFile;

    public void clickMenuItemNewGame() {
        if(game != null && !isTheGameSaved) {
            Options.YNC yesNoCancel = BoxConfirm.display("New Game", "Do you want to save the game before startGame new game?");
            if (yesNoCancel == Options.YNC.YES) {
                if (saveGame(Options.SOrSAs.SAVE_AS)){
                    BoxAlert.display("Save", "Game is saved");
                    newGame();
                }
            }
            else if (yesNoCancel == Options.YNC.NO){
                newGame();
            }
        }
        else newGame();
    }

    public void clickMenuItemLoad(){
        File loadGameFile = ChooseFile.display(window, Options.SOrL.LOAD);
        if(loadGameFile != null){
            saveGameFile = loadGameFile;
            continueGame();
        }
    }

    public void clickMenuItemSave(){
        if (game != null)
            save();
    }

    public void clickMenuItemSaveAs(){
        if (game != null)
            saveGame(Options.SOrSAs.SAVE_AS);
    }

    public void clickMenuItemQuit() {
        closeProgram();
    }

    public void controlCloseProgram(){
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
    }

    private void closeProgram() {
        if (game != null && !game.getEmptyPositions().isEmpty()) {
            Options.YNC yesNoCancel = BoxConfirm.display("Exit", "Do you want to save the game before exit?");
            if (yesNoCancel == Options.YNC.YES){
                if (save()){
                    BoxAlert.display("Save", "Game is saved");
                    window.close();
                }
            }
            else if (yesNoCancel == Options.YNC.NO)
                window.close();
        }
        else
            window.close();
    }

    public void setWindow(Stage window) {
        this.window = window;
    }

    private void newGame(){
        game = new Game();
        game.startGame();
        imageV = new GameImageViews(game,maimGridPane,nextBallGridPane,score,round);
        settingsForStart();
    }

    private void continueGame(){
        SaveGame load = SLFile.loadGameFromFile(saveGameFile);
        game = new Game();
        game.continueGame(load);
        settingsForStart();
    }

    private void settingsForStart(){
        ImageView selectedImageView = new ImageView();
        selectedImageView.setImage(ImageOfBall.getNullImage());
        imageV.setSelectedImageView(selectedImageView);
        imageV.setFrom(null);
        imageV.setTo(null);
        imageV.setImageViewToNextBallGridPane();
        imageV.setImageViewAndInteractionToMainGridPane();
        imageV.setLabels();
    }

    public boolean saveGame(Options.SOrSAs saveOrSaveAs){
        SaveGame save = game.saveGame();
        File newFile = SLFile.saveGameToFile(save, saveGameFile, window, saveOrSaveAs);
        if (newFile != null) {
            saveGameFile = newFile;
            isTheGameSaved = true;
            return true;
        } else {
            return false;
        }
    }

    private boolean save(){
        return saveGame(saveGameFile == null ? Options.SOrSAs.SAVE_AS : Options.SOrSAs.SAVE);
    }
}