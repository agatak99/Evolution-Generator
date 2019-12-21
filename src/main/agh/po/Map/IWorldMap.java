package agh.po.Map;


import agh.po.MapElements.GameAnimal;
import agh.po.Properties.Vector2d;

import java.util.List;

public interface IWorldMap extends IPositionChangeObserver {

        List<Object> objectsAt(Vector2d position);

        boolean isOccupied(Vector2d position);

        boolean place(GameAnimal animal);

        Vector2d getLowerLeft();

        Vector2d getUpperRight();

        Vector2d getJungleLowerLeft();

        boolean isInsideJungle(Vector2d position);

        Vector2d correctNewPosition(Vector2d newPosition);
}
