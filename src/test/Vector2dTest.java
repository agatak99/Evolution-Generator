import agh.po.Properties.Vector2d;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Vector2dTest
{
    @Test
    void testToString()
    {
        assertEquals(new Vector2d(2,4).toString(), "(2,4)");
        assertEquals(new Vector2d(0,0).toString(), "(0,0)");
        assertEquals(new Vector2d(-2,5).toString(), "(-2,5)");
        assertEquals(new Vector2d(2,-5).toString(), "(2,-5)");
        assertEquals(new Vector2d(-2,-5).toString(), "(-2,-5)");
    }
    @Test
    void testPrecedes()
    {
        assertTrue(new Vector2d(0,0).precedes(new Vector2d(0,1)));
        assertTrue(new Vector2d(-5,5).precedes(new Vector2d(3,5)));
        assertTrue(new Vector2d(1,5).precedes(new Vector2d(1,5)));
        assertTrue(new Vector2d(5,10).precedes(new Vector2d(5,20)));
        assertFalse(new Vector2d(5,10).precedes(new Vector2d(5,9)));
        assertFalse(new Vector2d(6,1).precedes(new Vector2d(6,-1)));
        assertFalse(new Vector2d(5,3).precedes(new Vector2d(-5,-3)));
        assertFalse(new Vector2d(1,1).precedes(new Vector2d(1,0)));
    }

    @Test
    void testFollows()
    {
        assertFalse(new Vector2d(0,0).follows(new Vector2d(0,1)));
        assertFalse(new Vector2d(-5,5).follows(new Vector2d(3,5)));
        assertFalse(new Vector2d(2,3).follows(new Vector2d(2,4)));
        assertFalse(new Vector2d(5,10).follows(new Vector2d(5,20)));
        assertTrue(new Vector2d(1,5).follows(new Vector2d(1,5)));
        assertTrue(new Vector2d(5,10).follows(new Vector2d(5,9)));
        assertTrue(new Vector2d(6,1).follows(new Vector2d(6,-1)));
        assertTrue(new Vector2d(5,3).follows(new Vector2d(-5,-3)));
        assertTrue(new Vector2d(1,1).follows(new Vector2d(1,0)));
    }

    @Test
    void testAdd(){
        assertEquals(new Vector2d(0,0).add(new Vector2d(0,0)), new Vector2d(0,0));
        assertEquals(new Vector2d(-2,-2).add(new Vector2d(4,5)), new Vector2d(2,3));
        assertEquals(new Vector2d(4,5).add(new Vector2d(-2,-2)), new Vector2d(2,3));
        assertEquals(new Vector2d(6,1).add(new Vector2d(3,4)), new Vector2d(9,5));
    }

    @Test
    void testEquals(){
        assertTrue(new Vector2d(0,0).equals(new Vector2d(0,0)));
        assertTrue(new Vector2d(-2,-3).equals(new Vector2d(-2,-3)));
        assertTrue(new Vector2d(5,0).equals(new Vector2d(5,0)));
        assertFalse(new Vector2d(-2,0).equals(new Vector2d(0,0)));
        assertFalse(new Vector2d(-2,-3).equals(new Vector2d(-2,3)));
        assertFalse(new Vector2d(5,0).equals(new Vector2d(3,0)));
    }

    @Test
    void testGetX(){
        assertEquals(new Vector2d(0,0).getX(), 0);
        assertEquals(new Vector2d(-2,2).getX(), -2);
        assertEquals(new Vector2d(4,0).getX(), 4);
        assertEquals(new Vector2d(10,10).getX(), 10);
    }

    @Test
    void testGetY(){
        assertEquals(new Vector2d(0,0).getY(), 0);
        assertEquals(new Vector2d(-2,2).getY(), 2);
        assertEquals(new Vector2d(4,0).getY(), 0);
        assertEquals(new Vector2d(10,-10).getY(), -10);
    }





}
