package ui.gui;

import javax.swing.*;

import model.Player;
import model.ScrabbleGame;
import model.tile.LetterTile;

import java.awt.*;
import java.awt.event.ActionListener;

// The panel in the Graphical user interface which 
// displays the current state of the current player's 
// tile rack
public class RackPanel extends JPanel {

    private JButton playWordButton;
    private JButton swapTilesButton;
    private JButton skipTurnButton;
    private JButton turnSelectionConfirmationButton;
    private JPanel actionPanel;
    private int startRow;
    private int startCol;
    private Player player;

    public RackPanel(ScrabbleGame game, Player player) {
        this.player = player;
        setLayout(new FlowLayout(FlowLayout.CENTER));
        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        addActionButtons();
        for (LetterTile letter : player.getTilesOnRack()) {
            JPanel tilePanel = createTilePanel(letter);
            add(tilePanel);
        }
    }

    private void addActionButtons() {
        playWordButton = new JButton("Play");
        swapTilesButton = new JButton("Swap");
        skipTurnButton = new JButton("Skip");
        turnSelectionConfirmationButton = new JButton("Confirm");

        actionPanel.add(playWordButton);
        actionPanel.add(swapTilesButton);
        actionPanel.add(skipTurnButton);
        actionPanel.add(turnSelectionConfirmationButton);
    }

    private JPanel createTilePanel(LetterTile letter) {
        JPanel tilePanel = new JPanel();
        tilePanel.setPreferredSize(new Dimension(40, 40));
        tilePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tilePanel.setBackground(new Color(244, 217, 138));
        JLabel label = new JLabel(letter.toDisplay());
        label.setFont(new Font("Arial", Font.BOLD, 16));
        tilePanel.add(label);
        return tilePanel;
    }

}
