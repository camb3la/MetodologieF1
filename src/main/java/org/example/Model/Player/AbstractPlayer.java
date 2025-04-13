package org.example.Model.Player;

import javafx.scene.paint.Color;
import org.example.Model.Grid.IGrid;
import org.example.Model.Interface.MovementStrategy;
import org.example.Model.Position;
import org.example.Model.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe astratta che implementa la logica comune a tutti i tipi di giocatori
 */
public abstract class AbstractPlayer implements IPlayer {

    protected final String name;

    protected Position currentPosition;

    protected Vector currentVector;

    protected final MovementStrategy strategy;

    protected final Color color;

    protected boolean hasFinished;

    protected boolean firstMove;

    protected AbstractPlayer(String name, Position startPosition, MovementStrategy strategy, Color color) {
        this.name = name;
        this.currentPosition = startPosition;
        this.currentVector = new Vector(0, 0);  // All'inizio il veicolo è fermo
        this.strategy = strategy;
        this.hasFinished = false;
        this.firstMove = true;
        this.color = color;
    }

    @Override
    public List<Position> getPossibleMoves(IGrid grid) {
        List<Position> validMoves = new ArrayList<>();
        List<Vector> possibleVectors = currentVector.getPossibleNextMoves();

        System.out.println("Calcolo mosse possibili da posizione: " + currentPosition.getX() + "," + currentPosition.getY());
        System.out.println("Vettore corrente: " + currentVector.getDx() + "," + currentVector.getDy());

        for (Vector vector : possibleVectors) {
            int newX = currentPosition.getX() + vector.getDx();
            int newY = currentPosition.getY() + vector.getDy();
            Position newPos = new Position(newX, newY);

            if (grid.isValidPosition(newX, newY) && grid.isWalkable(newX, newY)) {
                if(firstMove) {
                    // Il primo movimento deve essere verso sinistra
                    if (newY < currentPosition.getY()) {
                        validMoves.add(newPos);
                        System.out.println("Aggiunta mossa valida: " + newX + "," + newY);
                    }
                } else {
                    validMoves.add(newPos);
                    System.out.println("Aggiunta mossa valida: " + newX + "," + newY);
                }
            }
        }
        return validMoves;
    }

    @Override
    public void makeMove(IGrid grid) {
        Vector nextMove = strategy.getNextMove(currentPosition, currentVector, grid);
        if (nextMove != null && isValidMove(nextMove, grid)) {
            currentVector = nextMove;
            updatePosition();
        }
    }

    @Override
    public void moveTo(Position newPosition) {
        this.currentVector = new Vector(
                newPosition.getX() - currentPosition.getX(),
                newPosition.getY() - currentPosition.getY()
        );
        this.currentPosition = newPosition;
    }

    protected void updatePosition() {
        currentPosition = new Position(
                currentPosition.getX() + currentVector.getDx(),
                currentPosition.getY() + currentVector.getDy()
        );
    }

    protected boolean isValidMove(Vector newVector, IGrid grid) {
        if (!newVector.isValidMove(currentVector)) {
            return false;
        }

        int newX = currentPosition.getX() + newVector.getDx();
        int newY = currentPosition.getY() + newVector.getDy();

        return grid.isWalkable(newX, newY);
    }

    @Override
    public boolean canReach(Position target) {
        Vector requiredMovement = new Vector(
                target.getX() - currentPosition.getX(),
                target.getY() - currentPosition.getY()
        );

        // Verifica se questo movimento può essere ottenuto con una variazione valida della velocità
        return Math.abs(requiredMovement.getDx() - currentVector.getDx()) <= 1 &&
                Math.abs(requiredMovement.getDy() - currentVector.getDy()) <= 1;
    }

    @Override
    public void setFinished() {
        this.hasFinished = true;
    }

    // Implementazione dei getters
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Position getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public Vector getCurrentVector() {
        return currentVector;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean hasFinished() {
        return hasFinished;
    }

    @Override
    public boolean isFirstMove() {
        return firstMove;
    }

    @Override
    public void setFirstMove() {
        firstMove = false;
    }
}