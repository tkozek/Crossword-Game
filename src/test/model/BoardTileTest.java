package model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BoardTileTest {

    BoardTile doubleWordTile;
    BoardTile doubleLetterTile;
    BoardTile tripleWordTile;
    BoardTile tripleLetterTile;


    @BeforeEach
    void runBefore() {
         doubleWordTile = new BoardTile(1,1,false,true,false,false);
        tripleWordTile = new BoardTile(0,0,false,false,false,true);
        doubleLetterTile= new BoardTile(0,3,true,false,false,false);
        tripleLetterTile= new BoardTile(5,1,false,false,true,false);

    }

    @Test
    void testConstructor() {
        assertTrue(doubleWordTile.checkIsDoubleWord());
        assertFalse(doubleWordTile.checkIsTripleWord());
        assertFalse(doubleWordTile.checkIsDoubleLetter());
        assertFalse(doubleWordTile.checkIsTripleLetter());
        assertEquals(1,doubleWordTile.getRow());
        assertEquals(1,doubleWordTile.getCol());

        assertTrue(tripleWordTile.checkIsTripleWord());
        assertFalse(tripleWordTile.checkIsDoubleWord());
        assertFalse(tripleWordTile.checkIsDoubleLetter());
        assertFalse(tripleWordTile.checkIsTripleLetter());
        assertEquals(0,tripleWordTile.getRow());
        assertEquals(0,tripleWordTile.getCol());


        assertTrue(doubleLetterTile.checkIsDoubleLetter());
        assertFalse(doubleLetterTile.checkIsTripleLetter());
        assertFalse(doubleLetterTile.checkIsDoubleWord());
        assertFalse(doubleLetterTile.checkIsTripleWord());
        assertEquals(0,doubleLetterTile.getRow());
        assertEquals(3,doubleLetterTile.getCol());


        assertTrue(tripleLetterTile.checkIsTripleLetter());
        assertFalse(tripleLetterTile.checkIsTripleWord());
        assertFalse(tripleLetterTile.checkIsDoubleLetter());
        assertFalse(tripleLetterTile.checkIsDoubleLetter());
        assertEquals(5,tripleLetterTile.getRow());
        assertEquals(1,tripleLetterTile.getCol());
    }
    @Test
    void testgetStringToDisplay() {
        assertEquals("DW", doubleWordTile.getStringToDisplay());
        assertEquals("TW", tripleWordTile.getStringToDisplay());
        assertEquals("DL", doubleLetterTile.getStringToDisplay());
        assertEquals("TL", tripleLetterTile.getStringToDisplay());
    }
}

