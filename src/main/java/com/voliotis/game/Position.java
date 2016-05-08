package com.voliotis.game;

import java.util.Objects;

public class Position {
    public enum Access { ROW, COLUMN, CROSS_A, CROSS_B }
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position getNextPosition(Access access){
        int rightX = getNextX(access);
        int rightY  = getNextY(access);
        if(isItWithinTheLimit(rightX,rightY))
            return new Position(rightX,rightY);
        else
            return null;
    }

    public Position getPreviousPosition(Access access){
        int leftX = getPreviousX(access);
        int leftY  = getPreviousY(access);
        if(isItWithinTheLimit(leftX,leftY))
            return new Position(leftX,leftY);
        else
            return null;
    }

    private boolean isItWithinTheLimit(int x, int y){
        return x >= 0 && x < GameField.SIZE && y >= 0 && y < GameField.SIZE;
    }

    private int getNextX(Access access){
        if (access == Access.ROW)
            return this.x;
        else if (access == Access.COLUMN || access == Access.CROSS_A)
            return this.x + 1;
        else
            return this.x - 1;
    }

    private int getNextY(Access access){
        if (access == Access.ROW)
            return this.y + 1;
        else if (access == Access.COLUMN)
            return this.y;
        else
            return this.y - 1;
    }

    private int getPreviousX(Access access){
        if (access == Access.ROW)
            return this.x;
        else if (access == Access.COLUMN || access == Access.CROSS_A)
            return this.x - 1;
        else
            return this.x + 1;
    }

    private int getPreviousY(Access access){
        if (access == Access.ROW)
            return this.y - 1;
        else if (access == Access.COLUMN)
            return this.y;
        else
            return this.y + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Position position = (Position) o;
        return Objects.equals(x, position.x) &&
                Objects.equals(y, position.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
