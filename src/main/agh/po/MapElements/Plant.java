package agh.po.MapElements;
import agh.po.Properties.Vector2d;

public class Plant{

    public Vector2d position;

    public Plant(Vector2d position)
    {
        this.position=position;
    }
   public Vector2d getPosition(){

        return this.position;
    }
    public String toString(){

        return " \ud83c\udf31";
    }

}