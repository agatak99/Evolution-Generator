import agh.po.Map.Simulation;
import agh.po.MapElements.GameAnimal;
import agh.po.MapElements.Plant;
import agh.po.Properties.GameDirection;
import agh.po.Properties.Vector2d;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//:todo napisz testy
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
    }

    //4.generateGrassPlant
    //5.generateJunglePlant


    private Simulation simulation = new Simulation(10, 8, 0.4, 1, 1, 10, 0);


    @Test
    void testplaceFirstAnimals()
    {
        assertEquals(simulation.map.getGameAnimals().size(),0);
        simulation.placeFirstAnimals(10);
        assertEquals(simulation.map.getGameAnimals().size(),10);
    }

    @Test
    void testStrongestAndSecStrongest()
    {
        Vector2d position=new Vector2d(3,4);
        GameAnimal animal1=new GameAnimal(position, simulation.map, 20);
        GameAnimal animal2=new GameAnimal(position, simulation.map, 20);
        GameAnimal animal3=new GameAnimal(position, simulation.map, 14);
        GameAnimal animal4=new GameAnimal(position, simulation.map, 14);
        GameAnimal animal5=new GameAnimal(position, simulation.map, 12);
        simulation.map.place(animal5);
        simulation.map.place(animal3);
        simulation.map.place(animal4);
        simulation.map.place(animal1);
        simulation.map.place(animal2);
        List <GameAnimal> animals=simulation.map.animalsAtPosition(position);
        List <GameAnimal> strongestAnimals=simulation.strongestAnimals(animals);
        List <GameAnimal> secStrongestAnimals=simulation.secStrongestAnimals(animals);
        assertEquals(strongestAnimals.size(), 2);
        assertEquals(simulation.secStrongestAnimals(animals).size(), 2);
        assertTrue(strongestAnimals.contains(animal1) && strongestAnimals.contains(animal2) );
        assertFalse(strongestAnimals.contains(animal3) || strongestAnimals.contains(animal4) || strongestAnimals.contains(animal5) );
        assertFalse(secStrongestAnimals.contains(animal1) ||  secStrongestAnimals.contains(animal5));
        assertTrue(secStrongestAnimals.contains(animal3) ||secStrongestAnimals.contains(animal4));
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
    void testPlantComsumption()
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



}
