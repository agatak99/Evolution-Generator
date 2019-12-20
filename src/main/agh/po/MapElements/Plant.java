package agh.po.MapElements;
import agh.po.Properties.Vector2d;
/**Musimy monitorować pozycję roślin
 * Rosliny wyrastają w losowych miejscach, ich koncentracja jest więlsza w dżungli niż na stepach.
 * Każdego dnia w każdej strefie pojawia się się jedna nowa roślina; Pomyśleć o stworzeniu klasy abstrakcyjnej dla obszarów zielonych
 *Roślina  może wyrosnąć tylko na pustym polu*/


public class Plant{

    public Vector2d position;

    public Plant(Vector2d position)
    {
        this.position=position;
    }
    public Vector2d getPosition(){ //todo:sprawdź czy korzsytam wgl z tej metody

        return this.position;
    }
    public String toString(){

        return " \ud83c\udf31";
    }

}