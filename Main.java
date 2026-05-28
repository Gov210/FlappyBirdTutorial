//import classes
import javax.swing.*;

//main class
public class Main{
    public static void main(String[] args){
        //frame
        JFrame frame = new JFrame("Flappy Bird");

        //gamepanel
        GamePanel panel = new GamePanel();

        //add to main frame
        frame.add(panel);

        //screen
        frame.setSize(850,500);

        //exit and basic functions
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
