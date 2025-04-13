package org.example.Model.Player;

import javafx.scene.paint.Color;
import org.example.Model.Position;
import org.example.Model.Strategy.HumanStrategy;

/**
 * Implementazione di un giocatore umano
 */
public class HumanPlayer extends AbstractPlayer {

    /**
     * Crea un nuovo giocatore umano
     * @param name Il nome del giocatore
     * @param startPosition La posizione iniziale
     */
    public HumanPlayer(String name, Position startPosition) {
        super(name, startPosition, new HumanStrategy(), PlayerColorGenerator.generateRandomColor());
    }

    /**
     * Crea un nuovo giocatore umano con un colore specifico
     * @param name Il nome del giocatore
     * @param startPosition La posizione iniziale
     * @param color Il colore del giocatore
     */
    public HumanPlayer(String name, Position startPosition, Color color) {
        super(name, startPosition, new HumanStrategy(), color);
    }

    @Override
    public boolean isBot() {
        return false;
    }

    @Override
    public boolean isHuman() {
        return true;
    }
}