package model;

import java.util.List;
import java.util.ArrayList;

// A move made by a player, either a word played or a swap
public class Move {
    
    // Constructor if the player played a word on a board
    public Move(Player p, Board board, List<LetterTile> letters, int startRow, int startCol, int pointsEarned) {

    }

    // Constructor if the player swapped letters

    public Move(Player p, Board b, List<LetterTile> swappedLetters, List<LetterTile> receivedLetters) {

    }

    // Constructor for end of game where last player gains points from unplayed tiles on opponent racks
    // REQUIRES: Exchanged points > 0;
    public Move(Player winner, Player loser, Board b, List<LetterTile> unplayedLetters, int exchangedPoints) {

    }

}
