package org.example.Model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.example.Utils.ColorAnalyzer;

public class Grid {
    private boolean[][] track;
    private List<Position> startLine;
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

        if (ColorAnalyzer.isRedColor(cellColor)) {
            startLine.add(new Position(row, col));
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

        // Prima prova a posizionare sulla linea di partenza
        int startLineSize = startLine.size();
        for (int i = 0; i < Math.min(numPlayers, startLineSize); i++) {
            positions.add(startLine.get(i));
        }

        // Se servono più posizioni, cerca a sinistra della linea di partenza
        if (numPlayers > startLineSize) {
            Position leftmostStart = startLine.stream()
                    .min((p1, p2) -> Integer.compare(p1.getX(), p2.getX()))
                    .orElseThrow();

            for (int i = positions.size(); i < numPlayers; i++) {
                // Cerca la prima posizione libera a sinistra
                int col = leftmostStart.getX() - (i - startLineSize + 1);
                if (col >= 0 && isWalkable(leftmostStart.getY(), col)) {
                    positions.add(new Position(leftmostStart.getY(), col));
                } else {
                    throw new IllegalStateException("Non c'è abbastanza spazio per tutti i giocatori");
                }
            }
        }

        return positions;
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

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getCellSize() {
        return CELL_SIZE;
    }
}