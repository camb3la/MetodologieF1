package org.example.Controller;

import org.example.Controller.Move.MoveValidator;
import org.example.Model.Grid.IGrid;
import org.example.Model.Player.IPlayer;
import org.example.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MoveValidatorTest {

    private IGrid mockGrid;
    private IPlayer mockPlayer;
    private MoveValidator validator;
    private Position currentPosition;

    @BeforeEach
    void setUp() {
        mockGrid = Mockito.mock(IGrid.class);
        mockPlayer = Mockito.mock(IPlayer.class);
        validator = new MoveValidator(mockGrid);
        currentPosition = new Position(5, 5);

        when(mockPlayer.getCurrentPosition()).thenReturn(currentPosition);
    }

    @Test
    void testIsValidMoveWhenReachableAndWalkable() {
        // Test di una mossa valida (raggiungibile e percorribile)
        Position targetPosition = new Position(6, 6);

        when(mockPlayer.canReach(targetPosition)).thenReturn(true);
        when(mockGrid.isWalkable(targetPosition.getX(), targetPosition.getY())).thenReturn(true);

        assertTrue(validator.isValidMove(mockPlayer, targetPosition));
    }

    @Test
    void testIsValidMoveWhenNotReachable() {
        // Test di una mossa non valida perché non raggiungibile
        Position targetPosition = new Position(10, 10);

        when(mockPlayer.canReach(targetPosition)).thenReturn(false);
        when(mockGrid.isWalkable(targetPosition.getX(), targetPosition.getY())).thenReturn(true);

        assertFalse(validator.isValidMove(mockPlayer, targetPosition));
    }

    @Test
    void testIsValidMoveWhenNotWalkable() {
        // Test di una mossa non valida perché non percorribile
        Position targetPosition = new Position(6, 6);

        when(mockPlayer.canReach(targetPosition)).thenReturn(true);
        when(mockGrid.isWalkable(targetPosition.getX(), targetPosition.getY())).thenReturn(false);

        assertFalse(validator.isValidMove(mockPlayer, targetPosition));
    }

    @Test
    void testIsValidMoveWhenNeitherReachableNorWalkable() {
        // Test di una mossa non valida perché né raggiungibile né percorribile
        Position targetPosition = new Position(10, 10);

        when(mockPlayer.canReach(targetPosition)).thenReturn(false);
        when(mockGrid.isWalkable(targetPosition.getX(), targetPosition.getY())).thenReturn(false);

        assertFalse(validator.isValidMove(mockPlayer, targetPosition));
    }
}