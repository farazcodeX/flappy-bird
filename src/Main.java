import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        int width = 360;
        int height = 640;

        JFrame frame = new JFrame("Flappy Bird");

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        Gamepanel gp = new Gamepanel();

        frame.add(gp);

        frame.setVisible(true);

    }
}