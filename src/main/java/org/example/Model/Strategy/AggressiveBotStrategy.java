package org.example.Model.Strategy;

import org.example.Model.Grid;
import org.example.Model.Interface.AbstractBotStrategy;
import org.example.Model.Position;
import org.example.Model.Vector;
import java.util.List;

public class AggressiveBotStrategy extends AbstractBotStrategy {
    private static final double MAX_SAFE_SPEED = 4.0;

    @Override
    public Vector getNextMove(Position currentPosition, Vector currentVector, Grid grid) {
        List<Vector> possibleMoves = currentVector.getPossibleNextMoves();

        return possibleMoves.stream()
                .filter(move -> isValidMove(currentPosition, move, grid))
                .filter(move -> isSafeMove(currentPosition, move, grid))
                .max((v1, v2) -> {
                    double speed1 = calculateSpeed(v1);
                    double speed2 = calculateSpeed(v2);

                    // Se entrambe le velocità sono sicure, scegli la più veloce
                    if (speed1 <= MAX_SAFE_SPEED && speed2 <= MAX_SAFE_SPEED) {
                        return Double.compare(speed1, speed2);
                    }

                    // Se una velocità supera il limite di sicurezza, preferisci quella più sicura
                    if (speed1 > MAX_SAFE_SPEED && speed2 <= MAX_SAFE_SPEED) {
                        return -1;
                    }
                    if (speed2 > MAX_SAFE_SPEED && speed1 <= MAX_SAFE_SPEED) {
                        return 1;
                    }

                    // Se entrambe superano il limite, scegli in base alla sicurezza
                    return Double.compare(
                            calculateSafetyScore(currentPosition, v1, grid),
                            calculateSafetyScore(currentPosition, v2, grid)
                    );
                })
                .orElse(new Vector(0, 0));
    }

}