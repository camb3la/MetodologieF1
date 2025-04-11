package org.example.Service.Track;

import org.example.Model.Position;
import org.example.Service.Coordinate.CoordinateConverter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FinishLineDetector implements IFinishLineDetector {

    private final List<Position> startLine;

    private final CoordinateConverter coordinateConverter;

    /**
     * Costruttore che accetta la lista delle posizioni della linea di partenza/arrivo.
     *
     * @param startLine La lista delle posizioni che compongono la linea di partenza/arrivo
     * @param coordinateConverter Il convertitore di coordinate
     */
    public FinishLineDetector(List<Position> startLine, CoordinateConverter coordinateConverter) {
        this.startLine = new ArrayList<>(startLine);
        this.coordinateConverter = coordinateConverter;

        if (startLine.isEmpty()) {
            throw new IllegalStateException("No start/finish line (red) found in the image");
        }
    }

    @Override
    public boolean isFinishLine(Position position) {
        // Convertiamo la posizione nel sistema di coordinate della linea d'arrivo
        Position convertedPosition = coordinateConverter.gameToGrid(position);
        return startLine.stream()
                .anyMatch(p -> p.getX() == convertedPosition.getX() && p.getY() == convertedPosition.getY());
    }

    @Override
    public boolean crossesFinishLine(Position from, Position to) {
        // Se il punto di arrivo è sulla linea d'arrivo, allora la attraversiamo
        if (isFinishLine(to)) {
            return true;
        }

        // Convertiamo le posizioni nel sistema di coordinate della griglia
        Position fromConverted = coordinateConverter.gameToGrid(from);
        Position toConverted = coordinateConverter.gameToGrid(to);

        // Controlliamo se il segmento da 'from' a 'to' interseca la linea del traguardo
        int x1 = fromConverted.getX();
        int y1 = fromConverted.getY();
        int x2 = toConverted.getX();
        int y2 = toConverted.getY();

        // Per ogni punto della linea del traguardo
        for (Position finishPoint : startLine) {
            if (isPointOnLine(finishPoint, x1, y1, x2, y2)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Position> getStartingPositions(int numPlayers) {
        List<Position> positions = new ArrayList<>();

        // Assegna le posizioni disponibili sulla linea di partenza
        int startLineSize = startLine.size();
        for (int i = 0; i < Math.min(numPlayers, startLineSize); i++) {
            Position gridPos = startLine.get(i);
            // Convertiamo la posizione della griglia in posizione di gioco
            Position gamePos = coordinateConverter.gridToGame(gridPos);
            positions.add(gamePos);
        }

        // Se servono più posizioni, cerca a sinistra della linea di partenza
        if (numPlayers > startLineSize) {
            Position leftmostStart = startLine.stream()
                    .min(Comparator.comparingInt(Position::getY))
                    .orElseThrow();

            for (int i = positions.size(); i < numPlayers; i++) {
                // Cerca la prima posizione libera a sinistra
                int col = leftmostStart.getX() - (i - startLineSize + 1);
                if (col >= 0) {
                    Position gridPos = new Position(col, leftmostStart.getY());
                    Position gamePos = coordinateConverter.gridToGame(gridPos);
                    positions.add(gamePos);
                } else {
                    throw new IllegalStateException("Non c'è abbastanza spazio per tutti i giocatori");
                }
            }
        }

        return positions;
    }

    /**
     * Verifica se un punto si trova su un segmento di linea.
     *
     * @param point Il punto da verificare
     * @param x1 La coordinata x del primo estremo del segmento
     * @param y1 La coordinata y del primo estremo del segmento
     * @param x2 La coordinata x del secondo estremo del segmento
     * @param y2 La coordinata y del secondo estremo del segmento
     * @return true se il punto si trova sul segmento, false altrimenti
     */
    private boolean isPointOnLine(Position point, int x1, int y1, int x2, int y2) {
        // Verifica se il punto si trova sul segmento tra (x1,y1) e (x2,y2)

        // Prima controlliamo se il punto è nel rettangolo delimitato dai punti
        if (point.getX() < Math.min(x1, x2) || point.getX() > Math.max(x1, x2) ||
                point.getY() < Math.min(y1, y2) || point.getY() > Math.max(y1, y2)) {
            return false;
        }

        // Se il segmento è verticale
        if (x1 == x2) {
            return point.getX() == x1;
        }

        // Se il segmento è orizzontale
        if (y1 == y2) {
            return point.getY() == y1;
        }

        // Altrimenti verifichiamo se il punto giace sulla retta
        // usando l'equazione della retta y = mx + q
        double m = (double)(y2 - y1) / (x2 - x1);
        double q = y1 - m * x1;

        // Il punto deve soddisfare l'equazione della retta con una certa tolleranza
        // dato che stiamo lavorando con coordinate intere
        return Math.abs(point.getY() - (m * point.getX() + q)) < 0.1;
    }
}
