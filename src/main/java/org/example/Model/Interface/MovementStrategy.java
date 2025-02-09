package org.example.Model.Interface;

import org.example.Model.Grid;
import org.example.Model.Position;
import org.example.Model.Vector;

public interface MovementStrategy {

    Vector getNextMove(Position currentPosition, Vector currentVector, Grid grid);

}
