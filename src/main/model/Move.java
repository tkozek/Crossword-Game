package model;

import java.util.List;
import java.util.ArrayList;

// A move made by a player, either a word played or a swap
public class Move {
    private MoveType moveType;
    private int pointsForMove;
    private List<LetterTile> lettersInvolved;
    private Direction direction;
    private int startRow;
    private int startCol;
    private Board board;
    private Player player;

    // Constructor if the player played a word on a board
    public Move(Player player, Board board, List<LetterTile> letters, 
    int startRow, int startCol, int pointsEarned, Direction direction) {
        this.moveType = MoveType.PLAY_WORD;
        this.player = player;
        this.board = board;
        this.pointsForMove = pointsEarned;
        this.lettersInvolved = letters;
        this.startRow = startRow;
        this.startCol = startCol;
        this.direction = direction;

    }

    // Constructor if the player swapped letters

    public Move(Player player, Board board, List<LetterTile> swappedLetters, List<LetterTile> receivedLetters) {
        this.moveType = MoveType.SWAP_TILES;
        this.player = player;
        this.board = board;
        swappedLetters.addAll(receivedLetters);
        this.lettersInvolved = swappedLetters;
        this.pointsForMove = 0;

    }

    // Constructor for end of game where last player gains points from unplayed tiles on opponent racks
    // REQUIRES: Exchanged points >= 0;
    public Move(Player player, Board board, boolean wasLastToPlay, List<LetterTile> unplayedLetters, int exchangedPoints) {
        if (wasLastToPlay) {
            this.moveType = MoveType.END_GAME_WINNER;
            this.pointsForMove = exchangedPoints;
        } else {
            this.moveType = MoveType.END_GAME_LOSER;
            this.pointsForMove = -1 * exchangedPoints;
        }
        this.player = player;
        this.board = board;
        this.lettersInvolved = unplayedLetters;
        

    }
    public Player getPlayer() {
        return this.player;
    }
    public MoveType getMoveType() {
        return this.moveType;
    }
    // EFFECTS: returns points gained OR lost
    // in a move, 0 for a swap
    public int getPointsForMove() {
        return this.pointsForMove;
    }
    // EFFECTS: returns list of letters played, swapped,
    //  or involved in point adjustments at end game
    public List<LetterTile> getLettersInvolved() {
        return this.lettersInvolved;
    }

    public Board getBoard() {
        return this.board;
    }

    // REQUIRES: getMoveType() == MoveType.PLAY_WORD
    public Direction getDirection() {
        return this.direction;
    }
    public int getStartRow() {
        return this.startRow;
    }
    public int getStartColumn() {
        return this.startCol;
    }
    // REQUIRES: getMoveType() == MoveType.PLAY_WORD, letter is 
    // uppercase between 'A' to 'Z' or '-'
    // EFFECTS: returns true if at least one 
    // letter in the move matches the given letter.
    public boolean moveContainsLetter(char letter) {
        for (LetterTile letterTile : this.getLettersInvolved()) {
            if (letterTile.getCharacter() == letter) {
                return true;
            }
        }
        return false;
    }


    

}
