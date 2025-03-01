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
            JsonReader reader = new JsonReader("./data/testWriterInitialGame");
            game = reader.read();
            assertEquals("Test", game.getName());
            assertTrue(game.getHistory().getMoves().isEmpty());
            TileBag bagThatWasReadFromJson = game.getTileBag();
            assertEquals(bagThatWasReadFromJson.getCurrentLetterFrequencies(), tileBag.getCurrentLetterFrequencies()); 
            // Should test board as well
            Player playerAddedToGameFromJson = game.getPlayers().get(0);
            assertEquals(playerAddedToGameFromJson.getPlayerName(), "Tester");
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterTwoMovesHaveBeenPlayed() {
        try {
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
            player.selectTile(1);
            List<LetterTile> selectedTiles = player.getSelectedTiles();
            LetterTile letter1 = new LetterTile(selectedTiles.get(0));
            LetterTile letter2 = new LetterTile(selectedTiles.get(1));
            // 
            int score = board.playWord(selectedTiles, 7, 7, Direction.DOWN);
            // log word logs it to player's game too
            player.logWord(board, 7, 7, score, Direction.DOWN);
            player.removeSelectedTiles();
            player.logSkippedTurn(board);
            // should expect there to be two letters played there that match
            // and expect tileBag to be missing the 7 originally drawn tiles      
            JsonWriter writer = new JsonWriter("./data/testWriterTwoMovesPlayed.json");
            writer.open();
            writer.write(game);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterTwoMovesPlayed.json");
            ScrabbleGame newGame = reader.read();
            assertEquals(newGame.getTileBag().numTilesRemaining(), TileBag.TOTAL_LETTERS_INITIALLY - 7);
            Map<Character, Integer> afterReadingTileBagCounts = newGame.getTileBag().getCurrentLetterFrequencies();

            char letter1Char = letter1.getCharacter();
            char letter2Char = letter2.getCharacter();

            int originalLetter1CountDrawn = originalLetterCounts.get(letter1.getCharacter());
            int originalLetter2CountDrawn = originalLetterCounts.get(letter2.getCharacter());

            int initializedLetter1BagCount = tileBag.getInitialLetterFrequencies().get(letter1Char);
            int initializedLetter2BagCount = tileBag.getInitialLetterFrequencies().get(letter2Char);
            //Check tilebag counts
            assertEquals(afterReadingTileBagCounts.get(letter1Char) + originalLetter1CountDrawn, initializedLetter1BagCount);
            assertEquals(afterReadingTileBagCounts.get(letter2Char) + originalLetter2CountDrawn, initializedLetter2BagCount);
            Board newBoard = newGame.getBoard();
            LetterTile firstPlacedLetter = (LetterTile) newBoard.getTileAtPositionOnBoard(7,7);
            LetterTile secondPlacedLetter = (LetterTile) newBoard.getTileAtPositionOnBoard(8,7);
            // Check board updated correctly
            assertEquals(firstPlacedLetter.getCharacter(), letter1Char);
            assertEquals(secondPlacedLetter.getCharacter(), letter2Char);
            // 1 play 1 skip
            History newGameHistory = newGame.getHistory();
            assertEquals(newGameHistory.getMoves().size(), 2);
            Move firstMove = newGameHistory.getMoves().get(0);
            Move secondMove = newGameHistory.getMoves().get(1);
            assertEquals(firstMove.getMoveType(), MoveType.PLAY_WORD);
            assertEquals(secondMove.getMoveType(), MoveType.SKIP);
            assertEquals(newGameHistory.getName(), "Test History");
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    
}
