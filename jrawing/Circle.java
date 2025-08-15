package jrawing;

import java.awt.Color;
import java.util.Random;

public class Circle implements Drawable {
    private final int radius;
    private final Point center;
    private final Color color;
    public Circle(Point center, int radius) {
        this.center = center;
        this.radius = radius;
        this.color = new Color(
            (int)(Math.random() * 256),
            (int)(Math.random() * 256),
            (int)(Math.random() * 256)
        ); // Random color for circles
    }
    public static Circle random(int maxX, int maxY) {
        Random rand = new Random();
        Point center = Point.random(maxX, maxY);
        int maxSize = Integer.max(maxX, maxY);
        int radius = rand.nextInt(maxSize) + 10; // Radius between 10 and 60
        return new Circle(center, radius);
    }
    @Override
    public void draw(Displayable displayable) {
        // Midpoint circle algorithm
        int x0 = center.getX();
        int y0 = center.getY();
        int x = radius;
        int y = 0;
        int err = 0;

        while (x >= y) {
            displayable.display(x0 + x, y0 + y, color);
            displayable.display(x0 + y, y0 + x, color);
            displayable.display(x0 - y, y0 + x, color);
            displayable.display(x0 - x, y0 + y, color);
            displayable.display(x0 - x, y0 - y, color);
            displayable.display(x0 - y, y0 - x, color);
            displayable.display(x0 + y, y0 - x, color);
            displayable.display(x0 + x, y0 - y, color);

            if (err <= 0) {
                y += 1;
                err += 2*y + 1;
            }
            if (err > 0) {
                x -= 1;
                err -= 2*x + 1;
            }
        }
    }

    @Override
    public Color getColor() {
        return color;
    }

}
