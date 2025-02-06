package org.example.Model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.example.Utils.ColorAnalyzer;

import static org.example.Utils.ColorAnalyzer.isRedColor;

public class Grid {
    private final boolean[][] track;
    private final List<Position> startLine;
    private static final int CELL_SIZE = 20;
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    private static final int COLS = WIDTH / CELL_SIZE;
    private static final int ROWS = HEIGHT / CELL_SIZE;

    public Grid(BufferedImage image) throws IllegalAccessException {
        validateImage(image);

        track = new boolean[ROWS][COLS];
        startLine = new ArrayList<>();

        processGridCells(image);
        validateStartLine();
    }

    private void validateImage(BufferedImage image) {
        if (image.getWidth() != WIDTH || image.getHeight() != HEIGHT) {
            throw new IllegalArgumentException(
                    String.format("Image dimensions must be %dx%d pixels", WIDTH, HEIGHT)
            );
        }
    }

    private void processGridCells(BufferedImage image) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                processSingleCell(image, row, col);
            }
        }
    }

    private void processSingleCell(BufferedImage image, int row, int col) {
        // Calcola il pixel centrale della cella
        int pixelX = col * CELL_SIZE + (CELL_SIZE / 2);
        int pixelY = row * CELL_SIZE + (CELL_SIZE / 2);

        Color cellColor = new Color(image.getRGB(pixelX, pixelY));

        if (isRedColor(cellColor)) {
            startLine.add(new Position(col, row));  // Manteniamo (col, row) come prima
            System.out.println("Found red pixel at: pixel(" + pixelX + "," + pixelY + ") -> cell(" + col + "," + row + ")");
        }

        track[row][col] = ColorAnalyzer.isWhiteColor(cellColor);
    }

    private void validateStartLine() {
        if (startLine.isEmpty()) {
            throw new IllegalStateException("No start/finish line (red) found in the image");
        }
    }

    public List<Position> getStartingPositions(int numPlayers) {
        List<Position> positions = new ArrayList<>();

        int startLineSize = startLine.size();
        for (int i = 0; i < Math.min(numPlayers, startLineSize); i++) {
            // Inverte le coordinate per matchare il nuovo sistema
            Position startPos = startLine.get(i);
            positions.add(new Position(startPos.getY(), startPos.getX()));
        }

        // Se servono più posizioni, cerca a sinistra della linea di partenza
        if (numPlayers > startLineSize) {
            Position leftmostStart = startLine.stream()
                    .min((p1, p2) -> Integer.compare(p1.getY(), p2.getY()))
                    .orElseThrow();

            for (int i = positions.size(); i < numPlayers; i++) {
                // Cerca la prima posizione libera a sinistra
                int col = leftmostStart.getX() - (i - startLineSize + 1);
                if (col >= 0 && isWalkable(col, leftmostStart.getX())) {
                    positions.add(new Position(leftmostStart.getX(), col));
                } else {
                    throw new IllegalStateException("Non c'è abbastanza spazio per tutti i giocatori");
                }
            }
        }

        return positions;
    }

    public boolean crossFinishLine(Position from, Position to){
        Position fromConverted = new Position(from.getY(), from.getX());
        Position toConverted = new Position(to.getY(), to.getX());

        if (isFinishLine(toConverted)) {
            return true;
        }

        // Controlliamo se il segmento da 'from' a 'to' interseca la linea del traguardo
        int x1 = fromConverted.getX();
        int y1 = fromConverted.getY();
        int x2 = toConverted.getX();
        int y2 = toConverted.getY();

        // Per ogni punto della linea del traguardo
        for (Position finishPoint : startLine) {
            if (isPointOnLine(finishPoint, x1, y1, x2, y2)) return true;
        }
        return false;
    }

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

    public boolean isFinishLine(Position position) {
        return startLine.stream()
                .anyMatch(p -> p.getX() == position.getX() && p.getY() == position.getY());
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    public boolean isWalkable(int row, int col) {
        return isValidPosition(row, col) && track[row][col];
    }

    public int getRows() {
        return ROWS;
    }

    public int getCols() {
        return COLS;
    }

    public int getCellSize() {
        return CELL_SIZE;
    }
}