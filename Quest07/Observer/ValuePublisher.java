import java.util.ArrayList;
import java.util.List;
public class ValuePublisher {
    private List<NumericBaseObserver> observers;
    public ValuePublisher(){
        observers = new ArrayList<>();
    }
    public void updateState(int value){
        for(NumericBaseObserver observer : observers){
            observer.updateState(value);
        }
    }
    public void subscribe(NumericBaseObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(NumericBaseObserver observer) {
        observers.remove(observer);
    }
}
