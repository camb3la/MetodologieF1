package org.example.Model.Strategy;

import org.example.Model.Grid.IGrid;
import org.example.Model.Interface.AbstractBotStrategy;
import org.example.Model.Position;
import org.example.Model.Vector;

import java.util.List;

public class ConservativeBotStrategy extends AbstractBotStrategy {
    private static final double OPTIMAL_SPEED = 2.0;

    @Override
    public Vector getNextMove(Position currentPosition, Vector currentVector, IGrid grid) {
        List<Vector> possibleMoves = currentVector.getPossibleNextMoves();

        return possibleMoves.stream()
                .filter(move -> isValidMove(currentPosition, move, grid))
                .filter(move -> isSafeMove(currentPosition, move, grid))
                .min((v1, v2) -> {
                    double speed1 = calculateSpeed(v1);
                    double speed2 = calculateSpeed(v2);
                    double speedDiff1 = Math.abs(speed1 - OPTIMAL_SPEED);
                    double speedDiff2 = Math.abs(speed2 - OPTIMAL_SPEED);

                    if (Math.abs(speedDiff1 - speedDiff2) < 0.1) {
                        return Double.compare(
                                calculateSafetyScore(currentPosition, v2, grid),
                                calculateSafetyScore(currentPosition, v1, grid)
                        );
                    }
                    return Double.compare(speedDiff1, speedDiff2);
                })
                .orElse(new Vector(0, 0));
    }
}
