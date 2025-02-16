package model;

import java.util.ArrayList;
import java.util.List;

// Represents a Player's history, which includes
// a list of their moves across their games
public class History {

    private String name;
    private List<Move> moveHistory;

    // Makes a new history with no moves,
    // and its associated player's name
    public History(String name) {

    }
    
    public List<Move> getMoves() {
        return null;
    }
    // REQUIRES: Move was made user with this.name
    // MODIFIES: this
    // EFFECTS: adds move to this history
    public void addMove(Move move) {

    }

    // EFFECTS: Filters player's move history and returns
    // moves in order least to most recent, only including
    // moves which used the given letter at least once
    public List<Move> getListOfMovesContainingLetter(char letter) {
        return null;
    }
}
