package ui.gui;

import javax.swing.*;

import model.Player;
import model.ScrabbleGame;
import java.util.List;

import java.awt.*;

// The panel in the Graphical user interface which 
// displays the current scores of all players
// in this game, under their names
public class ScorePanel extends JPanel {
    
    public ScorePanel(ScrabbleGame scrabbleGame) {
        List<Player> players = scrabbleGame.getPlayers();
        setLayout(new GridLayout(players.size(), 1));

        for (Player player : players) {
            JLabel playerLabel = new JLabel(player.getPlayerName() + ": " + player.getPointsThisGame());
            add(playerLabel);
        }
    }
}
