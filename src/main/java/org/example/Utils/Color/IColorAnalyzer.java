package org.example.Utils.Color;

import java.awt.Color;

public interface IColorAnalyzer {

    /**
     * Verifica se un colore è considerato bianco (percorribile nella traccia).
     *
     * @param color Il colore da analizzare
     * @return true se il colore è bianco, false altrimenti
     */
    boolean isWhiteColor(Color color);

    /**
     * Verifica se un colore è considerato rosso (linea di partenza/arrivo).
     *
     * @param color Il colore da analizzare
     * @return true se il colore è rosso, false altrimenti
     */
    boolean isRedColor(Color color);

    /**
     * Verifica se un colore è considerato percorribile.
     *
     * @param color Il colore da analizzare
     * @return true se il colore rappresenta una zona percorribile, false altrimenti
     */
    boolean isWalkable(Color color);

}
