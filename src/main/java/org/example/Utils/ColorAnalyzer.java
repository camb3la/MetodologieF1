package org.example.Utils;

import java.awt.*;

public class ColorAnalyzer {

    public static boolean isWhiteColor(Color color) {
        return color.getRed() > 200 && color.getGreen() > 200 && color.getBlue() > 200;
    }

    public static boolean isRedColor(Color color) {
        // Verifica se il colore è prevalentemente rosso
        return color.getRed() > 200 && color.getGreen() < 50 && color.getBlue() < 50;
    }
}