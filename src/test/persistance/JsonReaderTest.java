package persistance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import model.Player;
import model.ScrabbleGame;
import model.move.Move;
import model.move.MoveType;
import model.tile.TileBag;



public class JsonReaderTest extends JsonTest {

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
        JsonReader reader = new JsonReader("./data/test/reader/testReadEmptyGame.json");
        try {
            ScrabbleGame game = reader.read();
            assertEquals(game.getNumPlayers(), 0);
            assertNotNull(game.getBoard());
        } catch (IOException e) {
            fail("Couldn't read file");
        }
    }

    @Test
    public void testReaderStartOfGame() {
        JsonReader reader = new JsonReader("./data/test/reader/testReaderThreePlayersThreeMovesEmptyRacksFullBag.json");
        testBag = new TileBag();
        try {
            ScrabbleGame game = reader.read();
            assertEquals(game.getNumPlayers(), 3);
            // Override Board.equals() and update line below this
            assertNotNull(game.getBoard());
            assertEquals(game.getTileBag().getCurrentLetterFrequencies(), testBag.getInitialLetterFrequencies());
            assertEquals(game.getMoves().size(), 3);
            Player player = game.getPlayers().get(0);
            assertEquals(player.getPlayerName(), "Trevor");
            assertEquals(player.getPointsThisGame(), 0);
            assertEquals(player.getMoves().get(0).getMoveType(), MoveType.SKIP);
        } catch (IOException e) {
            fail("Couldn't read file");
        }
    }

    @Test
    public void testReaderTwoPlayersTwoMovesFullTileRacks() {
        JsonReader reader = new JsonReader("./data/test/reader/testReaderTwoWordsPlayedFullRacks.json");
        testBag = new TileBag();
        try {
            ScrabbleGame game = reader.read();
            assertEquals(game.getNumPlayers(), 2);
            // Override Board.equals() and update line below this
            assertNotNull(game.getBoard());
            assertFalse(game.getTileBag().getCurrentLetterFrequencies().equals(testBag.getInitialLetterFrequencies()));

            assertEquals(game.getTileBag().numTilesRemaining(), 100 - 7 * 2 - 2 - 5);

            assertEquals(game.getMoves().size(), 2);
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
    
    @Test
    public void testReaderEndGame() {
        JsonReader reader = new JsonReader("./data/test/reader/testReadEndGame.json");
        testBag = new TileBag();
        try {
            ScrabbleGame game = reader.read();
            assertEquals(game.getNumPlayers(), 14);
            // Override Board.equals() and update line below this
            assertNotNull(game.getBoard());
            assertTrue(game.getTileBag().getCurrentLetterFrequencies().isEmpty());

            assertEquals(game.getTileBag().numTilesRemaining(), 0);

            assertEquals(game.getMoves().size(), 16);
            Player player;
            List<Integer> endScores = new ArrayList<>();
            endScores.add(-16);
            endScores.add(200);
            endScores.add(-31);
            endScores.add(-15);
            endScores.add(-10);
            endScores.add(-12);
            endScores.add(-15);
            endScores.add(-10);
            endScores.add(-7);
            endScores.add(-10);
            endScores.add(-8);
            endScores.add(-7);
            endScores.add(-7);
            endScores.add(-7);
            int index;
            for (int i = 1; i <= 14; i++) {
                index = i - 1;
                player = game.getPlayers().get(index);
                assertEquals(player.getPlayerName(), "Player" + i);
                assertEquals(player.getPointsThisGame(), endScores.get(index));
                assertTrue(player.getTilesOnRack().isEmpty());
            }

            Player p1 = game.getPlayerByName("Player1");
            List<Move> p1Moves = p1.getMoves();
            assertEquals(p1Moves.size(), 2);
            assertEquals(p1Moves.get(0).getMoveType(), MoveType.PLAY_WORD);
            assertEquals(p1Moves.get(0).getLettersInvolved(), "IT");
            assertEquals(p1Moves.get(0).getPointsForMove(), 4);

            assertEquals(p1Moves.get(1).getMoveType(), MoveType.END_GAME_ADJUSTMENT);
            assertEquals(p1Moves.get(1).getLettersInvolved(), "EEADLZH");
            assertEquals(p1Moves.get(1).getPointsForMove(), -20);

            Player p2 = game.getPlayerByName("Player2");
            List<Move> p2Moves = p2.getMoves();
            assertEquals(p2Moves.size(), 2);
            assertEquals(p2Moves.get(0).getMoveType(), MoveType.PLAY_WORD);
            assertEquals(p2Moves.get(0).getLettersInvolved(), "WIBJYMF");
            assertEquals(p2Moves.get(0).getPointsForMove(), 41);

            assertEquals(p2Moves.get(1).getMoveType(), MoveType.END_GAME_ADJUSTMENT);
            assertEquals(p2Moves.get(1).getLettersInvolved(), 
                    "VBOQFXOPEKEAACMILIBLYATCHUERVPENWORDUGGLRAASONTOISSAURGDILUEDSNOTAEAENTOEIIEENNROIEREEADLZH");
            assertEquals(p2Moves.get(1).getPointsForMove(), 159);
            assertEquals(p2.getPointsThisGame(), 200);

            for (int i = 3; i <= 14; i++) {
                index = i - 1;
                player = game.getPlayers().get(index);
                assertEquals(player.getMoves().size(), 1);
                assertEquals(player.getMoves().get(0).getMoveType(), MoveType.END_GAME_ADJUSTMENT);
                assertEquals(player.getMoves().get(0).getPointsForMove(), endScores.get(index));
            }
        } catch (IOException e) {
            fail("Couldn't read file");
        }
    }
}
