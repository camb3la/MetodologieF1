package org.example.Controller.Game;

import org.example.Model.Grid.IGrid;
import org.example.Model.Position;

public class VictoryDetector {
    private final IGrid grid;

    public VictoryDetector(IGrid grid) {
        this.grid = grid;
    }

    public boolean checkVictory(Position oldPosition, Position newPosition) {
        return grid.crossFinishLine(oldPosition, newPosition);
    }
}