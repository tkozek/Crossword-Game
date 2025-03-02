package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.board.Board;
import model.move.Move;
import model.tile.LetterTile;
import model.tile.TileBag;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ScrabbleGameTest {

    private ScrabbleGame game;
    private Board board;
    private TileBag tileBag;
    private Player player;


    @BeforeEach
    void setup() {
        board = new Board();
        tileBag = new TileBag();
        game = new ScrabbleGame("test", board, tileBag);
        player = new Player("playerTest", board, tileBag, game);
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

        Move play = new Move(player, board, lettersToPlay, 7, 7, 10, Direction.DOWN);
        game.addMove(play);
        List<Move> moves = game.getHistory().getMoves();
        assertEquals(moves.size(), 1);
        assertEquals(moves.get(0), play);
        Move skip = new Move(player, board);
        game.addMove(skip);
        moves = game.getHistory().getMoves();
        assertEquals(moves.size(), 2);
        assertEquals(moves.get(1), skip);
        assertEquals(moves.get(0), play);
    }

    @Test
    void testGetPlayerByName() {
        game.addPlayer(player);
        Player player2 = new Player("otherPlayer", board, tileBag, game);
        game.addPlayer(player2);
        assertEquals(game.getPlayerByName("playerTest"), player);
        assertEquals(game.getPlayerByName("otherPlayer"), player2);
        assertEquals(game.getPlayerByName("NoPlayerwithThisName"), null);
    }
}
