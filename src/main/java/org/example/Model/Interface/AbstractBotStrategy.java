package org.example.Model.Interface;

import org.example.Model.Grid.IGrid;
import org.example.Model.Position;
import org.example.Model.Vector;

public abstract class AbstractBotStrategy implements MovementStrategy {
    protected static final int SAFETY_CHECK_DISTANCE = 3;

    @Override
    public abstract Vector getNextMove(Position currentPosition, Vector currentVector, IGrid grid);

    protected double calculateSpeed(Vector vector) {
        return Math.sqrt(vector.getDx() * vector.getDx() + vector.getDy() * vector.getDy());
    }

    protected boolean isValidMove(Position currentPos, Vector move, IGrid grid) {
        int newX = currentPos.getX() + move.getDx();
        int newY = currentPos.getY() + move.getDy();
        return grid.isWalkable(newX, newY);
    }

    protected boolean isSafeMove(Position currentPos, Vector move, IGrid grid) {
        int x = currentPos.getX();
        int y = currentPos.getY();
        int dx = move.getDx();
        int dy = move.getDy();

        for (int i = 1; i <= SAFETY_CHECK_DISTANCE; i++) {
            int futureX = x + (dx * i);
            int futureY = y + (dy * i);

            if (!grid.isValidPosition(futureX, futureY) || !grid.isWalkable(futureX, futureY)) {
                return false;
            }

            boolean hasSpace = false;
            for (int offsetX = -1; offsetX <= 1; offsetX++) {
                for (int offsetY = -1; offsetY <= 1; offsetY++) {
                    if (grid.isWalkable(futureX + offsetX, futureY + offsetY)) {
                        hasSpace = true;
                        break;
                    }
                }
                if (hasSpace) break;
            }
            if (!hasSpace) return false;
        }
        return true;
    }

    protected double calculateSafetyScore(Position currentPos, Vector move, IGrid grid) {
        double score = 0;
        int x = currentPos.getX();
        int y = currentPos.getY();

        for (int i = 1; i <= SAFETY_CHECK_DISTANCE; i++) {
            int futureX = x + (move.getDx() * i);
            int futureY = y + (move.getDy() * i);

            int freeSpaces = 0;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (grid.isWalkable(futureX + dx, futureY + dy)) {
                        freeSpaces++;
                    }
                }
            }
            score += freeSpaces * (1.0 / i);
        }
        return score;
    }
}