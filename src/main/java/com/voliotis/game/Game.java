package com.voliotis.game;

import com.voliotis.file.controler.SaveGame;
import com.voliotis.findmatches.FindMatches;

import java.util.*;

public class Game {
    public static final int NUMBER_OF_BALLS_TO_HAVE_SCORE = 5;
    private static final Random random = new Random();
    private GameField gameField = new GameField();
    private List<Color> nextBallsColor;
    private List<Position> path;

    public void startGame(){
        path = new ArrayList<>();
        addBallsInTheGameFieldFirstTime();
        createNextBallsColorToInsetToTheGameField();
    }

    public void continueGame(SaveGame load){
        path = new ArrayList<>();
        gameField = new GameField(load.getMap(),load.getEmptyPositions(),load.getScore(),load.getRound(),load.getBonus(),load.isHaveScore());
        nextBallsColor = load.getNextBallsColor();
    }

    public boolean moveAndCheckGameOver(Position from, Position to){
        Color color = gameField.removeAndGetColorFromPosition(from);
        gameField.addColorToPosition(color, to);
        FindMatches matches = new FindMatches(to,gameField,NUMBER_OF_BALLS_TO_HAVE_SCORE);
        gameField = matches.getClearGameFieldFromScore();
        return nextRound();
    }

    public boolean isWalkablePath(Position start, Position goal){
        return !getPathFromTo(start, goal).isEmpty();
    }

    public List<Position> getLastCheckedWalkablePath() {
        return path;
    }

    public List<Position> getBallsPositions(){
        List<Position> positions = new ArrayList<>();
        gameField.getMap().forEach((position, ball) -> positions.add(position));
        return positions;
    }

    public List<Position> getEmptyPositions(){
        return gameField.getEmptyPositions();
    }

    public List<Color> getNextBallsColor() {
        return nextBallsColor;
    }

    public Color getBallsColorFrom(Position position){
        return gameField.getColorFromPosition(position);
    }

    public String getScore(){
        return String.valueOf(gameField.getScore());
    }

    public String getRound(){
        return String.valueOf(gameField.getRound());
    }

    public SaveGame saveGame(){
        return new SaveGame(gameField.getMap(),
                gameField.getEmptyPositions(),
                nextBallsColor,
                gameField.getScore(), gameField.getRound(), gameField.getBonus(), gameField.haveScoreInPreviousRound());
    }

    private boolean nextRound(){
        if (!gameField.haveScoreInPreviousRound()) {
            gameField.setBonus(1);
            increaseTheRound();
            addNewBallsToGameField();
            return createNextBallsColorToInsetToTheGameField();
        }
        else {
            int bonus = gameField.getBonus();
            bonus++;
            gameField.setBonus(bonus);
            gameField.setHaveScore(false);
            return false;
        }
    }

    private void addBallsInTheGameFieldFirstTime(){
        for (int i = 0; i < GameField.NUMBER_OF_BALLS_FIRST_TIME; i++)
            gameField.createAndPutColorInRandomPosition(gameField.getRandomColorWithRightFrequency());
    }

    private boolean createNextBallsColorToInsetToTheGameField(){
        nextBallsColor = new ArrayList<>();
        int numberOfNextBalls = random.nextInt(2) + 2; //Number of Balls is 2 or 3 every time
        while (numberOfNextBalls > gameField.getNumberOfRemainingMoves())
            numberOfNextBalls--;
        for (int i = 0; i < numberOfNextBalls; i++)
            nextBallsColor.add(gameField.getRandomColorWithRightFrequency());
        return nextBallsColor.isEmpty();
    }

    private void addNewBallsToGameField() {
        nextBallsColor.forEach(color -> {
            Position position = gameField.createAndPutColorInRandomPosition(color);
            FindMatches matches = new FindMatches(position, gameField, NUMBER_OF_BALLS_TO_HAVE_SCORE);
            gameField = matches.getClearGameFieldFromScore();
        });
    }

    private List<Position> getPathFromTo(Position start, Position goal){
        path = gameField.getPathToGoal(start,goal);
        return path;
    }

    private void increaseTheRound(){
        int round = gameField.getRound();
        round++;
        gameField.setRound(round);
    }
}