package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

public class PlayerTest {
    
    private Board board;
    private TileBag testBag;
    private Set<Character> set;
    private Player testPlayer;
    private Player testPlayer2;
    private Board testBoard;
    private HashMap<Character, Integer> preSwapChars;
    private HashMap<Character, Integer> postSwapChars;

    @BeforeEach
    void runBefore() {
        testBag = new TileBag();
        board =  new Board();
        testPlayer = new Player("Trevor", testBoard,testBag);
        testPlayer2 = new Player("Rovert", testBoard,testBag);
        preSwapChars = new HashMap<>();
        postSwapChars = new HashMap<>();
    }

    @Test
    void testConstructor() {
        assertEquals(0, testPlayer.getPointsThisGame());
        assertEquals(0, testPlayer.getPlayerMoves().size());
        assertEquals(0, testPlayer.getNumTilesOnRack());
        assertEquals("Trevor", testPlayer.getPlayerName());
        assertEquals(0, testPlayer.getPlayerMoves());

    }
    @Test
    void testSetPlayerName() {
        assertEquals("Trevor", testPlayer.getPlayerName());
        testPlayer.setPlayerName("Michael");
        assertEquals("Michael", testPlayer.getPlayerName());
    }
    @Test
    void testDrawTilesEmptyRack() {
        assertEquals(testPlayer.getNumTilesOnRack(), 0);
        testPlayer.drawTiles();
        assertEquals(testPlayer.getNumTilesOnRack(), Player.MAX_NUM_TILES);
    }
    @Test
    void testSwapAllTiles() {
        assertEquals(testPlayer.getNumTilesOnRack(), 0);
        testPlayer.drawTiles();
        assertEquals(testPlayer.getNumTilesOnRack(), Player.MAX_NUM_TILES);

        List<LetterTile> initialLetters = testPlayer.getTilesOnRack();

        int numTilesToSwap  = 7;
        assertTrue(testBag.numTilesRemaining() >= numTilesToSwap);
        testPlayer.swapTiles(initialLetters);

        List<LetterTile> postSwapLetters = testPlayer.getTilesOnRack();
        int numThingsChanged = 0;
        for (int i = 0; i < numTilesToSwap; i++) {
            if (!initialLetters.contains(postSwapLetters.get(i))) {
                numThingsChanged++;
            }
    }
    // Chance of drawing one of the 7 initial tiles first is 7/100,
    // Chance of drawing one of the 6 remaining initial tiles is 6/99
    // Chance of drawing one of the 5 remaining initial tiles is 5/98
    // .....
    // Chance of drawing all the tiles we had initially is 7!/(100!/93!)
    // So the chances of this assertion failing are 
    // 50.4 / 806,781,064,320 about 6.25 in a 100 billion chance
        assertTrue(numThingsChanged>0);
    }
    @Test
    void testSwapZeroTiles() {
        assertEquals(testPlayer.getNumTilesOnRack(), 0);
        testPlayer.drawTiles();
        assertEquals(testPlayer.getNumTilesOnRack(), Player.MAX_NUM_TILES);
        
        List<LetterTile> initialLetters = testPlayer.getTilesOnRack();
        //Select tiles to swap
        List<LetterTile> selectedLetters = testPlayer.getSelectedTiles();
        int numTilesToSwap  = selectedLetters.size();
        assertEquals(numTilesToSwap,0);
        testPlayer.swapTiles(selectedLetters);

        List<LetterTile> postSwapLetters = testPlayer.getTilesOnRack();
        int numThingsChanged = 0;
        for (int i = 0; i < Player.MAX_NUM_TILES; i++) {
            if (!initialLetters.contains(postSwapLetters.get(i))) {
                numThingsChanged++;
            }
    }
        assertEquals(numThingsChanged,0);
    

    }
    @Test
    void testAddtile() {
        assertEquals(testPlayer.getNumTilesOnRack(), 0);
        LetterTile a = new LetterTile('A',1);
        testPlayer.addTile(a);
        assertEquals(testPlayer.getNumTilesOnRack(), 1);
    }

    @Test
    void testTileAddedToRackIsRemovedFromTileBag() {
        assertEquals(testPlayer.getNumTilesOnRack(), 0);
        assertEquals(testBag.numTilesRemaining(), TileBag.TOTAL_LETTERS_INITIALLY);
        testPlayer.drawTiles();
        List<LetterTile> drawnTiles = testPlayer.getTilesOnRack();
        for (LetterTile drawnTile : drawnTiles) {
            assertFalse(testBag.contains(drawnTile));
        }
        assertEquals(testPlayer.getNumTilesOnRack(), 7);
    }

    @Test
    void testSelectTile() {
        testPlayer.drawTiles();
        List <LetterTile> drawnTiles = testPlayer.getTilesOnRack();
        //Player.MAX_NUM_TILES == 7
        assertEquals(testPlayer.getNumTilesOnRack(), Player.MAX_NUM_TILES);
        testPlayer.selectTile(0);
        assertTrue(testPlayer.getSelectedTiles().contains(drawnTiles.get(0)));
        assertFalse(testPlayer.getSelectedTiles().contains(drawnTiles.get(1)));
        assertFalse(testPlayer.getSelectedTiles().contains(drawnTiles.get(2)));
        assertFalse(testPlayer.getSelectedTiles().contains(drawnTiles.get(3)));
        assertFalse(testPlayer.getSelectedTiles().contains(drawnTiles.get(4)));
        assertFalse(testPlayer.getSelectedTiles().contains(drawnTiles.get(5)));
        assertFalse(testPlayer.getSelectedTiles().contains(drawnTiles.get(6)));

        // Clear
        testPlayer.clearSelectedTiles();
        assertTrue(testPlayer.getSelectedTiles().isEmpty());

    }
    @Test 
    void testPlayWord() {
        testPlayer.drawTiles();
        // no moves played yet
        assertEquals(testPlayer.getPlayerMoves().size(),0);

        // Center square as if first turn
        assertTrue(board.squareIsAvailable(7,7));
        assertTrue(board.squareIsAvailable(7,8));
        //2nd rack tile placed first, 1st rack tile placed second
        testPlayer.selectTile(1);
        testPlayer.selectTile(0);
        List<LetterTile> selectedTiles = testPlayer.getSelectedTiles();
        LetterTile tile1 = selectedTiles.get(0);
        LetterTile tile2 = selectedTiles.get(1);
        assertEquals(tile1, testPlayer.getTilesOnRack().get(1));
        assertEquals(tile2, testPlayer.getTilesOnRack().get(0));
        testPlayer.playWord(selectedTiles,7,7, RIGHT);
        assertEquals(board[7][7].equals(tile1));
        assertEquals(board[7][8].equals(tile2));
        int pointsGained = board.getPointsForMove(selectedTiles,7,7,RIGHT);
        //Double word score at center of board
        assertEquals(pointsGained, 2* (tile1.getLetterPoints() + tile2.getLetterPoints()));
        assertEquals(testPlayer.getPointsThisGame(), pointsGained);
        // Selected tiles should be removed from rack once played
        assertTrue(testPlayer.getSelectedTiles().isEmpty());
        //New move should be added
        assertEquals(testPlayer.getPlayerMoves().size(),1);


    }

}

