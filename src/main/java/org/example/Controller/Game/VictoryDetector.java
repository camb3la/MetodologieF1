package org.example.Controller.Game;

import org.example.Model.Grid;
import org.example.Model.Position;

public class VictoryDetector {
    private final Grid grid;

    public VictoryDetector(Grid grid) {
        this.grid = grid;
    }

    public boolean checkVictory(Position oldPosition, Position newPosition) {
        return grid.crossFinishLine(oldPosition, newPosition);
    }
}