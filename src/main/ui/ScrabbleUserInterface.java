package ui;

import model.EventLog;
import model.ScrabbleGame;
import model.Event;

public abstract class ScrabbleUserInterface {

    protected ScrabbleGame game;
    protected int numPlayers;
    protected boolean gameRunning;

    // Represents a user interface for Scrabble
    public ScrabbleUserInterface() {

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
    }
}
