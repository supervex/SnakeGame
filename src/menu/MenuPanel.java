package menu;

import java.awt.*;
import javax.swing.*;

public class MenuPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JButton confirmButton, newGameButton;
    private JRadioButton facileButton, medioButton, difficileButton, estremoButton, impossibileButton, superImpossibile;
    private ButtonGroup difficultyGroup;
    private JRadioButton classicaButton, campagnaButton;
    private ButtonGroup gameModeGroup;
    private JPanel modeSelectionPanel, difficultyPanel, campaignPanel;
    private JRadioButton level1Button, level2Button, level3Button;
    private ButtonGroup levelGroup;

    public MenuPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(1000, 600));

        // Pannello per la selezione della modalità di gioco
        modeSelectionPanel = new JPanel();
        modeSelectionPanel.setLayout(new BoxLayout(modeSelectionPanel, BoxLayout.Y_AXIS));
        modeSelectionPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Snake Game");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.GREEN);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        modeSelectionPanel.add(titleLabel);
        modeSelectionPanel.add(Box.createVerticalStrut(30));

        JLabel modeLabel = new JLabel("Seleziona la modalità di gioco:");
        modeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        modeLabel.setForeground(Color.WHITE);
        modeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        modeSelectionPanel.add(modeLabel);
        modeSelectionPanel.add(Box.createVerticalStrut(20));

        JPanel modePanel = new JPanel(new GridLayout(2, 1, 10, 10));
        modePanel.setOpaque(false);

        classicaButton = new JRadioButton("Classica");
        campagnaButton = new JRadioButton("Campagna" + " (work in progress..)");

        gameModeGroup = new ButtonGroup();
        gameModeGroup.add(classicaButton);
        gameModeGroup.add(campagnaButton);

        classicaButton.setForeground(Color.WHITE);
        campagnaButton.setForeground(Color.WHITE);
        classicaButton.setOpaque(false);
        campagnaButton.setOpaque(false);
        classicaButton.setFont(new Font("Arial", Font.BOLD, 18));
        campagnaButton.setFont(new Font("Arial", Font.BOLD, 18));

        modePanel.add(classicaButton);
        modePanel.add(campagnaButton);

        classicaButton.setSelected(true);

        modeSelectionPanel.add(modePanel);
        modeSelectionPanel.add(Box.createVerticalStrut(20));

        // Bottone di conferma della modalità
        confirmButton = new JButton("Conferma");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 22));
        modeSelectionPanel.add(confirmButton);

        // Pannello per la selezione della difficoltà (per modalità Classica)
        difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new BoxLayout(difficultyPanel, BoxLayout.Y_AXIS));
        difficultyPanel.setOpaque(false);
        difficultyPanel.setVisible(false);

        JLabel diffLabel = new JLabel("Seleziona la difficoltà:");
        diffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        diffLabel.setForeground(Color.WHITE);
        diffLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        difficultyPanel.add(diffLabel);
        difficultyPanel.add(Box.createVerticalStrut(20));

        JPanel diffButtonsPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        diffButtonsPanel.setOpaque(false);

        facileButton = new JRadioButton("Facile");
        medioButton = new JRadioButton("Medio");
        difficileButton = new JRadioButton("Difficile");
        estremoButton = new JRadioButton("Estremo");
        impossibileButton = new JRadioButton("Impossibile");
        superImpossibile = new JRadioButton("SuperImpossibile");

        difficultyGroup = new ButtonGroup();
        JRadioButton[] buttons = {facileButton, medioButton, difficileButton, estremoButton, impossibileButton, superImpossibile};
        for (JRadioButton button : buttons) {
            button.setForeground(Color.WHITE);
            button.setOpaque(false);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            difficultyGroup.add(button);
            diffButtonsPanel.add(button);
        }
        facileButton.setSelected(true);

        difficultyPanel.add(diffButtonsPanel);
        difficultyPanel.add(Box.createVerticalStrut(30));
        
        // Bottone per iniziare il gioco in modalità Classica
        newGameButton = new JButton("Nuova Partita");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.setFont(new Font("Arial", Font.BOLD, 22));
        difficultyPanel.add(newGameButton);

        // Aggiungi uno spazio verticale e il pulsante "Indietro" nel pannello di difficoltà
        difficultyPanel.add(Box.createVerticalStrut(20));
        JButton backFromDifficultyButton = new JButton("Indietro");
        backFromDifficultyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backFromDifficultyButton.setFont(new Font("Arial", Font.BOLD, 22));
        backFromDifficultyButton.addActionListener(e -> {
            difficultyPanel.setVisible(false);
            modeSelectionPanel.setVisible(true);
        });
        difficultyPanel.add(backFromDifficultyButton);

        // Pannello per la selezione dei livelli (per modalità Campagna)
        campaignPanel = new JPanel();
        campaignPanel.setLayout(new BoxLayout(campaignPanel, BoxLayout.Y_AXIS));
        campaignPanel.setOpaque(false);
        campaignPanel.setVisible(false);

        JLabel levelLabel = new JLabel("Seleziona un livello:");
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        campaignPanel.add(levelLabel);
        campaignPanel.add(Box.createVerticalStrut(20));

        JPanel levelButtonsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        levelButtonsPanel.setOpaque(false);

        level1Button = new JRadioButton("Livello 1");
        level2Button = new JRadioButton("Livello 2");
        level3Button = new JRadioButton("Livello 3");

        levelGroup = new ButtonGroup();
        levelGroup.add(level1Button);
        levelGroup.add(level2Button);
        levelGroup.add(level3Button);

        JRadioButton[] levels = {level1Button, level2Button, level3Button};
        for (JRadioButton level : levels) {
            level.setForeground(Color.WHITE);
            level.setOpaque(false);
            level.setFont(new Font("Arial", Font.BOLD, 18));
            levelButtonsPanel.add(level);
        }
        level1Button.setSelected(true); // Livello predefinito

        campaignPanel.add(levelButtonsPanel);
        campaignPanel.add(Box.createVerticalStrut(30));

        // Aggiungi il pulsante di conferma nella schermata dei livelli
        JButton confirmLevelButton = new JButton("Conferma");
        confirmLevelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmLevelButton.setFont(new Font("Arial", Font.BOLD, 22));
        campaignPanel.add(confirmLevelButton);

        // Aggiungi uno spazio verticale e il pulsante "Indietro" nel pannello dei livelli
        campaignPanel.add(Box.createVerticalStrut(20));
        JButton backFromCampaignButton = new JButton("Indietro");
        backFromCampaignButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backFromCampaignButton.setFont(new Font("Arial", Font.BOLD, 22));
        backFromCampaignButton.addActionListener(e -> {
            campaignPanel.setVisible(false);
            modeSelectionPanel.setVisible(true);
        });
        campaignPanel.add(backFromCampaignButton);

        // Layout principale
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(modeSelectionPanel, gbc);
        add(difficultyPanel, gbc);
        add(campaignPanel, gbc);

        // Ascoltatore per il bottone di conferma della modalità
        confirmButton.addActionListener(e -> showNextPanel());

        // Ascoltatore per il pulsante di conferma dei livelli
        confirmLevelButton.addActionListener(e -> startGame());
        
        // Ascoltatore per il bottone "Nuova Partita" in modalità Classica
        newGameButton.addActionListener(e -> startGame());
    }

    private void showNextPanel() {
        if (classicaButton.isSelected()) {
            modeSelectionPanel.setVisible(false);
            difficultyPanel.setVisible(true);
        } else if (campagnaButton.isSelected()) {
            modeSelectionPanel.setVisible(false);
            campaignPanel.setVisible(true);
        }
    }

    private void startGame() {
        String diff = getSelectedDifficulty();
        String mode = getSelectedGameMode();
        int lvl = getSelectedLevel();
        JPanel mainPanel = (JPanel) getParent();
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        if (mode.equalsIgnoreCase("Classica")) {
            // Crea il GamePanel per la modalità Classica
            gamePanel.GamePanel gamePanel = new gamePanel.GamePanel(diff);
            mainPanel.add(gamePanel, "game");
            cardLayout.show(mainPanel, "game");
            gamePanel.requestFocusInWindow();
        } else {
            // Crea il CampaignGamePanel per la modalità Campagna
            gamePanel.CampaignGamePanel campaignGamePanel = new gamePanel.CampaignGamePanel(lvl);
            mainPanel.add(campaignGamePanel, "campaign");
            cardLayout.show(mainPanel, "campaign");
            campaignGamePanel.requestFocusInWindow();
        }
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }

    public String getSelectedDifficulty() {
        if (facileButton.isSelected()) return "Facile";
        if (medioButton.isSelected()) return "Medio";
        if (difficileButton.isSelected()) return "Difficile";
        if (estremoButton.isSelected()) return "Estremo";
        if (impossibileButton.isSelected()) return "Impossibile";
        if (superImpossibile.isSelected()) return "SuperImpossibile";
        return "Facile";
    }

    public String getSelectedGameMode() {
        return classicaButton.isSelected() ? "Classica" : "Campagna";
    }

    public int getSelectedLevel() {
        if (level1Button.isSelected()) return 1;
        if (level2Button.isSelected()) return 2;
        if (level3Button.isSelected()) return 3;
        return 1;
    }
}
