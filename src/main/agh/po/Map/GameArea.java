package agh.po.Map;
import agh.po.MapElements.GameAnimal;
import agh.po.Properties.GameDirection;
import agh.po.Properties.Vector2d;
import agh.po.MapElements.Plant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameArea implements IWorldMap, IPositionChangeObserver
{

    private final int width;
    private final int height;
    public final int jungleWidth;
    public final int jungleHeight;
    public final Vector2d grassLand_lower_left;
    public final Vector2d grassLand_upper_right;
    public final Vector2d jungle_lower_left;


    List<GameAnimal> animals = new ArrayList<>();
    Map<Vector2d, List<Object>> elementsMap = new HashMap<>();
    List <Vector2d> emptyPositionsInJungle =new ArrayList<>();
    List <Vector2d> emptyPositionsInGrassland =new ArrayList<>();

    public GameArea(int width, int height,double jungleRatio) {
        this.width=width;
        this.height=height;
        this.jungleWidth=(int) (width * jungleRatio);
        this.jungleHeight=(int) (height * jungleRatio);
        width=width-1;
        height=height-1;
        this.grassLand_lower_left = new Vector2d(0, 0);
        this.grassLand_upper_right = new Vector2d(width, height);
        this.jungle_lower_left=new Vector2d((this.width-this.jungleWidth)/2, (this.height-this.jungleHeight)/2);

        for (int i=0; i<=width; i++) {
            for (int j=0; j<=height; j++) {
                List<Object> newList = new ArrayList<>();
                elementsMap.put(new Vector2d(i, j), newList);
                emptyPositionsInGrassland.add(new Vector2d(i,j));
            }
        }


        for(int j=this.jungle_lower_left.x; j<this.jungle_lower_left.x+jungleWidth; j++)
        {
            for(int i=jungle_lower_left.y; i<this.jungle_lower_left.y+jungleHeight; i++)
            {
                emptyPositionsInGrassland.remove(new Vector2d(j,i));
                emptyPositionsInJungle.add(new Vector2d(j,i));
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
    public List<Object> objectsAt(Vector2d position) {
        return this.elementsMap.get(position);
    }

   @Override
    public boolean isOccupied(Vector2d position) {
        return !this.objectsAt(position).isEmpty();
    }

   @Override
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

    public Vector2d randomlyGetPosition()
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
        if(!isOccupied(babyOptionalPosition))
        {
            if(isInsideJungle(babyOptionalPosition))
            {
                emptyPositionsInJungle.remove(babyOptionalPosition);
            }
            else emptyPositionsInGrassland.remove(babyOptionalPosition);
        }
        return babyOptionalPosition;
    }

    public boolean isInsideJungle(Vector2d position)
    {
        return (position.x>=jungle_lower_left.x && position.x<jungle_lower_left.x+jungleWidth && position.y>=jungle_lower_left.y && position.y<jungle_lower_left.y+jungleHeight);
    }

    public List <GameAnimal> animalsAtPosition(Vector2d position)
    {
        List<GameAnimal> animals = new ArrayList<>();
        List<Object> elements = new ArrayList<>();
        elements=this.elementsMap.get(position);
        for(Object element:elements)
        {
            if(element instanceof GameAnimal)
                animals.add((GameAnimal) element);
        }
        return animals;
    }

    public List<GameAnimal> getGameAnimals() {return this.animals;}

    public boolean placePlant(Plant plant)
    {
        if (!(grassLand_lower_left.precedes(plant.getPosition()) && grassLand_upper_right.follows(plant.getPosition()))) {
            throw new IllegalArgumentException(plant.getPosition().toString() + " is out of map bounds");
        }
        this.elementsMap.get(plant.getPosition()).add(plant);
        return true;
    }



}


