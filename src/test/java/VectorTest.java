import org.example.Model.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {

    @Test
    public void testVectorCreation(){
        Vector vector = new Vector(1, 2);
        assertEquals(1, vector.getDx());
        assertEquals(2, vector.getDy());
    }

    @Test
    public void testGetPossibleNextMoves() {
        Vector vector = new Vector(0, 0);
        var moves = vector.getPossibleNextMoves();

        assertEquals(9, moves.size());

        assertTrue(moves.contains(new Vector(-1, -1)));
        assertTrue(moves.contains(new Vector(0, 0)));
        assertTrue(moves.contains(new Vector(1, 1)));
    }

    @Test
    public void testIsValidMove() {
        Vector currentVector = new Vector(0, 0);

        assertTrue(currentVector.isValidMove(new Vector(1, 0)));
        assertTrue(currentVector.isValidMove(new Vector(0, 1)));
        assertTrue(currentVector.isValidMove(new Vector(-1, -1)));

        assertFalse(currentVector.isValidMove(new Vector(2, 0)));
        assertFalse(currentVector.isValidMove(new Vector(0, 2)));
        assertFalse(currentVector.isValidMove(new Vector(2, 2)));
    }

}
