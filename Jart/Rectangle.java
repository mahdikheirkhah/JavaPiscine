package Jart;
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

    @Override
    public void draw(Displayable displayable) {
        int x1 = topLeft.getX();
        int y1 = topLeft.getY();
        int x2 = bottomRight.getX();
        int y2 = bottomRight.getY();

        // Draw four lines to form a rectangle
        Point topRight = new Point(x2, y1);
        Point bottomLeft = new Point(x1, y2);

        new Line(topLeft, topRight).draw(displayable);
        new Line(topRight, bottomRight).draw(displayable);
        new Line(bottomRight, bottomLeft).draw(displayable);
        new Line(bottomLeft, topLeft).draw(displayable);
    }

    @Override
    public Color getColor() {
        return color;
    }
}
