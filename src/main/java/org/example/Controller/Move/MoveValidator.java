package org.example.Controller.Move;

import org.example.Model.Grid.IGrid;
import org.example.Model.Player.IPlayer;
import org.example.Model.Position;

public class MoveValidator {
    private final IGrid grid;

    public MoveValidator(IGrid grid) {
        this.grid = grid;
    }

    public boolean isValidMove(IPlayer player, Position targetPosition) {
        return player.canReach(targetPosition) &&
                grid.isWalkable(targetPosition.getX(), targetPosition.getY());
    }
}