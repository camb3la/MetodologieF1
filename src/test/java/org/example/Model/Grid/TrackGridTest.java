package org.example.Model.Grid;

import org.example.Service.Coordinate.CoordinateConverter;
import org.example.Service.Track.IFinishLineDetector;
import org.example.Service.Track.ITrackProcessor;
import org.example.Utils.Valdation.ImageValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class TrackGridTest {

    private TrackGrid trackGrid;
    private ITrackProcessor mockTrackProcessor;
    private IFinishLineDetector mockFinishLineDetector;
    private CoordinateConverter mockCoordinateConverter;
    private ImageValidator mockImageValidator;
    private BufferedImage mockImage;

    // Dimensioni della griglia per il test
    private static final int CELL_SIZE = 20;
    private static final int ROWS = 10;
    private static final int COLS = 15;

    @BeforeEach
    public void setup() {
        // Creazione dei mock necessari
        mockTrackProcessor = mock(ITrackProcessor.class);
        mockFinishLineDetector = mock(IFinishLineDetector.class);
        mockCoordinateConverter = mock(CoordinateConverter.class);
        mockImageValidator = mock(ImageValidator.class);
        mockImage = mock(BufferedImage.class);

        // Configurazione del comportamento dei mock
        when(mockImage.getHeight()).thenReturn(ROWS * CELL_SIZE);
        when(mockImage.getWidth()).thenReturn(COLS * CELL_SIZE);

        // Prepara una matrice track simulata
        boolean[][] trackMatrix = new boolean[ROWS][COLS];
        // Imposta alcune celle come percorribili
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                // Crea un percorso circolare
                if ((i > 2 && i < 7) && (j > 2 && j < 12)) {
                    trackMatrix[i][j] = true;
                } else {
                    trackMatrix[i][j] = false;
                }
            }
        }

        when(mockTrackProcessor.processTrackImage(any(BufferedImage.class), anyInt(), anyInt(), anyInt()))
                .thenReturn(trackMatrix);

        // Crea la griglia di test
        trackGrid = new TrackGrid(mockImage, mockTrackProcessor, mockFinishLineDetector,
                mockCoordinateConverter, mockImageValidator, CELL_SIZE);
    }

    @Test
    public void testConstructor_ValidationCalled() {
        // Verifica che il validatore dell'immagine sia stato chiamato
        verify(mockImageValidator).validateImage(mockImage);

        // Verifica che il processore della traccia sia stato chiamato
        verify(mockTrackProcessor).processTrackImage(mockImage, CELL_SIZE, ROWS, COLS);
    }

}