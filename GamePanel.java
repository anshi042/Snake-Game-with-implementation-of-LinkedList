import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final int TILE_SIZE = 25;
    private final int WIDTH = 20;
    private final int HEIGHT = 20;
    private Timer timer;
    private LinkedList snake;
    private int foodX;
    private int foodY;
    private char direction = 'R'; // 'U', 'D', 'L', 'R'
    private boolean running = true;
    private boolean paused = false;
    private int score = 0;
    private boolean grow = false;

    private Font gameFont = new Font("Helvetica", Font.BOLD, 18);
    private Color snakeColor = new Color(0, 153, 0);
    private Color foodColor = new Color(204, 0, 0);
    private BufferedImage backgroundImage;

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("path/to/your/background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        initGame();
    }

    private void initGame() {
        snake = new LinkedList(WIDTH / 2, HEIGHT / 2);
        snake.addNode(WIDTH / 2 - 1, HEIGHT / 2);
        snake.addNode(WIDTH / 2 - 2, HEIGHT / 2);
        placeFood();
        score = 0;
        direction = 'R';
        running = true;
        paused = false;
        grow = false;
        timer = new Timer(250, this);
        timer.start();
    }

    private void placeFood() {
        foodX = (int) (Math.random() * WIDTH);
        foodY = (int) (Math.random() * HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
        if (running) {
            draw(g);
        } else {
            showGameOver(g);
        }
    }

    private void draw(Graphics g) {
        g.setColor(foodColor);
        g.fillRect(foodX * TILE_SIZE, foodY * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        Node current = snake.head;
        while (current != null) {
            g.setColor(snakeColor);
            g.fillRect(current.x * TILE_SIZE, current.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            current = current.next;
        }

        g.setColor(Color.WHITE);
        g.setFont(gameFont);
        g.drawString("Score: " + score, 10, 20);
    }

    private void showGameOver(Graphics g) {
        String msg = "Game Over";
        String scoreMsg = "Score: " + score;
        String restartMsg = "Press R to Restart";
        Font font = new Font("Helvetica", Font.BOLD, 30);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(msg, (getWidth() - metrics.stringWidth(msg)) / 2, getHeight() / 2 - 50);
        g.drawString(scoreMsg, (getWidth() - metrics.stringWidth(scoreMsg)) / 2, getHeight() / 2);
        g.drawString(restartMsg, (getWidth() - metrics.stringWidth(restartMsg)) / 2, getHeight() / 2 + 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !paused) {
            if (!checkCollision()) {
                move();
                checkFood();
            } else {
                running = false;
            }
        }
        repaint();
    }

    private void move() {
        int newX = snake.head.x;
        int newY = snake.head.y;

        switch (direction) {
            case 'U': newY--; break;
            case 'D': newY++; break;
            case 'L': newX--; break;
            case 'R': newX++; break;
        }

        snake.move(newX, newY, grow);
        grow = false; // Reset grow after moving
    }

    private void checkFood() {
        if (snake.head.x == foodX && snake.head.y == foodY) {
            grow = true;
            placeFood();
            score++;
        }
    }

    private boolean checkCollision() {
        Node current = snake.head.next;
        while (current != null) {
            if (current.x == snake.head.x && current.y == snake.head.y) {
                return true;
            }
            current = current.next;
        }

        if (snake.head.x < 0 || snake.head.x >= WIDTH || snake.head.y < 0 || snake.head.y >= HEIGHT) {
            return true;
        }

        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (running) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_P:
                    paused = !paused;
                    break;
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                initGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }
}
