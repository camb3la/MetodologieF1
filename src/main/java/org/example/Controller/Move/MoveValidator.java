package org.example.Controller.Move;

import org.example.Model.Grid;
import org.example.Model.Player;
import org.example.Model.Position;

public class MoveValidator {
    private final Grid grid;

    public MoveValidator(Grid grid) {
        this.grid = grid;
    }

    public boolean isValidMove(Player player, Position targetPosition) {
        return player.canReach(targetPosition) &&
                grid.isWalkable(targetPosition.getX(), targetPosition.getY());
    }
}