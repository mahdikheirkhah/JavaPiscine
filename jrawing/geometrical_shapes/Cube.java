package geometrical_shapes;

import java.awt.Color;

public class Cube implements Drawable {
    private final Point frontTopLeft;
    private final int size;
    private final Color color;

    public Cube(Point frontTopLeft, int size) {
        this.frontTopLeft = frontTopLeft;
        this.size = size;
        this.color = Color.PINK;
    }

    @Override
    public void draw(Displayable displayable) {
        int x = frontTopLeft.getX();
        int y = frontTopLeft.getY();
        
        // Define the points of the front face 
        Point frontTopRight = new Point(x + size, y);
        Point frontBottomLeft = new Point(x, y + size);
        Point frontBottomRight = new Point(x + size, y + size);
        
        // Define the points of the back face, shifted by size/2 for perspective
        Point backTopLeft = new Point(x + size/2, y - size/2);
        Point backTopRight = new Point(x + size + size/2, y - size/2);
        Point backBottomLeft = new Point(x + size/2, y + size - size/2);
        Point backBottomRight = new Point(x + size + size/2, y + size - size/2);
        
        // Draw the front face
        new Rectangle(frontTopLeft, frontBottomRight, this.color).draw(displayable);
        
        // Draw the back face
        new Rectangle(backTopLeft, backBottomRight, this.color).draw(displayable);
        
        // Connect corresponding corners between front and back face
        new Line(frontTopLeft, backTopLeft, this.color).draw(displayable);
        new Line(frontTopRight, backTopRight, this.color).draw(displayable);
        new Line(frontBottomLeft, backBottomLeft, this.color).draw(displayable);
        new Line(frontBottomRight, backBottomRight, this.color).draw(displayable);
    }

    @Override
    public Color getColor() {
        return color;
    }
}