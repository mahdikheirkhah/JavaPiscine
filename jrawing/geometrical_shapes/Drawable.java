package geometrical_shapes;

import java.awt.Color;

public interface Drawable {
    void draw(Displayable displayable);
    Color getColor();

    // Bonus: default method
    default void printInfo() {
        System.out.println("Drawing a " + this.getClass().getSimpleName() + " with color " + getColor());
    }
}
