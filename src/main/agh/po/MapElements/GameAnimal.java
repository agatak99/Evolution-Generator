package agh.po.MapElements;

import agh.po.Map.GameArea;
import agh.po.Map.IPositionChangeObserver;
import agh.po.Map.Simulation;
import agh.po.Properties.GameDirection;
import agh.po.Properties.Genes;
import agh.po.Properties.Vector2d;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameAnimal{

    private Vector2d position;
    private GameDirection direction;
    private double energy;
    public final GameArea map;
    private final Genes genotype;
    public final double startEnergy;

    private List<IPositionChangeObserver> observers = new ArrayList<>();

    public GameAnimal( Vector2d position, GameArea map, int startEnergy)
    {
        this.position=position;
        this.direction=GameDirection.randomlyGetDirection();
        this.energy= startEnergy;
        this.map=map;
        this.genotype=new Genes();
        this.startEnergy=startEnergy;

    }

    public GameAnimal ( Vector2d position, GameAnimal parent1, GameAnimal parent2)
    {
        this.position=position;
        this.direction=GameDirection.randomlyGetDirection();
        this.energy= (0.25 * parent1.energy) +  (0.25 * parent2.energy);
        this.map=parent1.map;
        this.genotype=new Genes(parent1.genotype, parent2.genotype);
        this.startEnergy=parent1.startEnergy;
        parent1.reduceEnergy (0.25 * parent1.energy);
        parent2.reduceEnergy( 0.25 * parent2.energy);

    }
    @Override
    public String toString()
    {
        if(this.isDead()) return " \ud83d\udc80";
        return " \ud83d\udc7e";
    }

    public void turn()
    {
        this.direction=this.randomlyChangeDirection();
    }

    public void move()
    {
        Vector2d newPosition = position.add(this.direction.toUnitVector());
        newPosition = this.map.correctNewPosition(newPosition);
        this.changePosition(this.getPosition(), newPosition);
        this.position = newPosition;
        this.energy =reduceEnergy(Simulation.moveEnergy);
    }

    public boolean isAbleToReproduce()
    {
        return this.energy>=this.startEnergy/2;
    }

    public void reproduce(GameAnimal parent2)
    {

        if(this.isAbleToReproduce() && parent2.isAbleToReproduce())
        {
            Vector2d babyPosition=this.map.getBabyPosition(this.position);
            GameAnimal child= new GameAnimal (babyPosition, this, parent2);
            this.map.place(child);
        }
    }

    private GameDirection randomlyChangeDirection()
    {
        int index=ThreadLocalRandom.current().nextInt(32);
        int indexRotation=this.genotype.getGenAtIndex(index);
        return this.direction.rotation(indexRotation);
    }

    public void addPlantEnergy(double plantEnergy)
    {
        this.energy+= plantEnergy;
    }

    private double reduceEnergy(double energy)
    {
        return this.energy-= energy;
    }


    public boolean isDead()
    {
        return this.energy<=0;

    }

    private void changePosition(Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangeObserver observer : observers)
            observer.changePosition(oldPosition, newPosition, this);
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    public Vector2d getPosition()
    {
        return this.position;
    }

    public double getEnergy()
    {
        return this.energy;
    }

    public GameDirection getDirection()
    {
        return this.direction;
    }

}


