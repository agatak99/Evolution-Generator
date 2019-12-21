import agh.po.MapElements.GameAnimal;
import agh.po.Map.Simulation;
import agh.po.Properties.Vector2d;
import agh.po.Properties.GameDirection;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameAnimalTest {

  private Simulation simulation =new Simulation(20, 10, 0.4, 2 , 1, 4, 5 );

    @RepeatedTest(value = 10, name="Test {displayName} - {currentRepetition} / {totalRepetitions}")
    void testMoveAndTurn(){

        Vector2d position;
        Vector2d expectedPosition;

        for(int i=0; i<3; i++)
        {
            for(GameAnimal animal: simulation.map.getGameAnimals())
            {
                position=animal.getPosition();
                animal.turn();
                animal.move();
                GameDirection direction=animal.getDirection();
                assertTrue(direction==GameDirection.NORTH || direction==GameDirection.NORTHEAST || direction==GameDirection.EAST || direction==GameDirection.SOUTHEAST || direction==GameDirection.SOUTH || direction==GameDirection.SOUTHWEST || direction==GameDirection.WEST || direction==GameDirection.NORTHWEST);
                expectedPosition=simulation.map.correctNewPosition(position.add(direction.toUnitVector()));
                assertEquals(animal.getPosition(), expectedPosition);
                assertEquals(animal.getEnergy(), animal.startEnergy-(i+1));
                assertFalse(animal.isDead());
                assertTrue(simulation.map.getGameAnimals().size()==5);
            }
        }

        for(GameAnimal animal: simulation.map.getGameAnimals())
        {
            animal.move();
            assertTrue(animal.isDead());
        }

    }

   @Test
    void testReproduce() {
       Vector2d position = new Vector2d(3, 5);
       Vector2d babyPosition = new Vector2d(3, 6);
       GameAnimal parent1 = new GameAnimal(position, simulation.map, 8);
       simulation.map.place(parent1);
       GameAnimal parent2 = new GameAnimal(position, simulation.map, 10);
       simulation.map.place(parent2);
       GameAnimal baby = new GameAnimal(babyPosition, parent1, parent2);
       assertEquals(baby.getEnergy(), 4.5);
       assertEquals(parent1.getEnergy(), 6);
       assertEquals(parent2.getEnergy(), 7.5);
       assertTrue(parent1.isAbleToReproduce());
       assertTrue(parent2.isAbleToReproduce());
       assertTrue(simulation.map.getGameAnimals().size() == 7);
       parent1.reproduce(parent2);
       assertTrue(simulation.map.getGameAnimals().size()==8);
       Vector2d expectedBabyPosition;
       boolean positionIsCorrect = false;
       GameDirection direction = GameDirection.NORTH;
       for (int i = 0; i < 8; i++) {
           expectedBabyPosition = simulation.map.correctNewPosition(position.add(direction.rotation(i).toUnitVector()));
           if (simulation.map.isOccupied(expectedBabyPosition)) {
               positionIsCorrect = true;
           }
       }
       assertTrue(positionIsCorrect);
   }

    @Test
    void testIsAbleToReproduceAndIsDead()
    {
        GameAnimal animal1 = new GameAnimal( new Vector2d(1,1),simulation.map,  10);
        for (int i=0; i<10; i++){
            if (animal1.getEnergy() >= 5.0) assertTrue(animal1.isAbleToReproduce());
            else assertFalse(animal1.isAbleToReproduce());
            animal1.move();
        }
        assertTrue(animal1.isDead());
    }

    @Test
    void testToString()
    {
        GameAnimal animal = new GameAnimal(new Vector2d(1, 1), simulation.map, 1);
        assertEquals(animal.toString(), " \ud83d\udc7e");
        animal.move();
        assertEquals(animal.toString(), " \ud83d\udc80");
    }

}
