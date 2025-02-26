package gamePanel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class CampaignGamePanel extends JPanel implements ActionListener {

    private static final int GRID_WIDTH = 1000;
    private static final int GRID_HEIGHT = 600;
    private static final int BORDER_THICKNESS = 20; 
    private static final int SNAKE_SIZE = 20;
    
    private int snakeX = 40, snakeY = 40;
    private int direction = KeyEvent.VK_RIGHT;
    private int score = 0;
    private boolean gameOver = false;
    private ArrayList<int[]> snakeBody = new ArrayList<>();
    private ArrayList<int[]> enemies = new ArrayList<>();
    private int foodX, foodY;
    private Timer timer;
    private int level;
    
    public CampaignGamePanel(int level) {
        this.level = level;
        setPreferredSize(new Dimension(GRID_WIDTH, GRID_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        
        snakeBody.add(new int[]{snakeX, snakeY});
        generateFood();
        spawnEnemies();
        
        timer = new Timer(100, this);
        timer.start();
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) {
                    if (e.getKeyCode() == KeyEvent.VK_R) resetGame();
                    if (e.getKeyCode() == KeyEvent.VK_M) returnToMenu();
                    return;
                }
                updateDirection(e.getKeyCode());
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (gameOver) {
            displayGameOverMessage(g);
            return;
        }
        
        drawBorders(g);
        drawSnake(g);
        drawFood(g);
        drawEnemies(g);
        drawScore(g);
    }
    
    private void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);
        for (int[] segment : snakeBody) {
            g.fillRect(segment[0], segment[1], SNAKE_SIZE, SNAKE_SIZE);
        }
    }
    
    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(foodX, foodY, SNAKE_SIZE, SNAKE_SIZE);
    }
    
    private void drawEnemies(Graphics g) {
        g.setColor(Color.BLUE);
        for (int[] enemy : enemies) {
            g.fillRect(enemy[0], enemy[1], SNAKE_SIZE, SNAKE_SIZE);
        }
    }
    
    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 30);
    }

    private void displayGameOverMessage(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Game Over! Score: " + score, GRID_WIDTH / 4, GRID_HEIGHT / 2);
        g.drawString("Press 'R' to Restart or 'M' to Return to Menu", GRID_WIDTH / 4, GRID_HEIGHT / 2 + 40);
    }

    private void updateDirection(int key) {
        if (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) direction = KeyEvent.VK_UP;
        if (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) direction = KeyEvent.VK_DOWN;
        if (key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) direction = KeyEvent.VK_LEFT;
        if (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) direction = KeyEvent.VK_RIGHT;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;
        
        moveSnake();
        
        // Collision checks
        if (checkCollisions()) {
            gameOver = true;
            timer.stop();
            repaint();
        }
        
        repaint();
    }
    
    private void moveSnake() {
        int newSnakeX = snakeX;
        int newSnakeY = snakeY;
        if (direction == KeyEvent.VK_UP) newSnakeY -= SNAKE_SIZE;
        if (direction == KeyEvent.VK_DOWN) newSnakeY += SNAKE_SIZE;
        if (direction == KeyEvent.VK_LEFT) newSnakeX -= SNAKE_SIZE;
        if (direction == KeyEvent.VK_RIGHT) newSnakeX += SNAKE_SIZE;
        
        snakeX = newSnakeX;
        snakeY = newSnakeY;
        
        snakeBody.add(0, new int[]{snakeX, snakeY});
        if (!hasEatenFood()) snakeBody.remove(snakeBody.size() - 1);
    }

    private boolean hasEatenFood() {
        return snakeX < foodX + SNAKE_SIZE && snakeX + SNAKE_SIZE > foodX &&
               snakeY < foodY + SNAKE_SIZE && snakeY + SNAKE_SIZE > foodY;
    }
    
    private boolean checkCollisions() {
        // Check wall collisions
        if (isCollisionWithWalls()) return true;

        // Check self collision
        if (checkSelfCollision()) return true;

        // Check enemy collision
        for (int[] enemy : enemies) {
            if (snakeX < enemy[0] + SNAKE_SIZE && snakeX + SNAKE_SIZE > enemy[0] &&
                snakeY < enemy[1] + SNAKE_SIZE && snakeY + SNAKE_SIZE > enemy[1]) {
                return true;
            }
        }
        
        // Check if snake ate food
        if (hasEatenFood()) {
            score += 10;
            generateFood();
        }

        return false;
    }
    
    private boolean isCollisionWithWalls() {
        return (snakeX < BORDER_THICKNESS || snakeX >= GRID_WIDTH - BORDER_THICKNESS ||
                snakeY < BORDER_THICKNESS || snakeY >= GRID_HEIGHT - BORDER_THICKNESS);
    }

    private boolean checkSelfCollision() {
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeX == snakeBody.get(i)[0] && snakeY == snakeBody.get(i)[1]) return true;
        }
        return false;
    }
    
    private void generateFood() {
        Random rand = new Random();
        foodX = rand.nextInt(GRID_WIDTH / SNAKE_SIZE) * SNAKE_SIZE;
        foodY = rand.nextInt(GRID_HEIGHT / SNAKE_SIZE) * SNAKE_SIZE;
    }
    
    private void spawnEnemies() {
        enemies.clear();
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            enemies.add(new int[]{rand.nextInt(GRID_WIDTH / SNAKE_SIZE) * SNAKE_SIZE,
                                  rand.nextInt(GRID_HEIGHT / SNAKE_SIZE) * SNAKE_SIZE});
        }
    }
    
    private void resetGame() {
        snakeX = 40;
        snakeY = 40;
        direction = KeyEvent.VK_RIGHT;
        snakeBody.clear();
        snakeBody.add(new int[]{snakeX, snakeY});
        score = 0;
        enemies.clear();
        gameOver = false;
        generateFood();
        spawnEnemies();
        if (!timer.isRunning()) timer.start();
        repaint();
    }

    private void returnToMenu() {
        JPanel mainPanel = (JPanel)getParent();
        CardLayout cardLayout = (CardLayout)mainPanel.getLayout();
        cardLayout.show(mainPanel, "menu");
    }

    private void drawBorders(Graphics g) {
        g.setColor(Color.RED);
        if (level == 1) {
            g.fillRect(0, 0, GRID_WIDTH, BORDER_THICKNESS); // Top border
            g.fillRect(0, 0, BORDER_THICKNESS, GRID_HEIGHT); // Left border
            g.fillRect(GRID_WIDTH - BORDER_THICKNESS, 0, BORDER_THICKNESS, GRID_HEIGHT); // Right border
            g.fillRect(0, GRID_HEIGHT - BORDER_THICKNESS, GRID_WIDTH, BORDER_THICKNESS); // Bottom border
        } else if (level == 2) {
            g.fillRect(0, GRID_HEIGHT / 2, GRID_WIDTH, BORDER_THICKNESS); // Horizontal crossbar
            g.fillRect(GRID_WIDTH / 2, 0, BORDER_THICKNESS, GRID_HEIGHT); // Vertical crossbar
        } else if (level == 3) {
            g.fillRect(100, 100, GRID_WIDTH - 200, BORDER_THICKNESS); // Horizontal wall
            g.fillRect(100, GRID_HEIGHT / 2, GRID_WIDTH - 200, BORDER_THICKNESS); // Middle horizontal wall
            g.fillRect(100, 100, BORDER_THICKNESS, GRID_HEIGHT / 2); // Left vertical wall
            g.fillRect(GRID_WIDTH - 100, 100, BORDER_THICKNESS, GRID_HEIGHT / 2); // Right vertical wall
        }
    }
}