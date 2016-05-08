package com.voliotis.file.controler;

import com.voliotis.game.Color;
import com.voliotis.game.Position;

import java.util.*;

public class SaveGame {
    private List<Position> mapPositions;
    private List<Color> mapColor;
    private List<Position> emptyPositions;
    private List<Color> nextBallsColor;
    private int score;
    private int round;
    private int bonus;
    private boolean haveScore;

    public SaveGame(Map<Position,Color> map, List<Position> emptyPositions, List<Color> nextBallsColor, int score, int round, int bonus, boolean haveScore) {
        mapPositions = new ArrayList<>();
        mapColor = new ArrayList<>();
        map.forEach((p,c) ->{
            mapPositions.add(p);
            mapColor.add(c);
        });
        this.emptyPositions = emptyPositions;
        this.nextBallsColor = nextBallsColor;
        this.score = score;
        this.round = round;
        this.bonus = bonus;
        this.haveScore = haveScore;
    }

    public Map<Position,Color> getMap(){
        Map<Position,Color> map = new HashMap<>();
        for (int i = 0; i < mapPositions.size(); i++) {
            map.put(mapPositions.get(i),mapColor.get(i));
        }
        return map;
    }

    public List<Position> getEmptyPositions() {
        return emptyPositions;
    }

    public List<Color> getNextBallsColor() {
        return nextBallsColor;
    }

    public int getScore() {
        return score;
    }

    public int getRound() {
        return round;
    }

    public int getBonus() {
        return bonus;
    }

    public boolean isHaveScore() {
        return haveScore;
    }
}