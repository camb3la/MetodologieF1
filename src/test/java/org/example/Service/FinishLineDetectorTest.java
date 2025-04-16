package org.example.Service;

import org.example.Model.Position;
import org.example.Service.Coordinate.CoordinateConverter;
import org.example.Service.Track.FinishLineDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FinishLineDetectorTest {

    private FinishLineDetector finishLineDetector;
    private CoordinateConverter mockConverter;
    private List<Position> finishLine;

    @BeforeEach
    void setUp() {
        // Create a mock coordinate converter
        mockConverter = Mockito.mock(CoordinateConverter.class);

        // Setup the mock to just return the same position for simplicity in some tests
        when(mockConverter.gameToGrid(any(Position.class))).thenAnswer(
                invocation -> invocation.getArgument(0)
        );

        // Create a simple finish line (horizontal line from (10,5) to (10,9))
        finishLine = new ArrayList<>();
        for (int col = 5; col <= 9; col++) {
            finishLine.add(new Position(10, col));
        }

        finishLineDetector = new FinishLineDetector(finishLine, mockConverter);
    }

    @Test
    void testIsFinishLine() {
        // Set up the mock to simulate the conversion
        when(mockConverter.gameToGrid(new Position(10, 7))).thenReturn(new Position(10, 7));
        when(mockConverter.gameToGrid(new Position(11, 7))).thenReturn(new Position(11, 7));

        // Test a position that is on the finish line
        assertTrue(finishLineDetector.isFinishLine(new Position(10, 7)));

        // Test a position that is not on the finish line
        assertFalse(finishLineDetector.isFinishLine(new Position(11, 7)));
    }

    @Test
    void testCrossesFinishLine_DirectCross() {
        // Test when movement crosses the finish line directly
        Position from = new Position(9, 7);
        Position to = new Position(11, 7);

        // Configure mock to support this test
        when(mockConverter.gameToGrid(from)).thenReturn(from);
        when(mockConverter.gameToGrid(to)).thenReturn(to);

        assertTrue(finishLineDetector.crossesFinishLine(from, to));
    }

    @Test
    void testCrossesFinishLine_MovesAlong() {
        // Test when movement moves along the finish line but doesn't cross it
        Position from = new Position(10, 5);
        Position to = new Position(10, 9);

        // Configure mock
        when(mockConverter.gameToGrid(from)).thenReturn(from);
        when(mockConverter.gameToGrid(to)).thenReturn(to);

        // This should return true because the end point is on the finish line
        assertTrue(finishLineDetector.crossesFinishLine(from, to));
    }

    @Test
    void testCrossesFinishLine_NoCross() {
        // Test when movement doesn't cross or touch the finish line
        Position from = new Position(5, 5);
        Position to = new Position(5, 9);

        // Configure mock
        when(mockConverter.gameToGrid(from)).thenReturn(from);
        when(mockConverter.gameToGrid(to)).thenReturn(to);

        assertFalse(finishLineDetector.crossesFinishLine(from, to));
    }

    @Test
    void testGetStartingPositions_EnoughPositions() {
        // Test getting starting positions when there are enough positions on the finish line
        int numPlayers = 3;

        // Configure mock to convert grid positions to game positions
        when(mockConverter.gridToGame(new Position(10, 5))).thenReturn(new Position(5, 10));
        when(mockConverter.gridToGame(new Position(10, 6))).thenReturn(new Position(6, 10));
        when(mockConverter.gridToGame(new Position(10, 7))).thenReturn(new Position(7, 10));

        List<Position> startingPositions = finishLineDetector.getStartingPositions(numPlayers);

        assertEquals(numPlayers, startingPositions.size());
        assertEquals(new Position(5, 10), startingPositions.get(0));
        assertEquals(new Position(6, 10), startingPositions.get(1));
        assertEquals(new Position(7, 10), startingPositions.get(2));
    }

    @Test
    void testGetStartingPositions_NotEnoughPositions() {
        // Test getting starting positions when there are not enough positions on the finish line
        int numPlayers = 7; // More than the 5 positions in our finish line

        // Configure mock for the first 5 positions (on finish line)
        for (int i = 0; i < 5; i++) {
            when(mockConverter.gridToGame(new Position(10, 5 + i)))
                    .thenReturn(new Position(5 + i, 10));
        }

        // Configure mock for the positions to the left of the finish line
        when(mockConverter.gridToGame(new Position(9, 5)))
                .thenReturn(new Position(5, 9));
        when(mockConverter.gridToGame(new Position(8, 5)))
                .thenReturn(new Position(5, 8));

        // Set up the leftmost position of the finish line for the algorithm
        when(mockConverter.gridToGame(finishLine.get(0)))
                .thenReturn(new Position(5, 10));

        List<Position> startingPositions = finishLineDetector.getStartingPositions(numPlayers);

        assertEquals(numPlayers, startingPositions.size());
        // Check the extra positions are to the left of the finish line
        assertEquals(new Position(5, 9), startingPositions.get(5));
        assertEquals(new Position(5, 8), startingPositions.get(6));
    }

    @Test
    void testEmptyFinishLine() {
        // Test that an exception is thrown when trying to create a detector with an empty finish line
        List<Position> emptyFinishLine = new ArrayList<>();

        assertThrows(IllegalStateException.class, () -> {
            new FinishLineDetector(emptyFinishLine, mockConverter);
        });
    }
}