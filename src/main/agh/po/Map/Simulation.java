package agh.po.Map;

import agh.po.MapElements.GameAnimal;
import agh.po.Properties.Vector2d;
import agh.po.Utilities.MapVisualizer;

public class Simulation
{
    private GameArea map;
    private int startEnergy;
    private int moveEnergy;
    public Simulation(int width, int height, double jungleRatio, int plantEnergy, int moveEnergy, int startEnergy, int startNumberOfAnimals )
    {
        this.map=new GameArea(width, height, jungleRatio,plantEnergy, moveEnergy);
        this.startEnergy=startEnergy;
        this.moveEnergy=moveEnergy;

        if ( plantEnergy <= 0 || moveEnergy <= 0 ||startEnergy <= 0 || width <= 0 || height <= 0 || jungleRatio <= 0 || jungleRatio>1 ){
            throw new IllegalArgumentException("Every start parameter must be bigger than 0");
        }

        if (startNumberOfAnimals > width * height) {
            throw new IllegalArgumentException("This number of animals is too big to fit on the map");
        }

        placeFirstAnimals(startNumberOfAnimals);

    }

    @Override
    public String toString() {
        return this.draw();
    }

    private String draw(){
        MapVisualizer mapVisualizer = new MapVisualizer(this.map);
        Vector2d lower_left=map.getLowerLeft();
        Vector2d upper_right=map.getUpperRight();
        return mapVisualizer.draw(lower_left, upper_right);
    }

    public void simulate()
    {
        this.map.removeDeadAnimals();
        this.map.run();
        this.map.generateJungle_Plant();
        this.map.generateGrassland_Plant();
    }

   private void placeFirstAnimals(int startNumberOfAnimals)
    {
        for(int i=0; i<startNumberOfAnimals;i++)
        {
            Vector2d randomPosition;
            do
            {
                randomPosition = map.randomlyGetPosition();
            }while(map.isOccupied(randomPosition));

            GameAnimal newAnimal=new GameAnimal(randomPosition, this.map, this.startEnergy);
            map.place(newAnimal);

        }
    }

    public boolean areAnimalsAlive(){
        return(map.animals.size() > 0);
    }

}
