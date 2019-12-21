
import agh.po.MapElements.GameAnimal;
import agh.po.Map.Simulation;
import agh.po.Properties.Vector2d;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class GameAreaTest {

    private Simulation simulation = new Simulation(10, 8, 0.4, 1, 1, 10, 2);

    @Test
    void testIsOccupiedAndAnimalsAtPosition() {
        int counter = 0;
        int sum=0;
        simulation.placeFirstAnimals(10);

        for (int i=0; i<10; i++) {
            for (int j=0; j<8; j++) {
                if (simulation.map.isOccupied(new Vector2d(i, j))) {
                    counter += 1;
                    sum+=simulation.map.animalsAtPosition(new Vector2d(i,j)).size();
                }
            }
        }
        assertEquals(12, sum);
        assertEquals(12, counter);
    }

    @Test
    void testObjectsAtAndPlace() {

        int counter = 0;

        assertEquals(simulation.map.getGameAnimals().size(), 2);

        Vector2d position=new Vector2d(2,4);
        GameAnimal animal = new GameAnimal(position, simulation.map, 2);

        simulation.map.place(animal);
        assertEquals(simulation.map.getGameAnimals().size(), 3);

        assertTrue(!simulation.map.objectsAt(position).isEmpty());
        animal.move();
        position=animal.getPosition();
        assertTrue(!simulation.map.objectsAt(position).isEmpty());

        GameAnimal animal1 = new GameAnimal(new Vector2d(12, 10),simulation.map,  10);
        GameAnimal animal2 = new GameAnimal( new Vector2d(12, 7),simulation.map, 10);
        GameAnimal animal3 = new GameAnimal(new Vector2d(7, 10),simulation.map,  10);
        GameAnimal animal4 = new GameAnimal( new Vector2d(-1, -1),simulation.map, 10);
        GameAnimal animal5 = new GameAnimal( new Vector2d(-5, 0),simulation.map, 10);
        GameAnimal animal6 = new GameAnimal( new Vector2d(5, -3),simulation.map, 10);

        assertThrows(IllegalArgumentException.class, () -> {
            simulation.map.place(animal1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            simulation.map.place(animal2);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            simulation.map.place(animal3);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            simulation.map.place(animal4);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            simulation.map.place(animal5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            simulation.map.place(animal6);
        });

    }

    @Test
    void testCorrectNewPosition()
    {
        Vector2d position0=new Vector2d(2,3);
        assertEquals(simulation.map.correctNewPosition(position0), new Vector2d(2,3));
        Vector2d position1=new Vector2d(9,7);//pop
        assertEquals(simulation.map.correctNewPosition(position1), new Vector2d(9,7));
        Vector2d position2=new Vector2d(-1,7);
        assertEquals(simulation.map.correctNewPosition(position2), new Vector2d(9,7));
        Vector2d position3=new Vector2d(5,8);
        assertEquals(simulation.map.correctNewPosition(position3), new Vector2d(5,0));
        Vector2d position4=new Vector2d(10,4);
        assertEquals(simulation.map.correctNewPosition(position4), new Vector2d(0,4));
        Vector2d position5=new Vector2d(10,8);
        assertEquals(simulation.map.correctNewPosition(position5), new Vector2d(0,0));
        Vector2d position6=new Vector2d(-1,-1);
        assertEquals(simulation.map.correctNewPosition(position6), new Vector2d(9,7));

    }

    @Test
    void insideJungle(){
        Vector2d jungleLowerLeft = simulation.map.jungle_lower_left;
        Vector2d jungleUpperRight = new Vector2d(jungleLowerLeft.x+simulation.map.jungleWidth, jungleLowerLeft.y+simulation.map.jungleHeight);
        Vector2d position0=new Vector2d(jungleLowerLeft.x-1, jungleLowerLeft.y);
        Vector2d position1=new Vector2d(jungleLowerLeft.x, jungleLowerLeft.y-1);
        Vector2d position2=new Vector2d(jungleUpperRight.x+1, jungleLowerLeft.y);
        Vector2d position3=new Vector2d(jungleUpperRight.x, jungleUpperRight.y+1);
        Vector2d position4=new Vector2d(jungleUpperRight.x+1, jungleLowerLeft.y-1);
        Vector2d position5=new Vector2d(jungleUpperRight.x-1, jungleUpperRight.y-1);
        Vector2d position6=new Vector2d(jungleLowerLeft.x+1, jungleLowerLeft.y+1);

        assertFalse(simulation.map.isInsideJungle(position0));
        assertFalse(simulation.map.isInsideJungle(position1));
        assertFalse(simulation.map.isInsideJungle(position2));
        assertFalse(simulation.map.isInsideJungle(position3));
        assertFalse(simulation.map.isInsideJungle(position4));
        assertTrue(simulation.map.isInsideJungle(position5));
        assertTrue(simulation.map.isInsideJungle(position6));

    }

    @RepeatedTest(value = 40, name="Test {displayName} - {currentRepetition} / {totalRepetitions}")
    void testRandomlyGetPosition()
    {
        Vector2d lowerLeft=simulation.map.grassLand_lower_left;
        Vector2d upperRight=simulation.map.grassLand_upper_right;
        Vector2d position=simulation.map.randomlyGetPosition();
        assertTrue(position.precedes(upperRight) && position.follows(lowerLeft));
    }

    @RepeatedTest(value = 20, name="Test {displayName} - {currentRepetition} / {totalRepetitions}")
    void testGetBabyPosition()
    {
        GameAnimal parent1=new GameAnimal(new Vector2d(2,3), simulation.map, 10);
        GameAnimal parent2=new GameAnimal(new Vector2d(2,3), simulation.map, 10);
        simulation.map.place(parent1);
        simulation.map.place(parent2);
        parent1.reproduce(parent2);
        GameAnimal child=simulation.map.getGameAnimals().get(4);
        assertTrue(Math.abs(parent1.getPosition().x-child.getPosition().x)<=1 && Math.abs(parent1.getPosition().y-child.getPosition().y)<=1 );
    }
}
