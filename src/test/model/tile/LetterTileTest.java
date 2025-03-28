package model.tile;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class LetterTileTest {
    
    LetterTile testTileA;
    LetterTile testTileB;
    LetterTile testTileC;
    LetterTile testTileD;
    LetterTile testTileK;
    LetterTile testTileQ;
    LetterTile testBlankTile;
    TileBag bag;

    @BeforeEach
    public void runBefore() {
        testTileA = new LetterTile('A',1);
        testTileB = new LetterTile('B',3);
        testTileC = new LetterTile('C',3);
        testTileD = new LetterTile('D',2);
        testTileK = new LetterTile('K',5);
        testTileQ = new LetterTile('Q',10);
        testBlankTile = new LetterTile('-',0);
        bag = new TileBag();
    }

    @Test
    public void testConstructor() {
        assertEquals('A', testTileA.getCharacter());
        assertEquals('Q', testTileQ.getCharacter());
        assertEquals('-', testBlankTile.getCharacter());
        
        assertEquals(1, testTileA.getPoints());
        assertEquals(10, testTileQ.getPoints());
        assertEquals(0, testBlankTile.getPoints());      
    }

    @Test
    public void testDeepCopyConstructor() {
        LetterTile copyLetter = new LetterTile(testTileB);
        assertEquals('B', copyLetter.getCharacter());
        assertEquals(3, copyLetter.getPoints());
        assertFalse(copyLetter == testTileB);
        testTileB = null;
        assertNotNull(copyLetter);
        assertEquals('B', copyLetter.getCharacter());
        assertEquals(3, copyLetter.getPoints());
        assertNull(testTileB);
    }

    @Test
    public void testDeepCopyConstructorMultipleTiles() {
        LetterTile copyLetter = new LetterTile(testTileB);
        LetterTile copyQ = new LetterTile(testTileQ);
        assertEquals('Q', copyQ.getCharacter());
        assertEquals(10, copyQ.getPoints());

        assertEquals('B', copyLetter.getCharacter());
        assertEquals(3, copyLetter.getPoints());

        assertFalse(copyQ == testTileQ);
        assertFalse(copyLetter == testTileB);
        testTileQ = null;
        testTileB = null;
        assertNotNull(copyLetter);
        assertEquals('B', copyLetter.getCharacter());
        assertEquals(3, copyLetter.getPoints());
        assertNull(testTileB);

        assertNotNull(copyQ);
        assertEquals('Q', copyQ.getCharacter());
        assertEquals(10, copyQ.getPoints());
        assertNull(testTileQ);
    }

    @Test
    public void testToDisplay() {
        assertEquals("A", testTileA.toDisplay());
        assertEquals("Q", testTileQ.toDisplay());
        assertEquals("-", testBlankTile.toDisplay());           
    }

    @Test
    public void testGetTerminalPrintout() {
        assertEquals(" A_| ", testTileA.getTerminalPrintoutString());
        assertEquals(" Q_| ", testTileQ.getTerminalPrintoutString());
        assertEquals(" -_| ", testBlankTile.getTerminalPrintoutString());           
    }

    @Test
    public void testCharacterOnlyConstructor() {
        // Works as long as some instance of TileBag has been instantiated
        LetterTile c = new LetterTile('C');
        assertEquals(c.getPoints(), 3);
    }

}

