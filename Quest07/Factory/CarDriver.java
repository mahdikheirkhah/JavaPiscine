public class CarDriver extends Driver {
    @Override
    public Transport createTransport(){
        return new Car();
    }
}
