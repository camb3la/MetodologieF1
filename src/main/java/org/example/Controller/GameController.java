package org.example.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.Model.Grid;
import org.example.Model.Player;
import org.example.Model.Position;
import org.example.Model.Interface.OnGameEndListener;
import org.example.View.GridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameController {
    private final Grid track;
    private final List<Player> players;
    private int currentPlayerIndex;
    private final GridView gridView;
    private boolean isProcessingTurn;
    private boolean gameFinished;
    private OnGameEndListener gameEndListener;

    public GameController(Grid track, List<Player> players, GridView gridView) {
        this.track = track;
        this.players = new ArrayList<>(players);
        this.currentPlayerIndex = 0;
        this.gridView = gridView;
        this.isProcessingTurn = false;
        this.gameFinished = false;

        initializeGame();
    }

    public void setOnGameEndListener(OnGameEndListener listener) {
        this.gameEndListener = listener;
    }

    private void initializeGame() {
        System.out.println("Inizializzazione gioco...");

        // Posiziona i marker iniziali per tutti i giocatori
        for (Player player : players) {
            gridView.updatePlayerMarker(player, player.getCurrentPosition());
        }

        // Setup del listener per i click sulla griglia
        gridView.setOnCellSelectListener(this::handleCellSelection);

        // Imposta e mostra il primo giocatore
        Player firstPlayer = getCurrentPlayer();
        gridView.setCurrentPlayer(firstPlayer);

        if (firstPlayer.isHuman()) {
            System.out.println("Primo giocatore è umano, calcolo mosse possibili");
            List<Position> possibleMoves = firstPlayer.getPossibleMoves(track);

            // Aggiungiamo qui i log dettagliati
            System.out.println("Calcolate posizioni per highlight:");
            possibleMoves.forEach(pos ->
                    System.out.println("Posizione calcolata: X=" + pos.getX() + ", Y=" + pos.getY()));

            System.out.println("Trovate " + possibleMoves.size() + " mosse possibili");
            gridView.highlightPossibleMoves(possibleMoves);
        }
    }

    private void startTurn() {
        if (gameFinished) return;

        Player currentPlayer = getCurrentPlayer();
        gridView.clearHighlights();

        // Se è un bot, processa il suo turno
        if (currentPlayer.isBot()) {
            processBotTurn();
        } else {
            // Se è un giocatore umano, mostra le mosse possibili
            List<Position> possibleMoves = currentPlayer.getPossibleMoves(track);
            gridView.highlightPossibleMoves(possibleMoves);
        }
    }

    private void handleCellSelection(Position position) {
        if (gameFinished || isProcessingTurn) return;

        Player currentPlayer = getCurrentPlayer();
        if (!currentPlayer.isHuman()) return;

        if (isValidMove(currentPlayer, position)) {
            isProcessingTurn = true;
            processPlayerMove(currentPlayer, position);
        }
    }

    private void processPlayerMove(Player player, Position targetPosition) {
        // Esegui il movimento
        player.moveTo(targetPosition);

        // Aggiorna la visualizzazione
        gridView.updatePlayerMarker(player, targetPosition);

        // Verifica vittoria
        if (track.isFinishLine(targetPosition)) {
            handlePlayerWin(player);
            return;
        }

        completeCurrentTurn();
    }

    private void processBotTurn() {
        if (isProcessingTurn) return;
        isProcessingTurn = true;

        Player bot = getCurrentPlayer();
        bot.makeMove(track);
        gridView.updatePlayerMarker(bot, bot.getCurrentPosition());

        // Verifica vittoria
        if (track.isFinishLine(bot.getCurrentPosition())) {
            handlePlayerWin(bot);
            return;
        }

        // Breve delay prima di completare il turno
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        completeCurrentTurn();
    }

    private void handlePlayerWin(Player player) {
        gameFinished = true;
        player.setFinished();
        gridView.clearHighlights();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Race Finished!");
        alert.setHeaderText(player.getName() + " has won the race!");
        alert.setContentText("Would you like to start a new game?");

        ButtonType newGameButton = new ButtonType("New Game");
        ButtonType quitButton = new ButtonType("Quit");
        alert.getButtonTypes().setAll(newGameButton, quitButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == newGameButton && gameEndListener != null) {
                gameEndListener.newGame();
            } else if (gameEndListener != null) {
                gameEndListener.quitGame();
            }
        }
    }

    private void completeCurrentTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        isProcessingTurn = false;
        gridView.setCurrentPlayer(getCurrentPlayer());
        startTurn();
    }

    private boolean isValidMove(Player player, Position targetPosition) {
        return player.canReach(targetPosition) &&
                track.isWalkable(targetPosition.getX(), targetPosition.getY());
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
}