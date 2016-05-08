package com.voliotis.game;

import javafx.scene.image.Image;

public class Options {


    public enum ImageType {IMAGE_TYPE_A, IMAGE_TYPE_B}
    public enum SOrSAs { SAVE, SAVE_AS }
    public enum SOrL { SAVE, LOAD }
    public enum YNC { YES, NO, CANCEL }
    
    public static final String HIGH_SCORE_FILE = "\\highScore.json";
    public static final String MAIN_FXML_FILE_NAME = "/mainStage.fxml";
    public static final String MAIN_CSS_FILE_NAME = "/Style.css";

    public static final String IMAGES_PATH = "/img/";
    public static final Image NULL_IMAGE = new Image(IMAGES_PATH + "NULL.png");
    public static final Image NULL_IMAGE_2 = new Image(IMAGES_PATH + "NULL2.png");

}