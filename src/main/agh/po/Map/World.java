package agh.po.Map;


import agh.po.Utilities.JSONReader;
import agh.po.Utilities.Visualization;

public class World {
    public static void main(String[] args){
        JSONReader jsonReader = new JSONReader("src/resources/parameters.json");
        Simulation simulation = new Simulation(jsonReader.getWidth(), jsonReader.getHeight(), jsonReader.getJungleRatio(),
                jsonReader.getPlantEnergy(), jsonReader.getMoveEnergy(), jsonReader.getStartEnergy(),jsonReader.getStartNumberOfAnimals() );
        Visualization visualization = new Visualization(simulation);
        visualization.startAnimation();
    }
}
//todo:dodaj `wyjątki!!!!