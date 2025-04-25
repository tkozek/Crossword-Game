package model.move;

import org.json.JSONObject;

import model.Direction;
import persistance.JsonWritable;

// A move made by a player, either a word played or a swap
public class Move implements JsonWritable<JSONObject> {
    private MoveType moveType;
    private int pointsForMove;
    private String lettersInvolved;
    private Direction direction;
    private int startRow;
    private int startCol;
    private String playerName;
    private String lastPlayer;

    // Constructor if the player played a word on a board
    public Move(String player, String letters, int startRow, int startCol, int pointsEarned, Direction direction) {
        this.moveType = MoveType.PLAY_WORD;
        this.playerName = player;
        this.pointsForMove = pointsEarned;
        this.lettersInvolved = letters;
        this.startRow = startRow;
        this.startCol = startCol;
        this.direction = direction;
    }

    // Constructor if the player swapped letters
    public Move(String player, String swappedLetters, String postSwapLetters) {
        this.moveType = MoveType.SWAP_TILES;
        this.playerName = player;
        swappedLetters += postSwapLetters;
        this.lettersInvolved = swappedLetters;
        this.pointsForMove = 0;
    }

    // Constructor if the player skipped turn
    public Move(String player) {
        this.moveType = MoveType.SKIP;
        this.playerName = player;
        this.pointsForMove = 0;
    }

     // Constructor for end of game where last player gains points from unplayed tiles on opponent racks
    public Move(String player, String lastPlayer, String lettersAccountedFor, int pointChange) {
        this.moveType = MoveType.END_GAME_ADJUSTMENT;
        this.playerName = player;
        this.lastPlayer = lastPlayer;
        this.lettersInvolved = lettersAccountedFor;  
        this.pointsForMove = pointChange;
    }

    // EFFECTS: returns JSONObject representing
    // the this move, depending on its type
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("MoveType", moveType);
        json.put("PlayerName", playerName);
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

    // REQUIRES: MoveType is PLAY_WORD, letter is 
    // uppercase between 'A' to 'Z' or '-'
    // EFFECTS: returns true if at least one 
    // letter in the move matches the given letter.
    public boolean moveContainsLetter(char letter) {
        int length = lettersInvolved.length();
        for (int i = 0; i < length; i++) {
            if (lettersInvolved.charAt(i) == letter) {
                return true;
            }
        }
        return false;
    }

    public String getPlayerName() {
        return this.playerName;
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
    public String getLastPlayerName() {
        return lastPlayer;
    }

    // REQUIRES: this is a move of type PLAY_WORD
    // MODIFIES: input JSONObject
    // EFFECTS: adds information about this move to JSONObject
    private void playWordToJson(JSONObject json) {
        json.put("LettersPlayed", lettersInvolved);
        json.put("Row", startRow);
        json.put("Col", startCol);
        json.put("Points", pointsForMove);
        String dir = (Direction.DOWN == direction) ? "D" : "R";
        json.put("Direction", dir);
    }

    // REQUIRES: this is a move of type SWAP_TILES
    // MODIFIES: input JSONObject
    // EFFECTS: adds information about this move to JSONObject
    private void swapTilesToJson(JSONObject json) {
        json.put("InitialLetters", lettersInvolved.substring(0,7));
        json.put("AfterSwapLetters", lettersInvolved.substring(7,lettersInvolved.length()));
    }

    // REQUIRES: this is a move of type END_GAME_ADJUSTMENT
    // MODIFIES: input JSONObject
    // EFFECTS: adds information about this move to JSONObject
    private void endGameAdjustmentToJson(JSONObject json) {
        json.put("FinalPlayer", lastPlayer);
        json.put("LettersAccountedFor", lettersInvolved);
        json.put("ChangeInPoints", pointsForMove);
    }
}
