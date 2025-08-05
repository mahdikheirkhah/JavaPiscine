import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Galaxy {
    private List<CelestialObject> celestialObjects;


    public Galaxy() {
        celestialObjects = new ArrayList<>();
    }

    public List<CelestialObject> getCelestialObjects() {
        return celestialObjects;
    }

    public void addCelestialObject(CelestialObject obj) {
        if (obj != null) {
            celestialObjects.add(obj);
        }
    }

    public Map<String, Integer> computeMassRepartition() {
        Map<String, Integer> massSum = new HashMap<>();
        massSum.put("Star", 0);
        massSum.put("Planet", 0);
        massSum.put("Other", 0);
        for (CelestialObject obj : celestialObjects) {
            if (obj instanceof Star) {
                massSum.put("Star", massSum.get("Star") + obj.getMass());
            } else if (obj instanceof Planet) {
                massSum.put("Planet", massSum.get("Planet") + obj.getMass());
            } else {
                massSum.put("Other", massSum.get("Other") + obj.getMass());
            }
        }
        return massSum;
    }
}

