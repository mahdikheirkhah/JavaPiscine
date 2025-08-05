import java.util.ArrayList;
import java.util.List;

public class Galaxy {
    private List<CelestialObject> celestialObjects;

    public Galaxy(){
        super();
        celestialObjects = new ArrayList<CelestialObject>();
    }
    public List<CelestialObject> getCelestialObjects(){
        return celestialObjects;
    }

    public void addCelestialObject(CelestialObject obj){
        if (obj == null) {
            return;
        }
        celestialObjects.add(obj);
    }
}
