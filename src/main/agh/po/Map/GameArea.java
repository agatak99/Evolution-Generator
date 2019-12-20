package agh.po.Map;
import agh.po.MapElements.GameAnimal;
import agh.po.Properties.GameDirection;
import agh.po.MapElements.Plant;
import agh.po.Properties.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**prostokąrna połać, której brzegi zawijają się na drugą stronę,
 * pokryta stepami, a w jej środku występuje dżungla
 * proporcja dżungli do sawanny to jungleRatio
 * width:200, height:100*/

/**  Rosliny wyrastają w losowych miejscach, ich koncentracja jest większa w dżungli niż na stepach.
 * Każdego dnia w każdej strefie pojawia się się jedna nowa roślina w kazdej strefie*/

/** Dżungla- obszar znajdujący się w środku areny gry, rośliny rosną tam dużo szybciej */

/** Usunięcie martwych zwierząt z mapy
 * skręt, przemieszczenie, jedzenie i rozmnażanie wszystkich zwierząt po kolei
 * dodanie nowych roślin do mapy*/

public class GameArea implements IWorldMap, IPositionChangeObserver
{
    private final double plantEnergy;
    private final int width;
    private final int height;
    private final int jungleWidth;
    private final int jungleHeight;
    private Vector2d grassLand_lower_left;
    private Vector2d grassLand_upper_right;
    private final Vector2d jungle_lower_left;
    public static int moveEnergy;

    List<GameAnimal> animals = new ArrayList<>();
    private Map<Vector2d, List<Object>> elementsMap = new HashMap<>();

    public GameArea(int width, int height,double jungleRatio, int plantEnergy, int moveEnergy) {
        this.width=width;
        this.height=height;
        this.jungleWidth=(int) (width * jungleRatio);
        this.jungleHeight=(int) (height * jungleRatio);
        width=width-1;
        height=height-1;
        this.grassLand_lower_left = new Vector2d(0, 0);
        this.grassLand_upper_right = new Vector2d(width, height);
        this.jungle_lower_left=new Vector2d((this.width-this.jungleWidth)/2, (this.height-this.jungleHeight)/2);
        this.plantEnergy=plantEnergy;
        GameArea.moveEnergy=moveEnergy;

        for (int i=0; i<=width; i++) {
            for (int j=0; j<=height; j++) {
                List<Object> newList = new ArrayList<>();
                elementsMap.put(new Vector2d(i, j), newList);
            }
        }

        if (!grassLand_lower_left.precedes(jungle_lower_left)) {
            throw new IllegalArgumentException("Jungle lower left corner can't precedes map lower left corner");
        }
        if (!grassLand_upper_right.follows(new Vector2d(jungle_lower_left.x+jungleWidth, jungle_lower_left.y+jungleHeight))) {
            throw new IllegalArgumentException("Jungle upper right corner can't follows map upper right corner");
        }

    }


    @Override
    public void run()
    {
        List<Vector2d> animalsPosition = new ArrayList<>();
        for(GameAnimal animal: this.animals)
        {
            animal.turn();
        }

        for (GameAnimal animal : this.animals) {
            animalsPosition.add(animal.move());

        }
        this.reproduction(animalsPosition);
        this.plantConsumption(animalsPosition);
    }

     void generateJungle_Plant()
    {
        if(!isJungleFull())
        {
            int positionXInJungle;
            int positionYInJungle;

            Vector2d randomJungleGrassPosition;

            do{
                positionXInJungle=ThreadLocalRandom.current().nextInt(this.jungleWidth);
                positionYInJungle=ThreadLocalRandom.current().nextInt(this.jungleHeight);
                randomJungleGrassPosition=new Vector2d(positionXInJungle+this.jungle_lower_left.x, positionYInJungle+this.jungle_lower_left.y);
            } while(isOccupied(randomJungleGrassPosition));

            Plant newPlant =new Plant(randomJungleGrassPosition);
            this.elementsMap.get(randomJungleGrassPosition).add(newPlant);
        }
    }

    private Vector2d generateOptionalPositionForPlant() {
        int random = ThreadLocalRandom.current().nextInt(4);
        int positionXInGrassland;
        int positionYInGrassland;
        switch (random) {
            case 0:
                positionXInGrassland = ThreadLocalRandom.current().nextInt(this.width );
                positionYInGrassland = ThreadLocalRandom.current().nextInt(this.jungle_lower_left.y+1);
                return new Vector2d(positionXInGrassland, positionYInGrassland);
            case 1:
                positionXInGrassland = ThreadLocalRandom.current().nextInt(this.width);
                positionYInGrassland = ThreadLocalRandom.current().nextInt(this.jungle_lower_left.y + jungleHeight , height);
                return new Vector2d(positionXInGrassland, positionYInGrassland);
            case 2:
                positionXInGrassland = ThreadLocalRandom.current().nextInt(this.jungle_lower_left.x+1);
                positionYInGrassland = ThreadLocalRandom.current().nextInt(this.jungle_lower_left.y+1, this.jungle_lower_left.y+jungleHeight );
                return new Vector2d(positionXInGrassland, positionYInGrassland);

            case 3:
                positionXInGrassland = ThreadLocalRandom.current().nextInt(this.jungle_lower_left.x + jungleWidth , width );
                positionYInGrassland = ThreadLocalRandom.current().nextInt(this.jungle_lower_left.y, this.jungle_lower_left.y+jungleHeight  );
                return new Vector2d(positionXInGrassland, positionYInGrassland);

        }
        throw new IllegalArgumentException( "Incorrect argument returned by ThreadLocalRandom");

    }

     void generateGrassland_Plant()
    {
        Vector2d randomPlantPosition;
        do {
            randomPlantPosition=generateOptionalPositionForPlant();
        }while(isOccupied(randomPlantPosition));

        Plant newPlant =new Plant(randomPlantPosition);
        elementsMap.get(randomPlantPosition).add(newPlant);
    }

    private List<GameAnimal> strongestAnimals(List <Object> elements){//simulation
        double maxEnergy = 0;
        List<GameAnimal> strongestAnimals = new ArrayList<>();
        GameAnimal strongest = null;
        for( Object element : elements){
            if(element instanceof GameAnimal)
            {
                GameAnimal animal=(GameAnimal) element;
                if(animal.getEnergy() > maxEnergy)
                {
                    strongest=animal;
                    maxEnergy=strongest.getEnergy();
                    for(GameAnimal strongestAnimal: strongestAnimals)
                    {
                        strongestAnimals.remove(strongestAnimal);
                    }
                }
                else if(animal.getEnergy() == maxEnergy)
                {
                    strongestAnimals.add(animal);
                }
            }
        }
        if(strongest!=null)
        {
            strongestAnimals.add(strongest);
        }

        return strongestAnimals;
    }

    private List<GameAnimal> secStrongestAnimals(List <Object> elements) {
        double maxEnergy = 0;
        GameAnimal secStrongest = null;
        List<GameAnimal> secStrongestAnimals = new ArrayList<>();

        GameAnimal strongest = strongestAnimals(elements).get(0);
        for (Object element : elements) {
            if (element instanceof GameAnimal) {
                GameAnimal animal = (GameAnimal) element;
                if (animal.getEnergy() > maxEnergy && animal != strongest) {
                    secStrongest = animal;
                    maxEnergy = secStrongest.getEnergy();
                    for(GameAnimal secStrongestAnimal:secStrongestAnimals)
                    {
                        secStrongestAnimals.remove(secStrongestAnimal);
                    }
                } else if (animal.getEnergy() == maxEnergy) {
                    secStrongestAnimals.add(animal);
                }
            }
        }

        if(secStrongest!=null)
        {
            secStrongestAnimals.add(secStrongest);
        }
        return secStrongestAnimals;
    }

    private void plantConsumption(List <Vector2d> Positions) {
        int quantity;
        for (Vector2d position : Positions) {
            Plant plant = null;

            for (Object element : elementsMap.get(position)) {
                if (element instanceof Plant) {
                    plant = (Plant) element;
                    break;
                }
            }
            if (plant != null) {
                List<GameAnimal> animalsWithMaxEnergy;
                animalsWithMaxEnergy = strongestAnimals(this.elementsMap.get(position));
                if(!animalsWithMaxEnergy.isEmpty())
                {
                    quantity = animalsWithMaxEnergy.size();
                    double plantEnergy = this.plantEnergy / quantity;
                    for (GameAnimal animal : animalsWithMaxEnergy) {
                        animal.addPlantEnergy(plantEnergy);
                    }
                    this.elementsMap.get(position).remove(plant);

                }
            }

        }
    }
//todo:popraw rozmnażanie=> pojawia sie chyba więcej zwierząt niz powinno
    private void reproduction(List <Vector2d> AnimalsPosition)
    {
        GameAnimal animal1;
        GameAnimal animal2;
        for(Vector2d animalPosition:AnimalsPosition)
        {
            if (elementsMap.get(animalPosition).size() >=2)
            {
                List<GameAnimal> animalsWithMaxEnergy;
                List<GameAnimal> animalsWithSecondMaxEnergy;
                animalsWithMaxEnergy=strongestAnimals(elementsMap.get(animalPosition));
                if(!animalsWithMaxEnergy.isEmpty())
                {
                    if(animalsWithMaxEnergy.size()<2)
                    {
                        animalsWithSecondMaxEnergy=secStrongestAnimals(elementsMap.get(animalPosition));
                        int randomIndex;
                        if(!animalsWithSecondMaxEnergy.isEmpty())
                        {
                            randomIndex=ThreadLocalRandom.current().nextInt(animalsWithSecondMaxEnergy.size());
                            animal2=animalsWithSecondMaxEnergy.get(randomIndex);
                            animal1=animalsWithMaxEnergy.get(0);
                            animal1.reproduce(animal2);
                        }
                    }
                    else if(animalsWithMaxEnergy.size()>=2)
                    {
                        int randomIndex1;
                        int randomIndex2;
                        do{
                            randomIndex1=ThreadLocalRandom.current().nextInt(animalsWithMaxEnergy.size());
                            randomIndex2=ThreadLocalRandom.current().nextInt(animalsWithMaxEnergy.size());
                        }while(randomIndex1==randomIndex2);
                        animal1=animalsWithMaxEnergy.get(randomIndex1);
                        animal2=animalsWithMaxEnergy.get(randomIndex2);
                        animal1.reproduce(animal2);
                    }
                }
            }
        }

    }

    //@Override
    public List<Object> objectsAt(Vector2d position) {
        return this.elementsMap.get(position);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return !this.objectsAt(position).isEmpty();
    }


    public boolean place(GameAnimal animal) {
        if (!(grassLand_lower_left.precedes(animal.getPosition()) && grassLand_upper_right.follows(animal.getPosition()))) {
            throw new IllegalArgumentException(animal.getPosition().toString() + " is out of map bounds");
        }
        animal.addObserver(this);
        this.animals.add (animal);
        this.elementsMap.get(animal.getPosition()).add(animal);

        return true;
    }

    @Override
    public void changePosition(Vector2d oldPosition, Vector2d newPosition, GameAnimal animal) {
        elementsMap.get(oldPosition).remove(animal);
        elementsMap.get(newPosition).add(animal);
    }

    public Vector2d correctNewPosition(Vector2d newPosition){
        return new Vector2d((newPosition.getX() + this.width) % this.width,
                (newPosition.getY() + this.height) % this.height);
    }

     void removeDeadAnimals()
    {
        List<GameAnimal> animalsToRemove = new ArrayList<>();
        for (GameAnimal animal : this.animals) {
            if (animal.isDead()) {
                animalsToRemove.add(animal);
            }
        }

        for (GameAnimal animal : animalsToRemove) {
            animal.removeObserver(this);
            this.elementsMap.get(animal.getPosition()).remove(animal);
            this.animals.remove(animal);
        }

       /** for (GameAnimal animal : this.animals)
        {
            if (animal.isDead())
            {
                this.elementsMap.get(animal.getPosition()).remove(animal);
                this.animals.remove(animal);
            }
        }*/
    }

     Vector2d randomlyGetPosition()
    {
        int xCoord= ThreadLocalRandom.current().nextInt(this.width);
        int yCoord=ThreadLocalRandom.current().nextInt(this.height);
        return new Vector2d(xCoord, yCoord);
    }

    public Vector2d getBabyPosition(Vector2d parentPosition)
    {
        GameDirection randomBabyDirection;
        Vector2d babyOptionalPosition;
        int attempt=0;

        do{
            randomBabyDirection=GameDirection.randomlyGetDirection();
            babyOptionalPosition=correctNewPosition(parentPosition.add(randomBabyDirection.toUnitVector()));
            attempt++;

        }while(this.isOccupied(babyOptionalPosition) && attempt<8) ;

        if(attempt==8 && this.isOccupied(babyOptionalPosition))
        {
            randomBabyDirection=GameDirection.randomlyGetDirection();
            return correctNewPosition(parentPosition.add(randomBabyDirection.toUnitVector()));
        }
        return babyOptionalPosition;
    }

    private boolean isJungleFull()
    {
        Vector2d jungle_upper_right=new Vector2d(jungle_lower_left.x+jungleWidth, jungle_lower_left.y+jungleHeight);
        for(int i=jungle_lower_left.x; i<jungle_upper_right.x; i++)
        {
            for(int j=jungle_lower_left.y; j<jungle_upper_right.y; j++)
            {
               if((elementsMap.get(new Vector2d(i, j))).isEmpty())
                {
                   return false;
                }
            }
        }

        return true;
    }

    public boolean isInsideJungle(Vector2d position)
    {
        return (position.x>=jungle_lower_left.x && position.x<jungle_lower_left.x+jungleWidth && position.y>=jungle_lower_left.y && position.y<jungle_lower_left.y+jungleHeight);
    }


    public Vector2d getLowerLeft() {
        return grassLand_lower_left;
    }

    public Vector2d getUpperRight() {
        return grassLand_upper_right;
    }

    public List<GameAnimal> getGameAnimals() {return this.animals;
    }

    public int countAnimalsAtPosition(Vector2d position)
    {
        int animalCounter=0;
        List<Object> elements = new ArrayList<>();
        elements=this.elementsMap.get(position);
        for(Object element:elements)
        {
            if(element instanceof GameAnimal)
                animalCounter++;
        }
        return animalCounter;
    }

}

