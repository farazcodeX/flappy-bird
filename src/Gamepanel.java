import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Gamepanel extends JPanel implements Runnable{
    public static int width = 360;
    public static int height = 640;

    private BufferedImage background, upperPipe, lowerPipe;
    public Bird bird = new Bird(width/8, height/2);
    private Thread mainThread;
    KeyHandler keyHandler = new KeyHandler();

    // temp ---------
    public final int pipeDelata = 35;
    int velocityX = -5;
    private ArrayList<Pipe> pipes = new ArrayList<>();
    private int pipeMoveInterval = 60;
    public int pipeMoveCounter = 0;
    public int pipeSpawnCounter = 0;
    public int pipeSpawnInterval = 100;

    // --------

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

    // tamp ------
    public void spawnPipes() {
        /* 
        int rand = random();
        int gap = 40+(rand-1)*pipeDelata;
        Pipe topP = new Pipe(upperPipe, 0, gap);
        Pipe buttomP = new Pipe(lowerPipe, height, height-gap);

        pipes.add(buttomP);
        pipes.add(topP);*/

        int pipeX = Gamepanel.width;
        int gapHeight = 150;

        int topPipeHeight = 100 + random() * 5;
        int bottomPipeY = topPipeHeight + gapHeight;
        int bottomPipeHeight = Gamepanel.height - bottomPipeY;

        Pipe topP = new Pipe(upperPipe, 0, topPipeHeight);
        Pipe bottomP = new Pipe(lowerPipe, bottomPipeY, bottomPipeHeight);

        pipes.add(topP);
        pipes.add(bottomP);

    } 
    public int random() {
        Random random = new Random();
        return random.nextInt(16) + 1;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        g2d.drawImage(background, 0 ,0, width, height, null);
        bird.draw(g2d);
        pipes.stream().forEach(p -> p.draw(g2d));
    }

    public void update() {

        ++pipeMoveCounter;
        ++pipeSpawnCounter;

        if(keyHandler.move) {
            bird.jump();
            keyHandler.move = false;
        }
        bird.update();
        

        if(pipeMoveCounter >= pipeMoveInterval) {
            for(Pipe p : pipes) {
                p.update();
                if(p.isOffScreen()) {
                    pipes.remove(p);
                }

            }
            pipeMoveCounter = 0;
        }

        if(pipeSpawnCounter >= pipeSpawnInterval) {
            spawnPipes();
            pipeSpawnCounter = 0;
        }

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
