public class Car implements Transport {
    @Override
    public int getDistance(){
        return TransportFactory.getTransport("Car");
    }
}
