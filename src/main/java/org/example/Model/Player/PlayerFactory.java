package org.example.Model.Player;

import org.example.Model.Grid.IGrid;
import org.example.Model.Interface.MovementStrategy;
import org.example.Model.Position;
import org.example.Model.Strategy.AggressiveBotStrategy;
import org.example.Model.Strategy.ConservativeBotStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory per la creazione di giocatori
 */
public class PlayerFactory {

    /**
     * Crea un giocatore umano
     * @param name Nome del giocatore
     * @param startPosition Posizione iniziale
     * @return Un nuovo giocatore umano
     */
    public static IPlayer createHumanPlayer(String name, Position startPosition) {
        return new HumanPlayer(name, startPosition);
    }

    /**
     * Crea un bot con strategia casuale
     * @param name Nome del bot
     * @param startPosition Posizione iniziale
     * @return Un nuovo bot
     */
    public static IPlayer createBot(String name, Position startPosition) {
        return new BotPlayer(name, startPosition);
    }

    /**
     * Crea un bot con strategia aggressiva
     * @param name Nome del bot
     * @param startPosition Posizione iniziale
     * @return Un nuovo bot aggressivo
     */
    public static IPlayer createAggressiveBot(String name, Position startPosition) {
        return new BotPlayer(name, startPosition, new AggressiveBotStrategy());
    }

    /**
     * Crea un bot con strategia conservativa
     * @param name Nome del bot
     * @param startPosition Posizione iniziale
     * @return Un nuovo bot conservativo
     */
    public static IPlayer createConservativeBot(String name, Position startPosition) {
        return new BotPlayer(name, startPosition, new ConservativeBotStrategy());
    }

    /**
     * Crea un bot con strategia personalizzata
     * @param name Nome del bot
     * @param startPosition Posizione iniziale
     * @param strategy Strategia di movimento
     * @return Un nuovo bot con la strategia specificata
     */
    public static IPlayer createCustomBot(String name, Position startPosition, MovementStrategy strategy) {
        return new BotPlayer(name, startPosition, strategy);
    }

    /**
     * Crea una lista di giocatori per iniziare una partita
     * @param grid La griglia di gioco
     * @param humanPlayerNames I nomi dei giocatori umani
     * @param numBots Il numero di bot
     * @return Una lista di giocatori
     */
    public static List<IPlayer> createPlayers(IGrid grid, List<String> humanPlayerNames, int numBots) {
        int totalPlayers = humanPlayerNames.size() + numBots;
        List<Position> startPositions = grid.getStartingPositions(totalPlayers);
        List<IPlayer> players = new ArrayList<>();

        // Crea i giocatori umani
        for (int i = 0; i < humanPlayerNames.size(); i++) {
            players.add(createHumanPlayer(humanPlayerNames.get(i), startPositions.get(i)));
        }

        // Crea i bot
        for (int i = 0; i < numBots; i++) {
            String botName = "Bot " + (i + 1);
            players.add(createBot(botName, startPositions.get(humanPlayerNames.size() + i)));
        }

        return players;
    }
}