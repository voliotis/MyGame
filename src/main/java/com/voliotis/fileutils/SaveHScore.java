package com.voliotis.fileutils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SaveHScore {
    private List<HighScore> highScores;

    public SaveHScore(){
        highScores = new ArrayList<>();
    }

    public void updateHighScore(HighScore highScore){
        highScores.add(highScore);
        highScores = highScores.stream()
                .sorted(Comparator.comparing(HighScore::getScore).reversed())
                .limit(10)
                .collect(Collectors.toList());
        SLFile.saveHighScoreToFile(this);
    }

    public List<HighScore> getHighScores() {
        return highScores;
    }

    public boolean isInTopTen(int score){
        if (highScores.size() == 10){
            int limit = highScores
                    .stream()
                    .min((h1, h2) -> Integer.compare(h1.getScore(), h2.getScore()))
                    .get()
                    .getScore();
            return score > limit;
        }
        else
            return true;
    }

    public static int getLimit(){
        SaveHScore load;
        if ((load = SLFile.loadHighScoreFromFile()) != null ) {
            return load.highScores
                    .stream()
                    .min((h1,h2) -> Integer.compare(h1.getScore(), h2.getScore()))
                    .get()
                    .getScore();
        }
        else return 0;
    }
}
