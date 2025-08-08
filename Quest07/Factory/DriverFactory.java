public class DriverFactory {
    public static Driver getDriver(String Type){
        if (Type == "Car"){
            return new CarDriver();
        } else if (Type == "Plane"){
            return new PlaneDriver();
        }
        return null;
    }    
}
