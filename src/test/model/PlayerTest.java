package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
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
        testBoard =  new Board();
        testPlayer = new Player("Trevor", testBoard,testBag);
        testPlayer2 = new Player("Rovert", testBoard,testBag);
        preSwapChars = new HashMap<>();
        postSwapChars = new HashMap<>();
    }

    @Test
    void testConstructor() {
        assertEquals(0, testPlayer.getPointsThisGame());
        assertEquals(0, testPlayer.getHistory().getMoves().size());
        assertEquals(0, testPlayer.getNumTilesOnRack());
        assertEquals("Trevor", testPlayer.getPlayerName());

    }

    @Test
    void testSelectTileAlreadySelected() {
        testBag.drawTiles(testPlayer);
        assertEquals(testPlayer.getNumTilesOnRack(), 7);
        assertTrue(testPlayer.selectTile(0));
        assertFalse(testPlayer.selectTile(0));
        assertTrue(testPlayer.selectTile(2));
        assertFalse(testPlayer.selectTile(2));
        assertTrue(testPlayer.selectTile(6));
        assertFalse(testPlayer.selectTile(0));
        assertEquals(testPlayer.getSelectedTiles().size(), 3);
    }

    @Test
    void testClearSelectedTilesNoneSelected() {
        testBag.drawTiles(testPlayer);
        assertFalse(testPlayer.clearSelectedTiles());
    }

    @Test
    void testClearSelectedTiles() {
        testBag.drawTiles(testPlayer);
        assertEquals(testPlayer.getNumTilesOnRack(), 7);
        assertTrue(testPlayer.selectTile(0));
        assertEquals(testPlayer.getSelectedTiles().size(), 1);
        assertTrue(testPlayer.clearSelectedTiles());
        assertFalse(testPlayer.clearSelectedTiles());
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
        testBag.drawTiles(testPlayer);
        assertEquals(testPlayer.getNumTilesOnRack(), Player.MAX_NUM_TILES);
    }

    @Test
    void testSetPlayerPoints() {
        testPlayer.setPoints(10);
        assertEquals(testPlayer.getPointsThisGame(), 10);
    }
    @Test
    void testMakeMove() {
        testBag.drawTiles(testPlayer);
        testPlayer.selectTile(0);
        testPlayer.selectTile(6);
        assertEquals(testPlayer.getSelectedTiles().size(), 2);
        testPlayer.logWord(board, 2, 2, 10, Direction.DOWN);
        assertEquals(testPlayer.getHistory().getMoves().size(), 1);
        assertEquals(testPlayer.getHistory().getMoves().get(0).getPlayer(), testPlayer);

    }


    /* @Test
    void testSwapAllTiles() {
        assertEquals(testPlayer.getNumTilesOnRack(), 0);
        testBag.drawTiles(testPlayer);
        assertEquals(testPlayer.getNumTilesOnRack(), Player.MAX_NUM_TILES);

        List<LetterTile> initialLetters = testPlayer.getTilesOnRack();

        int numTilesToSwap  = 7;
        assertTrue(testBag.numTilesRemaining() >= numTilesToSwap);

        for (int i = 0; i < 1; i++) {
            testPlayer.selectTile(i);
        }
        assertTrue(testBag.numTilesRemaining() > 7);
        testPlayer.swapTiles();

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
        assertTrue(numThingsChanged==1);
    } */
    @Test
    void testSwapZeroTiles() {
        assertEquals(testPlayer.getNumTilesOnRack(), 0);
        testBag.drawTiles(testPlayer);
        assertEquals(testPlayer.getNumTilesOnRack(), Player.MAX_NUM_TILES);
        
        List<LetterTile> initialLetters = testPlayer.getTilesOnRack();
        //Select tiles to swap
        List<LetterTile> selectedLetters = testPlayer.getSelectedTiles();
        int numTilesToSwap  = selectedLetters.size();
        assertEquals(numTilesToSwap,0);
        testPlayer.swapTiles();

        List<LetterTile> postSwapLetters = testPlayer.getTilesOnRack();
        
        assertEquals(postSwapLetters,initialLetters);
    

    }

    @Test
    void testSwapTiles() {
        testBag.drawTiles(testPlayer);
        testPlayer.selectTile(0);
        testPlayer.selectTile(5);
        testPlayer.swapTiles();
    }


    @Test
    void testGetNumCharOnRackZeroChars() {
        Player playerTest = new Player("name", board, testBag);
        Map<Character,Integer> counts = testPlayer.getNumEachCharOnMyRack();
        assertTrue(counts.values().isEmpty());
    
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
        testBag.drawTiles(testPlayer);
        List<LetterTile> drawnTiles = testPlayer.getTilesOnRack();
        for (LetterTile drawnTile : drawnTiles) {
            assertFalse(testBag.contains(drawnTile));
        }
        assertEquals(testPlayer.getNumTilesOnRack(), 7);
    }

    @Test
    void testSelectTile() {
        testBag.drawTiles(testPlayer);
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
    void testGetNumEachCharInBagAndOpponentsNoLettersOnRackOrBoard() {
        //List<LetterTile> letters = new ArrayList<>();
        Map<Character, Integer> counts = testPlayer.getNumEachCharInBagAndOpponents();
        Map<Character, Integer> drawPileCounts = testBag.getInitialLetterFrequencies();
        
        // Both should have all keys
        assertEquals(counts.keySet().size(), drawPileCounts.keySet().size());
        
        //.equals for Map should compare that all keys and values are the same
        // in each map by default. which is the expected behaviour for this test
        assertTrue(counts.equals(drawPileCounts));
        
    }

    @Test
    void testGetNumEachCharOnMyRackEmptyRack() {
        assertEquals(0, testPlayer.getTilesOnRack().size());
        Map<Character, Integer> rackCharCounts = testPlayer.getNumEachCharOnMyRack();
        assertTrue(rackCharCounts.keySet().isEmpty());
        assertTrue(rackCharCounts.values().isEmpty());
    }
    @Test
    void testGetNumEachCharOnMyRackFullRack() {
        testBag.drawTiles(testPlayer);
        assertEquals(Player.MAX_NUM_TILES, testPlayer.getTilesOnRack().size());
        Map<Character, Integer> rackCharCounts = testPlayer.getNumEachCharOnMyRack();
        assertFalse(rackCharCounts.keySet().isEmpty());
        assertFalse(rackCharCounts.values().isEmpty());
        int totalCounts = 0;
        for (int charCount : rackCharCounts.values()) {
            totalCounts += charCount;
        }
        assertEquals(Player.MAX_NUM_TILES, totalCounts);
    }

    @Test
    void testRemoveSelectedTiles() {
        testBag.drawTiles(testPlayer);
        testPlayer.selectTile(0);
        testPlayer.selectTile(5);
        List<LetterTile> selectedTilesTemp = testPlayer.getSelectedTiles();
        List<LetterTile> selectedTiles = new ArrayList<>();
        for (LetterTile letter : selectedTilesTemp) {
            selectedTiles.add(letter);
        }
        assertEquals(selectedTiles.size(), 2);
        for (LetterTile selectedTile : selectedTiles) {
            assertTrue(testPlayer.getTilesOnRack().contains(selectedTile));
        }
        testPlayer.removeSelectedTiles();
        //TileRack wont' contain these anymore
        assertEquals(selectedTiles.size(), 2);
        for (LetterTile selectedTile : selectedTiles) {
            assertFalse(testPlayer.getTilesOnRack().contains(selectedTile));
        }
        //Selected tiles are empty now
        assertEquals(testPlayer.getSelectedTiles().size(), 0);
    }

    @Test
    void testAddPoints() {
        assertEquals(0, testPlayer.getPointsThisGame());
        testPlayer.addPoints(10);
        assertEquals(10, testPlayer.getPointsThisGame());
        testPlayer.addPoints(30);
        assertEquals(40, testPlayer.getPointsThisGame());

    }
    @Test 
    void testCopySelectedTiles() {
        testBag.drawTiles(testPlayer);
        testPlayer.selectTile(0);
        List<LetterTile> copy = testPlayer.copySelectedTiles();
        assertEquals(copy.size(), 1);
        assertTrue(copy.get(0).equals(testPlayer.getSelectedTiles().get(0)));
    }

    @Test
    void testLogSwappedAndLogSkippedTurn() {
        testPlayer.logSkippedTurn(testBoard);
        assertEquals(testPlayer.getHistory().getMoves().size(), 1);
        assertEquals(testPlayer.getHistory().getMoves().get(0).getMoveType(), MoveType.SKIP);
        testBag.drawTiles(testPlayer);
        testPlayer.selectTile(0);
        testPlayer.selectTile(1);
        List<LetterTile> lettersToSwap = testPlayer.getSelectedTiles();
        assertEquals(testPlayer.getHistory().getMoves().get(0).getPointsForMove(), 0);
        testBoard.playWord(lettersToSwap, 0, 0, Direction.DOWN);
        List<LetterTile> tilesAfterSwap = testPlayer.getTilesOnRack();
        testPlayer.logSwap(board, lettersToSwap, tilesAfterSwap);
        assertEquals(testPlayer.getHistory().getMoves().size(), 2);
        assertEquals(testPlayer.getHistory().getMoves().get(1).getMoveType(), MoveType.SWAP_TILES);
        assertEquals(testPlayer.getHistory().getMoves().get(1).getPointsForMove(), 0);
    }

}

