package org.example.Utils;

import org.example.Utils.Color.ColorAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

public class ColorAnalyzerTest {

    private ColorAnalyzer colorAnalyzer;

    @BeforeEach
    public void setup() {
        colorAnalyzer = new ColorAnalyzer();
    }

    @Test
    public void testIsWhiteColor() {
        // Colori che dovrebbero essere riconosciuti come bianchi
        assertTrue(colorAnalyzer.isWhiteColor(new Color(255, 255, 255)), "Bianco puro dovrebbe essere riconosciuto come bianco");
        assertTrue(colorAnalyzer.isWhiteColor(new Color(240, 240, 240)), "Bianco leggermente sporco dovrebbe essere riconosciuto come bianco");

        // Colori che non dovrebbero essere riconosciuti come bianchi
        assertFalse(colorAnalyzer.isWhiteColor(new Color(180, 180, 180)), "Grigio medio non dovrebbe essere riconosciuto come bianco");
        assertFalse(colorAnalyzer.isWhiteColor(new Color(100, 100, 100)), "Grigio scuro non dovrebbe essere riconosciuto come bianco");
        assertFalse(colorAnalyzer.isWhiteColor(new Color(0, 0, 0)), "Nero non dovrebbe essere riconosciuto come bianco");
        assertFalse(colorAnalyzer.isWhiteColor(new Color(255, 0, 0)), "Rosso non dovrebbe essere riconosciuto come bianco");
    }

    @Test
    public void testIsRedColor() {
        // Colori che dovrebbero essere riconosciuti come rossi
        assertTrue(colorAnalyzer.isRedColor(new Color(255, 0, 0)), "Rosso puro dovrebbe essere riconosciuto come rosso");
        assertTrue(colorAnalyzer.isRedColor(new Color(220, 10, 10)), "Rosso intenso dovrebbe essere riconosciuto come rosso");

        // Colori che non dovrebbero essere riconosciuti come rossi
        assertFalse(colorAnalyzer.isRedColor(new Color(255, 255, 255)), "Bianco non dovrebbe essere riconosciuto come rosso");
        assertFalse(colorAnalyzer.isRedColor(new Color(0, 255, 0)), "Verde non dovrebbe essere riconosciuto come rosso");
    }

    @Test
    public void testIsWalkable() {
        // isWalkable dovrebbe avere lo stesso comportamento di isWhiteColor nella versione attuale
        assertTrue(colorAnalyzer.isWalkable(new Color(255, 255, 255)), "Il bianco dovrebbe essere percorribile");
        assertTrue(colorAnalyzer.isWalkable(new Color(240, 240, 240)), "Il bianco leggermente sporco dovrebbe essere percorribile");

        assertFalse(colorAnalyzer.isWalkable(new Color(0, 0, 0)), "Il nero non dovrebbe essere percorribile");
        assertFalse(colorAnalyzer.isWalkable(new Color(255, 0, 0)), "Il rosso non dovrebbe essere percorribile");
    }

}