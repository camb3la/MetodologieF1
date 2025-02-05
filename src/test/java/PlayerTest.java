import org.example.Model.Grid;
import org.example.Model.Player;
import org.example.Model.Position;
import org.example.Model.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {
    private Grid mockGrid;
    private Player humanPlayer;
    private Position startPosition;

    @BeforeEach
    public void setup() {
        mockGrid = mock(Grid.class);
        startPosition = new Position(5, 5);
        when(mockGrid.isValidPosition(anyInt(), anyInt())).thenReturn(true);
        when(mockGrid.isWalkable(anyInt(), anyInt())).thenReturn(true);

        humanPlayer = new Player("Human", startPosition, false);
    }

    @Test
    public void testPlayerCreation() {
        assertEquals("Human", humanPlayer.getName());
        assertEquals(startPosition, humanPlayer.getCurrentPosition());
        assertFalse(humanPlayer.isBot());
        assertEquals(new Vector(0, 0), humanPlayer.getCurrentVector());
        assertFalse(humanPlayer.hasFinished());
    }

    @Test
    public void testMoveTo() {
        Position newPosition = new Position(6, 6);
        humanPlayer.moveTo(newPosition);

        assertEquals(newPosition, humanPlayer.getCurrentPosition());
        assertEquals(new Vector(1, 1), humanPlayer.getCurrentVector());
    }

    @Test
    public void testCanReach() {
        Position reachablePosition = new Position(6, 5);
        assertTrue(humanPlayer.canReach(reachablePosition));

        Position unreachablePosition = new Position(7, 7);
        assertFalse(humanPlayer.canReach(unreachablePosition));
    }

    @Test
    public void testGetPossibleMoves() {
        var moves = humanPlayer.getPossibleMoves(mockGrid);
        assertFalse(moves.isEmpty());

        for (Position pos : moves) {
            assertTrue(pos.getY() < startPosition.getY());
        }
    }

    @Test
    public void testStop() {
        humanPlayer.moveTo(new Position(6, 6));
        humanPlayer.stop();
        assertEquals(new Vector(0, 0), humanPlayer.getCurrentVector());
    }

    @Test
    public void testSetFinished() {
        assertFalse(humanPlayer.hasFinished());
        humanPlayer.setFinished();
        assertTrue(humanPlayer.hasFinished());
    }

}
