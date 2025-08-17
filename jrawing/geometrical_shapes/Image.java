package geometrical_shapes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Image implements Displayable {
    private final BufferedImage image;
    private final int width;
    private final int height;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Initialize with white background
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, Color.BLACK.getRGB());
            }
        }
    }

    @Override
    public void display(int x, int y, Color color) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            image.setRGB(x, y, color.getRGB());
        }
    }

    @Override
    public void save(String filename) {
        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (Exception e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
