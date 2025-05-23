package ui;

import model.Player;
import model.ScrabbleGame;
import model.event.Event;
import model.event.EventLog;

import java.util.List;

// Represents a user interface for Scrabble
public abstract class ScrabbleUserInterface {

    protected ScrabbleGame game;
    protected int numPlayers;
    protected boolean gameRunning;

    protected void printScoreSummaries() {
        List<Player> players = game.getPlayers();
        for (Player player : players) {
            System.out.println(player.getPlayerName() + " scored " + player.getPointsThisGame() 
                    + " points this game.\n");
        }
    }

    // REQUIRES: user has quit application or the game has ended.
    // EFFECTS: prints event log to console
    protected void printEventLog() {
        EventLog log = EventLog.getInstance();
        System.out.println();
        System.out.println();
        for (Event event : log) {
            System.out.println(event.toString());
        }
        log.clear();
    }

    protected void handleEndGame(Player lastPlayer) {
        this.gameRunning = false;
        game.performEndGameAdjustments(lastPlayer);
        System.out.println(lastPlayer.getPlayerName() + " was the last to play");
        System.out.println("The winner is " + game.getHighestScoringPlayer().getPlayerName());
        printEventLog();
        printScoreSummaries();
    }
}
