package Jart;

import java.awt.Color;
import java.util.Random;

public class Point implements Drawable {
    private final int x;
    private final int y;
    private final Color color;
    public Point(int x, int y){
        this.x = x;
        this.y = y;
        this.color = Color.WHITE;
    }
    public static Point random(int maxX, int maxY) {
        Random rand = new Random();
        return new Point(rand.nextInt(maxX), rand.nextInt(maxY));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void draw(Displayable displayable) {
        displayable.display(x, y, color);
    }

    @Override
    public Color getColor() {
        return color;
    }
}
