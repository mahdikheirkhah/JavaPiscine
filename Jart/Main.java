package Jart;

import java.awt.Color;

interface Displayable {
    void display(int x, int y, Color color);
    void save(String string);
}

interface Drawable {
    void draw(Displayable displayable);
    Color getColor();
}

public class Main {
    public static void main(String[] args) {
        Image image = new Image(1000, 1000);
        Rectangle rectangle = new Rectangle(new Point(50, 50), new Point(300, 200));
        rectangle.draw(image);
        Triangle triangle = new Triangle(new Point(100, 100), new Point(900, 900), new Point(100, 900));
        triangle.draw(image);

        for (int i = 0; i < 50; i++) {
            Circle circle = Circle.random(image.getWidth(), image.getHeight());
            circle.draw(image);
        }
        
        // Bonus shapes
        Pentagon pentagon = new Pentagon(new Point(500, 500), 100);
        pentagon.draw(image);
        
        Cube cube = new Cube(new Point(700, 200), 100);
        cube.draw(image);
        
        image.save("Jart/image.png");
    }
}