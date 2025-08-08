public class TransportFactory {
    public static int getTransport(String Type){
        if (Type == "Car"){
            return 600;
        } else if (Type == "Plane"){
            return 10000;
        }
        return -1;
    }
}
