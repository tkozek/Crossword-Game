package persistance;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Board;
import model.LetterTile;
import model.ScrabbleGame;
import model.TileBag;

public class JsonReader {

    private String source;

    // EFFECTS: constructs reader that reads 
    // from data stored in file at source
    // Citation: Based on WorkRoom example on edX
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Scrabble Game from file
    // and returns it; throws IOException if an
    // error occurs reading data from file
    public ScrabbleGame read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGame(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses ScrabbleGame from JSON Object and returns it
    private ScrabbleGame parseGame(JSONObject jsonObject) {
        String gameName = jsonObject.getString("GameName");
        Board board = new Board();
        updateBoardToStoredState(board, jsonObject);
        TileBag tileBag = new TileBag();
        updateTileBagToStoredState(tileBag, jsonObject);
        return null;
    }

    // MODIFIES: game
    // EFFECTS: parses board state from JSON object and adds it to 
    // the Scrabble Game
    private void updateBoardToStoredState(Board board, JSONObject jsonObject) {
        JSONArray boardArray = jsonObject.getJSONArray("Board");
        for (int i = 0; i < boardArray.length(); i++) {
            String value = boardArray.getString(i);
            boolean isOneCharacter = value.length() == 0;
            char valueChar = value.charAt(0);
            boolean isNotSpace = (valueChar != ' ');
            // (value.length() == 0 && 'A' <= value.charAt(0) <= 'Z');
            if (isOneCharacter && isNotSpace) {
                board.updatePositionToMatchOldBoard(new LetterTile(valueChar), i / 15, i % 15);
            }
        }
    }


    // MODIFIES: game
    // EFFECTS: parses tile bag from JSON object and adds it to 
    // the Scrabble Game
    private void updateTileBagToStoredState(TileBag tileBag, JSONObject jsonObject) {
        Map<Character, Integer> oldTileCounts = new HashMap<>();
        JSONObject tiles = jsonObject.getJSONObject("TileBag");
        for (String key : tiles.keySet()) { 
            int value = tiles.getInt(key);
            oldTileCounts.put(key.charAt(0), value);
        }
        tileBag.initializeTiles(oldTileCounts);
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
