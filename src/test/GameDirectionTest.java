import agh.po.Properties.GameDirection;
import agh.po.Properties.Vector2d;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static agh.po.Properties.GameDirection.*;
import static org.junit.jupiter.api.Assertions.*;


public class GameDirectionTest {

    @Test
    void testToUnitVector() {
        assertEquals(NORTH.toUnitVector(), new Vector2d(0, 1));
        assertEquals(NORTHEAST.toUnitVector(), new Vector2d(1, 1));
        assertEquals(EAST.toUnitVector(), new Vector2d(1, 0));
        assertEquals(SOUTHEAST.toUnitVector(), new Vector2d(1, -1));
        assertEquals(SOUTH.toUnitVector(), new Vector2d(0, -1));
        assertEquals(SOUTHWEST.toUnitVector(), new Vector2d(-1, -1));
        assertEquals(WEST.toUnitVector(), new Vector2d(-1, 0));
        assertEquals(NORTHWEST.toUnitVector(), new Vector2d(-1, 1));
    }

    @Test
    void testRotation() {
        assertEquals(NORTHWEST.rotation(0), NORTHWEST);
        assertEquals(NORTHWEST.rotation(1), NORTH);
        assertEquals(NORTHWEST.rotation(2), NORTHEAST);
        assertEquals(NORTHWEST.rotation(3), EAST);
        assertEquals(NORTHWEST.rotation(4), SOUTHEAST);
        assertEquals(NORTHWEST.rotation(5), SOUTH);
        assertEquals(NORTHWEST.rotation(6), SOUTHWEST);
        assertEquals(NORTHWEST.rotation(7), WEST);
        assertThrows(IllegalArgumentException.class, () -> {
            NORTHWEST.rotation(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            NORTHWEST.rotation(8);
        });
        assertEquals(EAST.rotation(0), EAST);
        assertEquals(EAST.rotation(1), SOUTHEAST);
        assertEquals(EAST.rotation(2), SOUTH);
        assertEquals(EAST.rotation(3), SOUTHWEST);
        assertEquals(EAST.rotation(4), WEST);
        assertEquals(EAST.rotation(5), NORTHWEST);
        assertEquals(EAST.rotation(6), NORTH);
        assertEquals(EAST.rotation(7), NORTHEAST);
        assertThrows(IllegalArgumentException.class, () -> {
            EAST.rotation(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EAST.rotation(8);
        });
    }

    @Test
    void testToString()
    {
        assertEquals(NORTH.toString(), "North");
        assertEquals(NORTHEAST.toString(), "Northeast");
        assertEquals(EAST.toString(), "East");
        assertEquals(SOUTHEAST.toString(), "Southeast");
        assertEquals(SOUTH.toString(), "South");
        assertEquals(SOUTHWEST.toString(), "Southwest");
        assertEquals(WEST.toString(), "West");
        assertEquals(NORTHWEST.toString(), "Northwest");

    }

    @RepeatedTest(value = 10, name="Test {displayName} - {currentRepetition} / {totalRepetitions}")
    void testRandomlyGetDirection()
    {
            GameDirection direction = GameDirection.randomlyGetDirection();
            assertTrue(direction == NORTH || direction == NORTHEAST || direction == EAST || direction == SOUTHEAST || direction == SOUTH || direction == SOUTHWEST || direction == WEST || direction == NORTHWEST);
    }
}




