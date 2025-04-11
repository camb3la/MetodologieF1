package org.example.Model.Strategy;

import org.example.Model.Grid.IGrid;
import org.example.Model.Interface.MovementStrategy;
import org.example.Model.Position;
import org.example.Model.Vector;

public class HumanStrategy implements MovementStrategy {
    private Vector move;

    public void setMove(Vector move){
        this.move = move;
    }

    @Override
    public Vector getNextMove(Position currentPosition, Vector currentVector, IGrid grid){
        Vector result = move;
        move = null;
        return result;
    }
}
