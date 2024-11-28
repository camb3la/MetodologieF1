package org.example.Model;

import java.util.ArrayList;
import java.util.List;

public class Vector {
    private final int dx;
    private final int dy;

    public Vector(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public List<Vector> getPossibleNextMoves() {
        List<Vector> moves = new ArrayList<>();

        // Per ogni componente possiamo variare di ±1 rispetto al valore attuale
        for (int newDx = dx - 1; newDx <= dx + 1; newDx++) {
            for (int newDy = dy - 1; newDy <= dy + 1; newDy++) {
                moves.add(new Vector(newDx, newDy));
            }
        }

        return moves;
    }

    public boolean isValidMove(Vector currentVector) {
        // Verifica che il nuovo vettore vari al massimo di ±1 in ogni componente
        return Math.abs(dx - currentVector.dx) <= 1 &&
                Math.abs(dy - currentVector.dy) <= 1;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return dx == vector.dx && dy == vector.dy;
    }

    @Override
    public int hashCode() {
        return 31 * dx + dy;
    }

    @Override
    public String toString() {
        return "Vector{dx=" + dx + ", dy=" + dy + "}";
    }
}