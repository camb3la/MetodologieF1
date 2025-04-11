package org.example.Service.Track;

import org.example.Model.Position;

import java.awt.image.BufferedImage;
import java.util.List;

public interface ITrackProcessor {

    /**
     * Processa l'immagine della traccia per identificare le celle percorribili.
     *
     * @param image L'immagine della traccia da processare
     * @param cellSize La dimensione di ogni cella (in pixel)
     * @param rows Il numero di righe della griglia
     * @param cols Il numero di colonne della griglia
     * @return Una matrice booleana che indica quali celle sono percorribili
     */
    boolean[][] processTrackImage(BufferedImage image, int cellSize, int rows, int cols);

    /**
     * Identifica la linea di partenza/arrivo nell'immagine della traccia.
     *
     * @param image L'immagine della traccia da processare
     * @param cellSize La dimensione di ogni cella (in pixel)
     * @param rows Il numero di righe della griglia
     * @param cols Il numero di colonne della griglia
     * @return Una lista di posizioni che compongono la linea di partenza/arrivo
     */
    List<Position> identifyStartLine(BufferedImage image, int cellSize, int rows, int cols);

}
