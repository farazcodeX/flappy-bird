import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class Gamepanel extends JPanel {
    public int width = 360;
    public int height = 640;

    public Gamepanel() {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.BLUE);
    }
    
}
