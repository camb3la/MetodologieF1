package org.example.Model.Player;

import javafx.scene.paint.Color;
import org.example.Model.Grid.IGrid;
import org.example.Model.Position;
import org.example.Model.Vector;

import java.util.List;

/**
 * Interfaccia che definisce il contratto per tutti i tipi di giocatori
 */
public interface IPlayer {

    /**
     * Ottiene il nome del giocatore
     * @return Il nome del giocatore
     */
    String getName();

    /**
     * Ottiene la posizione attuale del giocatore
     * @return La posizione attuale
     */
    Position getCurrentPosition();

    /**
     * Ottiene il vettore velocità attuale del giocatore
     * @return Il vettore velocità attuale
     */
    Vector getCurrentVector();

    /**
     * Ottiene il colore del giocatore
     * @return Il colore del giocatore
     */
    Color getColor();

    /**
     * Verifica se il giocatore è un bot
     * @return true se è un bot, false altrimenti
     */
    boolean isBot();

    /**
     * Verifica se il giocatore è umano
     * @return true se è umano, false altrimenti
     */
    boolean isHuman();

    /**
     * Verifica se il giocatore ha terminato la gara
     * @return true se ha terminato, false altrimenti
     */
    boolean hasFinished();

    /**
     * Imposta lo stato di fine gara del giocatore
     */
    void setFinished();

    /**
     * Verifica se è la prima mossa del giocatore
     * @return true se è la prima mossa, false altrimenti
     */
    boolean isFirstMove();

    /**
     * Imposta che la prima mossa è stata compiuta
     */
    void setFirstMove();

    /**
     * Ottiene le possibili mosse del giocatore sulla griglia
     * @param grid La griglia di gioco
     * @return La lista delle posizioni raggiungibili
     */
    List<Position> getPossibleMoves(IGrid grid);

    /**
     * Fa eseguire una mossa al giocatore sulla griglia
     * @param grid La griglia di gioco
     */
    void makeMove(IGrid grid);

    /**
     * Muove il giocatore alla posizione specificata
     * @param newPosition La nuova posizione
     */
    void moveTo(Position newPosition);

    /**
     * Verifica se il giocatore può raggiungere una determinata posizione
     * @param target La posizione da raggiungere
     * @return true se la posizione è raggiungibile, false altrimenti
     */
    boolean canReach(Position target);

}