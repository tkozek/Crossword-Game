package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.board.Board;
import model.move.Move;
import model.move.MoveType;
import model.tile.LetterTile;
import model.tile.TileBag;

import java.util.List;
import java.util.Map;

public class ScrabbleGameTest {

    private ScrabbleGame game;
    private Board board;
    private TileBag tileBag;
    private Player player;
    private Player player2;


    @BeforeEach
    void setup() {
        board = new Board();
        tileBag = new TileBag();
        game = new ScrabbleGame("test", board, tileBag);
        player = new Player("playerTest", game);
        player2 = new Player("P", game);
    }

    @Test
    void testConstructor() {
        assertEquals("test", game.getName());
        game.addPlayer(player);
        assertEquals(game.getNumPlayers(), 1);
        assertEquals(game.getPlayers().get(0), player);
        assertTrue(game.getHistory().getMoves().isEmpty());        
        assertEquals(game.getBoard(), board);
        assertEquals(game.getTileBag(), tileBag);
    }

    @Test
    void testSetName() {
        game.setName("Trevor's game");
        assertEquals(game.getName(), "Trevor's game");
    }

// EFFECTS: returns list of letter tiles
    // based on input string
    private String getStringFromLetters(List<LetterTile> letters) {
        String result = "";
        for (LetterTile letter : letters) {
            result += letter.getString();
        }
        return result;
    }  
    
    @Test
    void testAddMoves() {
        game.setName("Trevor's game");
        assertEquals(game.getName(), "Trevor's game");
        tileBag.drawTiles(player);
        List<LetterTile> lettersToPlay = player.getTilesOnRack();

        Move play = new Move(player, getStringFromLetters(lettersToPlay), 7, 7, 10, Direction.DOWN);
        game.addMove(play);
        List<Move> moves = game.getHistory().getMoves();
        assertEquals(moves.size(), 1);
        assertEquals(moves.get(0), play);
        Move skip = new Move(player);
        game.addMove(skip);
        moves = game.getHistory().getMoves();
        assertEquals(moves.size(), 2);
        assertEquals(moves.get(1), skip);
        assertEquals(moves.get(0), play);
    }

    @Test
    void testGetPlayerByName() {
        game.addPlayer(player);
        Player player2 = new Player("otherPlayer", game);
        game.addPlayer(player2);
        assertEquals(game.getPlayerByName("playerTest"), player);
        assertEquals(game.getPlayerByName("otherPlayer"), player2);
        assertEquals(game.getPlayerByName("NoPlayerwithThisName"), null);
    }

    @Test
    void testFirstPlayer() {
        assertEquals(0, game.getFirstPlayerIndex());
        game.addPlayer(player);
        game.addPlayer(player2);
        game.setFirstPlayerIndex(1);
        assertEquals(1, game.getFirstPlayerIndex());
        game.setFirstPlayer(player);
        assertEquals(0, game.getFirstPlayerIndex());
    }

    @Test 
    void testGetNumEachCharInBagAndOpponentsNoLettersOnRackOrBoard() {
        //List<LetterTile> letters = new ArrayList<>();
        Map<Character, Integer> counts = game.getNumEachCharInBagAndOpponents(player);
        Map<Character, Integer> drawPileCounts = tileBag.getInitialLetterFrequencies();
        
        // Both should have all keys
        assertEquals(counts.keySet().size(), drawPileCounts.keySet().size());
        
        //.equals for Map should compare that all keys and values are the same
        // in each map by default. which is the expected behaviour for this test
        assertTrue(counts.equals(drawPileCounts));
        
    }

    @Test
    void testScorePerpendicularConnectedByBlank() {
        player.addTile(new LetterTile('B', 3));
        player.addTile(new LetterTile('R', 1));
        player.addTile(new LetterTile('I', 1));
        player.addTile(new LetterTile('N', 1));
        player.addTile(new LetterTile('K', 5));
        
        for (int i = 0; i < 5; i++) {
            player.selectTile(i);
        }
        assertEquals(32, game.playWord(player, 7, 7, Direction.DOWN));
        assertEquals(1, game.getHistory().getMoves().size());
        assertEquals(1, player.getHistory().getMoves().size());
        assertEquals(player.getNumTilesOnRack(), 7);
        assertEquals(game.getTileBag().numTilesRemaining(), 100 - 7);
        assertEquals(32, player.getPointsThisGame());

        for (int i = 0; i < 7; i++) {
            player.selectTile(i);
        }
        player.removeSelectedTiles();
        assertEquals(player.getNumTilesOnRack(), 0);
        player.addTile(new LetterTile('I', 1));
        player.addTile(new LetterTile('T', 1));
        player.addTile(new LetterTile('E', 1));
        player.addTile(new LetterTile('S', 1));

        assertEquals(player.getNumTilesOnRack(), 4);
        for (int i = 0; i < 4; i++) {
            player.selectTile(i);
        }
        assertEquals(18, game.playWord(player, 11, 8, Direction.RIGHT));
        assertEquals(player.getNumTilesOnRack(), 7);
        for (int i = 0; i < 7; i++) {
            player.selectTile(i);
        }
        player.removeSelectedTiles();
        player.addTile(new LetterTile('-'));
        player.addTile(new LetterTile('I'));
        player.addTile(new LetterTile('T'));
        for (int i = 0; i < 3; i++) {
            player.selectTile(i);
        }
        assertEquals(8, game.playWord(player, 10, 10, Direction.RIGHT));
    }

    @Test
    void testEndGameAdjustments() {
        game.addPlayer(player);
        game.addPlayer(player2);
        tileBag.drawTiles(player2);
        assertEquals(player2.getNumTilesOnRack(), 7);
        int scoreToLoseP2 = 0;
        for (LetterTile unplayedLetter : player2.getTilesOnRack()) {
            scoreToLoseP2 += unplayedLetter.getLetterPoints();
        }
        player2.setPoints(scoreToLoseP2);
        //player goes out, gains all those unplayed points
        // player 2 loses them all so p2 has zero points left

        //add 1 point so we are convinced that they didn't just swap entire scores
        player.setPoints(1);
        game.performEndGameAdjustments(player);
        assertEquals(player.getPointsThisGame(), 1 + scoreToLoseP2);
        assertEquals(player2.getPointsThisGame(), 0);
        assertEquals(1, player.getHistory().getMoves().size());
        assertEquals(1, player2.getHistory().getMoves().size());
        assertEquals(MoveType.END_GAME_ADJUSTMENT, player2.getHistory().getMoves().get(0));
        assertEquals(MoveType.END_GAME_ADJUSTMENT, player.getHistory().getMoves().get(0));
    }
}
