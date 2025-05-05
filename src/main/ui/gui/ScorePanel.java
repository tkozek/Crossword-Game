package ui.gui;

import java.awt.Font;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.ScrabbleGame;

public class ScorePanel extends JPanel {

    public ScorePanel(ScrabbleGame game) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel scoreLabel = new JLabel("<html><u>Scoreboard</u></html>");
        scoreLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        add(scoreLabel);
        updateScorePanel(game);
    }

    public void updateScorePanel(ScrabbleGame game) {
        removeAll();
        Map<String, Integer> playerScoreMap = game.getPlayerScoreMap();
        for (Map.Entry<String, Integer> entry : playerScoreMap.entrySet()) {
            JLabel playerLabel = new JLabel(entry.getKey() + ": " + entry.getValue());
            add(playerLabel);
        }
    }
}
