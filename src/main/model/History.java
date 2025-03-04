package model;

import java.util.ArrayList;
import java.util.List;

import model.move.Move;
import model.move.MoveType;

// Represents a Player's history, which includes
// a list of their moves across their games
public class History {

    private List<Move> moveHistory;

    // Makes a new history with no moves,
    // and its associated player's name
    public History() {
        this.moveHistory = new ArrayList<>();
    }

    
    public List<Move> getMoves() {
        return this.moveHistory;
    }

    // EFFECTS: Returns only the moves where a word was 
    // played. Excludes any turns which were swaps
    // or skips
    public List<Move> getMovesWithWordPlayed() {
        List<Move> allMoves = this.moveHistory;
        List<Move> wordsPlayed = new ArrayList<>();
        for (Move move : allMoves) {
            if (move.getMoveType() == MoveType.PLAY_WORD) {
                wordsPlayed.add(move);
            }
        }
        return wordsPlayed;
    }

    // REQUIRES: Move was made by user with this.getName()
    // MODIFIES: this
    // EFFECTS: adds move to this history
    public void addMove(Move move) {
        this.moveHistory.add(move);
    }

    // REQUIRES: letter is uppercase,
    //      between 'A' to 'Z' or '-'
    // EFFECTS: Filters player's words played and returns
    // them in order least to most recent, only including
    // moves which used the given letter at least once
    public List<Move> getListOfWordsPlayedContainingLetter(char letter) {
        List<Move> movesWithLetter = new ArrayList<>();
        List<Move> playedWords = this.getMovesWithWordPlayed();
        for (Move move : playedWords) {
            if (move.moveContainsLetter(letter)) {
                movesWithLetter.add(move);
            }
        }
        return movesWithLetter;
    }
}
