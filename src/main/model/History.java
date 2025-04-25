package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.move.Move;
import model.move.MoveType;

// Represents a Player's history, which includes
// a list of their moves across their games
public class History implements Iterable<Move> {

    private List<Move> moveHistory;

    // Makes a new history with no moves,
    // and its associated player's name
    public History() {
        this.moveHistory = new ArrayList<>();
    }

    // REQUIRES: letter is uppercase,
    //      between 'A' to 'Z' or '-'
    // EFFECTS: Filters player's words played and returns
    // them in order least to most recent, only including
    // moves which used the given letter at least once
    public List<Move> getWordsContainingLetter(char letter) {
        List<Move> movesWithLetter = new ArrayList<>();
        List<Move> playedWords = this.getWordsPlayed();
        for (Move move : playedWords) {
            if (move.moveContainsLetter(letter)) {
                movesWithLetter.add(move);
            }
        }
        return movesWithLetter;
    }

    // MODIFIES: this
    // EFFECTS: adds move to this history
    public void addMove(Move move) {
        this.moveHistory.add(move);
    }

    // EFFECTS: Returns only the moves where a word was 
    // played. Excludes any turns which were swaps
    // or skips
    public List<Move> getWordsPlayed() {
        List<Move> wordsPlayed = new ArrayList<>();
        for (Move move : this) {
            if (move.getMoveType() == MoveType.PLAY_WORD) {
                wordsPlayed.add(move);
            }
        }
        return wordsPlayed;
    }
    
    public List<Move> getMoves() {
        return this.moveHistory;
    }

    public List<Move> getMoves(Player player) {
        return null; // !!! TODO remove player's sense of history
    }

    @Override
    public Iterator<Move> iterator() {
        return moveHistory.iterator();
    }
}
