package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BoardTileTest {

    BoardTile doubleWordTile;
    BoardTile doubleLetterTile;
    BoardTile tripleWordTile;
    BoardTile tripleLetterTile;
    BoardTile regularTile;

    @BeforeEach
    void runBefore() {
        doubleWordTile = new BoardTile(1,1,TileType.DOUBLE_WORD);
        tripleWordTile = new BoardTile(0,0,TileType.TRIPLE_WORD);
        doubleLetterTile= new BoardTile(0,3,TileType.DOUBLE_LETTER);
        tripleLetterTile= new BoardTile(5,1,TileType.TRIPLE_LETTER);
        regularTile = new BoardTile(0,1, TileType.NORMAL);
    }

    @Test
    void testConstructor() {
        assertTrue(doubleWordTile.isSpecial());
        assertTrue(tripleWordTile.isSpecial());
        assertTrue(tripleLetterTile.isSpecial());
        assertTrue(doubleLetterTile.isSpecial());

        assertFalse(regularTile.isSpecial());

        assertTrue(doubleLetterTile.checkIsTileType(TileType.DOUBLE_LETTER));
        assertFalse(doubleLetterTile.checkIsTileType(TileType.TRIPLE_WORD));
        
        assertEquals(1,doubleWordTile.getRow());
        assertEquals(1,doubleWordTile.getCol());

        assertTrue(tripleWordTile.checkIsTileType(TileType.TRIPLE_WORD));
        
        assertEquals(0,tripleWordTile.getRow());
        assertEquals(0,tripleWordTile.getCol());


        assertTrue(tripleLetterTile.checkIsTileType(TileType.TRIPLE_LETTER));
        assertFalse(doubleLetterTile.checkIsTileType(TileType.TRIPLE_WORD));
       
        assertEquals(0,doubleLetterTile.getRow());
        assertEquals(3,doubleLetterTile.getCol());

    }
    @Test
    void testgetStringToDisplay() {
        assertEquals("DW", doubleWordTile.getStringToDisplay());
        assertEquals("TW", tripleWordTile.getStringToDisplay());
        assertEquals("DL", doubleLetterTile.getStringToDisplay());
        assertEquals("TL", tripleLetterTile.getStringToDisplay());
        assertEquals("_", regularTile.getStringToDisplay());

    }
}

