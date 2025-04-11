package org.example.Utils.Color;

import java.awt.*;

public class ColorAnalyzer implements IColorAnalyzer {

    private static final int WHITE_THRESHOLD = 200;
    private static final int RED_MIN_THRESHOLD = 200;
    private static final int OTHER_MAX_THRESHOLD = 50;



    @Override
    public boolean isWhiteColor(Color color) {
        return color.getRed() > WHITE_THRESHOLD &&
                color.getGreen() > WHITE_THRESHOLD &&
                color.getBlue() > WHITE_THRESHOLD;
    }

    @Override
    public boolean isRedColor(Color color) {
        // Verifica se il colore è prevalentemente rosso
        return color.getRed() > RED_MIN_THRESHOLD &&
                color.getGreen() < OTHER_MAX_THRESHOLD &&
                color.getBlue() < OTHER_MAX_THRESHOLD;
    }

    @Override
    public boolean isWalkable(Color color) {
        // Nella versione attuale, una cella è percorribile se è bianca
        return isWhiteColor(color);
    }

    /**
     * Calcola la distanza tra due colori nello spazio RGB.
     * Può essere utile per il matching di colori con una certa tolleranza.
     *
     * @param c1 Il primo colore
     * @param c2 Il secondo colore
     * @return La distanza euclidea tra i due colori nello spazio RGB
     */
    public double colorDistance(Color c1, Color c2) {
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();

        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }
}