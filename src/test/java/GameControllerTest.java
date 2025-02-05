import org.example.Controller.GameController;
import org.example.Model.Grid;
import org.example.Model.Interface.OnGameEndListener;
import org.example.Model.Player;
import org.example.Model.Position;
import org.example.View.GridView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class GameControllerTest {
    private Grid mockGrid;
    private GridView mockGridView;
    private Player mockHumanPlayer;
    private Player mockBotPlayer;
    private GameController controller;
    private GridView.OnCellSelectListener cellSelectListener;

    @BeforeEach
    void setup() {
        mockGrid = mock(Grid.class);
        mockGridView = mock(GridView.class);
        mockHumanPlayer = mock(Player.class);
        mockBotPlayer = mock(Player.class);

        // Cattura il listener quando viene impostato
        doAnswer(invocation -> {
            cellSelectListener = invocation.getArgument(0);
            return null;
        }).when(mockGridView).setOnCellSelectListener(any());

        when(mockHumanPlayer.isHuman()).thenReturn(true);
        when(mockHumanPlayer.isBot()).thenReturn(false);
        when(mockBotPlayer.isHuman()).thenReturn(false);
        when(mockBotPlayer.isBot()).thenReturn(true);

        when(mockGrid.isWalkable(anyInt(), anyInt())).thenReturn(true);

        List<Player> players = Arrays.asList(mockHumanPlayer, mockBotPlayer);
        controller = new GameController(mockGrid, players, mockGridView);
    }

    @Test
    void testInitialization() {
        verify(mockGridView).setOnCellSelectListener(any());
        verify(mockGridView).setCurrentPlayer(mockHumanPlayer);
        verify(mockGridView, times(2)).updatePlayerMarker(any(), any());
    }

    @Test
    void testHandleHumanPlayerMove() {
        Position targetPosition = new Position(5, 5);
        when(mockHumanPlayer.canReach(targetPosition)).thenReturn(true);
        when(mockGrid.isWalkable(targetPosition.getX(), targetPosition.getY())).thenReturn(true);

        // Usa il listener catturato per simulare il click sulla griglia
        assertNotNull(cellSelectListener, "Cell select listener should be set");
        cellSelectListener.onCellSelected(targetPosition);

        verify(mockHumanPlayer).moveTo(targetPosition);
        verify(mockGridView).updatePlayerMarker(eq(mockHumanPlayer), eq(targetPosition));
    }

    @Test
    void testBotPlayerTurn() {
        Position initialMove = new Position(1, 1);
        when(mockHumanPlayer.canReach(initialMove)).thenReturn(true);
        when(mockGrid.isWalkable(initialMove.getX(), initialMove.getY())).thenReturn(true);

        // Reset delle interazioni precedenti (inclusa initializeGame)
        clearInvocations(mockGridView);

        // Completa il turno del giocatore umano
        assertNotNull(cellSelectListener, "Cell select listener should be set");
        cellSelectListener.onCellSelected(initialMove);

        // Ora dovrebbe essere il turno del bot
        verify(mockBotPlayer).makeMove(mockGrid);
        // Verifica che updatePlayerMarker sia chiamato una volta dopo il reset
        verify(mockGridView, times(1)).updatePlayerMarker(eq(mockBotPlayer), any());
    }

    @Test
    void testGameEndCondition() {
        Position finishPosition = new Position(10, 10);
        when(mockGrid.isFinishLine(finishPosition)).thenReturn(true);
        when(mockHumanPlayer.canReach(finishPosition)).thenReturn(true);
        when(mockGrid.isWalkable(finishPosition.getX(), finishPosition.getY())).thenReturn(true);
        when(mockHumanPlayer.getName()).thenReturn("Test Player");

        OnGameEndListener mockListener = mock(OnGameEndListener.class);
        controller.setOnGameEndListener(mockListener);

        assertNotNull(cellSelectListener, "Cell select listener should be set");
        cellSelectListener.onCellSelected(finishPosition);

        verify(mockHumanPlayer).setFinished();
        verify(mockGridView).clearHighlights();

        Position newPosition = new Position(1, 1);
        cellSelectListener.onCellSelected(newPosition);

        verify(mockHumanPlayer, never()).moveTo(newPosition);
        verify(mockGridView, never()).updatePlayerMarker(eq(mockHumanPlayer), eq(newPosition));
    }

    @Test
    void testInvalidMove() {
        Position invalidPosition = new Position(15, 15);
        when(mockHumanPlayer.canReach(invalidPosition)).thenReturn(false);

        assertNotNull(cellSelectListener, "Cell select listener should be set");
        cellSelectListener.onCellSelected(invalidPosition);

        verify(mockHumanPlayer, never()).moveTo(invalidPosition);
        verify(mockGridView, never()).updatePlayerMarker(eq(mockHumanPlayer), eq(invalidPosition));
    }

}