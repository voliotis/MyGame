package com.voliotis.findpath;

import com.voliotis.game.Position;

import java.util.*;

public class PathFinderAlgorithm {
    private WalkablePosition [][] gameField;
    private int size;
    private WalkablePosition start;
    private WalkablePosition goal;
    private List<WalkablePosition> path;

    public PathFinderAlgorithm(boolean[][] gameField, int startX, int startY, int goalX, int goalY) {
        size = gameField.length;
        this.gameField = new WalkablePosition[size][size];
        this.path = new ArrayList<>();
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.gameField[i][j] = new WalkablePosition(gameField[i][j],i,j);

        this.start = new WalkablePosition(false, startX,startY,0, getDistance(startX,startY,goalX,goalY));
        this.goal = new WalkablePosition(true, goalX, goalY,getDistance(startX,startY,goalX,goalY),0);
    }

    private int getDistance(int aX, int aY, int bX, int bY){
        return Math.abs(aX - bX) + Math.abs(aY - bY);
    }

    private int getDistance(WalkablePosition a, WalkablePosition b){
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    private WalkablePosition getPositionWithBetterCost(List<WalkablePosition> openSet){
        WalkablePosition position = openSet.get(0);
        for (int i = 1; i < openSet.size(); i++)
            if(openSet.get(i).getCost() < position.getCost() ||
                    openSet.get(i).getCost() == position.getCost() && openSet.get(i).getCostToEnd() < position.getCostToEnd())
                position = openSet.get(i);
        return position;
    }

    private boolean isNeighbourCloserToGaol(WalkablePosition position, WalkablePosition neighbour, List<WalkablePosition> openSet){
        int newMovementCostToNeighbour = position.getCostFromStar() + getDistance(position,neighbour);
        if (newMovementCostToNeighbour < neighbour.getCostFromStar() || !openSet.contains(neighbour)){
            neighbour.setAllCosts(newMovementCostToNeighbour,getDistance(neighbour,goal));
            neighbour.setParent(position);
            return !openSet.contains(neighbour);
        }
        return false;
    }

    private List<WalkablePosition> getNeighbours(WalkablePosition position){
        int x = position.getX();
        int y = position.getY();
        List<WalkablePosition> neighbors = new ArrayList<>();
        if((x - 1) >= 0 && gameField[x - 1][y].isWalkable())
            neighbors.add(gameField[x - 1][y]);
        if((y + 1) < size && gameField[x][y + 1].isWalkable())
            neighbors.add(gameField[x][y + 1]);
        if((x + 1) < size && gameField[x + 1][y].isWalkable())
            neighbors.add(gameField[x + 1][y]);
        if((y - 1) >= 0 && gameField[x][y - 1].isWalkable())
            neighbors.add(gameField[x][y - 1]);
        return neighbors;
    }

    private void retracePath(){
        path = new ArrayList<>();
        WalkablePosition currentPosition = goal;
        //hasSamePosition
        while (!currentPosition.equals(start)){
            path.add(currentPosition);
            currentPosition = currentPosition.getParent();
        }
        Collections.reverse(path);
    }

    public List<Position> getPath(){
        List<Position> returnPositions = new ArrayList<>();
        List<WalkablePosition> openSet = new ArrayList<>();
        HashSet<WalkablePosition> closedSet = new HashSet<>();

        openSet.add(start);

        while (!openSet.isEmpty()){
            WalkablePosition position;
            position = getPositionWithBetterCost(openSet);

            openSet.remove(position);
            closedSet.add(position);

            if (position.equals(goal)){
                goal.setParent(position.getParent());
                retracePath();
                for (WalkablePosition p : path)
                    returnPositions.add(p);
                return returnPositions;
            }
                getNeighbours(position).forEach(neighbour ->{
                    if (!closedSet.contains(neighbour) && isNeighbourCloserToGaol(position, neighbour, openSet))
                        openSet.add(neighbour);
                });
        }
        return returnPositions;
    }
}
