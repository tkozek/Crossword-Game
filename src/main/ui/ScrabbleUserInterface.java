package ui;

import model.EventLog;
import model.Event;

public abstract class ScrabbleUserInterface {

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
