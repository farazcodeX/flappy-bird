import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class main {

    public static void main(String[] args) {
        int width = 360;
        int height = 640;

        ImageIcon logo = new ImageIcon("C:\\Users\\TUF\\Desktop\\flappy bird baby\\flappy-bird\\src\\assets\\logo.png");

        JFrame frame = new JFrame("Flappy Bird");

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setIconImage(logo.getImage());

        Gamepanel gp = new Gamepanel();

        frame.add(gp);
        frame.pack();

        frame.setVisible(true);

    }
}