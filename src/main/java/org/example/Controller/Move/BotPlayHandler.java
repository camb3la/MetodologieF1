package org.example.Controller.Move;

import org.example.Model.Grid.IGrid;
import org.example.Model.Player.IPlayer;
import org.example.Model.Position;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Gestisce le mosse dei bot, incluso il calcolo e l'esecuzione
 */
public class BotPlayHandler {
    private final ExecutorService executor;

    public BotPlayHandler() {
        // Executor per eseguire le mosse del bot in background
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Calcola e esegue una mossa del bot
     * @param bot Il bot
     * @param grid La griglia di gioco
     * @param onMoveComplete Callback da invocare quando la mossa è completata
     */
    public void makeBotMove(IPlayer bot, IGrid grid, Consumer<Position> onMoveComplete) {
        // Esegue il calcolo della mossa in background
        executor.submit(() -> {
            Position oldPosition = bot.getCurrentPosition();

            // Fa calcolare al bot la prossima mossa
            bot.makeMove(grid);
            Position newPosition = bot.getCurrentPosition();

            // Notifica il completamento della mossa
            if (onMoveComplete != null) {
                onMoveComplete.accept(newPosition);
            }
        });
    }

    /**
     * Libera le risorse quando non più necessarie
     */
    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}