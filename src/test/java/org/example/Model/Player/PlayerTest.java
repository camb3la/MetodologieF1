package org.example.Model.Player;

import org.example.Model.Grid.IGrid;
import org.example.Model.Position;
import org.example.Model.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class PlayerTest {

    private IGrid mockGrid;
    private Position startPosition;

    @BeforeEach
    void setUp() {
        // Creiamo un mock della griglia
        mockGrid = Mockito.mock(IGrid.class);
        when(mockGrid.isValidPosition(anyInt(), anyInt())).thenReturn(true);
        when(mockGrid.isWalkable(anyInt(), anyInt())).thenReturn(true);

        // Impostiamo una posizione di partenza
        startPosition = new Position(5, 5);
    }

    @Test
    void testHumanPlayerCreation() {
        // Test della creazione di un giocatore umano
        IPlayer human = PlayerFactory.createHumanPlayer("TestPlayer", startPosition);

        assertEquals("TestPlayer", human.getName());
        assertEquals(startPosition, human.getCurrentPosition());
        Assertions.assertEquals(new Vector(0, 0), human.getCurrentVector());
        assertTrue(human.isHuman());
        assertFalse(human.isBot());
        assertTrue(human.isFirstMove());
        assertFalse(human.hasFinished());
    }

    @Test
    void testBotPlayerCreation() {
        // Test della creazione di un bot
        IPlayer bot = PlayerFactory.createBot("TestBot", startPosition);

        assertEquals("TestBot", bot.getName());
        assertEquals(startPosition, bot.getCurrentPosition());
        assertEquals(new Vector(0, 0), bot.getCurrentVector());
        assertTrue(bot.isBot());
        assertFalse(bot.isHuman());
        assertTrue(bot.isFirstMove());
        assertFalse(bot.hasFinished());
    }

    @Test
    void testPlayerMoveTo() {
        // Test del movimento di un giocatore
        IPlayer player = PlayerFactory.createHumanPlayer("TestPlayer", startPosition);
        Position newPosition = new Position(6, 4);

        player.moveTo(newPosition);

        assertEquals(newPosition, player.getCurrentPosition());
        // Controlla che il vettore sia stato aggiornato correttamente
        assertEquals(new Vector(1, -1), player.getCurrentVector());
    }

    @Test
    void testGetPossibleMovesFirstMove() {
        // Test delle mosse possibili per la prima mossa (deve essere verso sinistra)
        IPlayer player = PlayerFactory.createHumanPlayer("TestPlayer", startPosition);

        // Configuriamo il mock per permettere tutti i movimenti
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = startPosition.getX() + dx;
                int newY = startPosition.getY() + dy;
                when(mockGrid.isWalkable(newX, newY)).thenReturn(true);
            }
        }

        List<Position> possibleMoves = player.getPossibleMoves(mockGrid);

        // La prima mossa deve essere verso sinistra (y diminuisce)
        assertFalse(possibleMoves.isEmpty());
        for (Position move : possibleMoves) {
            assertTrue(move.getY() < startPosition.getY(),
                    "La prima mossa deve essere verso sinistra, ma è: " + move);
        }
    }

    @Test
    void testGetPossibleMovesAfterFirstMove() {
        // Test delle mosse possibili dopo la prima mossa
        IPlayer player = PlayerFactory.createHumanPlayer("TestPlayer", startPosition);
        player.setFirstMove(); // Non è più la prima mossa

        // Configuriamo il mock per permettere tutti i movimenti
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = startPosition.getX() + dx;
                int newY = startPosition.getY() + dy;
                when(mockGrid.isWalkable(newX, newY)).thenReturn(true);
            }
        }

        List<Position> possibleMoves = player.getPossibleMoves(mockGrid);

        // Dovrebbe avere 9 mosse possibili (3x3)
        assertEquals(9, possibleMoves.size());
    }

    @Test
    void testFinishState() {
        // Test dello stato di fine gara
        IPlayer player = PlayerFactory.createHumanPlayer("TestPlayer", startPosition);
        assertFalse(player.hasFinished());

        player.setFinished();
        assertTrue(player.hasFinished());
    }
}