package org.example.Controller;

import org.example.Controller.Game.VictoryDetector;
import org.example.Model.Grid.IGrid;
import org.example.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class VictoryDetectorTest {

    private IGrid mockGrid;
    private VictoryDetector detector;

    @BeforeEach
    void setUp() {
        mockGrid = Mockito.mock(IGrid.class);
        detector = new VictoryDetector(mockGrid);
    }

    @Test
    void testCheckVictoryWhenCrossingFinishLine() {
        // Test che rilevi correttamente l'attraversamento della linea di arrivo
        Position oldPosition = new Position(5, 5);
        Position newPosition = new Position(6, 6);

        when(mockGrid.crossFinishLine(oldPosition, newPosition)).thenReturn(true);

        assertTrue(detector.checkVictory(oldPosition, newPosition));
    }

    @Test
    void testCheckVictoryWhenNotCrossingFinishLine() {
        // Test che rilevi correttamente il non attraversamento della linea di arrivo
        Position oldPosition = new Position(5, 5);
        Position newPosition = new Position(6, 6);

        when(mockGrid.crossFinishLine(oldPosition, newPosition)).thenReturn(false);

        assertFalse(detector.checkVictory(oldPosition, newPosition));
    }

}