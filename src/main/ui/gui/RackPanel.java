package ui.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.Player;
import model.ScrabbleGame;
import model.tile.LetterTile;

public class RackPanel extends JPanel {


    private static final Color DEFAULT_LETTER_TILE_COLOR = new Color(244, 217, 138);
    private static final Color SELECTED_TILE_BORDER_COLOR = new Color(128, 0, 128);

    private ActionPanel actionPanel;
    private GuiListener listener;

    public RackPanel(ScrabbleGame game, GuiListener listener) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        this.actionPanel = new ActionPanel(game, listener);
        this.listener = listener;        
        updateRackPanel(game, true);
        
    }

    public void updateRackPanel(ScrabbleGame game, boolean tilesClickable) {
        this.removeAll();
        this.add(actionPanel);
        actionPanel.updateActionPanel(game);
        Player curPlayer = game.getCurrentPlayer();
        List<LetterTile> letters = curPlayer.getTilesOnRack();
        int numLetters = letters.size();
        for (int i = 0; i < numLetters; i++) {
            add(createTilePanel(curPlayer, letters.get(i), i, tilesClickable));
        }
    }

    public void updateToSaveAndQuitRackPanel(ScrabbleGame game) {
        this.removeAll();
        this.updateRackPanel(game, false);
        actionPanel.updateActionPanelToSaveAndQuit();
        
    }

    public void updateToPreviewPanel(ScrabbleGame game) {
        this.removeAll();
        this.updateRackPanel(game, false);
        actionPanel.updateToPreviewPanel();
    }

    private JButton createTilePanel(Player player, LetterTile letter, int letterIndex, boolean tilesClickable) {
        JButton tileButton = new JButton(letter.toDisplay());
        tileButton.setPreferredSize(new Dimension(40, 40));
        tileButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tileButton.setBackground(DEFAULT_LETTER_TILE_COLOR);
        tileButton.setFont(new Font("Arial", Font.BOLD, 16));
        if (tilesClickable) {
            tileButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    player.selectTile(letterIndex);
                    tileButton.setBorder(BorderFactory.createLineBorder(SELECTED_TILE_BORDER_COLOR));
                    // TODO notify main GUI so that tiles are repaint/revalidated
                }
            });
        }
        return tileButton;
    }

    public void updateDirection(String newDirection) {
        actionPanel.updateDirectionToggle(newDirection);
    }

    public ActionPanel getActionPanel() {
        return this.actionPanel;
    }

}
