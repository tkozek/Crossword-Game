package persistance;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import model.Board;
import model.Player;
import model.ScrabbleGame;
import model.TileBag; 

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
        JsonReader reader = new JsonReader("./data/testEmptyGame.json");
        try {
            ScrabbleGame game = reader.read();
            assertEquals(game.getName(), "EmptyGameName");
            assertEquals(game.getNumPlayers(), 0);
// Not going to initialize a default board with [] in json because i dont
// want to check for modifications or have to update modification status 
// throughout ui
            assertEquals(game.getBoard(), null);
            assertEquals(game.getTileBag(), null);
            assertEquals(game.getHistory(), null);
        } catch (IOException e) {
            fail("Couldn't read file");
        }
    }

    @Test
    public void testReaderStartOfGame() {
        JsonReader reader = new JsonReader("./data/testInitialGame.json");
        testBag = new TileBag();
        try {
            ScrabbleGame game = reader.read();
            assertEquals(game.getName(), "Initial Game");
            // Override Board.equals()
            assertTrue(game.getBoard().equals(new Board()));
            assertEquals(game.getTileBag().getCurrentLetterFrequencies(), testBag.getInitialLetterFrequencies());
            assertTrue(game.getHistory().getMoves().isEmpty());
            Player player = game.getPlayers().get(0);
            assertEquals(player.getPlayerName(), "testPlay");
            assertEquals(player.getPointsThisGame(), 0);
        } catch (IOException e) {
            fail("Couldn't read file");
        }
    }





}
