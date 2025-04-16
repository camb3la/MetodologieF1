package org.example.Service;

import org.example.Model.Position;
import org.example.Service.Track.TrackImageProcessor;
import org.example.Utils.Color.IColorAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TrackImageProcessorTest {

    private TrackImageProcessor processor;
    private IColorAnalyzer mockColorAnalyzer;
    private BufferedImage mockImage;

    @BeforeEach
    void setUp() {
        mockColorAnalyzer = Mockito.mock(IColorAnalyzer.class);
        processor = new TrackImageProcessor(mockColorAnalyzer);

        // Create a small test image (4x4 pixels)
        mockImage = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);

        // Set specific pixels to different colors
        // White pixels (for track)
        mockImage.setRGB(0, 0, Color.WHITE.getRGB());
        mockImage.setRGB(1, 0, Color.WHITE.getRGB());
        mockImage.setRGB(2, 0, Color.WHITE.getRGB());

        // Red pixels (for finish line)
        mockImage.setRGB(0, 1, Color.RED.getRGB());
        mockImage.setRGB(1, 1, Color.RED.getRGB());

        // Black pixels (for non-track)
        mockImage.setRGB(2, 1, Color.BLACK.getRGB());
        mockImage.setRGB(3, 0, Color.BLACK.getRGB());
        mockImage.setRGB(3, 1, Color.BLACK.getRGB());
    }

    @Test
    void testProcessTrackImage() {
        // Configure mock color analyzer
        when(mockColorAnalyzer.isWhiteColor(any(Color.class)))
                .thenAnswer(invocation -> {
                    Color color = invocation.getArgument(0);
                    return color.equals(Color.WHITE);
                });

        // Process the image with cell size = 1 (each pixel is a cell)
        boolean[][] track = processor.processTrackImage(mockImage, 1, 4, 4);

        // Verify the track grid matches our expectations
        assertTrue(track[0][0]); // White pixel at (0,0)
        assertTrue(track[0][1]); // White pixel at (0,1)
        assertTrue(track[0][2]); // White pixel at (0,2)
        assertFalse(track[0][3]); // Black pixel at (0,3)

        assertFalse(track[1][2]); // Black pixel at (1,2)
        assertFalse(track[1][3]); // Black pixel at (1,3)
    }

    @Test
    void testIdentifyStartLine() {
        // Configure mock color analyzer to recognize red pixels
        when(mockColorAnalyzer.isRedColor(any(Color.class)))
                .thenAnswer(invocation -> {
                    Color color = invocation.getArgument(0);
                    return color.equals(Color.RED);
                });

        // Identify the start line with cell size = 1
        List<Position> startLine = processor.identifyStartLine(mockImage, 1, 4, 4);

        // We should have identified 2 red pixels as part of the start line
        assertEquals(2, startLine.size());

        // Verify the positions
        assertTrue(startLine.contains(new Position(0, 1)));
        assertTrue(startLine.contains(new Position(1, 1)));
    }

    @Test
    void testIdentifyStartLineWithLargerCellSize() {
        // Configure mock color analyzer to recognize red pixels
        when(mockColorAnalyzer.isRedColor(any(Color.class)))
                .thenAnswer(invocation -> {
                    Color color = invocation.getArgument(0);
                    return color.equals(Color.RED);
                });

        // Identify the start line with cell size = 2
        List<Position> startLine = processor.identifyStartLine(mockImage, 2, 2, 2);

        // With cell size 2, the red pixels are in the first cell (0,0)
        assertEquals(1, startLine.size());
        assertEquals(new Position(0, 0), startLine.get(0));
    }

    @Test
    void testNoStartLine() {
        // Create an image with no red pixels
        BufferedImage noRedImage = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                noRedImage.setRGB(x, y, Color.WHITE.getRGB());
            }
        }

        // Configure mock color analyzer - no red pixels
        when(mockColorAnalyzer.isRedColor(any(Color.class))).thenReturn(false);

        // Identify the start line
        List<Position> startLine = processor.identifyStartLine(noRedImage, 1, 4, 4);

        // We should have an empty list since there are no red pixels
        assertTrue(startLine.isEmpty());
    }
}