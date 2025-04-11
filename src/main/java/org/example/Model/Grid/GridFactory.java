package org.example.Model.Grid;

import org.example.Service.Coordinate.CoordinateConverter;
import org.example.Service.Track.FinishLineDetector;
import org.example.Service.Track.IFinishLineDetector;
import org.example.Service.Track.ITrackProcessor;
import org.example.Service.Track.TrackImageProcessor;
import org.example.Utils.Color.ColorAnalyzer;
import org.example.Utils.Color.IColorAnalyzer;
import org.example.Utils.Valdation.ImageValidator;

import java.awt.image.BufferedImage;

/**
 * Factory per la creazione di oggetti Grid.
 * Questa classe aiuta a gestire la complessità della creazione di Grid
 * e dei suoi componenti dipendenti.
 */
public class GridFactory {

    // Costanti di default
    private static final int DEFAULT_CELL_SIZE = 20;
    private static final int DEFAULT_WIDTH = 1920;
    private static final int DEFAULT_HEIGHT = 1080;

    /**
     * Crea una nuova istanza di Grid con i parametri di default.
     *
     * @param image L'immagine della traccia
     * @return Una nuova istanza di IGrid
     * @throws IllegalArgumentException Se l'immagine non è valida
     */
    public static IGrid createGrid(BufferedImage image) {
        return createGrid(image, DEFAULT_CELL_SIZE, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Crea una nuova istanza di Grid con parametri personalizzati.
     *
     * @param image L'immagine della traccia
     * @param cellSize La dimensione in pixel di una cella
     * @param width La larghezza richiesta per l'immagine
     * @param height L'altezza richiesta per l'immagine
     * @return Una nuova istanza di IGrid
     * @throws IllegalArgumentException Se l'immagine non è valida
     */
    public static IGrid createGrid(BufferedImage image, int cellSize, int width, int height) {
        // Crea le dipendenze
        IColorAnalyzer colorAnalyzer = new ColorAnalyzer();
        ITrackProcessor trackProcessor = new TrackImageProcessor(colorAnalyzer);
        CoordinateConverter coordinateConverter = new CoordinateConverter();
        ImageValidator imageValidator = new ImageValidator(width, height);

        // Valida l'immagine e processa la traccia
        imageValidator.validateImage(image);
        var startLine = trackProcessor.identifyStartLine(image, cellSize, height / cellSize, width / cellSize);

        // Crea il rilevatore della linea di partenza/arrivo
        IFinishLineDetector finishLineDetector = new FinishLineDetector(startLine, coordinateConverter);

        // Crea e restituisce la griglia
        return new TrackGrid(image, trackProcessor, finishLineDetector, coordinateConverter, imageValidator, cellSize);
    }
}