package org.example.Model.Player;

import javafx.scene.paint.Color;
import org.example.Model.Grid.IGrid;
import org.example.Model.Position;
import org.example.Model.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class HumanPlayerTest {

    private IGrid mockGrid;
    private Position startPosition;

    @BeforeEach
    void setUp() {
        mockGrid = Mockito.mock(IGrid.class);
        when(mockGrid.isValidPosition(anyInt(), anyInt())).thenReturn(true);
        when(mockGrid.isWalkable(anyInt(), anyInt())).thenReturn(true);

        startPosition = new Position(5, 5);
    }

    @Test
    void testHumanPlayerCreation() {
        HumanPlayer human = new HumanPlayer("TestHuman", startPosition);

        assertEquals("TestHuman", human.getName());
        assertEquals(startPosition, human.getCurrentPosition());
        assertTrue(human.isHuman());
        assertFalse(human.isBot());
        assertNotNull(human.getColor());
    }

    @Test
    void testHumanPlayerColorSpecificCreation() {
        Color testColor = Color.RED;
        HumanPlayer human = new HumanPlayer("TestHuman", startPosition, testColor);

        assertEquals("TestHuman", human.getName());
        assertEquals(startPosition, human.getCurrentPosition());
        assertEquals(testColor, human.getColor());
    }

    @Test
    void testHumanPlayerMoveTo() {
        HumanPlayer human = new HumanPlayer("TestHuman", startPosition);
        Position newPosition = new Position(6, 6);

        human.moveTo(newPosition);

        assertEquals(newPosition, human.getCurrentPosition());
        assertEquals(new Vector(1, 1), human.getCurrentVector()); // Verifica che il vettore sia aggiornato
    }

    @Test
    void testHumanFirstMove() {
        HumanPlayer human = new HumanPlayer("TestHuman", startPosition);

        assertTrue(human.isFirstMove());

        human.setFirstMove();

        assertFalse(human.isFirstMove());
    }

    @Test
    void testGetPossibleMovesFilteredByFirstMove() {
        HumanPlayer human = new HumanPlayer("TestHuman", startPosition);

        // Nella prima mossa, solo le mosse verso sinistra (y diminuisce) sono valide
        var possibleMoves = human.getPossibleMoves(mockGrid);

        // Verifica che tutte le mosse siano verso sinistra
        for (Position move : possibleMoves) {
            assertTrue(move.getY() < startPosition.getY(),
                    "La mossa " + move + " non è verso sinistra");
        }

        // Dopo la prima mossa, tutte le direzioni sono valide
        human.setFirstMove();
        possibleMoves = human.getPossibleMoves(mockGrid);

        // Verifica che ci siano anche mosse orizzontali e verso destra
        boolean hasHorizontalMove = false;
        boolean hasRightMove = false;

        for (Position move : possibleMoves) {
            if (move.getY() == startPosition.getY()) {
                hasHorizontalMove = true;
            }
            if (move.getY() > startPosition.getY()) {
                hasRightMove = true;
            }
        }

        assertTrue(hasHorizontalMove, "Mancano le mosse orizzontali");
        assertTrue(hasRightMove, "Mancano le mosse verso destra");
    }
}