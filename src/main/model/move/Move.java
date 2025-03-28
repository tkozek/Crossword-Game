package model.move;

import org.json.JSONObject;

import model.Direction;
import model.Player;
import persistance.Writable;

// A move made by a player, either a word played or a swap
public class Move implements Writable {
    private MoveType moveType;
    private int pointsForMove;
    private String lettersInvolved;
    private Direction direction;
    private int startRow;
    private int startCol;
    private Player player; // !!! TODO make move only have player names, then move wont have reference to player
    private Player lastPlayer;

    // Constructor if the player played a word on a board
    public Move(Player player, String letters, int startRow, int startCol, int pointsEarned, Direction direction) {
        this.moveType = MoveType.PLAY_WORD;
        this.player = player;
        this.pointsForMove = pointsEarned;
        this.lettersInvolved = letters;
        this.startRow = startRow;
        this.startCol = startCol;
        this.direction = direction;
    }

    // Constructor if the player swapped letters
    public Move(Player player, String swappedLetters, String postSwapLetters) {
        this.moveType = MoveType.SWAP_TILES;
        this.player = player;
        swappedLetters += postSwapLetters;
        this.lettersInvolved = swappedLetters;
        this.pointsForMove = 0;
    }

    // Constructor if the player skipped turn
    public Move(Player player) {
        this.moveType = MoveType.SKIP;
        this.player = player;
        this.pointsForMove = 0;
    }

     // Constructor for end of game where last player gains points from unplayed tiles on opponent racks
    // REQUIRES: Exchanged points >= 0;
    public Move(Player player, Player lastPlayer, String lettersAccountedFor, int exchangedPoints) {
        this.moveType = MoveType.END_GAME_ADJUSTMENT;
        this.player = player;
        this.lastPlayer = lastPlayer;
        this.lettersInvolved = lettersAccountedFor;  
        this.pointsForMove = exchangedPoints;
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
    public String getLettersInvolved() {
        return this.lettersInvolved;
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

    // REQUIRES: this is an END_GAME_ADJUSTMENT
    public Player getLastPlayer() {
        return lastPlayer;
    }

    // REQUIRES: getMoveType() == MoveType.PLAY_WORD, letter is 
    // uppercase between 'A' to 'Z' or '-'
    // EFFECTS: returns true if at least one 
    // letter in the move matches the given letter.
    public boolean moveContainsLetter(char letter) {
        for (int i = 0; i < lettersInvolved.length(); i++) {
            if (lettersInvolved.charAt(i) == letter) {
                return true;
            }
        }
        return false;
    }


    @Override
    // EFFECTS: returns JSONObject representing
    // the this move, depending on its type
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("MoveType", moveType);
        json.put("PlayerName", player.getPlayerName());
        switch (moveType) {
            case PLAY_WORD:
                playWordToJson(json);
                break;
            case SWAP_TILES:
                swapTilesToJson(json);
                break;
            case END_GAME_ADJUSTMENT:
                endGameAdjustmentToJson(json);
                break;
            default: // No additional information is added for SKIP
                break;
        }
        return json;
    }

    // REQUIRES: this is a move of type PLAY_WORD
    // MODIFIES: json
    // EFFECTS: adds information about this move
    // to json
    private void playWordToJson(JSONObject json) {
        json.put("LettersPlayed", lettersInvolved);
        json.put("Row", startRow);
        json.put("Col", startCol);
        json.put("Points", pointsForMove);
        String dir = (Direction.DOWN == direction) ? "D" : "R";
        json.put("Direction", dir);
    }

    // REQUIRES: this is a move of type SWAP_TILES
    // MODIFIES: json
    // EFFECTS: adds information about this move
    // to json
    private void swapTilesToJson(JSONObject json) {
        json.put("InitialLetters", lettersInvolved.substring(0,7));
        json.put("AfterSwapLetters", lettersInvolved.substring(7,lettersInvolved.length()));
    }

    // REQUIRES: this is a move of type END_GAME_ADJUSTMENT
    // MODIFIES: json
    // EFFECTS: adds information about this move
    // to json
    private void endGameAdjustmentToJson(JSONObject json) {
        json.put("FinalPlayer", lastPlayer.getPlayerName());
        json.put("LettersAccountedFor", lettersInvolved);
        json.put("ChangeInPoints", pointsForMove);
    }


    

}
