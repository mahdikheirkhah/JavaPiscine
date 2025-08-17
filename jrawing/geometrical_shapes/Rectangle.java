package geometrical_shapes;
import java.awt.Color;

public class Rectangle implements Drawable {
    private final Point topLeft;
    private final Point bottomRight;
    private final Color color;

    public Rectangle(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.color = Color.WHITE;
    }
    public Rectangle(Point topLeft, Point bottomRight, Color color) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.color = color;
    }

    @Override
    public void draw(Displayable displayable) {
        // Extract coordinates
        int x1 = topLeft.getX();
        int y1 = topLeft.getY();
        int x2 = bottomRight.getX();
        int y2 = bottomRight.getY();

        // Comput the other two corners
        Point topRight = new Point(x2, y1);
        Point bottomLeft = new Point(x1, y2);

        // Draw four sides 
        new Line(topLeft, topRight, this.color).draw(displayable);
        new Line(topRight, bottomRight, this.color).draw(displayable);
        new Line(bottomRight, bottomLeft, this.color).draw(displayable);
        new Line(bottomLeft, topLeft, this.color).draw(displayable);
    }

    @Override
    public Color getColor() {
        return color;
    }
}
