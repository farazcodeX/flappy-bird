import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gamepanel extends JPanel implements Runnable {
    public static int width = 360;
    public static int height = 640;

    private BufferedImage background, upperPipe, lowerPipe, pause, overPanel;
    public Bird bird = new Bird(width / 8, height / 2);
    private Thread mainThread;
    KeyHandler keyHandler = new KeyHandler();
    private boolean gameOver = false;

    // score stuff
    public int score = 0;

    // ok they are working ---------
    public final int pipeDelata = 35;
    private ArrayList<Pipe> pipes = new ArrayList<>();
    private int pipeMoveInterval = 4;
    public int pipeMoveCounter = 0;
    public int pipeSpawnCounter = 0;
    public int pipeSpawnInterval = 170;

    // --------
    private JButton tryAgainButton;

    public Gamepanel() {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.BLUE);

        setImages();
        this.setFocusable(true);
        this.addKeyListener(keyHandler);

        gameSetUp();

        // Try Again button setup
        tryAgainButton = new JButton();
        tryAgainButton.setBounds(95, 468, 170, 50); // Position based on resized image
        tryAgainButton.setOpaque(false);
        tryAgainButton.setContentAreaFilled(false);
        tryAgainButton.setBorderPainted(false);
        tryAgainButton.setFocusPainted(false);
        tryAgainButton.setVisible(false); // Hidden until game over

        tryAgainButton.addActionListener(e -> restartGame());

        this.setLayout(null); // Important for absolute positioning
        this.add(tryAgainButton);

    }

    private void gameSetUp() {
        mainThread = new Thread(this, "main");
        mainThread.start();
    }

    private void setImages() {
        try {
            background = ImageIO.read(getClass().getResource("/assets/flappybirdbg.png"));
            // bird = ImageIO.read(getClass().getResource("/assets/flappybird.png"));
            lowerPipe = ImageIO.read(getClass().getResource("/assets/bottompipe.png"));
            upperPipe = ImageIO.read(getClass().getResource("/assets/toppipe.png"));
            pause = ImageIO.read(getClass().getResource("/assets/ChatGPT Image Jul 15, 2025, 04_59_12 PM.png"));
            overPanel = ImageIO.read(getClass().getResource("/assets/ChatGPT Image Jul 17, 2025, 11_52_35 PM.png"));

            /*
             * background = ImageIO.read(new
             * File("C:\\Users\\TUF\\Desktop\\flappy bird baby\\flappy-bird\\src\\assets\\flappybirdbg.png"
             * ));
             * bird = ImageIO.read(new
             * File("C:\\Users\\TUF\\Desktop\\flappy bird baby\\flappy-bird\\src\\assets\\flappybird.png"
             * ));
             * lowerPipe = ImageIO.read(new
             * File("C:\\Users\\TUF\\Desktop\\flappy bird baby\\flappy-bird\\src\\assets\\bottompipe.png"
             * ));;
             * upperPipe = ImageIO.read(new
             * File("C:\\Users\\TUF\\Desktop\\flappy bird baby\\flappy-bird\\src\\assets\\bottompipe.png"
             * ));
             */
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    // tamp ------
    public void spawnPipes() {

        int pipeX = Gamepanel.width;
        int gapHeight = 150;

        Random r = new Random();
        int topPipeHeight = 60 + r.nextInt(240); // Top pipe height: 80 to 299

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

        tryAgainButton.setVisible(false);

        
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        if (gameOver) {
            g2d.drawImage(overPanel, 0, 0, width, height, null);
            tryAgainButton.setVisible(true);
            return;
        }

        g2d.drawImage(background, 0, 0, width, height, null);
        bird.draw(g2d);
        pipes.stream().forEach(p -> p.draw(g2d));
        writeDialogd(g2d);

        if (KeyHandler.pause) {
            // g2d.setColor(Color.orange);
            // g2d.fillRoundRect(100, 210, 160, 90, 10, 10);
            g2d.drawImage(pause, 100, 210, 160, 90, null);

        }
    }

    public void writeDialogd(Graphics2D g2d) {

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int boxX = 20;
        int boxY = 570;
        int boxWidth = 320;
        int boxHeight = 50;

        GradientPaint gradient = new GradientPaint(boxX, boxY, new Color(50, 50, 200, 180),
                boxX + boxWidth, boxY + boxHeight, new Color(100, 100, 255, 180));
        g2d.setPaint(gradient);
        g2d.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 25, 25);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 25, 25);

        g2d.setColor(Color.YELLOW);
        g2d.fillOval(boxX + 10, boxY + 10, 30, 30);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Consolas", Font.BOLD, 24));
        g2d.drawString("SCORE :", boxX + 50, boxY + 35);

        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Consolas", Font.BOLD, 26));
        g2d.drawString(String.valueOf(score / 2), boxX + 170, boxY + 35);
    }

    public void update() {

        if (gameOver == false && KeyHandler.pause == false) {
            ++pipeMoveCounter;
            ++pipeSpawnCounter;

            if (keyHandler.move) {
                bird.jump();
                keyHandler.move = false;
            }
            bird.update();

            if (pipeMoveCounter >= pipeMoveInterval) {
                Iterator<Pipe> it = pipes.iterator();
                while (it.hasNext()) {
                    Pipe p = it.next();
                    p.update();
                    if (p.isOffScreen()) {
                        it.remove();
                        ++score;
                        makeHarder();

                    }
                }
                pipeMoveCounter = 0;
            }

            if (pipeSpawnCounter >= pipeSpawnInterval) {
                spawnPipes();
                pipeSpawnCounter = 0;
            }

            checkCollision();
        }

    }

    public void makeHarder() {
        if (score >= 5) {
            pipeSpawnInterval = 130;
            pipeMoveInterval = 3;
        } else if (score >= 10) {
            pipeSpawnInterval = 110;
            pipeMoveInterval = 3;
        } else if (score >= 16) {
            pipeSpawnInterval = 80;
        }
    }

    @Override
    public void run() {
        // Accumolator

        double drawInterval = 1_000_000_000 / 60;// THIS is basicly our TARGET_TIME_TO_CYCLE
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (mainThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;// میخوایم ببینیم ایا دلتا به حد نسابش رسیدع یا نه
            if (delta >= 1) {
                update();//
                repaint();// THIS call paint method (PaintCompnent)
                delta--;
            }
            lastTime = currentTime;// update our lastTime
        }
    }

    // temp
    public void checkCollision() {

        int birdX = bird.getX();
        int birdY = bird.getY();
        int birdWidth = bird.getWidth();
        int birdHeight = bird.getHeight();

        if (birdY + birdHeight >= height) {
            gameOver = true;
            return;
        }

        if (birdY <= 0) {
            gameOver = true;
            return;
        }

        for (Pipe pipe : pipes) {
            int pipeX = pipe.x;
            int pipeY = pipe.y;
            int pipeWidth = pipe.width;
            int pipeHeight = pipe.height;

            boolean intersectsX = birdX + birdWidth > pipeX && birdX < pipeX + pipeWidth;
            boolean intersectsY = birdY + birdHeight > pipeY && birdY < pipeY + pipeHeight;

            if (intersectsX && intersectsY) {
                gameOver = true;
                return;
            }
        }
    }

    // temp }

    private void restartGame() {
        // Reset game state
        bird = new Bird(width / 8, height / 2);
        pipes.clear();
        score = 0;
        pipeSpawnInterval = 170;
        pipeMoveInterval = 4;
        pipeMoveCounter = 0;
        pipeSpawnCounter = 0;
        gameOver = false;
        tryAgainButton.setVisible(false); // Hide button again
    }

}
