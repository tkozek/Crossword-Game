package ui.gui;

import javax.swing.*;

import model.Player;
import model.tile.LetterTile;

import java.awt.*;

// The panel in the Graphical user interface which 
// displays the current state of the current player's 
// tile rack
public class RackPanel extends JPanel {

    public RackPanel(Player p) {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        for (LetterTile letter : p.getTilesOnRack()) {
            JPanel tilePanel = createTilePanel(letter);
            add(tilePanel);
        }
    }

    private JPanel createTilePanel(LetterTile letter) {
        JPanel tilePanel = new JPanel();
        tilePanel.setPreferredSize(new Dimension(40, 40));
        tilePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel label = new JLabel(letter.getString());
        label.setFont(new Font("Arial", Font.BOLD, 16));
        tilePanel.add(label);

        return tilePanel;
    }

}
