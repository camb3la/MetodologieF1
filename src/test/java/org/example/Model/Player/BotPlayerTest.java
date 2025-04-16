package org.example.Model.Player;

import org.example.Model.Grid.IGrid;
import org.example.Model.Interface.MovementStrategy;
import org.example.Model.Position;
import org.example.Model.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BotPlayerTest {

    private IGrid mockGrid;
    private MovementStrategy mockStrategy;
    private Position startPosition;

    @BeforeEach
    void setUp() {
        mockGrid = Mockito.mock(IGrid.class);
        mockStrategy = Mockito.mock(MovementStrategy.class);
        startPosition = new Position(5, 5);
    }

    @Test
    void testBotPlayerCreation() {
        IPlayer bot = new BotPlayer("TestBot", startPosition, mockStrategy);

        assertEquals("TestBot", bot.getName());
        assertEquals(startPosition, bot.getCurrentPosition());
        assertTrue(bot.isBot());
        assertFalse(bot.isHuman());
    }

    @Test
    void testBotMakeMove() {
        // Configuriamo il mock della strategia per restituire un movimento specifico
        Vector expectedMove = new Vector(1, -1);
        when(mockStrategy.getNextMove(any(Position.class), any(Vector.class), any(IGrid.class)))
                .thenReturn(expectedMove);

        // Configuriamo il mock della griglia per consentire il movimento
        when(mockGrid.isWalkable(6, 4)).thenReturn(true); // nuova posizione: 5+1, 5-1

        IPlayer bot = new BotPlayer("TestBot", startPosition, mockStrategy);
        bot.makeMove(mockGrid);

        // Verifichiamo che il bot abbia aggiornato la sua posizione in base alla mossa
        assertEquals(new Position(6, 4), bot.getCurrentPosition());
        assertEquals(expectedMove, bot.getCurrentVector());
    }

    @Test
    void testBotFinishState() {
        IPlayer bot = new BotPlayer("TestBot", startPosition, mockStrategy);

        assertFalse(bot.hasFinished());

        bot.setFinished();

        assertTrue(bot.hasFinished());
    }
}