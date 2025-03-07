package model.board;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Direction;
import model.Player;
import model.ScrabbleGame;
import model.tile.LetterTile;
import model.tile.TileBag;
import model.tile.TileType;


import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class BoardTest {
    
    private Board board;
    private TileBag testBag;
    private Player testPlayer;
    private List<LetterTile>  letters;
    private LetterTile A;
    private LetterTile B;
    private LetterTile C;
    private LetterTile E;
    private LetterTile G;
    private LetterTile H;
    private LetterTile K;
    private LetterTile L;
    private LetterTile N;
    private LetterTile O;
    private LetterTile R;
    private LetterTile S;
    private LetterTile T;
    private LetterTile Y;
    private LetterTile Z;
    private ScrabbleGame game;

    @BeforeEach
    void runBefore() {
        testBag = new TileBag();
        board =  new Board();
        game = new ScrabbleGame("alphabet", board, testBag);
        testPlayer = new Player("Trevor", game);
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();
        A = new LetterTile('A',1);
        B = new LetterTile('B',3);
        C = new LetterTile('C', 3);
        
        E = new LetterTile('E', 1);
        G = new LetterTile('G', 2);
        H = new LetterTile('H', 4);
        N = new LetterTile('N', 1);
        R = new LetterTile('R', 1);
        L = new LetterTile('L', 1);
        K = new LetterTile('K', 5);
        T = new LetterTile('T', 1);
        Z = new LetterTile('Z',10);
        O = new LetterTile('O', 1);
        Y = new LetterTile('Y', 4);
        S = new LetterTile('S', 1);
    }
    
    /* @Test
    void testSectionContainsWithAllTypes() {
        assertTrue(board.sectionContainsTileType(letters,1,0,Direction.DOWN, TileType.DOUBLE_LETTER));
        assertTrue(board.sectionContainsTileType(letters,1,0,Direction.DOWN, TileType.TRIPLE_WORD));
        assertTrue(board.sectionContainsTileType(letters,1,0,Direction.RIGHT, TileType.DOUBLE_WORD));
        assertTrue(board.sectionContainsTileType(letters,1,0,Direction.RIGHT, TileType.TRIPLE_LETTER));
    } */

    /* @Test
    void testSquareIsTypeAllTypes() {
        assertTrue(board.squareisTileType(7,7,TileType.DOUBLE_WORD));
        assertFalse(board.squareisTileType(7,7,TileType.DOUBLE_LETTER));
        assertFalse(board.squareisTileType(7,7,TileType.TRIPLE_LETTER));
        assertFalse(board.squareisTileType(7,7,TileType.TRIPLE_WORD));
        //It's normal
        assertFalse(board.squareisTileType(4,5,TileType.DOUBLE_LETTER));
        assertTrue(board.squareisTileType(4,5,TileType.NORMAL));

        assertTrue(board.squareisTileType(5,13,TileType.TRIPLE_LETTER));
        assertFalse(board.squareisTileType(5,13,TileType.DOUBLE_LETTER));
        assertFalse(board.squareisTileType(5,13,TileType.DOUBLE_WORD));
        assertFalse(board.squareisTileType(5,13,TileType.TRIPLE_WORD));

        assertTrue(board.squareisTileType(14,14,TileType.TRIPLE_WORD));
        assertFalse(board.squareisTileType(14,14,TileType.DOUBLE_LETTER));
        assertFalse(board.squareisTileType(14,14,TileType.DOUBLE_WORD));
        assertFalse(board.squareisTileType(14,14,TileType.TRIPLE_LETTER));

    } */


   /*  @Test 
    public void testSquareIsAvailable() {
        assertTrue(board.squareIsAvailable(0, 0));
        assertTrue(board.squareIsAvailable(14, 14));
        assertTrue(board.squareIsAvailable(7, 7));
        assertTrue(board.squareIsAvailable(8, 1));
        assertTrue(board.squareIsAvailable(1, 13));

    } */

   
    @Test 
    public void testSectionIsAvailableNothingAddedGoingRight() {
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();

        boolean isAvailable = board.sectionIsAvailable(letters, 7,7,Direction.RIGHT);
        assertTrue(isAvailable);
    }

    @Test 
    public void testSectionIsAvailableAboveAndBelowWordGoingRight() {
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();

        boolean isAvailable = board.sectionIsAvailable(letters, 7,7,Direction.RIGHT);
        assertTrue(isAvailable);
        board.playWord(letters, 7, 7, Direction.RIGHT);
        isAvailable = board.sectionIsAvailable(letters, 7,7,Direction.RIGHT);
        assertFalse(isAvailable);

        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();
        boolean isAvailableBelow = board.sectionIsAvailable(letters, 8,7,Direction.RIGHT);
        assertTrue(isAvailableBelow);
        boolean isAvailableAbove = board.sectionIsAvailable(letters, 6,7,Direction.RIGHT);
        assertTrue(isAvailableAbove);
    }

    @Test
    void testInBoundsRight() {
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();
        assertEquals(7, letters.size());
        //
        Direction dir = Direction.RIGHT;
        assertTrue(board.sectionIsAvailable(letters, 7,3, dir));
        assertTrue(board.sectionIsAvailable(letters, 7,2, dir));
        assertTrue(board.sectionIsAvailable(letters, 7,1, dir));

        assertTrue(board.sectionIsAvailable(letters, 14,7, dir));
        assertTrue(board.sectionIsAvailable(letters, 14,8, dir));
        assertFalse(board.sectionIsAvailable(letters, 14,9, dir));
        assertFalse(board.sectionIsAvailable(letters, 14,10, dir));

        assertTrue(board.sectionIsAvailable(letters, 0,5, Direction.RIGHT));
        assertTrue(board.sectionIsAvailable(letters, 0,6, Direction.RIGHT));
        assertTrue(board.sectionIsAvailable(letters, 0,7, Direction.RIGHT));

    }

    @Test
    void testInBoundsDown() {
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();
        //
        assertTrue(board.sectionIsAvailable(letters, 7,7, Direction.DOWN));
        assertTrue(board.sectionIsAvailable(letters, 8,7, Direction.DOWN));
        assertFalse(board.sectionIsAvailable(letters, 9,7, Direction.DOWN));

        assertTrue(board.sectionIsAvailable(letters, 7,14, Direction.DOWN));
        assertTrue(board.sectionIsAvailable(letters, 8,14, Direction.DOWN));
        assertFalse(board.sectionIsAvailable(letters, 9,14, Direction.DOWN));

        assertTrue(board.sectionIsAvailable(letters, 7,0, Direction.DOWN));
        assertTrue(board.sectionIsAvailable(letters, 8,0, Direction.DOWN));
        assertFalse(board.sectionIsAvailable(letters, 9,0, Direction.DOWN));

    }

    @Test
    void testInBoundsStartOOBMoveDownIntoBounds() {
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();
        //
        assertFalse(board.sectionIsAvailable(letters, -1,0, Direction.DOWN));
        assertFalse(board.sectionIsAvailable(letters, -1,14, Direction.DOWN));
        assertFalse(board.sectionIsAvailable(letters, -2,7, Direction.DOWN));
    }

    @Test
    void testInBoundsStartOOBMoveRightUntilInBounds() {
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();
        //
        assertFalse(board.sectionIsAvailable(letters, 0,-2, Direction.RIGHT));
        assertFalse(board.sectionIsAvailable(letters, 7,-1, Direction.RIGHT));
        assertFalse(board.sectionIsAvailable(letters, 14,-1, Direction.RIGHT));
    }

    @Test
    void testSectionNotAvailableTileWasPlayed() {
        assertEquals(letters.size(),7);
        board.playWord(letters, 0,0,Direction.DOWN);
        assertFalse(board.sectionIsAvailable(letters, 0,0, Direction.DOWN));
        //(startRow,startCol) taken for both sets of arguments
        assertFalse(board.sectionIsAvailable(letters, 0,0, Direction.RIGHT));

    }

    @Test
    void testSectionAvailableSkipOverOneAndMultipleLetters() {
        assertEquals(letters.size(),7);
        board.playWord(letters, 4,4,Direction.DOWN);
        assertTrue(board.sectionIsAvailable(letters, 4,0, Direction.RIGHT));
        // Letters already placed from (4,4) to (10,4), should place from (0,4) to (3,4) and (11,4) to (13,4)
        assertTrue(board.sectionIsAvailable(letters, 0,4, Direction.DOWN));

    }

    @Test
    void testSectionAvailableVariousCases() {
        assertEquals(letters.size(),7);
        board.playWord(letters, 7,7,Direction.DOWN);
        // can place from (7,1) to (7,6), and at (7,8)
        assertTrue(board.sectionIsAvailable(letters, 7,1, Direction.RIGHT));
        // can place from (1,7) to (6,7) and at (15,7)
        assertTrue(board.sectionIsAvailable(letters, 1,7, Direction.DOWN));
        // can place from (2,7) to (6,7) then there is no more space in the column before going OOB, 2 letters unplaced
        assertFalse(board.sectionIsAvailable(letters, 2,7, Direction.DOWN));
        // Doesn't overlap with anything or skip over anything
        assertTrue(board.sectionIsAvailable(letters, 7,0, Direction.RIGHT));
        // Doesn't overlap with anything or skip over anything
        assertTrue(board.sectionIsAvailable(letters, 0,7, Direction.DOWN));
    }

    @Test
    void testSectionAvailableSkipMultipleSectionsTrue() {
        assertEquals(letters.size(),7);
        board.playWord(letters, 6,6,Direction.DOWN);

        board.playWord(letters, 6,8,Direction.DOWN);

        board.playWord(letters, 6,11,Direction.DOWN);
        // Exactly enough space to fit after jumping over the 3 existing letters
        assertTrue(board.sectionIsAvailable(letters, 6, 5, Direction.RIGHT));
        // One extra space than needed
        assertTrue(board.sectionIsAvailable(letters, 6, 4, Direction.RIGHT));
        // Not enough space
        assertFalse(board.sectionIsAvailable(letters, 6, 7, Direction.RIGHT));
        // Letters already placed from (4,4) to (10,4), should place from (0,4) to (3,4) and (11,4) to (13,4)
        assertTrue(board.sectionIsAvailable(letters, 0,4, Direction.DOWN));
    }

    @Test
    void testSectionIsAvailableSkipOneGroupOfTwoTiles() {
        assertEquals(letters.size(),7);
        board.playWord(letters, 8,6,Direction.RIGHT);

        board.playWord(letters, 9,6,Direction.RIGHT);

        // One too few spaces
        assertFalse(board.sectionIsAvailable(letters, 7,7, Direction.DOWN));
        assertFalse(board.sectionIsAvailable(letters, 7,9, Direction.DOWN));

        //doesnt intersect placed letters
        assertTrue(board.sectionIsAvailable(letters, 7,5, Direction.DOWN));
        assertTrue(board.sectionIsAvailable(letters, 8,5, Direction.DOWN));

        // exactly enough spaces
        assertTrue(board.sectionIsAvailable(letters, 6,6, Direction.DOWN));
        assertTrue(board.sectionIsAvailable(letters, 6,10, Direction.DOWN));
        // one extra space
        assertTrue(board.sectionIsAvailable(letters, 5,6, Direction.DOWN));
        assertTrue(board.sectionIsAvailable(letters, 5,10, Direction.DOWN));
    }

    @Test 
    void testCanPlay() {
        assertTrue(board.sectionIsAvailable(letters, 0, 0, Direction.DOWN));
        assertTrue(board.sectionIsAvailable(letters, 0, 0, Direction.RIGHT));

        assertFalse(board.sectionIsAvailable(letters, 9,9, Direction.DOWN));
        assertFalse(board.sectionIsAvailable(letters, 9,9, Direction.RIGHT));

        board.playWord(letters, 7, 7, Direction.RIGHT);
        assertTrue(board.sectionIsAvailable(letters, 1,14, Direction.DOWN));
        // Places 6 tiles starting at (1,13), places 7th tiles at (8,13)
        assertTrue(board.sectionIsAvailable(letters, 1,13, Direction.DOWN));
    }

    @Test
    void testScoreWord() {
        letters.clear();
        assertTrue(letters.isEmpty());
        letters.add(B);
        letters.add(A);
        letters.add(Z);
        assertEquals(letters.size(), 3);
        //TW
        assertEquals((3 + 1 + 10) * 3, board.scoreWord(letters, 0,0, 0, 1));
        board = new Board();
        assertEquals((3 + 1 + 10) * 3, board.scoreWord(letters, 0,0, 1, 0));
        board = new Board();
        // DW
        assertEquals((3 + 1 + 10) * 2, board.scoreWord(letters, 1,1,0, 1));
        board = new Board();
        assertEquals((3 + 1 + 10) * 2, board.scoreWord(letters, 1,1,0, 1));
        board = new Board();
        //Normal
        assertEquals((3 + 1 + 10), board.scoreWord(letters, 1,2, 0, 1));
        board = new Board();
        //TL
        assertEquals((3 * 3 + 1 + 10), board.scoreWord(letters, 1,5,1, 0));
        board = new Board();
        assertEquals((3 + 1 + 10 * 3), board.scoreWord(letters, 3,5, 1, 0));
        board = new Board();
        //DL
        assertEquals(3 + 1 * 2 + 10, board.scoreWord(letters, 10,14, 1, 0));
        board = new Board();
        
    }

    @Test
    void testGetNumCharOnBoardNoLettersPlaced() {
        Map<Character,Integer> counts = board.getNumEachCharOnBoard();
        // Every board position should be a board tile, 
        // so the Map will be empty, since nothing is put into it since
        // the loop continues if it views a board tile.
        assertTrue(counts.values().isEmpty());
        assertTrue(counts.keySet().isEmpty());
    }

    @Test
    void testGetNumCharOnBoardOneTilePlaced() {
        List<LetterTile> toAdd = new ArrayList<>();
        // Add lettertile B to toAdd
        toAdd.add(B);
        // Play word "B" at 7,7 (direction is arbitrary)
        board.playWord(toAdd,7,7, Direction.DOWN);
        // Now our Map shouldnt be empty
        Map<Character,Integer> counts = board.getNumEachCharOnBoard();

        // one key and one value, both for B
        assertEquals(1, counts.values().size());
        assertEquals(1, counts.keySet().size());

        // Check they're the right entries
        assertEquals(counts.get('B'), 1);
    }

    @Test
    void testGetNumCharOnBoardFewWordsPlaced() {
        List<LetterTile> toAdd = new ArrayList<>();
        List<LetterTile> toAdd2 = new ArrayList<>();
        // Add lettertile B to toAdd
        toAdd.add(A);
        toAdd.add(Z);
        // Play word "AZ" at 7,7 ; 7,8
        board.playWord(toAdd,7,7, Direction.RIGHT);
        toAdd2.add(B);
        board.playWord(toAdd2, 0,0, Direction.DOWN);
        // Now our Map should have A, B, Z keys
        Map<Character,Integer> counts = board.getNumEachCharOnBoard();

        // one key and one value, both for B
        assertEquals(3, counts.values().size());
        assertEquals(3, counts.keySet().size());

        // Check they're the right entries
        assertEquals(counts.get('A'), 1);
        assertEquals(counts.get('B'), 1);
        assertEquals(counts.get('Z'), 1);
        // didn't add d so it should not be in the map
        assertEquals(counts.get('D'), null);
    }

    @Test
    void testGetNumCharOnBoardSameLetterAddedMoreThanOnce() {
        LetterTile A2 = new LetterTile('A', 1);
        List<LetterTile> toAdd = new ArrayList<>();
        List<LetterTile> toAdd2 = new ArrayList<>();
        // Add lettertile B to toAdd
        toAdd.add(A);
        toAdd.add(A2);
        toAdd.add(Z);
        // Play word "AZ" at 7,7 ; 7,8
        board.playWord(toAdd,6,8, Direction.RIGHT);
        toAdd2.add(B);
        board.playWord(toAdd2, 0,0, Direction.DOWN);
        // Now our Map should have A, B, Z keys
        Map<Character,Integer> counts = board.getNumEachCharOnBoard();

        // one key and one value, both for B
        assertEquals(3, counts.values().size());
        assertEquals(3, counts.keySet().size());

        // Check they're the right entries
        assertEquals(counts.get('A'), 2);
        assertEquals(counts.get('B'), 1);
        assertEquals(counts.get('Z'), 1);
        // didn't add d so it should not be in the map
        assertEquals(counts.get('_'), null);
    }
    
    @Test
    void testGetTileAtPositionOnBoard() {
        BoardTile boardTile = (BoardTile) board.getTileAtPositionOnBoard(7,7);
        assertEquals(boardTile.getTileType(), TileType.DOUBLE_WORD);


        BoardTile twTile = (BoardTile) board.getTileAtPositionOnBoard(7,0);
        assertEquals(twTile.getTileType(), TileType.TRIPLE_WORD);

        BoardTile tLTile = (BoardTile) board.getTileAtPositionOnBoard(1,5);
        assertEquals(tLTile.getTileType(), TileType.TRIPLE_LETTER);

        BoardTile dLTile = (BoardTile) board.getTileAtPositionOnBoard(0,3);
        assertEquals(dLTile.getTileType(), TileType.DOUBLE_LETTER);
    }

    @Test
    void testScoreWordConnectsToExistingWordNoMultipliers() {
        List<LetterTile> BAT = new ArrayList<>();
        BAT.add(B);
        BAT.add(A);
        BAT.add(T);
        assertEquals(10, board.playWord(BAT, 7, 7, Direction.RIGHT));
        List<LetterTile> SOY = new ArrayList<>();
        SOY.add(S);
        SOY.add(O);
        SOY.add(Y);
        // BATS for 6 plus SOY for 6
        assertEquals(12, board.playWord(SOY, 7, 10, Direction.DOWN));
    }

    @Test
    void testScoreWordConnectTwiceAtOneSquareNoMultiplier() {
        List<LetterTile> BOAT = new ArrayList<>();
        BOAT.add(B);
        BOAT.add(O);
        BOAT.add(A);
        BOAT.add(T);
        // Will connect to BOAT's 'T'
        List<LetterTile> YET = new ArrayList<>();
        YET.add(Y);
        YET.add(E);
        List<LetterTile> justH = new ArrayList<>();
        justH.add(H);
        assertEquals(12, board.playWord(BOAT, 7,7, Direction.RIGHT));
        assertEquals(6, board.playWord(YET, 5, 10, Direction.DOWN));

        assertEquals(10, board.playWord(justH, 6, 9, Direction.DOWN));
        }

    @Test
    void testScoreWordOneJump() {
        List<LetterTile> CHORE = new ArrayList<>();
        CHORE.add(C);
        CHORE.add(H);
        CHORE.add(O);
        CHORE.add(R);
        CHORE.add(E);

        List<LetterTile> TOOL = new ArrayList<>();

        TOOL.add(T);
        TOOL.add(O);
        TOOL.add(L);

        assertEquals(22, board.playWord(CHORE, 7,7, Direction.DOWN));
        // Must jump over CHORE's 'O'
        assertEquals(6, board.playWord(TOOL, 9, 6, Direction.RIGHT));

        LetterTile boardTile97 = (LetterTile) board.getTileAtPositionOnBoard(9, 7);
        LetterTile boardTile98 = (LetterTile) board.getTileAtPositionOnBoard(9, 8);
        LetterTile boardTile99 = (LetterTile) board.getTileAtPositionOnBoard(9, 9);
        // First tile correctly placed
        assertEquals('O', boardTile97.getCharacter());
        assertEquals('O', boardTile98.getCharacter());
        assertEquals('L', boardTile99.getCharacter());
    }

    @Test
    void testScoreWordConnectTwiceAtOneSquareWithLetterMultiplier() {
        List<LetterTile> CHORE = new ArrayList<>();
        CHORE.add(C);
        CHORE.add(H);
        CHORE.add(O);
        CHORE.add(R);
        CHORE.add(E);
        List<LetterTile> TOOL = new ArrayList<>();
        TOOL.add(T);
        TOOL.add(O);
        TOOL.add(L);
        List<LetterTile> justA = new ArrayList<>();
        justA.add(A);

        assertEquals(22, board.playWord(CHORE, 7,7, Direction.DOWN));
        assertEquals(6, board.playWord(TOOL, 9, 6, Direction.RIGHT));
        // FORMS AH and AT
        assertEquals(9, board.playWord(justA, 8, 6, Direction.RIGHT));
    }

    @Test
    void testScoreWordPlayAtBothEndsBySkippingFourLetters() {
        List<LetterTile> HANG = new ArrayList<>();
        HANG.add(H);
        HANG.add(A);
        HANG.add(N);
        HANG.add(G);
        assertEquals(16, board.playWord(HANG, 7, 4, Direction.RIGHT));
        List<LetterTile> CHANGE = new ArrayList<>();
        CHANGE.add(C);
        CHANGE.add(E);
        // Adds C to start and E to the end of HANG to make CHANGE. Double letter applied
        // to C, double word not applied since it was already applied to HANG
        assertEquals(15, board.playWord(CHANGE, 7, 3, Direction.RIGHT));
        LetterTile boardTile73 = (LetterTile) board.getTileAtPositionOnBoard(7, 3);
        LetterTile boardTile74 = (LetterTile) board.getTileAtPositionOnBoard(7, 4);
        LetterTile boardTile78 = (LetterTile) board.getTileAtPositionOnBoard(7, 8);
        // First tile correctly placed
        assertEquals('C', boardTile73.getCharacter());
        // not overwritten by E
        assertEquals('H', boardTile74.getCharacter());
        // Skips over HANG and places
        assertEquals('E', boardTile78.getCharacter());
    }

    @Test
    void testScoreWordConnectTwiceAtOneSquareWithWordMultiplier() {
        List<LetterTile> HANG = new ArrayList<>();
        HANG.add(H);
        HANG.add(A);
        HANG.add(N);
        HANG.add(G);
        board.playWord(HANG, 7, 4, Direction.RIGHT);
        List<LetterTile> STEAK = new ArrayList<>();
        STEAK.add(S);
        STEAK.add(T);
        STEAK.add(E);
        STEAK.add(K);

        assertEquals(11, board.playWord(STEAK, 4, 5, Direction.DOWN));
        LetterTile boardTile75 = (LetterTile) board.getTileAtPositionOnBoard(7, 5);
        assertEquals(boardTile75.getCharacter(), 'A');
        List<LetterTile> ATE = new ArrayList<>();
        ATE.add(new LetterTile('A'));
        ATE.add(new LetterTile('E'));
        assertEquals(3, board.playWord(ATE, 5, 4, Direction.RIGHT));
        
        List<LetterTile> justA = new ArrayList<>();
        justA.add(new LetterTile('A'));
        assertEquals(8, board.playWord(justA, 4, 4, Direction.RIGHT));
    }

    @Test
    void testfindTripleWordMultiplierIsAdjacency() {
        assertEquals(board.findWordMultiplier(new Coordinate(0,0), true), 3);
        assertEquals(board.findWordMultiplier(new Coordinate(0,0), true), 3);
        assertEquals(board.findWordMultiplier(new Coordinate(0,0), false), 3);
        assertEquals(board.findWordMultiplier(new Coordinate(0,0), true), 1);
        assertEquals(board.findWordMultiplier(new Coordinate(0,0), false), 1);  
    }

    @Test
    void testInBounds() {
        assertTrue(board.inBounds(0,0));
        assertFalse(board.inBounds(-1,0));
        assertFalse(board.inBounds(-1,-1));
        assertFalse(board.inBounds(0,-1));
        assertFalse(board.inBounds(15,0));
        assertFalse(board.inBounds(15,15));
        assertFalse(board.inBounds(0,16));
        assertFalse(board.inBounds(16,-1));
        assertFalse(board.inBounds(-1,16));
    }


}
