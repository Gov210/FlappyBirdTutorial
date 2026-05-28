//import classes
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

//game panel class
public class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener{
    //random object
    Random rand = new Random();
    //image variables
    private Image bird;
    private Image pipe1;
    private Image pipe2;
    private Image pipe3;
    private Image pipe4;
    private Image pipe5;
    private Image pipe6;
    private Image cloud;

    //final variables
    private final int panelWidth = 850;
    private final int birdX = 50;
    private final int birdWidth = 55;
    private final int birdHeight = 55;
    private final int pipeHeight = 250;
    private final int pipeWidth = 50;
    private final double gravity = 0.2;

    //changing variables
    private double birdY = 50;
    private int pipeXSet1 = 1050;
    private int pipeXSet2 = 1400;
    private int pipeXSet3 = 1750;
    private int pipe1Y = rand.nextInt(100)+280;
    private int pipe2Y = rand.nextInt(130)-200;
    private int pipe3Y = rand.nextInt(100)+280;
    private int pipe4Y = rand.nextInt(130)-200;
    private int pipe5Y = rand.nextInt(100)+280;
    private int pipe6Y = rand.nextInt(130)-200;
    private double velocityY = 0;

    //timer
    private Timer timer;
    private int delay = 8;

    //score
    private int score = 0;
    private int highScore = 0;

    //booleans
    private boolean gameOver = false;
    private boolean debug = false;
    private boolean start = true;
    private boolean passed1 = false;
    private boolean passed2 = false;
    private boolean passed3 = false;

    //hitboxes
    private Rectangle birdRect = new Rectangle(birdX+8,(int)birdY+14,birdWidth-20,birdHeight-32);
    private Rectangle pipeRect1 = new Rectangle(pipeXSet1, pipe1Y, pipeWidth, pipeHeight);
    private Rectangle pipeRect2 = new Rectangle(pipeXSet1, pipe2Y, pipeWidth, pipeHeight);
    private Rectangle pipeRect3 = new Rectangle(pipeXSet2, pipe3Y, pipeWidth, pipeHeight);
    private Rectangle pipeRect4 = new Rectangle(pipeXSet2, pipe4Y, pipeWidth, pipeHeight);
    private Rectangle pipeRect5 = new Rectangle(pipeXSet3, pipe5Y, pipeWidth, pipeHeight);
    private Rectangle pipeRect6 = new Rectangle(pipeXSet3, pipe6Y, pipeWidth, pipeHeight);

    private JButton startButton;


    //constructor
    public GamePanel(){
        //must add to position button
        setLayout(null);
        //load images
        ImageIcon b = new ImageIcon(getClass().getResource("/bird1.png"));
        ImageIcon pBottom = new ImageIcon(getClass().getResource("/pipe1.png"));
        ImageIcon pTop = new ImageIcon(getClass().getResource("/pipe2.png"));
        ImageIcon c = new ImageIcon(getClass().getResource("/cloud.png"));

        //scale images
        bird = b.getImage().getScaledInstance(birdWidth,birdHeight, Image.SCALE_SMOOTH);
        pipe1 = pBottom.getImage().getScaledInstance(pipeWidth,pipeHeight, Image.SCALE_SMOOTH);
        pipe2 = pTop.getImage().getScaledInstance(pipeWidth,pipeHeight, Image.SCALE_SMOOTH);
        pipe3 = pBottom.getImage().getScaledInstance(pipeWidth,pipeHeight, Image.SCALE_SMOOTH);
        pipe4 = pTop.getImage().getScaledInstance(pipeWidth,pipeHeight, Image.SCALE_SMOOTH);
        pipe5 = pBottom.getImage().getScaledInstance(pipeWidth,pipeHeight, Image.SCALE_SMOOTH);
        pipe6 = pTop.getImage().getScaledInstance(pipeWidth,pipeHeight, Image.SCALE_SMOOTH);
        cloud = c.getImage().getScaledInstance(200,80,Image.SCALE_SMOOTH);

        //render images smoothly
        setDoubleBuffered(true);

        //bg
        setBackground(Color.decode("#87CEEB"));

        //start btn
        startButton = new JButton("Start");
        startButton.setBounds(335,250,150,50);

        //style button
        startButton.setBackground(Color.decode("#950606"));
        startButton.setForeground(Color.white);
        startButton.setFont(new Font("Arial", Font.BOLD,30));

        //add button to screen
        if(start){
            add(startButton);
        }

        //listeners
        addKeyListener(this);
        addMouseListener(this);
        startButton.addActionListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }

    //paintcomponent class
    @Override
    public void paintComponent(Graphics g){
        //clear old drawings
        super.paintComponent(g);

        //ground
        g.setColor(Color.decode("#56AE57"));
        g.fillRect(0,400,panelWidth, 100);

        //debugging
        if(debug){
            g.setColor(Color.RED);
            g.drawRect(birdX+8,(int)birdY+14,birdWidth-20,birdHeight-32);
            g.drawRect(pipeXSet1, pipe1Y, pipeWidth, pipeHeight);
            g.drawRect(pipeXSet1, pipe2Y, pipeWidth, pipeHeight);
            g.drawRect(pipeXSet2, pipe3Y, pipeWidth, pipeHeight);
            g.drawRect(pipeXSet2, pipe4Y, pipeWidth, pipeHeight);
            g.drawRect(pipeXSet3, pipe5Y, pipeWidth, pipeHeight);
            g.drawRect(pipeXSet3, pipe6Y, pipeWidth, pipeHeight);
        }

        //draw images
        //(imagename, x, y, (relative to pos))
        g.drawImage(cloud,50,50,this);
        g.drawImage(cloud,150,50,this);
        g.drawImage(cloud,500,150,this);
        g.drawImage(cloud,600,150,this);
        g.drawImage(bird, birdX, (int)birdY, this);
        g.drawImage(pipe1, pipeXSet1, pipe1Y, this);
        g.drawImage(pipe2, pipeXSet1, pipe2Y, this);
        g.drawImage(pipe3, pipeXSet2, pipe3Y, this);
        g.drawImage(pipe4, pipeXSet2, pipe4Y, this);
        g.drawImage(pipe5, pipeXSet3, pipe5Y, this);
        g.drawImage(pipe6, pipeXSet3, pipe6Y, this);

        //display score
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.BOLD, 30));
        g.drawString("Score: "+score,360,120 );

        //gameover screen
        if(gameOver){
            //screen
            g.setColor(Color.black);
            g.fillRect(0,0,panelWidth,500);

            //gameover text
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD,40));
            g.drawString("Game Over", 310, 180);

            //score
            g.setColor(Color.white);
            g.setFont(new Font("Arial",Font.BOLD, 30));
            g.drawString("Score: "+score,340,230 );

            //highscore
            if(score >= highScore){
                highScore = score;
                g.drawString("Highscore: "+highScore,315,270);
            } else{
                g.drawString("Highscore: "+highScore,315,270);
            }

            //restart key
            g.drawString("Press Enter to Restart", 250,350);

        }

        if(start){
            //screen
            g.setColor(Color.black);
            g.fillRect(0,0,panelWidth,500);

            //title
            g.setColor(Color.decode("#950606"));
            g.setFont(new Font("Arial",Font.BOLD,50));
            g.drawString("Flappy Bird", 270, 200);
        }
    }

    //********Necessary methods************
    @Override
    public void actionPerformed(ActionEvent e) {
        //activate button
        if(e.getSource() == startButton && start){
            start = false;
            startButton.setVisible(false);
            requestFocusInWindow();
        }
        if(!gameOver && !start){
            //move pipes
            movePipes();

            //birdYMovement
            birdYMovement();

            //score increase
            scoreIncrease();

            //update hitboxes
            updateHitboxes();

            //end game when pipes and bird collide
            if(birdRect.intersects(pipeRect1) || birdRect.intersects(pipeRect2) || birdRect.intersects(pipeRect3) || birdRect.intersects(pipeRect4) || birdRect.intersects(pipeRect5) || birdRect.intersects(pipeRect6)){
                gameOver = true;
            }

            //below/above
            if(birdY > 500 || birdY < -birdHeight){
                gameOver = true;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //bird jump
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -5;
        }

        //restart
        if(gameOver && e.getKeyCode()==KeyEvent.VK_ENTER){
            start = true;
            startButton.setVisible(true);
            resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    //***************

    //methods
    //move pipes
    public void movePipes(){
        //move pipe x-values
        pipeXSet1 -= 3;
        pipeXSet2 -= 3;
        pipeXSet3 -= 3;

        //reset pipes
        if(pipeXSet1 <= -pipeWidth){
            pipeXSet1 = 1050;
            pipe1Y = rand.nextInt(100)+280;
            pipe2Y = rand.nextInt(130)-200;
            //reset scoring
            passed1 = false;
        }

        if(pipeXSet2 <= -pipeWidth){
            pipeXSet2 = 1050;
            pipe3Y = rand.nextInt(100)+280;
            pipe4Y = rand.nextInt(130)-200;
            passed2 = false;
        }

        if(pipeXSet3 <= -pipeWidth){
            pipeXSet3 = 1050;
            pipe5Y = rand.nextInt(100)+280;
            pipe6Y = rand.nextInt(130)-200;
            passed3 = false;
        }
    }

    //bird y movement
    public void birdYMovement(){
        //apply gravity
        velocityY += gravity;

        //cap falling speed
        if(velocityY > 5){
            velocityY = 5;
        }

        //add velocity to birdY
        birdY += velocityY;
    }

    //update hitboxes
    public void updateHitboxes(){
        birdRect.setBounds(birdX+8,(int)birdY+14,birdWidth-20,birdHeight-32);
        pipeRect1.setBounds(pipeXSet1, pipe1Y, pipeWidth, pipeHeight);
        pipeRect2.setBounds(pipeXSet1, pipe2Y, pipeWidth, pipeHeight);
        pipeRect3.setBounds(pipeXSet2, pipe3Y, pipeWidth, pipeHeight);
        pipeRect4.setBounds(pipeXSet2, pipe4Y, pipeWidth, pipeHeight);
        pipeRect5.setBounds(pipeXSet3, pipe5Y, pipeWidth, pipeHeight);
        pipeRect6.setBounds(pipeXSet3, pipe6Y, pipeWidth, pipeHeight);
    }

    //score increase
    public void scoreIncrease(){
        if(!passed1 && birdX > pipeXSet1 + pipeWidth/2){
            score++;
            passed1 = true;
        }
        if(!passed2 && birdX > pipeXSet2 + pipeWidth/2){
            score++;
            passed2 = true;
        }
        if(!passed3 && birdX > pipeXSet3 + pipeWidth/2){
            score++;
            passed3 = true;
        }
    }

    public void resetGame(){
        //object positions
        birdY = 50;
        pipeXSet1 = 1050;
        pipeXSet2 = 1400;
        pipeXSet3 = 1750;
        pipe1Y = rand.nextInt(100)+280;
        pipe2Y = rand.nextInt(130)-200;
        pipe3Y = rand.nextInt(100)+280;
        pipe4Y = rand.nextInt(130)-200;
        pipe5Y = rand.nextInt(100)+280;
        pipe6Y = rand.nextInt(130)-200;
        velocityY = 0;

        //score
        score = 0;

        //booleans
        gameOver = false;
        passed1 = false;
        passed2 = false;
        passed3 = false;
    }
}
