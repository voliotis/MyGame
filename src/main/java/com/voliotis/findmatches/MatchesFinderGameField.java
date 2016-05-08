package com.voliotis.findmatches;

import com.voliotis.game.Color;
import com.voliotis.game.GameField;
import com.voliotis.game.Position;
import java.util.HashSet;
import java.util.Set;

public class MatchesFinderGameField {
    private GameField gameField;
    private final Position mainPosition;
    private final int numberOfRowColors;

    private Set<Position> positionsForRemove;
    private Set<Color> bombColorsForRemove;

    public MatchesFinderGameField(Position position, GameField gameField, int numberOfRowColors) {
        this.mainPosition = position;
        this.gameField = gameField;
        this.numberOfRowColors = numberOfRowColors;
        positionsForRemove = new HashSet<>();
        bombColorsForRemove = new HashSet<>();
    }

    public GameField getClearGameFieldFromMatches(){
        accessGameFieldForMatches(Position.Access.ROW);
        accessGameFieldForMatches(Position.Access.COLUMN);
        accessGameFieldForMatches(Position.Access.CROSS_A);
        accessGameFieldForMatches(Position.Access.CROSS_B);
        cleanGameFieldFromMatches();
        if (!positionsForRemove.isEmpty())
            gameField.setHaveScore(true);
        return gameField;
    }

    private void accessGameFieldForMatches(Position.Access access){
        Set<Position> positionsAccess = new HashSet<>();
        Set<Position> positions = new HashSet<>();
        Set<Color> bombColor = new HashSet<>();

        if(gameField.getColorFromPosition(mainPosition).hasRegularColor())
            positions.add(mainPosition);
        else
            positions.addAll(getMainPositionNeighbours(access));

        for (Position position : positions) {
            positionsAccess.add(position);
            Position nextPosition = position.getNextPosition(access);
            while (nextPosition != null && isPositionsMeetTheConditions(position,nextPosition)) {
                positionsAccess.add(nextPosition);
                if (gameField.getColorFromPosition(nextPosition) == Color.BOMB)
                    bombColor.add(gameField.getColorFromPosition(position));
                nextPosition = nextPosition.getNextPosition(access);
            }
            nextPosition = position.getPreviousPosition(access);
            while (nextPosition != null && isPositionsMeetTheConditions(position,nextPosition)) {
                positionsAccess.add(nextPosition);
                if (gameField.getColorFromPosition(nextPosition) == Color.BOMB)
                    bombColor.add(gameField.getColorFromPosition(position));
                nextPosition = nextPosition.getPreviousPosition(access);
            }
            setScore(access,positionsAccess,bombColor);
            positionsAccess.clear();
            bombColor.clear();
        }
    }

    private Set<Position> getMainPositionNeighbours(Position.Access access){
        Set<Position> neighbours = new HashSet<>();
        Position nextPosition = mainPosition.getNextPosition(access);
        while(nextPosition != null && !gameField.isEmptyPosition(nextPosition)) {
            if(gameField.getColorFromPosition(nextPosition).hasRegularColor()) {
                neighbours.add(nextPosition);
                break;
            }
            nextPosition = nextPosition.getNextPosition(access);
        }
        nextPosition = mainPosition.getPreviousPosition(access);
        while (nextPosition != null && !gameField.isEmptyPosition(nextPosition)){
            if(gameField.getColorFromPosition(nextPosition).hasRegularColor()) {
                neighbours.add(nextPosition);
                break;
            }
            nextPosition = nextPosition.getPreviousPosition(access);
        }
        return neighbours;
    }

    private void setScore(Position.Access access, Set<Position> positionsAccess, Set<Color> bombColor){
        if (positionsAccess.size() >= numberOfRowColors) {
            positionsForRemove.addAll(positionsAccess);
            bombColorsForRemove.addAll(bombColor);
            if(access == Position.Access.ROW || access == Position.Access.COLUMN)
                increaseTheScoreStraight(positionsAccess.size());
            else
                increaseTheScoreCross(positionsAccess.size());
        }
    }

    private boolean isPositionsMeetTheConditions(Position p1, Position p2){
        return !gameField.isEmptyPosition(p2) &&
                gameField.getColorFromPosition(p1).matches(gameField.getColorFromPosition(p2));
    }

    private void cleanGameFieldFromMatches(){
        positionsForRemove.forEach(gameField::removeAndGetColorFromPosition);
        bombColorsForRemove.forEach(gameField::removeColorFormAllPositions);
    }

    private void increaseTheScoreStraight(int numberOfColors){
        int score = gameField.getScore();
        int value = 3;
        for (int i = 5; i <= 9; i++){
            if (numberOfColors == i){
                score += value * gameField.getBonus();
                break;
            }
            value += 2;
        }
        gameField.setScore(score);
    }

    private void increaseTheScoreCross(int numberOfColors){
        int score = gameField.getScore();
        int value = 5;
        for (int i = 5; i <= 9; i++){
            if (numberOfColors ==i){
                score += value * gameField.getBonus();
                break;
            }
            value += 2;
        }
        gameField.setScore(score);
    }
}