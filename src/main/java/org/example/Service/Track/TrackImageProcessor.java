package org.example.Service.Track;

import java.awt.Color;
import org.example.Model.Position;
import org.example.Utils.Color.IColorAnalyzer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TrackImageProcessor implements ITrackProcessor{

    private final IColorAnalyzer colorAnalyzer;

    /**
     * Costruttore che accetta un analizzatore di colori.
     *
     * @param colorAnalyzer L'analizzatore di colori da utilizzare
     */
    public TrackImageProcessor(IColorAnalyzer colorAnalyzer) {
        this.colorAnalyzer = colorAnalyzer;
    }

    @Override
    public boolean[][] processTrackImage(BufferedImage image, int cellSize, int rows, int cols) {
        boolean[][] track = new boolean[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Calcola il pixel centrale della cella
                int pixelX = col * cellSize + (cellSize / 2);
                int pixelY = row * cellSize + (cellSize / 2);

                // Verifica che il pixel sia all'interno dell'immagine
                if (pixelX < image.getWidth() && pixelY < image.getHeight()) {
                    Color cellColor = new Color(image.getRGB(pixelX, pixelY));

                    // Una cella è percorribile se è di colore bianco
                    track[row][col] = colorAnalyzer.isWhiteColor(cellColor);
                }
            }
        }

        return track;
    }

    @Override
    public List<Position> identifyStartLine(BufferedImage image, int cellSize, int rows, int cols) {
        List<Position> startLine = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Calcola il pixel centrale della cella
                int pixelX = col * cellSize + (cellSize / 2);
                int pixelY = row * cellSize + (cellSize / 2);

                // Verifica che il pixel sia all'interno dell'immagine
                if (pixelX < image.getWidth() && pixelY < image.getHeight()) {
                    Color cellColor = new Color(image.getRGB(pixelX, pixelY));

                    // La linea di partenza/arrivo è identificata dal colore rosso
                    if (colorAnalyzer.isRedColor(cellColor)) {
                        startLine.add(new Position(col, row));
                        System.out.println("Found red pixel at: pixel(" + pixelX + "," + pixelY +
                                ") -> cell(" + col + "," + row + ")");
                    }
                }
            }
        }

        return startLine;
    }

}
