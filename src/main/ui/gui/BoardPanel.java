package ui.gui;

import javax.swing.*;
import java.awt.*;

import model.board.Board;


// The panel in the Graphical user interface which 
// displays the current state of the game's board
public class BoardPanel extends JPanel {

    private static final int TILE_SIZE = 30;

    public BoardPanel(Board board) {
        setLayout(new GridLayout(Board.BOARD_LENGTH, Board.BOARD_LENGTH));
        setPreferredSize(new Dimension(Board.BOARD_LENGTH * TILE_SIZE, Board.BOARD_LENGTH * TILE_SIZE));

        for (int i = 0; i < Board.BOARD_LENGTH; i++) {
            for (int j = 0; j < Board.BOARD_LENGTH; j++) {
                JPanel tile = new JPanel();
                tile.setBackground(new Color(244, 217, 138));
                JLabel letter = new JLabel(board.getTileAtPositionOnBoard(i, j).toDisplay());
                tile.add(letter);
                tile.setBorder(BorderFactory.createLineBorder(Color.black));
                add(tile);
            }
        }
    }
}
