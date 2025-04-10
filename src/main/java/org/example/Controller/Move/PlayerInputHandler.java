package org.example.Controller.Move;

import org.example.Model.Position;
import org.example.View.GridView;
import org.example.Event.OnCellSelectListener;

import java.util.List;

/**
 * Gestisce gli input del giocatore umano e li comunica al controller
 */
public class PlayerInputHandler {
    private final GridView gridView;
    private OnCellSelectListener cellSelectListener;

    public PlayerInputHandler(GridView gridView) {
        this.gridView = gridView;
    }

    /**
     * Imposta il listener per la selezione delle celle
     * @param listener Il listener da notificare quando una cella viene selezionata
     */
    public void setOnCellSelectListener(OnCellSelectListener listener) {
        this.cellSelectListener = listener;

        // Collega l'input della view a questo handler
        gridView.setOnCellSelectListener(this::handleCellSelection);
    }

    /**
     * Gestisce l'evento di selezione di una cella e lo inoltra al listener
     * @param position La posizione selezionata
     */
    private void handleCellSelection(Position position) {
        if (cellSelectListener != null) {
            cellSelectListener.onCellSelected(position);
        }
    }

    /**
     * Mostra le mosse possibili per un giocatore umano
     * @param possibleMoves Lista delle mosse possibili
     */
    public void showPossibleMoves(List<Position> possibleMoves) {
        gridView.highlightPossibleMoves(possibleMoves);
    }

    /**
     * Pulisce le evidenziazioni
     */
    public void clearHighlights() {
        gridView.clearHighlights();
    }
}