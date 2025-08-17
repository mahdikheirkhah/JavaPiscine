package geometrical_shapes;
import java.awt.Color;

public class Triangle implements Drawable {
    private final Point p1;
    private final Point p2;
    private final Point p3;
    private final Color color;

    public Triangle(Point p1, Point p2, Point p3){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.color = Color.GREEN;
    }
    @Override
    public void draw(Displayable displayable){
        Line L1 = new Line(p1, p2, this.color);
        Line L2 = new Line(p1, p3, this.color);
        Line L3 = new Line(p2, p3, this.color);
        L1.draw(displayable);
        L2.draw(displayable);
        L3.draw(displayable);
    }

    @Override
    public Color getColor() {
        return color;
    }

}
