package org.example.Model.Player;

import org.example.Model.Grid.IGrid;
import org.example.Model.Interface.MovementStrategy;
import org.example.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PlayerFactoryTest {

    private IGrid mockGrid;
    private List<Position> mockStartPositions;

    @BeforeEach
    void setUp() {
        // Creiamo un mock della griglia e le posizioni di partenza
        mockGrid = Mockito.mock(IGrid.class);
        mockStartPositions = Arrays.asList(
                new Position(5, 10),
                new Position(5, 9),
                new Position(5, 8),
                new Position(5, 7)
        );

        when(mockGrid.getStartingPositions(Mockito.anyInt())).thenReturn(mockStartPositions);
    }

    @Test
    void testCreateHumanPlayer() {
        // Test creazione di un giocatore umano
        Position startPos = new Position(5, 5);
        IPlayer human = PlayerFactory.createHumanPlayer("TestHuman", startPos);

        assertEquals("TestHuman", human.getName());
        assertEquals(startPos, human.getCurrentPosition());
        assertTrue(human.isHuman());
        assertFalse(human.isBot());
    }

    @Test
    void testCreateBot() {
        // Test creazione di un bot generico
        Position startPos = new Position(5, 5);
        IPlayer bot = PlayerFactory.createBot("TestBot", startPos);

        assertEquals("TestBot", bot.getName());
        assertEquals(startPos, bot.getCurrentPosition());
        assertTrue(bot.isBot());
        assertFalse(bot.isHuman());
    }

    @Test
    void testCreateAggressiveBot() {
        // Test creazione di un bot aggressivo
        Position startPos = new Position(5, 5);
        IPlayer bot = PlayerFactory.createAggressiveBot("AggressiveBot", startPos);

        assertEquals("AggressiveBot", bot.getName());
        assertEquals(startPos, bot.getCurrentPosition());
        assertTrue(bot.isBot());
    }

    @Test
    void testCreateConservativeBot() {
        // Test creazione di un bot conservativo
        Position startPos = new Position(5, 5);
        IPlayer bot = PlayerFactory.createConservativeBot("ConservativeBot", startPos);

        assertEquals("ConservativeBot", bot.getName());
        assertEquals(startPos, bot.getCurrentPosition());
        assertTrue(bot.isBot());
    }

    @Test
    void testCreateCustomBot() {
        // Test creazione di un bot con strategia personalizzata
        Position startPos = new Position(5, 5);
        MovementStrategy mockStrategy = Mockito.mock(MovementStrategy.class);

        IPlayer bot = PlayerFactory.createCustomBot("CustomBot", startPos, mockStrategy);

        assertEquals("CustomBot", bot.getName());
        assertEquals(startPos, bot.getCurrentPosition());
        assertTrue(bot.isBot());
    }

    @Test
    void testCreatePlayers() {
        // Test creazione di una lista di giocatori
        List<String> humanNames = Arrays.asList("Player1", "Player2");
        int numBots = 2;

        List<IPlayer> players = PlayerFactory.createPlayers(mockGrid, humanNames, numBots);

        // Dovremmo avere 4 giocatori in totale (2 umani + 2 bot)
        assertEquals(4, players.size());

        // Verifica che i primi due siano umani con i nomi corretti
        assertTrue(players.get(0).isHuman());
        assertEquals("Player1", players.get(0).getName());
        assertTrue(players.get(1).isHuman());
        assertEquals("Player2", players.get(1).getName());

        // Verifica che gli ultimi due siano bot
        assertTrue(players.get(2).isBot());
        assertTrue(players.get(3).isBot());

        // Verifica le posizioni di partenza
        assertEquals(mockStartPositions.get(0), players.get(0).getCurrentPosition());
        assertEquals(mockStartPositions.get(1), players.get(1).getCurrentPosition());
        assertEquals(mockStartPositions.get(2), players.get(2).getCurrentPosition());
        assertEquals(mockStartPositions.get(3), players.get(3).getCurrentPosition());
    }
}