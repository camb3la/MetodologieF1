package org.example.Model.Grid;

import org.example.Model.Position;
import org.example.Service.Coordinate.CoordinateConverter;
import org.example.Service.Track.IFinishLineDetector;
import org.example.Service.Track.ITrackProcessor;
import org.example.Utils.Valdation.ImageValidator;


import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Implementazione dell'interfaccia IGrid che rappresenta una griglia di gioco basata su una traccia.
 */
public class TrackGrid implements IGrid {
    private final boolean[][] track;
    private final IFinishLineDetector finishLineDetector;
    private final CoordinateConverter coordinateConverter;
    private final int rows;
    private final int cols;
    private final int cellSize;

    /**
     * Costruttore che inizializza la griglia a partire da un'immagine.
     *
     * @param image L'immagine della traccia
     * @param trackProcessor Il processore per l'immagine della traccia
     * @param finishLineDetector Il rilevatore della linea di partenza/arrivo
     * @param coordinateConverter Il convertitore di coordinate
     * @param imageValidator Il validatore dell'immagine
     * @param cellSize La dimensione in pixel di una cella
     * @throws IllegalArgumentException Se l'immagine non è valida
     */
    public TrackGrid(BufferedImage image,
                     ITrackProcessor trackProcessor,
                     IFinishLineDetector finishLineDetector,
                     CoordinateConverter coordinateConverter,
                     ImageValidator imageValidator,
                     int cellSize) {

        // Valida l'immagine
        imageValidator.validateImage(image);

        this.cellSize = cellSize;
        this.rows = image.getHeight() / cellSize;
        this.cols = image.getWidth() / cellSize;
        this.coordinateConverter = coordinateConverter;
        this.finishLineDetector = finishLineDetector;

        // Processa l'immagine per ottenere la matrice delle celle percorribili
        this.track = trackProcessor.processTrackImage(image, cellSize, rows, cols);
    }

    @Override
    public boolean isWalkable(int row, int col) {
        return isValidPosition(row, col) && track[row][col];
    }

    @Override
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    @Override
    public boolean isFinishLine(Position position) {
        return finishLineDetector.isFinishLine(position);
    }

    @Override
    public boolean crossFinishLine(Position from, Position to) {
        return finishLineDetector.crossesFinishLine(from, to);
    }

    @Override
    public List<Position> getStartingPositions(int numPlayers) {
        return finishLineDetector.getStartingPositions(numPlayers);
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public int getCellSize() {
        return cellSize;
    }
}