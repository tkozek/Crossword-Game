package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.board.Board;
import model.move.Move;
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
        player = new Player("playerTest", tileBag, game);
        player2 = new Player("P", tileBag, game);
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

    @Test
    void testAddMoves() {
        game.setName("Trevor's game");
        assertEquals(game.getName(), "Trevor's game");
        tileBag.drawTiles(player);
        List<LetterTile> lettersToPlay = player.getTilesOnRack();

        Move play = new Move(player, lettersToPlay, 7, 7, 10, Direction.DOWN);
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
        Player player2 = new Player("otherPlayer", tileBag, game);
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
}
