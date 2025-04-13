package org.example.Model.Player;

import javafx.scene.paint.Color;
import org.example.Model.Interface.MovementStrategy;
import org.example.Model.Position;
import org.example.Model.Strategy.AggressiveBotStrategy;
import org.example.Model.Strategy.ConservativeBotStrategy;

/**
 * Implementazione di un giocatore bot
 */
public class BotPlayer extends AbstractPlayer {

    /**
     * Crea un nuovo bot con strategia casuale
     * @param name Il nome del bot
     * @param startPosition La posizione iniziale
     */
    public BotPlayer(String name, Position startPosition) {
        this(name, startPosition,
                Math.random() < 0.5 ? new ConservativeBotStrategy() : new AggressiveBotStrategy());
    }

    /**
     * Crea un nuovo bot con strategia specifica
     * @param name Il nome del bot
     * @param startPosition La posizione iniziale
     * @param strategy La strategia di movimento
     */
    public BotPlayer(String name, Position startPosition, MovementStrategy strategy) {
        super(name, startPosition, strategy, PlayerColorGenerator.generateRandomColor());
    }

    /**
     * Crea un nuovo bot con strategia e colore specifici
     * @param name Il nome del bot
     * @param startPosition La posizione iniziale
     * @param strategy La strategia di movimento
     * @param color Il colore del bot
     */
    public BotPlayer(String name, Position startPosition, MovementStrategy strategy, Color color) {
        super(name, startPosition, strategy, color);
    }

    @Override
    public boolean isBot() {
        return true;
    }

    @Override
    public boolean isHuman() {
        return false;
    }
}