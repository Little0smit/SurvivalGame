import javax.swing.JFrame;


public class GameLoop {
    public static void main(String[] args) {

        JFrame window = new JFrame("Survival Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setContentPane(new GamePanel());

        window.pack();
        window.setVisible(true);
    }

}
