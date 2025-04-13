package org.example.Controller.Turn;

import org.example.Model.Player.IPlayer;

import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    private final List<IPlayer> players;
    private int currentPlayerIndex;

    public TurnManager(List<IPlayer> players) {
        this.players = new ArrayList<>(players);
        this.currentPlayerIndex = 0;
    }

    public IPlayer getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
}