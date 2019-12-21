package agh.po.Properties;

import java.util.concurrent.ThreadLocalRandom;

public enum GameDirection
{
    NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;

    public GameDirection rotation( int rotationDegree)
    {
        if (rotationDegree < 0 || rotationDegree > 7) throw new IllegalArgumentException("The rotation degree should be a number between 0 and 7");
        int positionInEnum=this.ordinal();
        if(rotationDegree==0) return this;
        return GameDirection.values()[(positionInEnum + rotationDegree)%GameDirection.values().length];

    }


    public Vector2d toUnitVector() {
        switch (this) {
            case NORTH:
                return new Vector2d(0, 1);
            case NORTHEAST:
                return new Vector2d(1, 1);
            case EAST:
                return new Vector2d(1, 0);
            case SOUTHEAST:
                return new Vector2d(1, -1);
            case SOUTH:
                return new Vector2d(0, -1);
            case SOUTHWEST:
                return new Vector2d(-1, -1);
            case WEST:
                return new Vector2d(-1, 0);
            case NORTHWEST:
                return new Vector2d(-1, 1);
        }

        throw new IllegalArgumentException("Given direction doesn't exist");
    }

    @Override
    public String toString(){
        switch(this){
            case NORTH:
                return "North";
            case NORTHEAST:
                return "Northeast";
            case EAST:
                return "East";
            case SOUTHEAST:
                return "Southeast";
            case SOUTH:
                return "South";
            case SOUTHWEST:
                return "Southwest";
            case WEST:
                return "West";
            case NORTHWEST:
                return "Northwest";
            default:
                return null;
        }
    }

   public static GameDirection randomlyGetDirection() //used in case of creating a new GameAnimal
    {
        int i=ThreadLocalRandom.current().nextInt(8);
        return GameDirection.values()[i];
    }

}

