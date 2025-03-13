package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.board.Board;
import model.move.MoveType;
import model.tile.LetterTile;
import model.tile.TileBag;


import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class PlayerTest {
    
    private Board board;
    private TileBag testBag;
    private Player testPlayer;
    private Board testBoard;
    private ScrabbleGame game;
    
    @BeforeEach
    void runBefore() {
        testBag = new TileBag();
        testBoard =  new Board();
        game = new ScrabbleGame("n", board, testBag);
        testPlayer = new Player("Trevor", game);
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
    void testPlayedAllTiles() {
        assertTrue(testPlayer.outOfTiles());
        game.drawTiles(testPlayer);
        assertFalse(testPlayer.outOfTiles());
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

    // EFFECTS: returns list of letter tiles
    // based on input string
    private String getStringFromLetters(List<LetterTile> letters) {
        String result = "";
        for (LetterTile letter : letters) {
            result += letter.toDisplay();
        }
        return result;
    }  

    @Test
    void testMakeMove() {
        testBag.drawTiles(testPlayer);
        testPlayer.selectTile(0);
        testPlayer.selectTile(6);
        List<LetterTile> letters = testPlayer.getSelectedTiles();
        assertEquals(letters.size(), 2);
        game.logWord(testPlayer, getStringFromLetters(letters),2, 2, 10, Direction.DOWN);
        assertEquals(testPlayer.getHistory().getMoves().size(), 1);
        assertEquals(testPlayer.getHistory().getMoves().get(0).getPlayer(), testPlayer);

    }


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
        game.swapTiles(testPlayer);

        List<LetterTile> postSwapLetters = testPlayer.getTilesOnRack();
        
        assertEquals(postSwapLetters,initialLetters);
    

    }

    @Test
    void testSwapTiles() {
        testBag.drawTiles(testPlayer);
        testPlayer.selectTile(0);
        testPlayer.selectTile(5);
        game.swapTiles(testPlayer);
    }


    @Test
    void testGetNumCharOnRackZeroChars() {
        Player playerTest = new Player("name", game);
        Map<Character,Integer> counts = playerTest.getNumEachCharOnMyRack();
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
        List<LetterTile> drawnTiles = testPlayer.getTilesOnRack();
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
        assertFalse(copy.get(0).equals(testPlayer.getSelectedTiles().get(0)));
    }

    @Test
    void testLogSwappedAndLogSkippedTurn() {
        game.logSkippedTurn(testPlayer);
        assertEquals(testPlayer.getHistory().getMoves().size(), 1);
        assertEquals(testPlayer.getHistory().getMoves().get(0).getMoveType(), MoveType.SKIP);
        testBag.drawTiles(testPlayer);
        testPlayer.selectTile(0);
        testPlayer.selectTile(1);
        List<LetterTile> lettersToSwap = testPlayer.getSelectedTiles();
        assertEquals(testPlayer.getHistory().getMoves().get(0).getPointsForMove(), 0);
        testBoard.playWord(lettersToSwap, 0, 0, Direction.DOWN);
        List<LetterTile> tilesAfterSwap = testPlayer.getTilesOnRack();
        game.logSwap(testPlayer, getStringFromLetters(lettersToSwap), getStringFromLetters(tilesAfterSwap));
        assertEquals(testPlayer.getHistory().getMoves().size(), 2);
        assertEquals(testPlayer.getHistory().getMoves().get(1).getMoveType(), MoveType.SWAP_TILES);
        assertEquals(testPlayer.getHistory().getMoves().get(1).getPointsForMove(), 0);
    }
    

}

