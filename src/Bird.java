import java.awt.Graphics2D;
import java.awt.font.ImageGraphicAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bird {

    public int x, y;
    public int birdWidth = 34;
    public int birdHeight = 24;
    public BufferedImage image;
    private float velocity = 0;
    private final float gravity = 0.25f;
    private final float jumpStrength = -5;

    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            image = ImageIO.read(getClass().getResource("/assets/flappybird.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void jump() {
        velocity = jumpStrength;
    }

    public void update() {
        velocity += gravity;
        y += velocity;

        if (y < 0)
            y = 0;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, x, y, birdWidth, birdHeight, null);

    }

}
