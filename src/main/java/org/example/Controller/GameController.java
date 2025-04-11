package org.example.Controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.Controller.Game.VictoryDetector;
import org.example.Controller.Move.BotPlayHandler;
import org.example.Controller.Move.MoveValidator;
import org.example.Controller.Move.PlayerInputHandler;
import org.example.Controller.Turn.TurnManager;
import org.example.Model.Grid.IGrid;
import org.example.Model.Interface.OnGameEndListener;
import org.example.Model.Player;
import org.example.Model.Position;
import org.example.View.GridView;


import java.util.ArrayList;
import java.util.List;

public class GameController {
    private final IGrid track;
    private final List<Player> players;
    private final GridView gridView;

    // Componenti specializzati
    private final TurnManager turnManager;
    private final MoveValidator moveValidator;
    private final VictoryDetector victoryDetector;
    private final PlayerInputHandler inputHandler;
    private final BotPlayHandler botHandler;

    // Stato del gioco
    private boolean isProcessingTurn;
    private boolean gameFinished;
    private OnGameEndListener gameEndListener;

    public GameController(IGrid track, List<Player> players, GridView gridView) {
        this.track = track;
        this.players = new ArrayList<>(players);
        this.gridView = gridView;
        this.isProcessingTurn = false;
        this.gameFinished = false;

        // Inizializza i componenti specializzati
        this.turnManager = new TurnManager(players);
        this.moveValidator = new MoveValidator(track);
        this.victoryDetector = new VictoryDetector(track);
        this.inputHandler = new PlayerInputHandler(gridView);
        this.botHandler = new BotPlayHandler();

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
        inputHandler.setOnCellSelectListener(this::handleCellSelection);

        // Imposta e mostra il primo giocatore
        Player firstPlayer = turnManager.getCurrentPlayer();
        gridView.setCurrentPlayer(firstPlayer);

        // Avvia il primo turno
        startTurn();
    }

    private void startTurn() {
        if (gameFinished) return;

        Player currentPlayer = turnManager.getCurrentPlayer();
        inputHandler.clearHighlights();

        // Se è un bot, processa il suo turno
        if (currentPlayer.isBot()) {
            processBotTurn();
        }
        // Se è un giocatore umano, mostra le mosse possibili
        else {
            List<Position> possibleMoves = currentPlayer.getPossibleMoves(track);
            inputHandler.showPossibleMoves(possibleMoves);
        }
    }

    private void handleCellSelection(Position position) {
        if (gameFinished || isProcessingTurn) return;

        Player currentPlayer = turnManager.getCurrentPlayer();
        if (!currentPlayer.isHuman()) return;

        if (moveValidator.isValidMove(currentPlayer, position)) {
            isProcessingTurn = true;
            processPlayerMove(currentPlayer, position);
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
            if(victoryDetector.checkVictory(oldPosition, targetPosition)) {
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

        Player bot = turnManager.getCurrentPlayer();
        Position oldPosition = bot.getCurrentPosition();

        // Delega al BotPlayHandler la gestione del turno del bot
        botHandler.makeBotMove(bot, track, newPosition -> {
            // Questa parte viene eseguita quando il bot ha completato la mossa
            Platform.runLater(() -> {
                gridView.updatePlayerMarker(bot, newPosition);

                // Verifica vittoria solo se non è la prima mossa
                if (!bot.isFirstMove() && victoryDetector.checkVictory(oldPosition, newPosition)) {
                    handlePlayerWin(bot);
                    return;
                }

                completeCurrentTurn();
            });
        });
    }

    private void handlePlayerWin(Player player) {
        gameFinished = true;
        player.setFinished();
        inputHandler.clearHighlights();

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
        turnManager.nextTurn();
        isProcessingTurn = false;
        gridView.setCurrentPlayer(turnManager.getCurrentPlayer());
        startTurn();
    }

}