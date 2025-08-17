package geometrical_shapes;

import java.awt.Color;

public class Pentagon implements Drawable {
    private final Point[] points;
    private final Color color;

    public Pentagon(Point center, int radius) {
        this.color = Color.BLUE;
        this.points = new Point[5];
        
        
        double angle = 2 * Math.PI / 5;

        // Compute coordinates of 5 equally spaced vertices
        for (int i = 0; i < 5; i++) {
            int x = (int) (center.getX() + radius * Math.cos(i * angle - Math.PI/2));
            int y = (int) (center.getY() + radius * Math.sin(i * angle - Math.PI/2));
            points[i] = new Point(x, y);
        }
    }

    @Override
    public void draw(Displayable displayable) {
        // Connect each vertex to the next, wrapping around at the end
        for (int i = 0; i < 5; i++) {
            new Line(points[i], points[(i + 1) % 5], this.color).draw(displayable);
        }
    }

    @Override
    public Color getColor() {
        return color;
    }
}