package com.voliotis.game;

import java.util.Random;

public enum Color {
    
    RED, YELLOW, BLUE, GREEN, BLACK, WHITE, ORANGE, GR_YEL, WH_BLU, JOKER, BOMB;

    private static final Random random = new Random();

    public static Color getRandomColor(int numberOfRemainingMoves, int gameFieldSize){
        int random2;
        if (numberOfRemainingMoves < gameFieldSize / 2){
            int random1 = random.nextInt(10);
            if (random1 < 9)
                random2 = random.nextInt(7);
            else
                random2 = 7 + random.nextInt(4);
        }
        else {
            random2 = random.nextInt(7);
        }
        return values()[random2];
    }

    public static Color getRandomColor(){
        int random2 = random.nextInt(11);
        return values()[random2];
    }

    private boolean matchesGreenYellow(Color color){
        return (this == GREEN || this == YELLOW) && color == GR_YEL;
    }

    private boolean matchesWhiteBlue(Color color){
        return (this == WHITE || this == BLUE) && color == WH_BLU;
    }

    private boolean isJokerOrBomb(){
        return this == JOKER || this == BOMB;
    }

    public boolean hasRegularColor(){
        return !(this == JOKER || this == BOMB || this == GR_YEL || this == WH_BLU);
    }

    public boolean matches(Color color){
        return color.isJokerOrBomb() || this.equals(color) || this.matchesGreenYellow(color) || this.matchesWhiteBlue(color);
    }
}
