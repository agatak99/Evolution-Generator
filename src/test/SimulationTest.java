import agh.po.Map.Simulation;
import agh.po.MapElements.GameAnimal;
import agh.po.MapElements.Plant;
import agh.po.Properties.Vector2d;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.RepeatedTest;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimulationTest {

    @Test
    void testSimulationParameters(){
        assertThrows(IllegalArgumentException.class, () -> {
            Simulation simulation = new Simulation(0, 5, 0.5, 5, 1, 10,0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
           Simulation simulation = new Simulation(5, 0, 0.5, 5, 1, 10,0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Simulation simulation = new Simulation(5, 5, 0, 5, 1, 10,0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Simulation simulation = new Simulation(5, 5, 1, 5, 1, 10,0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Simulation simulation = new Simulation(5, 5, 0.5, 0, 1, 10,0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Simulation simulation = new Simulation(5, 5, 0.5, 5, 0, 10,0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Simulation simulation = new Simulation(5, 5, 0.5, 5, 1, 0,0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
           Simulation simulation = new Simulation(5, 5, 1.5, 5, 1, 10,0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Simulation simulation = new Simulation(5, 5, 0.5, 5, 1, 10,25);
        });
    }

    private Simulation simulation = new Simulation(10, 8, 0.4, 1, 1, 10, 0);


    @Test
    void testPlaceFirstAnimals()
    {
        assertEquals(simulation.map.getGameAnimals().size(),0);
        simulation.placeFirstAnimals(10);
        assertEquals(simulation.map.getGameAnimals().size(),10);
        simulation.map.getGameAnimals();
        assertEquals(simulation.plantEnergy,1);
        assertEquals(simulation.moveEnergy,1);
        assertEquals(simulation.startEnergy,10);;
    }

   @Test
    void testReproduction()
    {
        Vector2d position=new Vector2d(2,3);
        GameAnimal animal1=new GameAnimal(position, simulation.map, 20);
        simulation.map.place(animal1);
        List <Vector2d> positions=new ArrayList<>();
        positions.add(position);
        simulation.reproduction(positions);
        assertEquals(animal1.getEnergy(),20);
        GameAnimal animal2=new GameAnimal(position, simulation.map, 14);
        GameAnimal animal3=new GameAnimal(position, simulation.map, 12);
        GameAnimal animal4=new GameAnimal(position, simulation.map, 4);
        simulation.map.place(animal2);
        simulation.map.place(animal3);
        simulation.map.place(animal4);
        Vector2d animalsPosition;
        simulation.reproduction(positions);
        assertTrue(animal1.getEnergy()==15 && animal2.getEnergy()==10.5);
        assertTrue(animal3.getEnergy()==12 && animal4.getEnergy()==4);
        GameAnimal animal5=new GameAnimal(position, simulation.map, 12);
        simulation.map.place(animal5);
        simulation.reproduction(positions);
        assertTrue(animal1.getEnergy()==11.25 && (animal5.getEnergy()==9 ||animal3.getEnergy()==9 ));

    }

    @Test
    void testPlantConsumption()
    {
        Vector2d position=new Vector2d(4,5);
        List <Vector2d> positions=new ArrayList<>();
        positions.add(position);
        Plant plant=new Plant(position);
        GameAnimal animal1=new GameAnimal(position, simulation.map, 14);
        GameAnimal animal2=new GameAnimal(position, simulation.map, 12);
        GameAnimal animal3=new GameAnimal(position, simulation.map, 4);
        simulation.map.placePlant(plant);
        simulation.map.place(animal1);
        simulation.map.place(animal2);
        simulation.map.place(animal3);
        simulation.plantConsumption(positions);
        assertTrue(animal1.getEnergy()==15);
        assertTrue(animal2.getEnergy()==12 && animal3.getEnergy()==4);
        GameAnimal animal4=new GameAnimal(position, simulation.map, 15);
        simulation.map.place(animal4);
        simulation.map.placePlant(plant);
        simulation.plantConsumption(positions);
        assertTrue(animal1.getEnergy()==15.5 && animal4.getEnergy()==15.5);
    }

    @RepeatedTest(value = 40, name="Test {displayName} - {currentRepetition} / {totalRepetitions}")
    void testGeneratePlants()
    {
       simulation.generateGrassland_Plant();
       simulation.generateGrassland_Plant();
       simulation.generateGrassland_Plant();
       simulation.generateJungle_Plant();
       simulation.generateJungle_Plant();
        int junglePlantsCounter=0;
        int grasslandPlantsCounter=0;

       for(int i=0; i<10; i++)
       {
           for(int j=0; j<8; j++)
           {
               if(simulation.map.isOccupied(new Vector2d(i,j)))
               {
                   if (simulation.map.isInsideJungle(new Vector2d(i, j))) junglePlantsCounter++;
                   else grasslandPlantsCounter++;
               }
           }
       }
       assertEquals(junglePlantsCounter, 2);
       assertEquals(grasslandPlantsCounter, 3);

    }



}
