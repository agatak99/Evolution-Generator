package agh.po.Map;
import agh.po.MapElements.GameAnimal;
import agh.po.Properties.Vector2d;

public interface IPositionChangeObserver {

    void changePosition(Vector2d oldPosition, Vector2d newPosition, GameAnimal animal);

}
