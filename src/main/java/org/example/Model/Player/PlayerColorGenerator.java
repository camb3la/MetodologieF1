package org.example.Model.Player;

import javafx.scene.paint.Color;

/**
 * Classe utilitaria per generare colori per i giocatori
 */
public class PlayerColorGenerator {

    private static final Color[] PREDEFINED_COLORS = {
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.PURPLE,
            Color.ORANGE,
            Color.CYAN,
            Color.MAGENTA,
            Color.YELLOW,
            Color.BROWN,
            Color.PINK
    };

    private static int colorIndex = 0;

    /**
     * Genera un colore casuale per un giocatore
     * @return Un colore casuale
     */
    public static Color generateRandomColor() {
        return Color.rgb(
                (int)(Math.random() * 200) + 55,   // Evita colori troppo scuri
                (int)(Math.random() * 200) + 55,
                (int)(Math.random() * 200) + 55
        );
    }

    /**
     * Restituisce il prossimo colore predefinito dalla lista
     * @return Un colore predefinito
     */
    public static Color getNextPredefinedColor() {
        Color color = PREDEFINED_COLORS[colorIndex % PREDEFINED_COLORS.length];
        colorIndex++;
        return color;
    }

    /**
     * Resetta l'indice dei colori predefiniti
     */
    public static void resetColorIndex() {
        colorIndex = 0;
    }
}