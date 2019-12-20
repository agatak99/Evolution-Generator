/**

public class GameAnimalTest {

    private GameArea map =new GameArea(20,10,0.4,1,1);

    @Test //testuje move, isDead, reduceEnergy
    void testMove(){

        map.placeFirstAnimals(5,4);
        Vector2d position;
        Vector2d expectedPosition;
        for(int i=0; i<3; i++)
        {
            for(GameAnimal animal: map.getGameAnimals())
            {
                position=animal.getPosition();
                animal.move();
                GameDirection direction=animal.getDirection();
                expectedPosition=map.correctNewPosition(position.add(direction.toUnitVector()));
                assertEquals(animal.getPosition(), expectedPosition);
                assertEquals(animal.getEnergy(), animal.startEnergy-(i+1));
                assertTrue(!animal.isDead());

            }
        }

        for(GameAnimal animal: map.getGameAnimals())
        {
            animal.move();
            assertTrue(animal.isDead());
        }

    }

    @Test
    void testReprocuce()
    {
        Vector2d position=new Vector2d(3,5);
        Vector2d babyPosition=new Vector2d(3,6);
        GameAnimal parent1=new GameAnimal(position, map, 8);
        GameAnimal parent2=new GameAnimal(position, map, 10);
        GameAnimal baby=new GameAnimal(babyPosition, parent1, parent2);
        assertEquals(baby.getEnergy(), 4.5);
        assertEquals(parent1.getEnergy(),6);
        assertEquals(parent2.getEnergy(),7.5 );
        int xBabyCoord=baby.getPosition().getX();
        int yBabyCoord=baby.getPosition().getY();
        int xParentCoord=parent1.getPosition().getX();
        int yParentCoord=parent1.getPosition().getY();
        assertTrue(Math.abs(xParentCoord-xBabyCoord)<=1 && Math.abs(yParentCoord-yBabyCoord)<=1 );
    }

    @Test
    void testIsAbleToReproduceAndIsDead()
    {
        GameAnimal animal1 = new GameAnimal( new Vector2d(1,1),map,  10);
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
        GameAnimal animal = new GameAnimal( new Vector2d(1,1),map,  1);
        assertEquals(animal.toString(), "\ud83d\udc12");
        animal.move();
        assertEquals(animal.toString(), "\u2620");
    }
}
*/