package org.example.Controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.Model.Grid;
import org.example.Model.Player;
import org.example.Model.Position;
import org.example.Model.Interface.OnGameEndListener;
import org.example.View.GridView;

import java.util.ArrayList;
import java.util.List;

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
        if (currentPlayer.isBot()) processBotTurn();
        // Se è un giocatore umano, mostra le mosse possibili
        else gridView.highlightPossibleMoves(currentPlayer.getPossibleMoves(track));
    }

    private void handleCellSelection(Position position) {
        if (gameFinished || isProcessingTurn) return;

        if (!getCurrentPlayer().isHuman()) return;

        if (isValidMove(getCurrentPlayer(), position)) {
            isProcessingTurn = true;
            processPlayerMove(getCurrentPlayer(), position);
        }
    }

    private void processPlayerMove(Player player, Position targetPosition) {
        Position oldPosition = player.getCurrentPosition();

        // Esegui il movimento
        player.moveTo(targetPosition);

        // Aggiorna la visualizzazione
        gridView.updatePlayerMarker(player, targetPosition);

        // Verifica vittoria solo se non è la prima mossa
        if (!player.isFirstMove()){
            if(track.crossFinishLine(oldPosition, targetPosition)) {
                handlePlayerWin(player);
                return;
            }
        }
        completeCurrentTurn();
        player.setFirstMove();
    }

    private void processBotTurn() {
        if (isProcessingTurn) return;
        isProcessingTurn = true;

        Player bot = getCurrentPlayer();
        Position oldPosition = bot.getCurrentPosition();

        bot.makeMove(track);
        gridView.updatePlayerMarker(bot, bot.getCurrentPosition());

        // Verifica vittoria solo se non è la prima mossa
        if (!bot.isFirstMove() && track.crossFinishLine(oldPosition, bot.getCurrentPosition())) {
            handlePlayerWin(bot);
            return;
        }

        completeCurrentTurn();
    }

    private void handlePlayerWin(Player player) {
        gameFinished = true;
        player.setFinished();
        gridView.clearHighlights();

        // In un contesto di test, Platform.runLater non sarà disponibile
        if (Platform.isFxApplicationThread()) showWinWindow(player);
        // Nel contesto di test, notifica direttamente il listener se presente
        else if (gameEndListener != null) gameEndListener.newGame();
    }

    private void showWinWindow(Player player) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gara Finita!");
        alert.setHeaderText(player.getName() + " ha vinto la gara!");
        alert.setContentText("Vuoi giocare una nuova partita?");

        ButtonType newGameButton = new ButtonType("New Game");
        ButtonType quitButton = new ButtonType("Quit");
        alert.getButtonTypes().setAll(newGameButton, quitButton);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == newGameButton && gameEndListener != null) {
                gameEndListener.newGame();
            } else if (gameEndListener != null) {
                gameEndListener.quitGame();
            }
        });
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

}