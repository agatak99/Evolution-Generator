package agh.po.MapElements;
import agh.po.Map.GameArea;
import agh.po.Map.IPositionChangeObserver;
import agh.po.Properties.GameDirection;
import agh.po.Properties.Genes;
import agh.po.Properties.Vector2d;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameAnimal{
    /** Każde zwierze ma określoną energię, która zmniejsza się automatycznie co dnia.
     * Atrybut energii mówi nam ile dni zostało danemu zwierzakowi, jeżeli nie znajdzie on wcześniej pożywienia
     * Znalezenienie i zjedzenie rośliny zwiększa poziom energii o pewną wartość
     * Musimy monitorować pozycję zwierzęcia
     * Musimy posiadać informację co do kierunku, w którym zwrócone jest zwierze
     * Istnieje 8 możliwych obrotów zwierzaka, o kąt 0 st, 45 st itd..
     * Przechowujemy 32 geny zwierzaka; 1 gen składa siię z liczby od 0 do 7. Geny świadczą o preferencjach do obrotu. Szansa wybrania danego obrotu jest proporcjonalna do liczby genów reprezentujących dany obrót
     * Rozmnażać się mogą tylko osobniki posiadające odpowiednią ilość energii (przynajmniej połowę energii startowej), tracą przy tym 1/4 swojej energii
     * W przypadku większej ilości zwierząt na jednej pozycji rozmnażają sie te z największą energią
     * Nowo powstały osobnik posiada takie same atrybuty, ale inne geny; jego kierunke jest losowy, a pozycja losowa wolna, sąsiednia
     * Zwierzęta rozmnażają się kiedy sa na tym samym polu
     * W przypadku większej ilości zwierząt na jednym polu, roślinę zjada osobnik posiadający wiecej energii, ale jeżeli > 1 posaida taką samą maksymalną energię to rośline dzielą między sobą
     * poczatkowa enrgia to startEnergy, energia tracona każdego dnia to moveEnergy, ilość eneergii zyskiwanej po zjedzeniu rośliny to plantEnergy*/

    /**Gen-32 liczby o wartościach od 0 do 7
     * Prawdopodobieństwo wystąpienia danego genu
     * Dzielenie genów na 3 grupy wg losowo wybranych miejsc (losowanie dwóch pozycji) + losowanie ile grup od danego rodzica
     * Dziecko otrzymuje 2 grupy genów od jednego rodzica i jedną grupę genów od drugiego
     * Na końcu wartości są porządkowane
     * Jeżeli jakikolwiek typ genu nie występuje, wybiera się losowo pozycję i zastępuje tym typem (Zwierze musi posiadać co najmniej jeden gen każdego obrotu)*/

    /**narodzone zwierze ma tylko połowę sumy energii rodzica*/

    private Vector2d position;
    private GameDirection direction;
    private double energy;
    private GameArea map;
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

    public Vector2d move()
    {
        Vector2d newPosition = position.add(this.direction.toUnitVector());
        newPosition = this.map.correctNewPosition(newPosition);
        this.changePosition(this.getPosition(), newPosition);
        this.position = newPosition;
        this.energy =reduceEnergy(GameArea.moveEnergy);
        return newPosition;

    }

    private boolean isAbleToReproduce()
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
        //tutaj czy bardziej już w GameArea
        this.energy+= plantEnergy;
    }

    private double reduceEnergy(double energy)
    { //tutaj czy bardziej już w GameArea
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
    {//spr czy potrzebne
        return this.direction;
    }

}


