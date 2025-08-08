import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

public class HexaBaseObserver implements NumericBaseObserver {
    private List<String> events;
    
    public HexaBaseObserver(){
        events = new ArrayList<>();
    }
    public List<String> getEvents(){
        return events;
    }
    @Override
    public void updateState(int state){
        events.add(Integer.toHexString(state));
    }
}