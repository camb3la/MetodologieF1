package org.example.Controller.Turn;

import org.example.Model.Player;
import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    private final List<Player> players;
    private int currentPlayerIndex;

    public TurnManager(List<Player> players) {
        this.players = new ArrayList<>(players);
        this.currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
}