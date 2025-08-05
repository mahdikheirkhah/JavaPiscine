public class CelestialObject {
    double x;
    double y;
    double z;
    String name;
    public static final double KM_IN_ONE_AU = 150000000;
    public CelestialObject(){
        x = 0.0;
        y = 0.0;
        z = 0.0;
        name = "Soleil";
    }
    public CelestialObject(String name,double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }
    public String getName(){
        return name;
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public void setZ(double z){
        this.z = z;
    }
    public void setName(String name){
        this.name = name;
    }
    public static double getDistanceBetween(CelestialObject obj1, CelestialObject obj2){
        double dx = obj1.getX() - obj2.getX();
        double dy = obj1.getY() - obj2.getY();
        double dz = obj1.getZ() - obj2.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    public static double getDistanceBetweenInKm(CelestialObject obj1, CelestialObject obj2){
        return CelestialObject.getDistanceBetween(obj1, obj2) * CelestialObject.KM_IN_ONE_AU;
    }
}
