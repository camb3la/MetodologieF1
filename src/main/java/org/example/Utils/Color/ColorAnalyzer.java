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

}