package gamePanel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JPanel;

import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int GRID_WIDTH = 1000;
    private final int GRID_HEIGHT = 600;
    
    private int snakeWidth = 20;
    private int snakeHeight = 20;
    private ArrayList<int[]> snakeBody = new ArrayList<>();
    private int direction = KeyEvent.VK_RIGHT; // Direzione iniziale
    private Timer timer;
    private int foodX, foodY;
    
    // Posizione iniziale della testa del serpente (allineata alla griglia)
    private int snakeX = 40;
    private int snakeY = 40;
    
    // Variabili per punteggio e stato di gioco
    private int score = 0;
    private boolean gameOver = false;
    
    // Liste di nemici e proiettili
    private ArrayList<int[]> enemies = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private int bulletSize = 10;
    private int bulletSpeed = 40;
    
    // Parametro per la difficoltà (es. "Facile", "Medio", "Difficile", ecc.)
    private String difficulty;
    
    // Classe interna per rappresentare i proiettili
    private class Bullet {
        int x, y, direction;
        public Bullet(int x, int y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
    }
    
    // Costruttore per la modalità Classica (campi relativi a campagna sono rimossi)
    public GamePanel(String difficulty) {
        this.difficulty = difficulty;
        setPreferredSize(new Dimension(1000, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        
        // Inizializza il corpo del serpente con la testa
        snakeBody.add(new int[]{snakeX, snakeY});
       
        
        timer = new Timer(100, this);
        timer.start();
        
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(gameOver){
                    if(key == KeyEvent.VK_R) resetGame();
                    else if(key == KeyEvent.VK_M) returnToMenu();
                    return;
                }
                if(key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN)
                    direction = KeyEvent.VK_UP;
                if(key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP)
                    direction = KeyEvent.VK_DOWN;
                if(key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT)
                    direction = KeyEvent.VK_LEFT;
                if(key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT)
                    direction = KeyEvent.VK_RIGHT;
                if(key == KeyEvent.VK_SPACE){
                    int bulletX = snakeX + (snakeWidth - bulletSize) / 2;
                    int bulletY = snakeY + (snakeHeight - bulletSize) / 2;
                    bullets.add(new Bullet(bulletX, bulletY, direction));
                }
            }
        });
        
        generateFood();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Disegna il serpente
        g.setColor(Color.GREEN);
        for(int[] segment : snakeBody){
            g.fillRect(segment[0], segment[1], snakeWidth, snakeHeight);
        }
        // Disegna il cibo
        if(!gameOver){
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, snakeWidth, snakeHeight);
        }
        // Disegna i nemici
        g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, snakeHeight - 4));
        for(int[] enemy : enemies){
            g.setColor(Color.BLUE);
            g.drawString("👾", enemy[0], enemy[1] + snakeHeight - 4);
        }
        // Disegna i proiettili
        g.setColor(Color.YELLOW);
        for(Bullet bullet : bullets){
            g.fillRect(bullet.x, bullet.y, bulletSize, bulletSize);
        }
        // Mostra il punteggio
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Score: " + score, 10, 20);
        // Se il gioco è terminato, mostra i messaggi
        if(gameOver){
            String msg = "Game Over";
            Font font = new Font("Arial", Font.BOLD, 50);
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int msgWidth = fm.stringWidth(msg);
            int msgHeight = fm.getHeight();
            g.setColor(Color.RED);
            g.drawString(msg, (getWidth()-msgWidth)/2, (getHeight()-msgHeight)/2);
            
            String retryMsg = "Press R to Retry";
            Font retryFont = new Font("Arial", Font.PLAIN, 20);
            g.setFont(retryFont);
            FontMetrics retryFm = g.getFontMetrics();
            int retryWidth = retryFm.stringWidth(retryMsg);
            g.setColor(Color.WHITE);
            g.drawString(retryMsg, (getWidth()-retryWidth)/2, (getHeight()-msgHeight)/2 + 40);
            
            String menuMsg = "Press M to return to Menu";
            Font menuFont = new Font("Arial", Font.PLAIN, 20);
            g.setFont(menuFont);
            FontMetrics menuFm = g.getFontMetrics();
            int menuWidth = menuFm.stringWidth(menuMsg);
            g.setColor(Color.WHITE);
            g.drawString(menuMsg, (getWidth()-menuWidth)/2, (getHeight()-msgHeight)/2 + 80);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gameOver) return;
        
        // Calcola la nuova posizione della testa
        int newSnakeX = snakeX;
        int newSnakeY = snakeY;
        if(direction == KeyEvent.VK_UP) newSnakeY -= snakeHeight;
        if(direction == KeyEvent.VK_DOWN) newSnakeY += snakeHeight;
        if(direction == KeyEvent.VK_LEFT) newSnakeX -= snakeWidth;
        if(direction == KeyEvent.VK_RIGHT) newSnakeX += snakeWidth;
        
        // Gestione dello screen wrapping
        if(newSnakeX < 0) newSnakeX = getWidth() - snakeWidth;
        else if(newSnakeX >= getWidth()) newSnakeX = 0;
        if(newSnakeY < 0) newSnakeY = getHeight() - snakeHeight;
        else if(newSnakeY >= getHeight()) newSnakeY = 0;
        
        snakeX = newSnakeX;
        snakeY = newSnakeY;
        snakeBody.add(0, new int[]{snakeX, snakeY});
        
        if(checkSelfCollision()){
            timer.stop();
            gameOver = true;
            repaint();
            return;
        }
        
        // Collisione con nemici
        for(int[] enemy : enemies){
            int enemyRight = enemy[0] + snakeWidth;
            int enemyBottom = enemy[1] + snakeHeight;
            int snakeRight = snakeX + snakeWidth;
            int snakeBottom = snakeY + snakeHeight;
            if(snakeX+2 < enemyRight && snakeRight-2 > enemy[0] &&
               snakeY+2 < enemyBottom && snakeBottom-2 > enemy[1]){
                timer.stop();
                gameOver = true;
                repaint();
                return;
            }
        }
        
        // Verifica se il serpente ha mangiato il cibo
        boolean ateFood = (snakeX < foodX + snakeWidth && snakeX + snakeWidth > foodX &&
                           snakeY < foodY + snakeHeight && snakeY + snakeHeight > foodY);
        if(!ateFood){
            snakeBody.remove(snakeBody.size()-1);
        } else {
            score += 10;
            // Determina quanti nemici spawnare in base alla difficoltà
            int enemiesToSpawn = 1; // Default per "Facile"
            if(difficulty.equalsIgnoreCase("Medio")) enemiesToSpawn = 2;
            else if(difficulty.equalsIgnoreCase("Difficile")) enemiesToSpawn = 3;
            else if(difficulty.equalsIgnoreCase("Estremo")) enemiesToSpawn = 5;
            else if(difficulty.equalsIgnoreCase("Impossibile")) enemiesToSpawn = 20;
            else if(difficulty.equalsIgnoreCase("SuperImpossibile")) enemiesToSpawn = 50;
            for(int i = 0; i < enemiesToSpawn; i++){
                spawnEnemy();
            }
            generateFood();
        }
        
        // Aggiornamento proiettili
        Iterator<Bullet> bulletIt = bullets.iterator();
        while(bulletIt.hasNext()){
            Bullet bullet = bulletIt.next();
            int increments = bulletSpeed / 10;
            boolean collided = false;
            for(int i = 0; i < increments && !collided; i++){
                if(bullet.direction == KeyEvent.VK_UP) bullet.y -= 10;
                else if(bullet.direction == KeyEvent.VK_DOWN) bullet.y += 10;
                else if(bullet.direction == KeyEvent.VK_LEFT) bullet.x -= 10;
                else if(bullet.direction == KeyEvent.VK_RIGHT) bullet.x += 10;
                
                Iterator<int[]> enemyIt = enemies.iterator();
                while(enemyIt.hasNext()){
                    int[] enemy = enemyIt.next();
                    if(bullet.x < enemy[0] + snakeWidth && bullet.x + bulletSize > enemy[0] &&
                       bullet.y < enemy[1] + snakeHeight && bullet.y + bulletSize > enemy[1]){
                        enemyIt.remove();
                        collided = true;
                        break;
                    }
                }
            }
            if(collided){
                bulletIt.remove();
                continue;
            }
            if(bullet.x < 0 || bullet.x > getWidth() || bullet.y < 0 || bullet.y > getHeight()){
                bulletIt.remove();
            }
        }
        repaint();
    }
    
    private boolean checkSelfCollision(){
        for(int i = 1; i < snakeBody.size(); i++){
            int[] segment = snakeBody.get(i);
            if(snakeX == segment[0] && snakeY == segment[1]) return true;
        }
        return false;
    }
    
    private void generateFood(){
        Random rand = new Random();
        int maxX = GRID_WIDTH / snakeWidth;
        int maxY = GRID_HEIGHT / snakeHeight;
        boolean validPosition = false;
        while(!validPosition){
            foodX = rand.nextInt(maxX) * snakeWidth;
            foodY = rand.nextInt(maxY) * snakeHeight;
            validPosition = true;
            for(int[] segment : snakeBody){
                if(segment[0] == foodX && segment[1] == foodY){
                    validPosition = false;
                    break;
                }
            }
            for(int[] enemy : enemies){
                if(enemy[0] == foodX && enemy[1] == foodY){
                    validPosition = false;
                    break;
                }
            }
        }
    }
    
    private void spawnEnemy(){
        Random rand = new Random();
        int maxX = GRID_WIDTH / snakeWidth;
        int maxY = GRID_HEIGHT / snakeHeight;
        boolean validPosition = false;
        int enemyX = 0, enemyY = 0;
        while(!validPosition){
            enemyX = rand.nextInt(maxX) * snakeWidth;
            enemyY = rand.nextInt(maxY) * snakeHeight;
            validPosition = true;
            for(int[] segment : snakeBody){
                if(segment[0] == enemyX && segment[1] == enemyY){
                    validPosition = false;
                    break;
                }
            }
            if(enemyX == foodX && enemyY == foodY) validPosition = false;
            for(int[] e : enemies){
                if(e[0] == enemyX && e[1] == enemyY){
                    validPosition = false;
                    break;
                }
            }
        }
        enemies.add(new int[]{enemyX, enemyY});
    }
    
    private void resetGame(){
        snakeX = 40;
        snakeY = 40;
        direction = KeyEvent.VK_RIGHT;
        snakeBody.clear();
        snakeBody.add(new int[]{snakeX, snakeY});
        score = 0;
        enemies.clear();
        bullets.clear();
        gameOver = false;
        generateFood();
        if(!timer.isRunning()) timer.start();
        repaint();
    }
    
    private void returnToMenu(){
        JPanel mainPanel = (JPanel)getParent();
        CardLayout cardLayout = (CardLayout)mainPanel.getLayout();
        cardLayout.show(mainPanel, "menu");
    }
    
//    @Override
//    public void addNotify(){
//        super.addNotify();
//        SwingUtilities.invokeLater(() -> generateFood());
//    }
}
