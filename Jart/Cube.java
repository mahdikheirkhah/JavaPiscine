package Jart;

import java.awt.Color;

public class Cube implements Drawable {
    private final Point frontTopLeft;
    private final int size;
    private final Color color;

    public Cube(Point frontTopLeft, int size) {
        this.frontTopLeft = frontTopLeft;
        this.size = size;
        this.color = Color.BLACK;
    }

    @Override
    public void draw(Displayable displayable) {
        int x = frontTopLeft.getX();
        int y = frontTopLeft.getY();
        
        // Front face
        Point frontTopRight = new Point(x + size, y);
        Point frontBottomLeft = new Point(x, y + size);
        Point frontBottomRight = new Point(x + size, y + size);
        
        // Back face (shifted by size/2 for perspective)
        Point backTopLeft = new Point(x + size/2, y - size/2);
        Point backTopRight = new Point(x + size + size/2, y - size/2);
        Point backBottomLeft = new Point(x + size/2, y + size - size/2);
        Point backBottomRight = new Point(x + size + size/2, y + size - size/2);
        
        // Draw front face
        new Rectangle(frontTopLeft, frontBottomRight).draw(displayable);
        
        // Draw back face
        new Rectangle(backTopLeft, backBottomRight).draw(displayable);
        
        // Connect front to back
        new Line(frontTopLeft, backTopLeft).draw(displayable);
        new Line(frontTopRight, backTopRight).draw(displayable);
        new Line(frontBottomLeft, backBottomLeft).draw(displayable);
        new Line(frontBottomRight, backBottomRight).draw(displayable);
    }

    @Override
    public Color getColor() {
        return color;
    }
}