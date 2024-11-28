package org.example.Model.Strategy;

import org.example.Model.Grid;
import org.example.Model.Interface.MovementStrategy;
import org.example.Model.Position;
import org.example.Model.Vector;

import java.util.List;

public class ConservativeBotStrategy implements MovementStrategy {

    @Override
    public Vector getNextMove(Position currentPosition, Vector currentVector, Grid grid) {
        List<Vector> possibleMoves = currentVector.getPossibleNextMoves();

        // Cerca il movimento che mantiene la velocità più simile a quella attuale
        return possibleMoves.stream()
                .filter(move -> isValidMove(currentPosition, move, grid))
                .min((v1, v2) -> {
                    double currentSpeed = Math.sqrt(currentVector.getDx() * currentVector.getDx() +
                            currentVector.getDy() * currentVector.getDy());
                    double speed1 = Math.sqrt(v1.getDx() * v1.getDx() + v1.getDy() * v1.getDy());
                    double speed2 = Math.sqrt(v2.getDx() * v2.getDx() + v2.getDy() * v2.getDy());
                    return Double.compare(Math.abs(speed1 - currentSpeed), Math.abs(speed2 - currentSpeed));
                })
                .orElse(new Vector(0, 0)); // In caso non ci siano mosse valide, ferma il veicolo
    }

    private boolean isValidMove(Position currentPos, Vector move, Grid grid) {
        // Controlla se il movimento porta a una posizione valida sulla griglia
        int newX = currentPos.getX() + move.getDx();
        int newY = currentPos.getY() + move.getDy();
        return grid.isWalkable(newX, newY);
    }
}
