package agh.po.Map;

import agh.po.MapElements.GameAnimal;
import agh.po.MapElements.Plant;
import agh.po.Properties.Vector2d;
import agh.po.Utilities.MapVisualizer;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Simulation
{
    public final GameArea map;
    public final int startEnergy;
    public static int moveEnergy;
    public final double plantEnergy;



    public Simulation(int width, int height, double jungleRatio, int plantEnergy, int moveEnergy, int startEnergy, int startNumberOfAnimals )
    {
        this.map=new GameArea(width, height, jungleRatio);
        this.startEnergy=startEnergy;
        Simulation.moveEnergy=moveEnergy;
        this.plantEnergy=plantEnergy;


        if ( plantEnergy <= 0 || moveEnergy <= 0 ||startEnergy <= 0 || width <= 0 || height <= 0 || jungleRatio <= 0 || jungleRatio>1 ){
            throw new IllegalArgumentException("Given wrong parameters");
        }

        if (startNumberOfAnimals >= width * height) {
            throw new IllegalArgumentException("This number of animals is bigger than the number of empty fields");
        }

        if(this.map.emptyPositionsInJungle.size()+this.map.emptyPositionsInGrassland.size()<startNumberOfAnimals)
            throw new IllegalArgumentException("This number of animals is bigger than the number of empty fields");
        placeFirstAnimals(startNumberOfAnimals);

    }

    @Override
    public String toString() {
        return this.draw();
    }

    private String draw(){
        MapVisualizer mapVisualizer = new MapVisualizer(this.map);
        Vector2d lower_left=map.grassLand_lower_left;
        Vector2d upper_right=map.grassLand_upper_right;
        return mapVisualizer.draw(lower_left, upper_right);
    }

    public void placeFirstAnimals(int startNumberOfAnimals)
    {
        if(!(this.map.emptyPositionsInGrassland.isEmpty() && this.map.emptyPositionsInJungle.isEmpty()))
        {
            Vector2d randomPosition=null;
            for(int i=0; i<startNumberOfAnimals;i++)
            {
                int option=ThreadLocalRandom.current().nextInt(2);
                if(option==0 && !this.map.emptyPositionsInJungle.isEmpty())
                {
                    int limit=map.emptyPositionsInJungle.size();
                    int randomIndex=ThreadLocalRandom.current().nextInt(limit);
                    randomPosition=map.emptyPositionsInJungle.get(randomIndex);
                    this.map.emptyPositionsInJungle.remove(randomPosition);
                }
                else if(!this.map.emptyPositionsInGrassland.isEmpty())
                {   int limit=map.emptyPositionsInGrassland.size();
                    int randomIndex=ThreadLocalRandom.current().nextInt(limit);
                    randomPosition=map.emptyPositionsInGrassland.get(randomIndex);
                    this.map.emptyPositionsInGrassland.remove(randomPosition);
                }
                if(randomPosition!=null)
                {
                    GameAnimal newAnimal=new GameAnimal(randomPosition, this.map, this.startEnergy);
                    map.place(newAnimal);
                }
             }
        }
    }

    public void simulate()
    {
        this.removeDeadAnimals();
        this.run();
        this.generateJungle_Plant();
        this.generateGrassland_Plant();
    }

    private void run()
    {
        boolean isInJungleBeforeMovement;
        boolean isInJungleAfterMovement;
        List<Vector2d> positions = new ArrayList<>();
        for(GameAnimal animal: map.animals)
            animal.turn();

        for (GameAnimal animal : map.animals) {
            Vector2d position1=animal.getPosition();
            isInJungleBeforeMovement=map.isInsideJungle(position1);
            animal.move();
            Vector2d position2=animal.getPosition();
            isInJungleAfterMovement=map.isInsideJungle(position2);
            if(isInJungleBeforeMovement)
            {
                    if(map.elementsMap.get(position1).size()==0 )
                    this.map.emptyPositionsInJungle.add(position1);
            }
            else
            {
                if(map.elementsMap.get(position1).size()==0 )
                    this.map.emptyPositionsInGrassland.remove(position1);
            }
            if(isInJungleAfterMovement)
            {
                this.map.emptyPositionsInJungle.remove(position2);
            }
            else
            {
                this.map.emptyPositionsInGrassland.add(position2);
            }
            if (map.objectsAt(animal.getPosition()).size() >= 1 && !positions.contains(animal.getPosition())) {
                positions.add(animal.getPosition());
            }
        }
        reproduction(positions);
        plantConsumption(positions);
    }

    public void plantConsumption(List <Vector2d> positions) {

        int quantity;
        for (Vector2d position : positions) {
            Plant plant = null;

            for (Object element : map.elementsMap.get(position)) {
                if (element instanceof Plant) {
                    plant = (Plant) element;
                    break;
                }
            }
            if(plant!=null && map.animalsAtPosition(position).size()==1)
            {
                map.animalsAtPosition(position).get(0).addPlantEnergy(plantEnergy);
                map.elementsMap.get(position).remove(plant);
                if(this.map.isInsideJungle(position))
                    this.map.emptyPositionsInJungle.add(position);
                else this.map.emptyPositionsInGrassland.add(position);
            }
            else if (plant != null &&map.animalsAtPosition(position).size()>1 ) {

                List<GameAnimal> animalsWithMaxEnergy;
                animalsWithMaxEnergy = getStrongestAnimals(map.animalsAtPosition(position));
                if(!animalsWithMaxEnergy.isEmpty())
                {
                    quantity = animalsWithMaxEnergy.size();
                    double plantEnergy = this.plantEnergy / quantity;
                    for (GameAnimal animal : animalsWithMaxEnergy) {
                        animal.addPlantEnergy(plantEnergy);
                    }
                    map.elementsMap.get(position).remove(plant);
                    if(this.map.isInsideJungle(position))
                        this.map.emptyPositionsInJungle.add(position);
                    else this.map.emptyPositionsInGrassland.add(position);
                }
            }

        }
    }

    public void reproduction(List <Vector2d> positions)
    {
        GameAnimal animal1;
        GameAnimal animal2;
        for(Vector2d animalPosition:positions)
        {
            if(map.animalsAtPosition(animalPosition).size()==2)
            {
                animal1=map.animalsAtPosition(animalPosition).get(0);
                animal2=map.animalsAtPosition(animalPosition).get(1);
                animal1.reproduce(animal2);
            }
            else if(map.animalsAtPosition(animalPosition).size()>2)
            {
                List<GameAnimal> animalsWithMaxEnergy;
                List<GameAnimal> animalsWithSecMaxEnergy;
                animalsWithMaxEnergy= getStrongestAnimals(map.animalsAtPosition(animalPosition));

                    if(animalsWithMaxEnergy.size()==2)
                    {
                        animal1 = animalsWithMaxEnergy.get(0);
                        animal2 = animalsWithMaxEnergy.get(1);
                        animal1.reproduce(animal2);
                    }
                    else if (animalsWithMaxEnergy.size() > 2) {
                        int randomIndex1;
                        int randomIndex2;
                        do {
                            randomIndex1 = ThreadLocalRandom.current().nextInt(animalsWithMaxEnergy.size());
                            randomIndex2 = ThreadLocalRandom.current().nextInt(animalsWithMaxEnergy.size());
                        } while (randomIndex1 == randomIndex2);
                        animal1 = animalsWithMaxEnergy.get(randomIndex1);
                        animal2 = animalsWithMaxEnergy.get(randomIndex2);
                        animal1.reproduce(animal2);
                    }
                    else  if (animalsWithMaxEnergy.size()<2) {
                        animalsWithSecMaxEnergy = getSecStrongestAnimals(map.animalsAtPosition(animalPosition));
                        int randomIndex;
                        if (!animalsWithSecMaxEnergy.isEmpty()) {
                            randomIndex = ThreadLocalRandom.current().nextInt(animalsWithSecMaxEnergy.size());
                            animal2 = animalsWithSecMaxEnergy.get(randomIndex);
                            animal1 = animalsWithMaxEnergy.get(0);
                            animal1.reproduce(animal2);
                        }
                    }
            }

        }

    }

    public void generateGrassland_Plant()
    {
        if(!this.map.emptyPositionsInGrassland.isEmpty())
        {
            int limit=this.map.emptyPositionsInGrassland.size();
            Vector2d randomPlantPosition;
            int randomIndex=ThreadLocalRandom.current().nextInt(limit);
            randomPlantPosition=this.map.emptyPositionsInGrassland.get(randomIndex);
            Plant newPlant =new Plant(randomPlantPosition);
            map.elementsMap.get(randomPlantPosition).add(newPlant);
            this.map.emptyPositionsInGrassland.remove(randomPlantPosition);
        }
    }

   public void generateJungle_Plant()
    {
        if(!this.map.emptyPositionsInJungle.isEmpty())
        {
            int limit=this.map.emptyPositionsInJungle.size();
            Vector2d randomPlantPosition;
            int randomIndex=ThreadLocalRandom.current().nextInt(limit);
            randomPlantPosition=this.map.emptyPositionsInJungle.get(randomIndex);
            Plant newPlant =new Plant(randomPlantPosition);
            map.elementsMap.get(randomPlantPosition).add(newPlant);
            this.map.emptyPositionsInJungle.remove(randomPlantPosition);
        }
    }

    private void removeDeadAnimals()
    {
        boolean isInJungle;
        List<GameAnimal> animalsToRemove = new ArrayList<>();
        for (GameAnimal animal : map.animals) {
            if (animal.isDead()) {
                animalsToRemove.add(animal);
            }
        }

        for (GameAnimal animal : animalsToRemove) {
            Vector2d position=animal.getPosition();
            isInJungle=(map.isInsideJungle(position));
            animal.removeObserver(this.map);
            map.elementsMap.get(animal.getPosition()).remove(animal);
            map.animals.remove(animal);
            if(isInJungle && map.elementsMap.get(position).size()==0)
            {
                this.map.emptyPositionsInJungle.add(position);
            }
            else if(map.elementsMap.get(position).size()==0)
            {
                this.map.emptyPositionsInGrassland.add(position);
            }
        }

    }

    private List<GameAnimal> getStrongestAnimals(List <GameAnimal> animalsAtSamePosition){//simulation

        List<GameAnimal> strongestAnimals = new ArrayList<>();

        double maxEnergy=animalsAtSamePosition.get(0).getEnergy();
        if(animalsAtSamePosition.size()>=1 )
        {
            for( GameAnimal animal: animalsAtSamePosition){

                if(animal.getEnergy() > maxEnergy) {
                    maxEnergy = animal.getEnergy();
                }
            }
            for(GameAnimal animal:animalsAtSamePosition)
            {
                if(animal.getEnergy()==maxEnergy)
                {
                    strongestAnimals.add(animal);
                }
            }
        }
        return strongestAnimals;
    }

    private List<GameAnimal> getSecStrongestAnimals(List <GameAnimal> animalsAtSamePosition) {



        List<GameAnimal> secStrongestAnimals = new ArrayList<>();
        GameAnimal strongest = getStrongestAnimals(animalsAtSamePosition).get(0);
        animalsAtSamePosition.remove(strongest);
        double maxEnergy= animalsAtSamePosition.get(0).getEnergy();
        if(animalsAtSamePosition.size()>=1)
        {
            for (GameAnimal animal : animalsAtSamePosition) {
                if (animal.getEnergy() > maxEnergy && animal.getEnergy() != strongest.getEnergy()) {
                    maxEnergy = animal.getEnergy();
                }
            }
            for(GameAnimal animal: animalsAtSamePosition)
            {
                if(animal.getEnergy()==maxEnergy)
                {
                    secStrongestAnimals.add(animal);
                }
            }
        }

        return secStrongestAnimals;
    }

    public boolean areAnimalsAlive(){
        return(map.animals.size() > 0);
    }

}

