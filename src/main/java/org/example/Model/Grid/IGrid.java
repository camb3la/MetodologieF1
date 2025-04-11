package org.example.Model.Grid;

import org.example.Model.Position;
import java.util.List;

/**
 * Interfaccia che definisce i comportamenti essenziali di una griglia di gioco.
 */
public interface IGrid {

    /**
     * Verifica se una posizione è percorribile.
     *
     * @param row La riga nella griglia
     * @param col La colonna nella griglia
     * @return true se la posizione è percorribile, false altrimenti
     */
    boolean isWalkable(int row, int col);

    /**
     * Verifica se una posizione è valida (all'interno dei limiti della griglia).
     *
     * @param row La riga nella griglia
     * @param col La colonna nella griglia
     * @return true se la posizione è valida, false altrimenti
     */
    boolean isValidPosition(int row, int col);

    /**
     * Verifica se una certa posizione si trova sulla linea di partenza/arrivo.
     *
     * @param position La posizione da verificare
     * @return true se la posizione è sulla linea di partenza/arrivo, false altrimenti
     */
    boolean isFinishLine(Position position);

    /**
     * Verifica se il movimento da una posizione all'altra attraversa la linea di partenza/arrivo.
     *
     * @param from La posizione di partenza
     * @param to La posizione di arrivo
     * @return true se il movimento attraversa la linea di partenza/arrivo, false altrimenti
     */
    boolean crossFinishLine(Position from, Position to);

    /**
     * Ottiene le posizioni iniziali per un determinato numero di giocatori.
     *
     * @param numPlayers Il numero di giocatori
     * @return Una lista di posizioni iniziali
     */
    List<Position> getStartingPositions(int numPlayers);

    /**
     * Ottiene il numero di righe della griglia.
     *
     * @return Il numero di righe
     */
    int getRows();

    /**
     * Ottiene il numero di colonne della griglia.
     *
     * @return Il numero di colonne
     */
    int getCols();

    /**
     * Ottiene la dimensione in pixel di una cella della griglia.
     *
     * @return La dimensione in pixel di una cella
     */
    int getCellSize();
}