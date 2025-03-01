package persistance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Direction;
import model.History;
import model.Player;
import model.ScrabbleGame;
import model.board.Board;
import model.move.Move;
import model.move.MoveType;
import model.tile.LetterTile;
import model.tile.TileBag;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonWriterTest extends JsonTest{
    
    private ScrabbleGame game;
    private Board board;
    private TileBag tileBag;
    private Player player;

    @BeforeEach
    void setup() {
        board = new Board();
        tileBag = new TileBag();
        game = new ScrabbleGame("Test", board, tileBag);
        player = new Player("Tester", board, tileBag, game);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("FileNotFoundException expected for illegal file name");
        } catch (IOException e) {
            // passes test
        }
    }

    @Test
    void testWriterInitialGame() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterInitialGame.json");
            writer.open();
            writer.write(game);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterInitialGame.json");
            game = reader.read();
            assertEquals("Test", game.getName());
            assertTrue(game.getHistory().getMoves().isEmpty());
            TileBag bagThatWasReadFromJson = game.getTileBag();
            assertEquals(bagThatWasReadFromJson.getCurrentLetterFrequencies(), tileBag.getCurrentLetterFrequencies()); 
            // Should test board as well
           // Player playerAddedToGameFromJson = game.getPlayers().get(0);
           // assertEquals(playerAddedToGameFromJson.getPlayerName(), "Tester");
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterTwoMovesHaveBeenPlayed() {
        try {
            game.addPlayer(player);
            tileBag.drawTiles(player);
            List<LetterTile> originalLettersDrawn = player.getTilesOnRack();
            List<LetterTile> originalLetterCopies = new ArrayList<>();
            Map<Character, Integer> originalLetterCounts = new HashMap<>();
            for (LetterTile letter : originalLettersDrawn) {
                char character = letter.getCharacter();
                originalLetterCopies.add(new LetterTile(letter));
                originalLetterCounts.put(character, originalLetterCounts.getOrDefault(character, 0) + 1);
            }
            player.selectTile(0);
            List<LetterTile> selectedTiles = player.getSelectedTiles();
            LetterTile letter1 = new LetterTile(selectedTiles.get(0));
            // 
            int score = board.playWord(selectedTiles, 7, 7, Direction.DOWN);
            // log word logs it to player's game too
            player.logWord(board, 7, 7, score, Direction.DOWN);
            player.removeSelectedTiles();
            //player.logSkippedTurn(board);
            // should expect there to be one letters played there that match
            // and expect tileBag to be missing the 7 originally drawn tiles      
            JsonWriter writer = new JsonWriter("./data/testWriterTwoMovesPlayed.json");
            writer.open();
            writer.write(game);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterTwoMovesPlayed.json");
            game = reader.read();
            assertEquals(game.getTileBag().numTilesRemaining(), TileBag.TOTAL_LETTERS_INITIALLY - 7);
            Map<Character, Integer> afterReadingTileBagCounts = game.getTileBag().getCurrentLetterFrequencies();

            char letter1Char = letter1.getCharacter();

            int originalLetter1CountDrawn = originalLetterCounts.get(letter1.getCharacter());

            int initializedLetter1BagCount = tileBag.getInitialLetterFrequencies().get(letter1Char);
            //Check tilebag counts
            assertEquals(afterReadingTileBagCounts.get(letter1Char) + originalLetter1CountDrawn, initializedLetter1BagCount);
            Board newBoard = game.getBoard();
            List<LetterTile> letterToPlay = new ArrayList<>();
            letterToPlay.add(new LetterTile('A', 1));
            assertFalse(newBoard.canPlay(letterToPlay, 7   , 7, Direction.DOWN));
            LetterTile firstPlacedLetter = (LetterTile) newBoard.getTileAtPositionOnBoard(7,7);
            // Check board updated correctly
            assertEquals(firstPlacedLetter.getCharacter(), letter1Char);
            // 1 play 1 skip
            History newGameHistory = game.getHistory();
            assertEquals(newGameHistory.getMoves().size(), 1);
            Move firstMove = newGameHistory.getMoves().get(0);
            assertEquals(firstMove.getMoveType(), MoveType.PLAY_WORD);
            assertEquals(newGameHistory.getName(), "Test");
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
 
    
}
