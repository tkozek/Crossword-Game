package model;

import java.util.List;
import java.util.ArrayList;

// A move made by a player, either a word played or a swap
public class Move {
    
    // Constructor if the player played a word on a board
    public Move(Player p, Board board, List<LetterTile> letters, int startRow, int startCol, int pointsEarned, Direction direction) {

    }

    // Constructor if the player swapped letters

    public Move(Player p, Board b, List<LetterTile> swappedLetters, List<LetterTile> receivedLetters) {

    }

    // Constructor for end of game where last player gains points from unplayed tiles on opponent racks
    // REQUIRES: Exchanged points >= 0;
    public Move(Player player, Board b, boolean wasLastToPlay, List<LetterTile> unplayedLetters, int exchangedPoints) {

    }
    
    public MoveType getMoveType() {
        return MoveType.PLAY_WORD;
    }
    // EFFECTS: returns points gained OR lost
    // in a move, 0 for a swap
    public int getPointsForMove() {
        return 0;
    }
    // EFFECTS: returns list of letters played, swapped,
    //  or involved in point adjustments at end game
    public List<LetterTile> getLettersInvolved() {
        return null;
    }

    // REQUIRES: getMoveType() == MoveType.PLAY_WORD
    public Direction getDirection() {
        return Direction.DOWN;
    }
    // REQUIRES: getMoveType() == MoveType.PLAY_WORD, letter is 
    // uppercase between 'A' to 'Z' or '-'
    // EFFECTS: returns true if at least one 
    // letter in the move matches the given letter.
    public boolean moveContainsLetter(char letter) {
        return false;
    }


    

}
