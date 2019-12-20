package agh.po.Map;


import agh.po.Properties.Vector2d;

//todo: dodać pozostałe funkcje
public interface IWorldMap extends IPositionChangeObserver {

        void run();

        boolean isOccupied(Vector2d position);

       // Object objectsAt(Vector2d position);



}
