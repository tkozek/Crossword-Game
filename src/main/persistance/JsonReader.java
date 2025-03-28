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

import model.Player;
import model.ScrabbleGame;
import model.board.Board;
import model.tile.LetterTile;
import model.tile.TileBag;
import model.Direction;

// Represents a reader for JSON files containing
// data to represent a game of Scrabble
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
        TileBag tileBag = new TileBag();
        updateBoardToStoredState(board, jsonObject);
        updateTileBagToStoredState(tileBag, jsonObject);
        ScrabbleGame game = new ScrabbleGame(gameName, board, tileBag);
        addPlayers(game, jsonObject);
        game.setFirstPlayerIndex(jsonObject.getInt("FirstPlayer"));
        addGameHistory(game, jsonObject);
        return game;
    }

    // MODIFIES: game
    // EFFECTS: parses board state from JSON object and adds it to 
    // the Scrabble Game
    private void updateBoardToStoredState(Board board, JSONObject jsonObject) {
        JSONArray boardArray = jsonObject.getJSONArray("Board");
        for (int i = 0; i < boardArray.length(); i++) {
            String value = boardArray.getString(i);
            boolean isOneCharacter = value.length() == 1;
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
        tileBag.emptyDrawPile();
        tileBag.initializeTiles(oldTileCounts);
    }

    // MODIFIES: game
    // EFFECTS: parses players (LetterTile or BoardTile) from JSON object and adds it to 
    // the Scrabble Game
    private void addPlayers(ScrabbleGame game, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Players");
        for (Object json : jsonArray) {
            JSONObject nextPlayer = (JSONObject) json;
            addPlayer(game, nextPlayer);
        }
    }

    // MODIFIES: game
    // EFFECTS: parses player (LetterTile or BoardTile) from JSON object and adds it to 
    // the Scrabble Game
    private void addPlayer(ScrabbleGame game, JSONObject nextPlayer) {
        String name = nextPlayer.getString("name");
        int score = nextPlayer.getInt("score");
        Player player = new Player(name);
        player.setPoints(score);
        addTilesFromSavedRack(player, nextPlayer);
        game.addPlayer(player);
    }

    // MODIFIES: player
    // EFFECTS: adds tiles from player's saved
    // tile rack to their current rack
    private void addTilesFromSavedRack(Player player, JSONObject jsonObject) {
        JSONArray tileRack = jsonObject.getJSONArray("tileRack");
        for (Object json : tileRack) {
            JSONObject tile = (JSONObject) json;
            addTileFromSavedRack(player, tile);
            
        }
    }

    // MODIFIES: player
    // EFFECTS: adds a new Letter Tile to player's rack
    // where the only key in tile is the character,
    private void addTileFromSavedRack(Player player, JSONObject tile) {
        // Loop executes exactly once
        for (String key : tile.keySet()) { 
            int value = tile.getInt(key);
            player.addTile(new LetterTile(key.charAt(0), value));
        }
    }

    // MODIFIES: game
    // EFFECTS: parses full game history (all player histories combined)
    // from JSON object and adds it to the Scrabble Game
    private void addGameHistory(ScrabbleGame game, JSONObject jsonObject) {
        JSONArray historyArray = jsonObject.getJSONArray("History");
        String moveType;
        String name;
        Player player;
        int numMoves = historyArray.length();
        for (int i = 0; i < numMoves; i++) {
            JSONObject moveObject = historyArray.getJSONObject(i);
            moveType = moveObject.getString("MoveType");
            name = moveObject.getString("PlayerName");
            player = game.getPlayerByName(name);
            switch (moveType) {
                case "PLAY_WORD":
                    addWordPlayed(game, moveObject, player);
                    break;        
                case "SWAP_TILES":
                    addSwap(game, moveObject, player);
                    break;  
                case "SKIP":
                    game.logSkipFromHistory(player); 
                    break;
                case "END_GAME_ADJUSTMENT":
                    addEndGame(game, moveObject, player);
            }
        }
    }
    
    // MODIFIES: player, game
    // EFFECTS: adds move of type PLAY to 
    // both player and game history
    private void addWordPlayed(ScrabbleGame game, JSONObject moveObject, Player player) {
        int points = moveObject.getInt("Points");
        int row = moveObject.getInt("Row");
        int col = moveObject.getInt("Col");
        Direction dir = (moveObject.getString("Direction").equals("D")) ? Direction.DOWN : Direction.RIGHT;
        String letters = moveObject.getString("LettersPlayed");
        game.logWord(player, letters, row, col, points, dir);
    }

    // MODIFIES: player, game
    // EFFECTS: adds move of type SWAP to 
    // both player and game history
    private void addSwap(ScrabbleGame game, JSONObject moveObject, Player player) {
        String initialLetters = moveObject.getString("InitialLetters");
        String postSwapLetters = moveObject.getString("AfterSwapLetters");
        game.logSwap(player, initialLetters, postSwapLetters);
    }

    // MODIFIES: player, game
    // EFFECTS: adds move of type SWAP to 
    // both player and game history
    private void addEndGame(ScrabbleGame game, JSONObject moveObject, Player player) {
        String letters = moveObject.getString("LettersAccountedFor");
        Player lastPlayer = game.getPlayerByName(moveObject.getString("FinalPlayer"));
        int pointsChange = moveObject.getInt("ChangeInPoints");
        game.logEndGameAdjustment(player, lastPlayer, letters, pointsChange);
    }
}
