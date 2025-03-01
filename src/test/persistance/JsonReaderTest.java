package persistance;

import java.io.IOException;

import org.junit.Test;

import model.Player;
import model.ScrabbleGame;
import model.board.Board;
import model.move.MoveType;
import model.tile.TileBag;

import static org.junit.jupiter.api.Assertions.*;


public class JsonReaderTest extends JsonTest{

    private TileBag testBag;

    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/iwillnevernameafilethis.json");
        try {
            ScrabbleGame game = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // passes test if it catches an exemption thrown
        }
    }

    @Test
    public void testReaderEmptyGame() {
        JsonReader reader = new JsonReader("./data/emptyGame.json");
        try {
            ScrabbleGame game = reader.read();
            assertEquals(game.getName(), "EmptyGameName");
            assertEquals(game.getNumPlayers(), 0);
            assertFalse(game.getBoard() == null);
        } catch (IOException e) {
            fail("Couldn't read file");
        }
    }

    @Test
    public void testReaderStartOfGame() {
        JsonReader reader = new JsonReader("./data/testReaderThreePlayersThreeMovesEmptyRacksFullBag.json");
        testBag = new TileBag();
        try {
            ScrabbleGame game = reader.read();
            assertEquals(game.getName(), "Initial Game");
            assertEquals(game.getNumPlayers(), 3);
            // Override Board.equals() and update line below this
            assertFalse(game.getBoard() == null);
            assertEquals(game.getTileBag().getCurrentLetterFrequencies(), testBag.getInitialLetterFrequencies());
            assertEquals(game.getHistory().getMoves().size(), 3);
            Player player = game.getPlayers().get(0);
            assertEquals(player.getPlayerName(), "Trevor");
            assertEquals(player.getPointsThisGame(), 0);
            assertEquals(player.getHistory().getMoves().get(0).getMoveType(), MoveType.SKIP);
        } catch (IOException e) {
            fail("Couldn't read file");
        }
    }

    @Test
    public void testReaderTwoPlayersTwoMovesFullTileRacks() {
        JsonReader reader = new JsonReader("./data/testReaderTwoWordsPlayedFullRacks.json");
        testBag = new TileBag();
        try {
            ScrabbleGame game = reader.read();
            assertEquals(game.getName(), "Initial Game");
            assertEquals(game.getNumPlayers(), 2);
            // Override Board.equals() and update line below this
            assertFalse(game.getBoard() == null);
            assertFalse(game.getTileBag().getCurrentLetterFrequencies().equals(testBag.getInitialLetterFrequencies()));

            assertEquals(game.getTileBag().numTilesRemaining(), 100- 7 * 2 - 2 - 5);

            assertEquals(game.getHistory().getMoves().size(), 2);
            Player player = game.getPlayers().get(0);
            assertEquals(player.getPlayerName(), "Tester");
            assertEquals(player.getPointsThisGame(), 10);
            Player player2 = game.getPlayers().get(1);
            assertEquals(player2.getPointsThisGame(), 6);

            assertTrue(player.getSelectedTiles().isEmpty());
            assertEquals(player.getTilesOnRack().get(5).getCharacter(), 'Q');

        } catch (IOException e) {
            fail("Couldn't read file");
        }
    }
    
}
