import agh.po.MapElements.Plant;
import agh.po.Properties.Vector2d;
import agh.po.Map.GameArea;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GrassTest
{
    private Plant plant1 =new Plant(new Vector2d(2,5));
    private Plant plant2 =new Plant(new Vector2d(0,-3));


    @Test
    void testToString()
    {
        String symbol=" \ud83c\udf31";
        assertEquals(plant1.toString(), symbol);
        assertEquals(plant1.toString(), symbol);
    }

    @Test
    void testGetPosition()
    {
        assertEquals(plant1.position, new Vector2d(2,5));
        assertEquals(plant2.position, new Vector2d(0,-3));
        assertNotEquals(plant1.position, new Vector2d(5,5));
        assertNotEquals(plant2.position, new Vector2d(0,3));
    }



}
