package ui.gui;

import javax.swing.*;
import java.awt.*;

import model.board.Board;


// The panel in the Graphical user interface which 
// displays the current state of the game's board
public class BoardPanel extends JPanel {

    private static final int TILE_SIZE = 40;

    public BoardPanel(Board board) {
        setLayout(new GridLayout(Board.BOARD_LENGTH, Board.BOARD_LENGTH));
        setPreferredSize(new Dimension(Board.BOARD_LENGTH * TILE_SIZE, Board.BOARD_LENGTH * TILE_SIZE));

        for (int i = 0; i < Board.BOARD_LENGTH * Board.BOARD_LENGTH; i++) {
            JPanel tile = new JPanel();
            tile.setBackground(Color.lightGray);
            tile.setBorder(BorderFactory.createLineBorder(Color.black));
            add(tile);
        }

    }
}
