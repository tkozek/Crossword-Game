/* package model;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

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
        player = new Player("player", board, tileBag, game);

    }

    @Test
    public void testConstructor() {
        assertEquals("test", game.getName());
        game.addPlayer(player);
        assertEquals(game.getNumPlayers(), 1);
        assertEquals(game.getPlayers().get(0), player);
        assertTrue(game.getHistory().getMoves().isEmpty());        
    }
}
 */