import agh.po.Properties.GameDirection;
import agh.po.Properties.Vector2d;
import org.junit.jupiter.api.Test;

import static agh.po.Properties.GameDirection.*;
import static org.junit.jupiter.api.Assertions.*;


public class GameDirectionTest {

    @Test
    void testToUnitVector() {
        assertEquals(N.toUnitVector(), new Vector2d(0, 1));
        assertEquals(NE.toUnitVector(), new Vector2d(1, 1));
        assertEquals(E.toUnitVector(), new Vector2d(1, 0));
        assertEquals(SE.toUnitVector(), new Vector2d(1, -1));
        assertEquals(S.toUnitVector(), new Vector2d(0, -1));
        assertEquals(SW.toUnitVector(), new Vector2d(-1, -1));
        assertEquals(W.toUnitVector(), new Vector2d(-1, 0));
        assertEquals(NW.toUnitVector(), new Vector2d(-1, 1));
    }

    @Test
    void testRotation() {
        assertEquals(NW.rotation(0), NW);
        assertEquals(NW.rotation(1), N);
        assertEquals(NW.rotation(2), NE);
        assertEquals(NW.rotation(3), E);
        assertEquals(NW.rotation(4), SE);
        assertEquals(NW.rotation(5), S);
        assertEquals(NW.rotation(6), SW);
        assertEquals(NW.rotation(7), W);
        assertThrows(IllegalArgumentException.class, () -> {
            NW.rotation(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            NW.rotation(8);
        });
        assertEquals(E.rotation(0), E);
        assertEquals(E.rotation(1), SE);
        assertEquals(E.rotation(2), S);
        assertEquals(E.rotation(3), SW);
        assertEquals(E.rotation(4), W);
        assertEquals(E.rotation(5), NW);
        assertEquals(E.rotation(6), N);
        assertEquals(E.rotation(7), NE);
        assertThrows(IllegalArgumentException.class, () -> {
            E.rotation(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            E.rotation(8);
        });
    }

    @Test
    void testToString()
    {
        assertEquals(N.toString(), "North");
        assertEquals(NE.toString(), "Northeast");
        assertEquals(E.toString(), "East");
        assertEquals(SE.toString(), "Southeast");
        assertEquals(S.toString(), "South");
        assertEquals(SW.toString(), "Southwest");
        assertEquals(W.toString(), "West");
        assertEquals(NW.toString(), "Northwest");

    }

    @Test
    void testRandomlyGetDirection()
    {
        for (int i = 0; i < 10; i++)
        {
            GameDirection direction = GameDirection.randomlyGetDirection();
            assertTrue(direction == N || direction == NE || direction == E || direction == SE || direction == S || direction == SW || direction == W || direction == NW);
        }
    }
}




