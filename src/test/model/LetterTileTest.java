package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class LetterTileTest {
    
    LetterTile testTileA;
    LetterTile testTileB;
    LetterTile testTileC;
    LetterTile testTileD;
    LetterTile testTileK;
    LetterTile testTileQ;

    LetterTile testBlankTile;

    @BeforeEach
    void runBefore() {
        testTileA = new LetterTile('A',1);
        testTileB = new LetterTile('B',3);
        testTileC = new LetterTile('C',3);
        testTileD = new LetterTile('D',2);
        testTileK = new LetterTile('K',5);
        testTileQ = new LetterTile('Q',10);
        testBlankTile = new LetterTile('-',0);
    }

    @Test
    void testConstructor() {
        assertEquals('A', testTileA.getCharacter());
        assertEquals('Q', testTileQ.getCharacter());
        assertEquals('-', testBlankTile.getCharacter());
        
        assertEquals(1, testTileA.getLetterPoints());
        assertEquals(10, testTileQ.getLetterPoints());
        assertEquals(0, testBlankTile.getLetterPoints());      
    }

    @Test
    void testGetCharacterAsString() {
        assertEquals("A", testTileA.getStringToDisplay());
        assertEquals("Q", testTileQ.getStringToDisplay());
        assertEquals("-", testBlankTile.getStringToDisplay());           
    }

}

