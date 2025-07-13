import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Gamepanel extends JPanel implements Runnable{
    public int width = 360;
    public int height = 640;

    private BufferedImage background, upperPipe, lowerPipe;
    public Bird bird = new Bird(width/8, height/2);
    private Thread mainThread;
    KeyHandler keyHandler = new KeyHandler();

    public Gamepanel() {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.BLUE);

        setImages();   
        this.setFocusable(true);
        this.addKeyListener(keyHandler);

        gameSetUp();
    }

    private void gameSetUp() {
        mainThread = new Thread(this, "main");
        mainThread.start();
    }

    private void setImages() {
        try {
            background = ImageIO.read(getClass().getResource("/assets/flappybirdbg.png"));
            //bird = ImageIO.read(getClass().getResource("/assets/flappybird.png"));
            lowerPipe = ImageIO.read(getClass().getResource("/assets/bottompipe.png"));
            upperPipe = ImageIO.read(getClass().getResource("/assets/toppipe.png"));

            /*  background = ImageIO.read(new File("C:\\Users\\TUF\\Desktop\\flappy bird baby\\flappy-bird\\src\\assets\\flappybirdbg.png"));
            bird = ImageIO.read(new File("C:\\Users\\TUF\\Desktop\\flappy bird baby\\flappy-bird\\src\\assets\\flappybird.png"));
            lowerPipe = ImageIO.read(new File("C:\\Users\\TUF\\Desktop\\flappy bird baby\\flappy-bird\\src\\assets\\bottompipe.png"));;
            upperPipe = ImageIO.read(new File("C:\\Users\\TUF\\Desktop\\flappy bird baby\\flappy-bird\\src\\assets\\bottompipe.png"));*/ 
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.drawImage(background, 0 ,0, width, height, null);
        bird.draw(g2d);
    }

    public void update() {
        if(keyHandler.move) {
            bird.jump();
            keyHandler.move = false;
        }
        bird.update();


    }

    @Override
    public void run() {
        // Accumolator
                
        double drawInterval = 1_000_000_000/60;// THIS is basicly our TARGET_TIME_TO_CYCLE
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        while ( mainThread != null)
        {
            currentTime = System.nanoTime();
             delta += (currentTime-lastTime)/drawInterval;// میخوایم ببینیم ایا دلتا به حد نسابش رسیدع یا نه
             if (delta >= 1)
             {
              update();//
              repaint();// THIS call paint method (PaintCompnent)
              delta--;
             }
             lastTime = currentTime;// update our lastTime 
        }
    }    
}
