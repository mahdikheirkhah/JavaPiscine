package jrawing;

import java.awt.Color;
import geometrical_shapes.*;

public class Main {
    public static void main(String[] args) {
        Image image = new Image(1000, 1000);
        Rectangle rectangle = new Rectangle(new Point(50, 50), new Point(300, 200));
        drawAndPrint(rectangle, image);
        Line line = new Line(new Point(900, 200), new Point(850, 850));
        drawAndPrint(line, image);
        Triangle triangle = new Triangle(new Point(100, 100), new Point(900, 900), new Point(100, 900));
        drawAndPrint(triangle, image);

        // Bonus shapes
        Pentagon pentagon = new Pentagon(new Point(500, 500), 100);
        drawAndPrint(pentagon, image);;
        Cube cube = new Cube(new Point(700, 200), 100);
        drawAndPrint(cube, image);;

        for (int i = 0; i < 50; i++) {
            Circle circle = Circle.random(image.getWidth(), image.getHeight());
            circle.draw(image);
        }
        System.out.println("Drew 50 random colored circles.");

        image.save("image.png");
    }
    public static void drawAndPrint(Drawable shape, Displayable displayable) {
        shape.printInfo();
        shape.draw(displayable);
    }
}