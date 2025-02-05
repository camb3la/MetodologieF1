import org.example.Model.Grid;
import org.example.Model.Position;
import org.example.Model.Strategy.AggressiveBotStrategy;
import org.example.Model.Strategy.ConservativeBotStrategy;
import org.example.Model.Strategy.HumanStrategy;
import org.example.Model.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StrategyTest {
    private Grid grid;
    private Position position;

    @BeforeEach
    void setup() {
        grid = mock(Grid.class);
        position = new Position(5, 5);
        when(grid.isWalkable(anyInt(), anyInt())).thenReturn(true);
    }

    @Test
    void testConservativeBotStrategy() {
        ConservativeBotStrategy strategy = new ConservativeBotStrategy();
        Vector currentVector = new Vector(1, 1);

        Vector nextMove = strategy.getNextMove(position, currentVector, grid);

        assertNotNull(nextMove);
        assertTrue(Math.abs(nextMove.getDx() - currentVector.getDx()) <= 1);
        assertTrue(Math.abs(nextMove.getDy() - currentVector.getDy()) <= 1);

        when(grid.isWalkable(6, 6)).thenReturn(false);
        nextMove = strategy.getNextMove(position, currentVector, grid);
        assertTrue(Math.sqrt(nextMove.getDx() * nextMove.getDx() +
                nextMove.getDy() * nextMove.getDy()) <=
                Math.sqrt(currentVector.getDx() * currentVector.getDx() +
                        currentVector.getDy() * currentVector.getDy()));
    }

    @Test
    void testAggressiveBotStrategy() {
        AggressiveBotStrategy strategy = new AggressiveBotStrategy();
        Vector currentVector = new Vector(1, 1);

        Vector nextMove = strategy.getNextMove(position, currentVector, grid);

        assertNotNull(nextMove);
        double currentSpeed = Math.sqrt(currentVector.getDx() * currentVector.getDx() +
                currentVector.getDy() * currentVector.getDy());
        double nextSpeed = Math.sqrt(nextMove.getDx() * nextMove.getDx() +
                nextMove.getDy() * nextMove.getDy());
        assertTrue(nextSpeed >= currentSpeed);

        when(grid.isWalkable(6, 6)).thenReturn(false);
        nextMove = strategy.getNextMove(position, currentVector, grid);
        assertNotNull(nextMove);
        assertTrue(nextMove.isValidMove(currentVector));
    }

    @Test
    void testHumanStrategy() {
        HumanStrategy strategy = new HumanStrategy();
        Vector expectedMove = new Vector(1, 0);

        strategy.setMove(expectedMove);
        Vector actualMove = strategy.getNextMove(position, new Vector(0, 0), grid);

        assertEquals(expectedMove, actualMove);

        actualMove = strategy.getNextMove(position, new Vector(0, 0), grid);
        assertNull(actualMove);
    }
}