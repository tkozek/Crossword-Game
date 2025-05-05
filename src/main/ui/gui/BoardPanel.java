package ui.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.ScrabbleGame;
import model.board.Board;
import model.exceptions.BoardSectionUnavailableException;

public class BoardPanel extends JPanel {

    private static final Color DOUBLE_LETTER_COLOR = new Color(173, 216, 230);
    private static final Color TRIPLE_LETTER_COLOR = new Color(5, 127, 187);
    private static final Color DOUBLE_WORD_COLOR =  Color.PINK;
    private static final Color TRIPLE_WORD_COLOR = new Color(255, 0, 0);
    private static final Color DEFAULT_BOARD_SPACE_COLOR = new Color(190, 171, 141);
    private static final Color DEFAULT_LETTER_TILE_COLOR = new Color(244, 217, 138);

    private static final Font TILE_FONT = new Font("Arial", Font.BOLD, 14);
    private static final int TILE_SIZE = 30;

    private static final int BOARD_PANEL_LENGTH = Board.BOARD_LENGTH * TILE_SIZE;
    

    public BoardPanel(ScrabbleGame game) {
        this.setLayout(new GridLayout(Board.BOARD_LENGTH, Board.BOARD_LENGTH));
        this.setPreferredSize(new Dimension(BOARD_PANEL_LENGTH, BOARD_PANEL_LENGTH));
        this.updateBoard(game);
    }

    public void updateBoard(ScrabbleGame game) {
        removeAll();
        for (int i = 0; i < Board.BOARD_LENGTH; i++) {
            for (int j = 0; j < Board.BOARD_LENGTH; j++) {
                String toDisplay = game.getTileAtPositionOnBoard(i, j).toDisplay();
                JButton tile = new JButton(toDisplay);
                tile.setBackground(selectBoardTileColor(toDisplay));
                tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                tile.setFont(TILE_FONT);
                final int row = i;
                final int col = j;
                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        game.setStartRow(row);
                        game.setStartCol(col);
                    }
                });
                this.add(tile);
            }
        }
    }

    public void updateToPreviewBoard(ScrabbleGame game) throws BoardSectionUnavailableException {
        removeAll();
        String[][] previewBoardDisplay = game.previewBoardDisplay();
        for (int i = 0; i < Board.BOARD_LENGTH; i++) {
            for (int j = 0; j < Board.BOARD_LENGTH; j++) {
                String toDisplay = previewBoardDisplay[i][j];
                
                JButton tile = new JButton(toDisplay);
                tile.setBackground(selectBoardTileColor(toDisplay));
                tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                if (!(toDisplay.equals(game.getTileAtPositionOnBoard(i, j).toDisplay()))) {
                    tile.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
                }
                tile.setFont(TILE_FONT);
                this.add(tile);
            }
        }
    }

    // EFFECTS: returns appropriate display color given
    // displayString which corresponds to a tile on the board
    private Color selectBoardTileColor(String displayString) {
        Color color;
        switch (displayString) {
            case "DLS":
                color = DOUBLE_LETTER_COLOR;
                break;
            case "DWS":
                color = DOUBLE_WORD_COLOR;
                break;
            case "TLS":
                color = TRIPLE_LETTER_COLOR;
                break;
            case "TWS":
                color = TRIPLE_WORD_COLOR;
                break;
            case " ":
                color = DEFAULT_BOARD_SPACE_COLOR;
                break;
            default:
                color = DEFAULT_LETTER_TILE_COLOR;
                break;
        }
        return color;
    }
}
