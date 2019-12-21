package agh.po.Utilities;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JSONReader {
    @Override
    public String toString() {
        return "Parameters{" +
                "width=" + width +
                ", height=" + height +
                ", startEnergy=" + startEnergy +
                ", moveEnergy=" + moveEnergy +
                ", plantEnergy=" + plantEnergy +
                ", jungleRatio=" + jungleRatio +
                ", startNumberOfAnimals=" + startNumberOfAnimals +
                '}';
    }

    private int width;
    private int height;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private double jungleRatio;
    private int startNumberOfAnimals;


    public JSONReader(String path) {
        try
        {
            Object object = new JSONParser().parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) object;

            this.width = Math.toIntExact((Long) jsonObject.get("width"));
            this.height = Math.toIntExact((Long) jsonObject.get("height"));
            this.startEnergy = Math.toIntExact((Long) jsonObject.get("startEnergy"));
            this.moveEnergy = Math.toIntExact((Long) jsonObject.get("moveEnergy"));
            this.plantEnergy = Math.toIntExact((Long) jsonObject.get("plantEnergy"));
            this.jungleRatio = (Double) jsonObject.get("jungleRatio");
            this.startNumberOfAnimals = Math.toIntExact((Long) jsonObject.get("startNumberOfAnimals"));

        }   catch (IOException | ParseException | NullPointerException | IllegalArgumentException ex){
            if(ex instanceof FileNotFoundException) System.out.println("File not found!");
            if(ex instanceof ParseException) System.out.println("ParseException");
            if(ex instanceof IllegalArgumentException) System.out.println(ex.toString());
            }
        }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public double getJungleRatio() {
        return jungleRatio;
    }

    public int getStartNumberOfAnimals() {
        return startNumberOfAnimals;
    }

}
