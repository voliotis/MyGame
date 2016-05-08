package com.voliotis.findmatches;

import com.voliotis.game.Color;
import com.voliotis.game.GameField;
import com.voliotis.game.Position;
import java.util.HashSet;
import java.util.Set;

public class FindMatches {
    private GameField gameField;
    private final Position mainPosition;
    private final int numberOfRowColors;

    private Set<Position> positionsWithColorForRemove;
    private Set<Color> bombColorsForRemove;

    public FindMatches(Position position, GameField gameField, int numberOfRowColors) {
        this.mainPosition = position;
        this.gameField = gameField;
        this.numberOfRowColors = numberOfRowColors;
        positionsWithColorForRemove = new HashSet<>();
        bombColorsForRemove = new HashSet<>();
    }

    public GameField getClearGameFieldFromScore(){
        accessField(Position.Access.ROW);
        accessField(Position.Access.COLUMN);
        accessField(Position.Access.CROSS_A);
        accessField(Position.Access.CROSS_B);
        cleanGameFieldFromPositionsWithColorForRemove();
        if (!positionsWithColorForRemove.isEmpty())
            gameField.setHaveScore(true);
        return gameField;
    }

    private void accessField(Position.Access access){
        Set<Position> positionsAccess = new HashSet<>();
        Set<Position> positions = new HashSet<>();
        Set<Color> bombColor = new HashSet<>();

        if(getColor(mainPosition).hasRegularColor())
            positions.add(mainPosition);
        else
            positions.addAll(getMainPositionNeighbours(access));

        for (Position position : positions) {
            positionsAccess.add(position);
            Position nextPosition = position.getRightPosition(access);
            while (nextPosition != null && isPositionsMeetTheConditions(position,nextPosition)) {
                positionsAccess.add(nextPosition);
                if (getColor(nextPosition) == Color.BOMB)
                    bombColor.add(getColor(position));
                nextPosition = nextPosition.getRightPosition(access);
            }
            nextPosition = position.getLeftPosition(access);
            while (nextPosition != null && isPositionsMeetTheConditions(position,nextPosition)) {
                positionsAccess.add(nextPosition);
                if (getColor(nextPosition) == Color.BOMB)
                    bombColor.add(getColor(position));
                nextPosition = nextPosition.getLeftPosition(access);
            }
            setScore(access,positionsAccess,bombColor);
            positionsAccess.clear();
            bombColor.clear();
        }
    }

    private Set<Position> getMainPositionNeighbours(Position.Access access){
        Set<Position> neighbours = new HashSet<>();
        Position nextPosition = mainPosition.getRightPosition(access);
        while(nextPosition != null && !isEmptyPosition(nextPosition)) {
            if(getColor(nextPosition).hasRegularColor()) {
                neighbours.add(nextPosition);
                break;
            }
            nextPosition = nextPosition.getRightPosition(access);
        }
        nextPosition = mainPosition.getLeftPosition(access);
        while (nextPosition != null && !isEmptyPosition(nextPosition)){
            if(getColor(nextPosition).hasRegularColor()) {
                neighbours.add(nextPosition);
                break;
            }
            nextPosition = nextPosition.getLeftPosition(access);
        }
        return neighbours;
    }

    private void setScore(Position.Access access, Set<Position> positionsAccess, Set<Color> bombColor){
        if (positionsAccess.size() >= numberOfRowColors) {
            positionsWithColorForRemove.addAll(positionsAccess);
            bombColorsForRemove.addAll(bombColor);
            if(access == Position.Access.ROW || access == Position.Access.COLUMN)
                increaseTheScoreStraight(positionsAccess.size());
            else
                increaseTheScoreCross(positionsAccess.size());
        }
    }

    private Color getColor(Position p){
        return gameField.getColorFromPosition(p);
    }

    private boolean isEmptyPosition(Position p){
        return gameField.getEmptyPositions().contains(p);
    }

    private boolean isPositionsMeetTheConditions(Position p1, Position p2){
        return !isEmptyPosition(p2) &&
                getColor(p1).matches(getColor(p2));
    }

    private void cleanGameFieldFromPositionsWithColorForRemove(){
        positionsWithColorForRemove.forEach(gameField::removeAndGetColorFromPosition);
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