import java.util.Objects;

public class Planet extends CelestialObject {
    private Star centerStar;
    public Planet(){
        super();
        centerStar = new Star();
    }
    public Planet(String name,double x, double y, double z, Star centerStar){
        super(name, x, y, z);
        this.centerStar = centerStar; 
    }

    public Star getCenterStar(){
        return centerStar;
    }

    public void setCenterStar(Star centeStar){
        this.centerStar = centeStar;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Planet)) return false;
        Planet other = (Planet) obj;

        return super.equals(other) && this.centerStar.equals(other.centerStar) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), centerStar.hashCode());
    }

    @Override
    public String toString(){
        return String.format("%s circles around %s at the %.3f AU", this.getName(), centerStar.getName(),  CelestialObject.getDistanceBetween(this, centerStar));
    }


}
