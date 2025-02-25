package persistance;

import java.io.IOException;

import org.json.JSONObject;

import model.ScrabbleGame;

public class JsonReader {

    private String source;

    // EFFECTS: constructs reader that reads 
    // from data stored in file at source
    // Citation: Based on WorkRoom example on edX
    public JsonReader(String source) {

    }

    // EFFECTS: reads Scrabble Game from file
    // and returns it; throws IOException if an
    // error occurs reading data from file
    public ScrabbleGame read() throws IOException {
        return null;
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        return "";
    }

    // EFFECTS: parses ScrabbleGame from JSON Object and returns it
    private ScrabbleGame parseGame(JSONObject jsonObject) {
        return null;
    }

    // MODIFIES: game
    // EFFECTS: parses board state from JSON object and adds it to 
    // the Scrabble Game
    private void addBoard(ScrabbleGame game, JSONObject jsonObject) {

    }

    // MODIFIES: game
    // EFFECTS: parses board space (LetterTile or BoardTile) from JSON object and adds it to 
    // the Scrabble Game
    private void addBoardSpace(ScrabbleGame game, JSONObject jsonObject) {

    }

    // MODIFIES: game
    // EFFECTS: parses tile bag from JSON object and adds it to 
    // the Scrabble Game
    private void addTileBag(ScrabbleGame game, JSONObject jsonObject) {
        
    }

    // MODIFIES: game
    // EFFECTS: parses players (LetterTile or BoardTile) from JSON object and adds it to 
    // the Scrabble Game
    private void addPlayers(ScrabbleGame game, JSONObject jsonObject) {
        
    }

    // MODIFIES: game
    // EFFECTS: parses player (LetterTile or BoardTile) from JSON object and adds it to 
    // the Scrabble Game
    private void addPlayer(ScrabbleGame game, JSONObject jsonObject) {
        
    }

    // MODIFIES: game
    // EFFECTS: parses full game history (all player histories combined)
    // from JSON object and adds it to the Scrabble Game
    private void addGameHistory(ScrabbleGame game, JSONObject jsonObject) {
        
    }

    // MODIFIES: game
    // EFFECTS: parses a player's history from JSON object and adds it to 
    // the Scrabble Game
    private void addPlayerHistory(ScrabbleGame game, JSONObject jsonObject) {
        
    }

    // MODIFIES: game
    // EFFECTS: parses player tiles from JSON object and adds it to 
    // the player in the Scrabble Game
    private void addPlayerTiles(ScrabbleGame game, JSONObject jsonObject) {
        
    }






}
