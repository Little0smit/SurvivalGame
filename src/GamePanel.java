import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    //FIELDS
    public static int WIDTH = 400;
    public static int HEIGHT = 400;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    private int FPS = 30;
    private double averageFPS;

    public static Player player;
    public static ArrayList<Bullet> bullets;

    //CONSTRUCTOR
    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
    }

    //FUNCTIONS
    public void addNotify(){
        super.addNotify();
        if(thread==null){
            thread = new Thread(this);
            thread.start();
        }
        addKeyListener(this);
    }

    public void run(){

        running = true;

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        player = new Player();
        bullets = new ArrayList<>();

        long startTime;
        long URDTimeMills;
        long waitTime;
        long totalTime = 0;


        int frameCount = 0;
        int maxFrameCount = 30;


        long targetTime = 1000/FPS;

        //GAME LOOP
        while (running){

            startTime = System.nanoTime();

            gameUpdate();
            gameRender();
            gameDraw();

            URDTimeMills = (System.nanoTime() - startTime)/1000000;

            waitTime = targetTime - URDTimeMills;

            try{
                Thread.sleep(waitTime);
            } catch (Exception e) {
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == maxFrameCount){
                averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }

    }

    private void gameUpdate() {
        player.update();
        for(int i =0;i<bullets.size();i++){
            boolean remove = bullets.get(i).update();
            if(remove){
                bullets.remove(i);
                i--;
            }
        }
    }

    private void gameRender() {
        g.setColor(new Color(0, 100,255));
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(Color.BLACK);
        g.drawString("FPS: " + averageFPS,10,10);
        g.drawString("Num bullets: " + bullets.size(),10,20);


        player.draw(g);

        player.update();
        for(Bullet b:bullets){
            b.draw(g);
        }
    }

    private void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image,0,0,null);
        g2.dispose();
    }

    public void keyTyped(KeyEvent key){}
    public void keyPressed(KeyEvent key){
        int keyCode = key.getKeyCode();
        if(keyCode == KeyEvent.VK_LEFT){
            player.setLeft(true);
        }
        if(keyCode == KeyEvent.VK_RIGHT){
            player.setRight(true);
        }
        if(keyCode == KeyEvent.VK_UP){
            player.setUp(true);
        }
        if(keyCode == KeyEvent.VK_DOWN){
            player.setDown(true);
        }
        if(keyCode == KeyEvent.VK_Z){
            player.setFiring(true);
        }
    }
    public void keyReleased(KeyEvent key){
        int keyCode = key.getKeyCode();
        if(keyCode == KeyEvent.VK_LEFT){
            player.setLeft(false);
        }
        if(keyCode == KeyEvent.VK_RIGHT){
            player.setRight(false);
        }
        if(keyCode == KeyEvent.VK_UP){
            player.setUp(false);
        }
        if(keyCode == KeyEvent.VK_DOWN){
            player.setDown(false);
        }
        if(keyCode == KeyEvent.VK_Z){
            player.setFiring(false);
        }
    }
}
