package com.voliotis.game;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;
import com.voliotis.boxs.*;
import com.voliotis.file.controler.*;

public class Controller {
    public GridPane maimGridPane, nextBallGridPane;
    public Label score, round;

    private Stage window;
    private ImageView[][] imageViews = new ImageView [9][9];
    private ImageView [] imageViewsNextBall = new ImageView[3];
    private ImageView selectedImageView = new ImageView();
    private Game game;
    private Position from;
    private Position to;
    private int fit = 42;
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
        settingsForStart();
    }

    private void continueGame(){
        SaveGame load = SLFile.loadGameFromFile(saveGameFile);
        game = new Game();
        game.continueGame(load);
        settingsForStart();
    }

    private void settingsForStart(){
        selectedImageView.setImage(ImageOfBall.getNullImage());
        from = null;
        to = null;
        setImageViewToNextBallGridPane();
        setImageViewAndInteractionToMainGridPane();
        setLabels();
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

    private void setImageViewToNextBallGridPane(){
        clearNextBallGridPane();
        List<Color> newBallsColor = game.getNextBallsColor();
        for (int i = 0; i < newBallsColor.size(); i++){
            imageViewsNextBall[i] =  new ImageView();
            imageViewsNextBall[i].setImage(ImageOfBall.getImage(newBallsColor.get(i), Options.ImageType.IMAGE_TYPE_A));
            imageViewsNextBall[i].setFitHeight(fit);
            imageViewsNextBall[i].setFitWidth(fit);
            nextBallGridPane.add(imageViewsNextBall[i], 0, i);
            GridPane.setHalignment(imageViewsNextBall[i], HPos.CENTER);
        }
    }

    private void setImageViewAndInteractionToMainGridPane(){
        clearMainGridPane();
        List<Position> ballPositions = game.getBallsPositions();
        List<Position> emptyPositions = game.getEmptyPositions();
        ballPositions.forEach(p -> {
            Color color = game.getBallsColorFrom(p);
            Image imageOfBall = ImageOfBall.getImage(color, Options.ImageType.IMAGE_TYPE_A);
            setImageViews(imageOfBall, p);
            maimGridPane.add(imageViews[p.getX()][p.getY()], p.getY(), p.getX());
            mouseClickedOnABall(p);
        });
        emptyPositions.forEach(p -> {
            Image imageOfBall = ImageOfBall.getNullImage();
            setImageViews(imageOfBall, p);
            maimGridPane.add(imageViews[p.getX()][p.getY()], p.getY(), p.getX());
            mouseEnteredOnPositionWithoutBall(p);
            mouseExitedOnPositionWithoutBall(p);
            mouseClickedOnPositionWithoutBall(p);
        });
    }

    private void setLabels(){
        score.setText("Score: " + game.getScore());
        round.setText("Round: " + game.getRound());
    }

    private void setImageViews(Image imageOfBall, Position position){
        int x = position.getX();
        int y = position.getY();
        imageViews[x][y] =  new ImageView();
        imageViews[x][y].setImage(imageOfBall);
        imageViews[x][y].setFitHeight(fit);
        imageViews[x][y].setFitWidth(fit);
        GridPane.setHalignment(imageViews[x][y], HPos.CENTER);
    }

    private void clearNextBallGridPane(){
        for (int i = 0; i < 3; i++)
            nextBallGridPane.getChildren().remove(imageViewsNextBall[i]);
    }

    private void clearMainGridPane(){
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                maimGridPane.getChildren().remove(imageViews[i][j]);
    }

    private void mouseClickedOnABall(Position position){
        int x = position.getX();
        int y = position.getY();
        Color colorFromPosition = game.getBallsColorFrom(position);
        imageViews[x][y].setOnMouseClicked(e -> {
            if (from != null) {
                Color color = game.getBallsColorFrom(from);
                imageViews[from.getX()][from.getY()].setImage(ImageOfBall.getImage(color, Options.ImageType.IMAGE_TYPE_A));
            }
            imageViews[x][y].setImage(ImageOfBall.getImage(colorFromPosition, Options.ImageType.IMAGE_TYPE_B));
            selectedImageView = imageViews[x][y];
            from = new Position(x, y);
        });
    }

    private void mouseClickedOnPositionWithoutBall(Position position){
        imageViews[position.getX()][position.getY()].setOnMouseClicked(imageClick -> {
            if (isMovePermissible(position)) {
                to = new Position(position.getX(), position.getY());
                ImageView imageView = (ImageView) imageClick.getSource();
                if (!(selectedImageView.getImage().equals(ImageOfBall.getNullImage()))) {
                    imageView.setImage(selectedImageView.getImage());
                    selectedImageView.setImage(ImageOfBall.getNullImage());
                    if(!game.moveAndCheckGameOver(from, to)) {
                        refreshPanel();
                    }
                    else {
                        refreshPanel();
                        BoxAlert.displayGameOver("Game Over!!!", game.getRound(), game.getScore());
                    }
                }
            }
        });
    }

    private void mouseEnteredOnPositionWithoutBall(Position position){
        imageViews[position.getX()][position.getY()].setOnMouseEntered(e -> {
            to = new Position(position.getX(), position.getY());
            if (isMovePermissible(position)) {
                game.getLastCheckedWalkablePath().forEach(p -> imageViews[p.getX()][p.getY()].setImage(ImageOfBall.getNullImage2()));
            }
        });
    }

    private void mouseExitedOnPositionWithoutBall(Position position){
        imageViews[position.getX()][position.getY()].setOnMouseExited(e -> {
            if (isMovePermissible(position)) {
                game.getLastCheckedWalkablePath().forEach(p -> imageViews[p.getX()][p.getY()].setImage(ImageOfBall.getNullImage()));
            }
        });
    }

    private boolean isMovePermissible(Position position){
        return from != null && game.isWalkablePath(from,position);
    }

    private void refreshPanel(){
        to = null;
        from = null;
        setLabels();
        setImageViewToNextBallGridPane();
        setImageViewAndInteractionToMainGridPane();
    }
}