package org.example.Model;

import javafx.scene.paint.Color;
import org.example.Model.Grid.IGrid;
import org.example.Model.Interface.MovementStrategy;
import org.example.Model.Strategy.AggressiveBotStrategy;
import org.example.Model.Strategy.ConservativeBotStrategy;
import org.example.Model.Strategy.HumanStrategy;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private Position currentPosition;
    private Vector currentVector;
    private final MovementStrategy strategy;
    private final boolean isBot;
    private final Color color;
    private boolean hasFinished;
    private boolean firstMove;

    public Player(String name, Position startPosition, boolean isBot) {
        this.name = name;
        this.currentPosition = startPosition;
        this.currentVector = new Vector(0, 0);  // All'inizio il veicolo è fermo
        this.isBot = isBot;
        this.hasFinished = false;
        this.firstMove = true;
        this.color = Color.rgb(
                (int)(Math.random() * 255),
                (int)(Math.random() * 255),
                (int)(Math.random() * 255)
        );
        this.strategy = isBot ?
                (Math.random() < 0.5 ? new ConservativeBotStrategy() : new AggressiveBotStrategy()) :
                new HumanStrategy();
    }

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
                if(firstMove){                          //Il primo movimento deve essere verso sinistra
                    if (newY < currentPosition.getY()){
                        validMoves.add(newPos);
                        System.out.println("Aggiunta mossa valida: " + newX + "," + newY);
                    }
                }
                else {
                    validMoves.add(newPos);
                    System.out.println("Aggiunta mossa valida: " + newX + "," + newY);
                }
            }
        }
        return validMoves;
    }

    public void makeMove(IGrid grid) {
        Vector nextMove = strategy.getNextMove(currentPosition, currentVector, grid);
        if (nextMove != null && isValidMove(nextMove, grid)) {
            currentVector = nextMove;
            updatePosition();
        }
    }

    public void moveTo(Position newPosition) {

        this.currentVector = new Vector(
                newPosition.getX() - currentPosition.getX(),
                newPosition.getY() - currentPosition.getY()
        );
        this.currentPosition = newPosition;
    }

    private void updatePosition() {
        currentPosition = new Position(
                currentPosition.getX() + currentVector.getDx(),
                currentPosition.getY() + currentVector.getDy()
        );
    }

    public boolean isValidMove(Vector newVector, IGrid grid) {
        if (!newVector.isValidMove(currentVector)) {
            return false;
        }

        int newX = currentPosition.getX() + newVector.getDx();
        int newY = currentPosition.getY() + newVector.getDy();

        return grid.isWalkable(newX, newY);
    }

    public void stop() {
        this.currentVector = new Vector(0, 0);
    }

    public boolean canReach(Position target) {
        Vector requiredMovement = new Vector(
                target.getX() - currentPosition.getX(),
                target.getY() - currentPosition.getY()
        );

        // Verifica se questo movimento può essere ottenuto con una variazione valida della velocità
        return Math.abs(requiredMovement.getDx() - currentVector.getDx()) <= 1 &&
                Math.abs(requiredMovement.getDy() - currentVector.getDy()) <= 1;
    }

    public void setFinished() {
        this.hasFinished = true;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public Vector getCurrentVector() {
        return currentVector;
    }

    public boolean isBot() {
        return isBot;
    }

    public Color getColor() {
        return color;
    }

    public boolean hasFinished() {
        return hasFinished;
    }

    public boolean isHuman() {
        return !isBot;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(){
        firstMove = false;
    }
}