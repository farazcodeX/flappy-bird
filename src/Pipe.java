import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Pipe {
    int x = Gamepanel.width;
    int y;
    int width = 64;
    int height;
    BufferedImage image;
    public boolean passed = false;
    public int speed = 7;

    public Pipe(BufferedImage image, int y, int height) {
        this.image = image;
        this.y = y;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void update() {
        x -= speed;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, x, y, width, height, null);

    }

    public boolean isOffScreen() {
        return x + width < 0;
    }

}
