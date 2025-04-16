package org.example.Service;

import org.example.Model.Position;
import org.example.Service.Coordinate.CoordinateConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoordinateConverterTest {

    private CoordinateConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CoordinateConverter();
    }

    @Test
    void testGameToGrid() {
        // In game coordinates: x=row, y=column
        // In grid coordinates: x=column, y=row
        Position gamePosition = new Position(5, 10);
        Position gridPosition = converter.gameToGrid(gamePosition);

        assertEquals(10, gridPosition.getX());
        assertEquals(5, gridPosition.getY());
    }

    @Test
    void testGridToGame() {
        // In grid coordinates: x=column, y=row
        // In game coordinates: x=row, y=column
        Position gridPosition = new Position(10, 5);
        Position gamePosition = converter.gridToGame(gridPosition);

        assertEquals(5, gamePosition.getX());
        assertEquals(10, gamePosition.getY());
    }

    @Test
    void testBidirectionalConversion() {
        // Test that converting from game to grid and back gives the original position
        Position original = new Position(7, 12);
        Position result = converter.gridToGame(converter.gameToGrid(original));

        assertEquals(original.getX(), result.getX());
        assertEquals(original.getY(), result.getY());
    }

    @Test
    void testPixelToGrid() {
        // For a cell size of 20 pixels, the pixel (45, 25) should be in cell (2, 1)
        int cellSize = 20;
        Position gridPosition = converter.pixelToGrid(45, 25, cellSize);

        assertEquals(2, gridPosition.getX());
        assertEquals(1, gridPosition.getY());
    }

    @Test
    void testGridToCenterPixel() {
        // For a cell size of 20 pixels, the center of cell (2, 1) should be at (50, 30)
        int cellSize = 20;
        Position gridPosition = new Position(2, 1);
        int[] centerPixel = converter.gridToCenterPixel(gridPosition, cellSize);

        assertEquals(50, centerPixel[0]); // x = 2 * 20 + 20/2 = 50
        assertEquals(30, centerPixel[1]); // y = 1 * 20 + 20/2 = 30
    }

    @Test
    void testEdgeCases() {
        // Test with zero values
        Position zeroPosition = new Position(0, 0);
        Position convertedZero = converter.gameToGrid(zeroPosition);
        assertEquals(0, convertedZero.getX());
        assertEquals(0, convertedZero.getY());

        // Test with negative values (not normally valid in a grid but should still convert correctly)
        Position negativePosition = new Position(-3, -5);
        Position convertedNegative = converter.gameToGrid(negativePosition);
        assertEquals(-5, convertedNegative.getX());
        assertEquals(-3, convertedNegative.getY());
    }
}