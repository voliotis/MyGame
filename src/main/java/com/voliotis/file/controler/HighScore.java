package com.voliotis.file.controler;

public class HighScore {
    private String name;
    private int score;
    private int round;

    public HighScore(String name, int score, int round) {
        this.name = name;
        this.score = score;
        this.round = round;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getRound() {
        return round;
    }

}
