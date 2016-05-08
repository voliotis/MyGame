package com.voliotis.findpath;

import com.voliotis.game.Position;

public class WalkablePosition extends Position {
    private boolean walkable;
    private int costFromStar; //Distance from starting position
    private int costToEnd; //Distance from end position
    private int cost; //G cost + H cost
    private WalkablePosition parent;

    public WalkablePosition(boolean walkable, int x, int y) {
        super(x,y);
        this.walkable = walkable;
    }

    public WalkablePosition(boolean walkable, int x, int y, int costFromStar, int costFromEnd) {
        super(x,y);
        this.walkable = walkable;
        this.costFromStar = costFromStar;
        this.costToEnd = costFromEnd;
        this.cost = costFromStar + costFromEnd;
    }

    public void setAllCosts(int costFromStar, int costFromEnd){
        this.costFromStar = costFromStar;
        this.costToEnd = costFromEnd;
        this.cost = costFromStar + costFromEnd;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public int getCostFromStar() {
        return costFromStar;
    }

    public int getCostToEnd() {
        return costToEnd;
    }

    public int getCost() {
        return cost;
    }

    public WalkablePosition getParent() {
        return parent;
    }

    public void setParent(WalkablePosition parent) {
        this.parent = parent;
    }
}