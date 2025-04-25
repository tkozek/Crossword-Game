package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.move.MoveType;
import model.tile.LetterTile;
import model.tile.TileBag;


import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class PlayerTest {
    
    private Player testPlayer;
    private ScrabbleGame game;
    
    @BeforeEach
    public void runBefore() {
        game = new ScrabbleGame();
        testPlayer = new Player("Trevor");
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testPlayer.getPointsThisGame());
        assertEquals(0, testPlayer.getMoves().size());
        assertEquals(0, testPlayer.getNumTilesOnRack());
        assertEquals("Trevor", testPlayer.getPlayerName());
    }

    @Test
    public void testSelectTileAlreadySelected() {
        game.drawTiles(testPlayer);
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
    public void testClearSelectedTilesNoneSelected() {
        game.drawTiles(testPlayer);
        assertFalse(testPlayer.clearSelectedTiles());
    }

    @Test
    public void testClearSelectedTiles() {
        game.drawTiles(testPlayer);
        assertEquals(testPlayer.getNumTilesOnRack(), 7);
        assertTrue(testPlayer.selectTile(0));
        assertEquals(testPlayer.getSelectedTiles().size(), 1);
        assertTrue(testPlayer.clearSelectedTiles());
        assertFalse(testPlayer.clearSelectedTiles());
    }

    @Test
    public void testPlayedAllTiles() {
        assertTrue(testPlayer.outOfTiles());
        game.drawTiles(testPlayer);
        assertFalse(testPlayer.outOfTiles());
    }

    @Test
    public void testSetPlayerName() {
        assertEquals("Trevor", testPlayer.getPlayerName());
        testPlayer.setPlayerName("Michael");
        assertEquals("Michael", testPlayer.getPlayerName());
    }

    @Test
    public void testDrawTilesEmptyRack() {
        assertEquals(testPlayer.getNumTilesOnRack(), 0);
        game.drawTiles(testPlayer);
        assertEquals(testPlayer.getNumTilesOnRack(), TileBag.MAX_NUM_PLAYER_TILES);
    }

    @Test
    public void testSetPlayerPoints() {
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
    public void testMakeMove() {
        game.drawTiles(testPlayer);
        testPlayer.selectTile(0);
        testPlayer.selectTile(6);
        List<LetterTile> letters = testPlayer.getSelectedTiles();
        assertEquals(letters.size(), 2);
        game.logWord(testPlayer, getStringFromLetters(letters),2, 2, 10, Direction.DOWN);
        assertEquals(testPlayer.getMoves().size(), 1);
        assertEquals(testPlayer.getMoves().get(0).getPlayerName(), testPlayer.getPlayerName());
    }


    @Test
    public void testSwapZeroTiles() {
        assertEquals(testPlayer.getNumTilesOnRack(), 0);
        game.drawTiles(testPlayer);
        assertEquals(testPlayer.getNumTilesOnRack(), TileBag.MAX_NUM_PLAYER_TILES);
        
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
    public void testSwapTiles() {
        game.drawTiles(testPlayer);
        testPlayer.selectTile(0);
        testPlayer.selectTile(5);
        game.swapTiles(testPlayer);
    }


    @Test
    public void testGetNumCharOnRackZeroChars() {
        Player playerTest = new Player("name");
        Map<Character,Integer> counts = playerTest.getNumEachCharOnMyRack();
        assertTrue(counts.values().isEmpty());
    
    }

    @Test
    public void testAddtile() {
        assertEquals(testPlayer.getNumTilesOnRack(), 0);
        LetterTile a = new LetterTile('A',1);
        testPlayer.addTile(a);
        assertEquals(testPlayer.getNumTilesOnRack(), 1);
    }

    @Test
    public void testTileAddedToRackIsRemovedFromTileBag() {
        assertEquals(testPlayer.getNumTilesOnRack(), 0);
        assertEquals(game.getTileBag().numTilesRemaining(), TileBag.TOTAL_LETTERS_INITIALLY);
        game.drawTiles(testPlayer);
        List<LetterTile> drawnTiles = testPlayer.getTilesOnRack();
        for (LetterTile drawnTile : drawnTiles) {
            assertFalse(game.getTileBag().contains(drawnTile));
        }
        assertEquals(testPlayer.getNumTilesOnRack(), 7);
    }

    @Test
    public void testSelectTile() {
        game.drawTiles(testPlayer);
        List<LetterTile> drawnTiles = testPlayer.getTilesOnRack();
        //Player.MAX_NUM_TILES == 7
        assertEquals(testPlayer.getNumTilesOnRack(), TileBag.MAX_NUM_PLAYER_TILES);
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
    public void testGetNumEachCharOnMyRackEmptyRack() {
        assertEquals(0, testPlayer.getTilesOnRack().size());
        Map<Character, Integer> rackCharCounts = testPlayer.getNumEachCharOnMyRack();
        assertTrue(rackCharCounts.keySet().isEmpty());
        assertTrue(rackCharCounts.values().isEmpty());
    }

    @Test
    public void testGetNumEachCharOnMyRackFullRack() {
        game.drawTiles(testPlayer);
        assertEquals(TileBag.MAX_NUM_PLAYER_TILES, testPlayer.getTilesOnRack().size());
        Map<Character, Integer> rackCharCounts = testPlayer.getNumEachCharOnMyRack();
        assertFalse(rackCharCounts.keySet().isEmpty());
        assertFalse(rackCharCounts.values().isEmpty());
        int totalCounts = 0;
        for (int charCount : rackCharCounts.values()) {
            totalCounts += charCount;
        }
        assertEquals(TileBag.MAX_NUM_PLAYER_TILES, totalCounts);
    }

    @Test
    public void testRemoveSelectedTiles() {
        game.drawTiles(testPlayer);
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
    public void testAddPoints() {
        assertEquals(0, testPlayer.getPointsThisGame());
        testPlayer.addPoints(10);
        assertEquals(10, testPlayer.getPointsThisGame());
        testPlayer.addPoints(30);
        assertEquals(40, testPlayer.getPointsThisGame());

    }
    
    @Test 
    public void testCopySelectedTiles() {
        game.drawTiles(testPlayer);
        testPlayer.selectTile(0);
        List<LetterTile> copy = testPlayer.copySelectedTiles();
        assertEquals(copy.size(), 1);
        assertFalse(copy.get(0).equals(testPlayer.getSelectedTiles().get(0)));
    }

    @Test
    public void testLogSwappedAndLogSkippedTurn() {
        game.logSkippedTurn(testPlayer);
        assertEquals(testPlayer.getMoves().size(), 1);
        assertEquals(testPlayer.getMoves().get(0).getMoveType(), MoveType.SKIP);
        game.drawTiles(testPlayer);
        testPlayer.selectTile(0);
        testPlayer.selectTile(1);
        List<LetterTile> lettersToSwap = testPlayer.getSelectedTiles();
        assertEquals(testPlayer.getMoves().get(0).getPointsForMove(), 0);
        game.getBoard().playWord(lettersToSwap, 0, 0, Direction.DOWN);
        List<LetterTile> tilesAfterSwap = testPlayer.getTilesOnRack();
        game.logSwap(testPlayer, getStringFromLetters(lettersToSwap), getStringFromLetters(tilesAfterSwap));
        assertEquals(testPlayer.getMoves().size(), 2);
        assertEquals(testPlayer.getMoves().get(1).getMoveType(), MoveType.SWAP_TILES);
        assertEquals(testPlayer.getMoves().get(1).getPointsForMove(), 0);
    }
}

