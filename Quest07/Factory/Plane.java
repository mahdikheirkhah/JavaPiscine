public class Plane implements Transport {
    @Override
    public int getDistance(){
        return TransportFactory.getTransport("Plane");
    }
}
