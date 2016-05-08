package com.voliotis.game;

import com.voliotis.boxs.BoxAlert;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

public class GameImageViews {
    private GridPane maimGridPane;
    private GridPane nextBallGridPane;
    private Label score;
    private Label round;
    private ImageView[][] imageViews;
    private ImageView [] imageViewsNextBall;
    private static final int FIT = 42;
    private Game game;
    private Position from;
    private Position to;
    private ImageView selectedImageView;

    public GameImageViews(Game game, GridPane maimGridPane, GridPane nextBallGridPane, Label score, Label round){
        imageViews = new ImageView [9][9];
        imageViewsNextBall = new ImageView[3];
        this.game = game;
        this.maimGridPane = maimGridPane;
        this.nextBallGridPane = nextBallGridPane;
        this.score = score;
        this.round = round;
    }

    public void setImageViewToNextBallGridPane(){
        clearNextBallGridPane(nextBallGridPane);
        List<Color> newBallsColor = game.getNextBallsColor();
        for (int i = 0; i < newBallsColor.size(); i++){
            imageViewsNextBall[i] =  new ImageView();
            imageViewsNextBall[i].setImage(ImageOfBall.getImage(newBallsColor.get(i), Options.ImageType.IMAGE_TYPE_A));
            imageViewsNextBall[i].setFitHeight(FIT);
            imageViewsNextBall[i].setFitWidth(FIT);
            GridPane.setHalignment(imageViewsNextBall[i], HPos.CENTER);
            nextBallGridPane.add(imageViewsNextBall[i], 0, i);
        }
    }

    private void clearNextBallGridPane(GridPane nextBallGridPane){
        for (int i = 0; i < 3; i++)
            nextBallGridPane.getChildren().remove(imageViewsNextBall[i]);
    }

    public void setImageViewAndInteractionToMainGridPane(){
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

    private void setImageViews(Image imageOfBall, Position position){
        int x = position.getX();
        int y = position.getY();
        imageViews[x][y] =  new ImageView();
        imageViews[x][y].setImage(imageOfBall);
        imageViews[x][y].setFitHeight(42);
        imageViews[x][y].setFitWidth(42);
        GridPane.setHalignment(imageViews[x][y], HPos.CENTER);
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

    public void setLabels(){
        score.setText("Score: " + game.getScore());
        round.setText("Round: " + game.getRound());
    }

    public void setTo(Position to) {
        this.to = to;
    }

    public void setFrom(Position from) {
        this.from = from;
    }

    public void setSelectedImageView(ImageView selectedImageView) {
        this.selectedImageView = selectedImageView;
    }
}
