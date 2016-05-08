package com.voliotis.game;

import com.voliotis.findpath.PathFinderAlgorithm;

import java.util.*;

public class GameField {
    public static final int SIZE = 9;
    public static final int NUMBER_OF_BALLS_FIRST_TIME = 6;
    private static final Random random = new Random();

    private Map<Position,Color> map;
    private List<Position> emptyPositions;
    private int score;
    private int round;
    private int bonus;
    private boolean haveScore;

    public GameField() {
        map = new HashMap<>();
        emptyPositions = new ArrayList<>();
        score = 0;
        round = 0;
        bonus = 1;
        haveScore = false;
        for (int x = 0; x < SIZE; x++)
            for (int y = 0; y < SIZE; y++)
                emptyPositions.add(new Position(x,y));
    }

    public GameField(Map<Position, Color> map, List<Position> emptyPositions, int score, int round, int bonus, boolean haveScore) {
        this.map = map;
        this.emptyPositions = emptyPositions;
        this.score = score;
        this.round = round;
        this.bonus = bonus;
        this.haveScore = haveScore;
    }

    private Position pickRandomEmptyPositionAndFillIt(){
        return emptyPositions.remove(random.nextInt(emptyPositions.size()));
    }

    private int getGameFieldSize(){
        return SIZE * SIZE;
    }

    public Position createAndPutColorInRandomPosition(Color setColor){
        Position position = pickRandomEmptyPositionAndFillIt();
        map.put(position,setColor);
        return position;
    }

    public Color removeAndGetColorFromPosition(Position position){
        emptyPositions.add(position);
        return map.remove(position);
    }

    public void addColorToPosition(Color color, Position position){
        emptyPositions.remove(position);
        map.put(position,color);
    }

    public List<Position> getPathToGoal(Position start, Position goal){
        boolean[][] temp = new boolean[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++)
            for (int y = 0; y < SIZE; y++)
                temp[x][y] = true;
        map.forEach((p, b) -> temp[p.getX()][p.getY()] = false);
        PathFinderAlgorithm path = new PathFinderAlgorithm(temp,start.getX(),start.getY(),goal.getX(),goal.getY());
        return path.getPath();
    }

    public Color getColorFromPosition(Position position){
        return map.get(position);
    }

    public Color getRandomColorWithRightFrequency(){
        //return Color.getRandomColor();
        return Color.getRandomColor(getNumberOfRemainingMoves(),getGameFieldSize());
    }

    public void removeColorFormAllPositions(Color color){
        Set<Position> positions = new HashSet<>();
        map.forEach((p, c) -> {
            if(c == color)
                positions.add(p);
        });
        positions.forEach(this::removeAndGetColorFromPosition);
    }

    public Map<Position, Color> getMap() {
        return map;
    }

    public List<Position> getEmptyPositions() {
        return emptyPositions;
    }

    public int getNumberOfRemainingMoves(){
        return emptyPositions.size();
    }

    public boolean haveScoreInPreviousRound() {
        return haveScore;
    }

    public void setHaveScore(boolean haveScore) {
        this.haveScore = haveScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}