package com.voliotis.game;

import com.voliotis.boxs.BoxAlert;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

public class GamePane {
    private static final int FIT = 42;
    private Controller controller;
    private ImageView[][] imageViews;
    private ImageView [] imageViewsNextBall;
    private Position from;
    private Position to;
    private ImageView selectedImageView;

    public GamePane(Controller controller){
        this.controller = controller;
        imageViews = new ImageView [9][9];
        imageViewsNextBall = new ImageView[3];
    }

    public void setImageViewToNextBallGridPane(){
        clearNextBallGridPane(controller.nextBallGridPane);
        List<Color> newBallsColor = controller.getGame().getNextBallsColor();
        for (int i = 0; i < newBallsColor.size(); i++){
            imageViewsNextBall[i] =  new ImageView();
            imageViewsNextBall[i].setImage(ImageOfBall.getImage(newBallsColor.get(i), Options.ImageType.IMAGE_TYPE_A));
            imageViewsNextBall[i].setFitHeight(FIT);
            imageViewsNextBall[i].setFitWidth(FIT);
            GridPane.setHalignment(imageViewsNextBall[i], HPos.CENTER);
            controller.nextBallGridPane.add(imageViewsNextBall[i], 0, i);
        }
    }

    private void clearNextBallGridPane(GridPane nextBallGridPane){
        for (int i = 0; i < 3; i++)
            nextBallGridPane.getChildren().remove(imageViewsNextBall[i]);
    }

    public void setImageViewAndInteractionToMainGridPane(){
        clearMainGridPane();
        List<Position> ballPositions = controller.getGame().getBallsPositions();
        List<Position> emptyPositions = controller.getGame().getEmptyPositions();
        ballPositions.forEach(p -> {
            Color color = controller.getGame().getBallsColorFrom(p);
            Image imageOfBall = ImageOfBall.getImage(color, Options.ImageType.IMAGE_TYPE_A);
            setImageViews(imageOfBall, p);
            controller.maimGridPane.add(imageViews[p.getX()][p.getY()], p.getY(), p.getX());
            mouseClickedOnABall(p);
        });
        emptyPositions.forEach(p -> {
            Image imageOfBall = ImageOfBall.getNullImage();
            setImageViews(imageOfBall, p);
            controller.maimGridPane.add(imageViews[p.getX()][p.getY()], p.getY(), p.getX());
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
        imageViews[x][y].setFitHeight(FIT);
        imageViews[x][y].setFitWidth(FIT);
        GridPane.setHalignment(imageViews[x][y], HPos.CENTER);
    }

    private void clearMainGridPane(){
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                controller.maimGridPane.getChildren().remove(imageViews[i][j]);
    }

    private void mouseClickedOnABall(Position position){
        int x = position.getX();
        int y = position.getY();
        Color colorFromPosition = controller.getGame().getBallsColorFrom(position);
        imageViews[x][y].setOnMouseClicked(e -> {
            if (from != null) {
                Color color = controller.getGame().getBallsColorFrom(from);
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
                    if(!controller.getGame().moveAndCheckGameOver(from, to)) {
                        refreshPanel();
                    }
                    else {
                        refreshPanel();
                        BoxAlert.displayGameOver("Game Over!!!", controller.getGame().getRound(), controller.getGame().getScore());
                    }
                }
            }
        });
    }

    private void mouseEnteredOnPositionWithoutBall(Position position){
        imageViews[position.getX()][position.getY()].setOnMouseEntered(e -> {
            to = new Position(position.getX(), position.getY());
            if (isMovePermissible(position)) {
                controller.getGame().getLastCheckedWalkablePath().forEach(p -> imageViews[p.getX()][p.getY()].setImage(ImageOfBall.getNullImage2()));
            }
        });
    }

    private void mouseExitedOnPositionWithoutBall(Position position){
        imageViews[position.getX()][position.getY()].setOnMouseExited(e -> {
            if (isMovePermissible(position)) {
                controller.getGame().getLastCheckedWalkablePath().forEach(p -> imageViews[p.getX()][p.getY()].setImage(ImageOfBall.getNullImage()));
            }
        });
    }

    private boolean isMovePermissible(Position position){
        return from != null && controller.getGame().isWalkablePath(from, position);
    }

    public void refreshPanel(){
        to = null;
        from = null;
        setImageViewToNextBallGridPane();
        setImageViewAndInteractionToMainGridPane();
        setLabels();
    }

    public void setLabels(){
        controller.score.setText("Score: " + controller.getGame().getScore());
        controller.round.setText("Round: " + controller.getGame().getRound());
    }
}