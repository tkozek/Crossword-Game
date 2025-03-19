package ui.gui;

import javax.swing.*;

import model.Direction;
import model.Player;
import model.board.Board;
import model.move.Move;
import model.move.MoveType;

import java.awt.*;

// The panel in the Graphical user interface which 
// displays the current player's move history, 
// either complete or filtered
public class HistoryPanel extends JPanel {

    public HistoryPanel(Player player) {
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(175, 1000));
        String summary = "";
        for (Move move : player.getHistory().getMoves()) {
            switch (move.getMoveType()) {
                case PLAY_WORD:
                    summary = getWordString(move, player);
                    break;
                case SWAP_TILES:
                    summary = getSwapSummary(move, player);
                    break;
                case SKIP:
                    summary = getSkipSummary(move, player);
                    break;
                case END_GAME_ADJUSTMENT:
                    summary = getSingleEndGameAdjustmentSummary(move, player);
                    break;
            }
            JTextArea moveSummary = new JTextArea(summary);
            moveSummary.setFont(new Font("Arial", Font.ITALIC, 12));
            moveSummary.setLineWrap(true);
            moveSummary.setWrapStyleWord(true);
            moveSummary.setCaretPosition(0);
            moveSummary.setEditable(false);
            moveSummary.setPreferredSize(new Dimension(150, 75));

            JPanel movePanel = new JPanel();
            movePanel.add(moveSummary);
            movePanel.setMaximumSize(new Dimension(200,300));
            add(movePanel);
            movePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        }
    }

    // EFFECTS: Returns string summary of a word played
    public String getWordString(Move word, Player p) {
        String printout = "\n" + p.getPlayerName() + " played ";
        String wordString = word.getLettersInvolved();
        String startRow = String.valueOf(word.getStartRow());
        String startCol = String.valueOf(word.getStartColumn());
        String coordinates = "(" + startRow + "," + startCol + ")";
        String direction = (word.getDirection() == Direction.RIGHT) ? "to the right" : "down";
        String points = String.valueOf(word.getPointsForMove());
        printout += wordString + " starting at " + coordinates + " and moving " 
                + direction + " earning " + points + " points.";
        return printout;
    } 

    //EFFECTS: Returns string summary of a player swap
    public String getSwapSummary(Move swap, Player p) {
        String printout = "\n" + p.getPlayerName() + " swapped tiles. ";
        String preAndPostLetters = swap.getLettersInvolved();
        int halfLength = preAndPostLetters.length() / 2;
        String preSwapLetters = preAndPostLetters.substring(0, halfLength);
        String postSwapLetters = preAndPostLetters.substring(halfLength);
        String points = String.valueOf(swap.getPointsForMove());
        printout += "Their tiles before swapping were: " + preSwapLetters + " and their tiles after swapping were " 
                 + postSwapLetters + ", earning " + points + " points.";
        return printout;
    }

    // EFFECTS: Returns string summary of a skipped turn
    public String getSkipSummary(Move skip, Player p) {
        return p.getPlayerName() + " skipped their turn";
    }

    // EFFECTS: Returns string summary of an end game adjustment
    public String getSingleEndGameAdjustmentSummary(Move move, Player player) {
        String playerName = player.getPlayerName();
        String lastPlayer = move.getLastPlayer().getPlayerName();
        int pointChange = move.getPointsForMove();
        int absolutePointChange = Math.abs(pointChange);
        String gainOrLoss = (pointChange >= 0) ? " gained " : " lost ";
        String pluralOrNot = (absolutePointChange != 1) ? "s." : ".";
        return lastPlayer + " used all their tiles first. " + playerName + gainOrLoss 
                + absolutePointChange + " point" + pluralOrNot;
    }
    
}
