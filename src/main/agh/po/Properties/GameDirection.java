package agh.po.Properties;

import java.util.concurrent.ThreadLocalRandom;

public enum GameDirection
{
    //kierunki + zwracanie nowego kierunku po obrocie
    N, NE, E, SE, S, SW, W, NW;

    public GameDirection rotation( int rotationDegree)
    {
        if (rotationDegree < 0 || rotationDegree > 7) throw new IllegalArgumentException("The rotation degree  should be a number between 0 and 7");
        int positionInEnum=this.ordinal();
        if(rotationDegree==0) return this;
        return GameDirection.values()[(positionInEnum + rotationDegree)%GameDirection.values().length];

    }


    public Vector2d toUnitVector() {
        switch (this) {
            case N:
                return new Vector2d(0, 1);
            case NE:
                return new Vector2d(1, 1);
            case E:
                return new Vector2d(1, 0);
            case SE:
                return new Vector2d(1, -1);
            case S:
                return new Vector2d(0, -1);
            case SW:
                return new Vector2d(-1, -1);
            case W:
                return new Vector2d(-1, 0);
            case NW:
                return new Vector2d(-1, 1);
        }

        throw new IllegalArgumentException("Given direction doesn't exist");
    }

    @Override
    public String toString(){
        switch(this){
            case N:
                return "North";
            case NE:
                return "Northeast";
            case E:
                return "East";
            case SE:
                return "Southeast";
            case S:
                return "South";
            case SW:
                return "Southwest";
            case W:
                return "West";
            case NW:
                return "Northwest";
            default:
                return null;
        }
    }

   public static GameDirection randomlyGetDirection() //used in case of creating new GameAnimal
    {
        int i=ThreadLocalRandom.current().nextInt(8);
        return GameDirection.values()[i];
    }

}

