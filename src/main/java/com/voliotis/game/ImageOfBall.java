package com.voliotis.game;

import javafx.scene.image.Image;

public class ImageOfBall {

    private ImageOfBall(){
    }

    public static Image getNullImage() {
        return Options.NULL_IMAGE;
    }
    public static Image getNullImage2() {
        return Options.NULL_IMAGE_2;
    }

    public static Image getImage(Color ballsColor, Options.ImageType imageType){
        String path = Options.IMAGES_PATH;
        Image image = new Image(path + "NULL.png");

        if (imageType == Options.ImageType.IMAGE_TYPE_A) {
            if (ballsColor == Color.RED) {
                image = new Image(path + "RED.png");
            } else if (ballsColor == Color.YELLOW) {
                image = new Image(path + "YELLOW.png");
            } else if (ballsColor == Color.BLUE) {
                image = new Image(path + "BLUE.png");
            } else if (ballsColor == Color.GREEN) {
                image = new Image(path + "GREEN.png");
            } else if (ballsColor == Color.BLACK) {
                image = new Image(path + "BLACK.png");
            } else if (ballsColor == Color.WHITE) {
                image = new Image(path + "WHITE.png");
            } else if (ballsColor == Color.ORANGE) {
                image = new Image(path + "ORANGE.png");
            } else if (ballsColor == Color.JOKER) {
                image = new Image(path + "JOKER.png");
            } else if (ballsColor == Color.GR_YEL) {
                image = new Image(path + "GR_YEL.png");
            } else if (ballsColor == Color.WH_BLU) {
                image = new Image(path + "WH_BLU.png");
            } else if (ballsColor == Color.BOMB) {
                image = new Image(path + "BOMB.png");
            }
        }
        else if (imageType == Options.ImageType.IMAGE_TYPE_B) {
            if (ballsColor == Color.RED) {
                image = new Image(path + "RED2.png");
            } else if (ballsColor == Color.YELLOW) {
                image = new Image(path + "YELLOW2.png");
            } else if (ballsColor == Color.BLUE) {
                image = new Image(path + "BLUE2.png");
            } else if (ballsColor == Color.GREEN) {
                image = new Image(path + "GREEN2.png");
            } else if (ballsColor == Color.BLACK) {
                image = new Image(path + "BLACK2.png");
            } else if (ballsColor == Color.WHITE) {
                image = new Image(path + "WHITE2.png");
            } else if (ballsColor == Color.ORANGE) {
                image = new Image(path + "ORANGE2.png");
            } else if (ballsColor == Color.JOKER) {
                image = new Image(path + "JOKER2.png");
            } else if (ballsColor == Color.GR_YEL) {
                image = new Image(path + "GR_YEL2.png");
            } else if (ballsColor == Color.WH_BLU) {
                image = new Image(path + "WH_BLU2.png");
            } else if (ballsColor == Color.BOMB) {
                image = new Image(path + "BOMB2.png");
            }
        }
        return image;
    }

}