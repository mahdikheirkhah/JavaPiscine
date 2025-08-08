import java.util.List;
public interface NumericBaseObserver {
    public void updateState(int state);
    public List<String> getEvents();
    
}
