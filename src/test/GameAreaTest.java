/**
public class GameAreaTest {

    private GameArea map = new GameArea(4, 8, 0.4, 1, 1);

    @Test
    void testIsOccupied() {
        map.placeFirstAnimals(2, 10);

        int counter = 0;

        for (int i=0; i<4; i++) {
            for (int j=0; j<8; j++) {
                if (map.isOccupied(new Vector2d(i, j))) {
                    counter += 1;
                }
            }
        }
        assertEquals(2, counter);
    }

    @Test
    void testObjectsAt() {
        map.placeFirstAnimals(1, 0);
        GameAnimal animal = map.getGameAnimals().get(0);
        Vector2d position = animal.getPosition();
        int x = position.getX();
        int y = position.getY();

        int counter = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == x && j == y) {
                    assertTrue(!map.objectsAt(position).isEmpty());
                } else assertTrue((map.objectsAt(new Vector2d(i, j)).isEmpty()));

            }
        }
    }

    //wgl nie działa :(
    @Test
    void testObjectsAt2() {
        map.placeFirstAnimals(2, 0);
        int counter = 0;

        for (int i=0; i<4; i++) {
            for (int j=0; j<8; j++) {
                if (!map.objectsAt(new Vector2d(i, j)).isEmpty()) {
                    counter += 1;
                }
            }
        }
        assertEquals(2, counter);

        map.simulate();



        for (int i=0; i<4; i++) {
            for (int j=0; j<8; j++) {
                if (!map.objectsAt(new Vector2d(i, j)).isEmpty()) {
                    counter += 1;
                }
            }
        }
        assertEquals(2, counter);
    }

    @Test
    void testNextDay() {
        map.placeFirstAnimals(3, 1);

        map.simulate();
        map.simulate();

        int counter = 0;
        for (int i=0; i<4; i++) {
            for (int j=0; j<8; j++) {
                if (map.isOccupied(new Vector2d(i, j))) {
                    counter += 1;

                    assertFalse(map.objectsAt(new Vector2d(i, j)).toArray()[0] instanceof GameAnimal);
                }
            }
        }
        assertEquals(4, counter);
    }

}*/
