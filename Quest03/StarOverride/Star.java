import java.util.Objects;
public class Star extends CelestialObject{
    private double magnitude;
    
    public Star(){
        super();
        this.magnitude = 0.0;
    }
    public Star(String name,double x, double y, double z, double magnitude){
        super(name,x,y,z);
        this.magnitude = magnitude;
    }
    public double getMagnitude(){
        return magnitude;
    }

    public void setMagnitude(double magnitude){
        this.magnitude = magnitude;
    }

    @Override
    public String toString(){
        return String.format("%s shines at the %.3f magnitude", this.name, this.magnitude);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Star)) return false;
        Star other = (Star) obj;

        return super.equals(other) && Double.compare(this.magnitude, other.magnitude) == 0;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(this.getName(),this.getX(),this.getY(),this.getZ(), this.magnitude);
    }
}