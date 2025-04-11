package org.example.Service.Track;

import org.example.Model.Position;

public interface IFinishLineDetector {

    /**
     * Verifica se una determinata posizione si trova sulla linea di partenza/arrivo.
     *
     * @param position La posizione da verificare
     * @return true se la posizione è sulla linea di partenza/arrivo, false altrimenti
     */
    boolean isFinishLine(Position position);

    /**
     * Verifica se il percorso da una posizione a un'altra attraversa la linea di partenza/arrivo.
     *
     * @param from La posizione di partenza
     * @param to La posizione di arrivo
     * @return true se il percorso attraversa la linea di partenza/arrivo, false altrimenti
     */
    boolean crossesFinishLine(Position from, Position to);

    /**
     * Ottiene le posizioni di partenza per un determinato numero di giocatori.
     *
     * @param numPlayers Il numero di giocatori
     * @return Una lista di posizioni di partenza
     */
    java.util.List<Position> getStartingPositions(int numPlayers);

}
