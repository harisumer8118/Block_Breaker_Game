import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class BlockBreaker extends JPanel implements ActionListener {
    private Ball ball;
    private Paddle paddle;
    private List<Block> blocks;
    private Timer timer;
    private boolean gameRunning;
    private boolean paused;  
    private BufferedImage backgroundImage;
    private int panelWidth;
    private int panelHeight;

    public BlockBreaker() {
        initializeGame();
        timer = new Timer(10, this);
        timer.start();
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT && !paused) {
                    paddle.move(-15);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT && !paused) {
                    paddle.move(15);
                }
                if (e.getKeyCode() == KeyEvent.VK_P) { 
                    togglePause();
                }
            }
        });
        
        try {
            backgroundImage = ImageIO.read(new File("background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeGame() {
        ball = new Ball(400, 300, 20);
        paddle = new Paddle(100, 200, 20);
        blocks = new ArrayList<>();

        for (int i = 0; i < 8; i++) { 
            for (int j = 0; j < 20; j++) {
                blocks.add(new Block(j * 65 + 30, i * 30 + 50, 60, 20));
            }
        }
        gameRunning = true;
        paused = false;
    }

    private void togglePause() {
        paused = !paused;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, null);
        ball.draw(g);
        paddle.draw(g);
        for (Block block : blocks) {
            block.draw(g);
        }

        // Watermark Text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(255, 255, 255, 90)); 
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        FontMetrics fm = g2d.getFontMetrics();
        String watermarkText = "Game Design by Haris Umer";
        int textWidth = fm.stringWidth(watermarkText);
        int textHeight = fm.getHeight();
        int x = (panelWidth - textWidth) / 2;
        int y = (panelHeight - textHeight) / 2;
        g2d.drawString(watermarkText, x, y);

        if (!gameRunning) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String gameOverText = "Game Over! Press R to Retry";
            FontMetrics gameOverMetrics = g.getFontMetrics();
            int gameOverTextWidth = gameOverMetrics.stringWidth(gameOverText);
            int gameOverX = (panelWidth - gameOverTextWidth) / 2; 
            int gameOverY = panelHeight / 2 + 50;

            g.drawString(gameOverText, gameOverX, gameOverY);
        }

        // Centered Paused Text
        if (paused) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String pausedText = "Paused";
            int pausedTextWidth = g.getFontMetrics().stringWidth(pausedText);
            int pausedX = (panelWidth - pausedTextWidth) / 2;
            int pausedY = panelHeight / 2;
            g.drawString(pausedText, pausedX, pausedY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning && !paused) { 
            ball.move();
            if (ball.getBounds().intersects(paddle.getBounds())) {
                ball.reverseY();
            }
            for (Block block : blocks) {
                if (block.isVisible() && block.getBounds().intersects(ball.getBounds())) {
                    block.setVisible(false);
                    ball.reverseY();
                }
            }
            if (ball.getY() > panelHeight) {
                gameRunning = false;
            }
        }
        repaint();
    }

    public void retryGame() {
        initializeGame();
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Block Breaker");
        BlockBreaker game = new BlockBreaker();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        frame.setUndecorated(true);
        gd.setFullScreenWindow(frame);
        game.panelWidth = frame.getWidth();
        game.panelHeight = frame.getHeight();
        
        frame.setVisible(true);
        
        game.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!game.gameRunning && e.getKeyCode() == KeyEvent.VK_R) {
                    game.retryGame();
                }
            }
        });
    }

    class Ball {
        private int x, y, diameter;
        private int xSpeed, ySpeed;

        public Ball(int x, int y, int diameter) {
            this.x = x;
            this.y = y;
            this.diameter = diameter;
            this.xSpeed = 2;
            this.ySpeed = -2;
        }

        public void draw(Graphics g) {
            g.setColor(new Color(128, 0, 0));
            g.fillOval(x, y, diameter, diameter);
        }

        public void move() {
            x += xSpeed;
            y += ySpeed;

            if (x < 0 || x > panelWidth - diameter) {
                xSpeed = -xSpeed;
            }
            if (y < 0) {
                ySpeed = -ySpeed;
            }
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, diameter, diameter);
        }

        public int getY() {
            return y;
        }

        public void reverseY() {
            ySpeed = -ySpeed;
        }
    }

    class Paddle {
        private int x, width, height;
        private final int bottomGap = 30; 

        public Paddle(int x, int width, int height) {
            this.x = x;
            this.width = width;
            this.height = height;
        }

        public void draw(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(x, panelHeight - height - bottomGap, width, height);
        }

        public void move(int deltaX) {
            x += deltaX;
            if (x < 0) x = 0;
            if (x > panelWidth - width) x = panelWidth - width;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, panelHeight - height - bottomGap, width, height); // Update bounds for gap
        }
    }

    class Block {
        private int x, y, width, height;
        private boolean isVisible;

        public Block(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.isVisible = true;
        }

        public void draw(Graphics g) {
            if (isVisible) {
                g.setColor(new Color(65, 105, 225)); 
                g.fillRect(x, y, width, height);
            }
        }

        public void setVisible(boolean isVisible) {
            this.isVisible = isVisible;
        }

        public boolean isVisible() {
            return isVisible;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }
}
