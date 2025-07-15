import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    public static boolean move = false;
    public static boolean pause = false;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            move = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if(pause) {
                pause = false;
            } else {
                pause = true;
            }
        }

        
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    
}
