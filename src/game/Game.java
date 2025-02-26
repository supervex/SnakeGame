package game;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import gamePanel.GamePanel;
import gamePanel.CampaignGamePanel;
import menu.MenuPanel;

public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Utilizza CardLayout per gestire il passaggio dal menu al gioco
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Crea e aggiungi il pannello del menu
        MenuPanel menuPanel = new MenuPanel();
        mainPanel.add(menuPanel, "menu");

        // Gestisci l'azione "Nuova Partita" per avviare il gioco
        menuPanel.getNewGameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String difficulty = menuPanel.getSelectedDifficulty();
                String gameMode = menuPanel.getSelectedGameMode();
                int level = menuPanel.getSelectedLevel();
                
                if (gameMode.equalsIgnoreCase("Classica")) {
                    // Crea il GamePanel per la modalità Classica
                    GamePanel gamePanel = new GamePanel(difficulty);
                    mainPanel.add(gamePanel, "game");
                    cardLayout.show(mainPanel, "game");
                    gamePanel.requestFocusInWindow();
                } else {
                    // Crea il CampaignGamePanel per la modalità Campagna
                    CampaignGamePanel campaignGamePanel = new CampaignGamePanel(level);
                    mainPanel.add(campaignGamePanel, "campaign");
                    cardLayout.show(mainPanel, "campaign");
                    campaignGamePanel.requestFocusInWindow();
                }
            }
        });

        frame.add(mainPanel);
        frame.pack(); // Rispetta le dimensioni preferite dei pannelli
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
