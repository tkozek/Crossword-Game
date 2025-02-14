package model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;


public class TileBagTest {
    private TileBag testBag;
    private Set<Character> set;
    @BeforeEach
    void runBefore() {
         testBag = new TileBag();
    }

    @Test
    void testConstructor() {
        //100 tiles initially
        assertEquals(testBag.numTilesRemaining(), 100);
    }

    @Test
    void testDrawTile() {
        //100 tiles initially
        LetterTile firstTile = testBag.drawTile();
        // 1 gets removed
        assertEquals(testBag.numTilesRemaining(), 99);
        char firstTileChar = firstTile.getCharacter();
        boolean isValid = ((firstTileChar <= 'Z' && 
        firstTileChar >='A') || firstTileChar=='-');
        //is it one of the values we meant to initialize
        assertTrue(isValid);
    }
    @Test
    void testDrawMultipleTiles() {
        //100 tiles initially
        LetterTile firstTile = testBag.drawTile();
        LetterTile secondTile = testBag.drawTile();
        LetterTile thirdTile = testBag.drawTile();
        // 1 gets removed
        assertEquals(testBag.numTilesRemaining(), 97);

        char firstTileChar = firstTile.getCharacter();
        char secondTileChar = secondTile.getCharacter();
        char thirdTileChar = thirdTile.getCharacter();

        boolean isValidFirst = ((firstTileChar <= 'Z' && 
        firstTileChar >='A') || firstTileChar=='-');

        boolean isValidSecond = ((secondTileChar <= 'Z' && 
        secondTileChar >='A') || secondTileChar=='-');

        boolean isValidThird = ((thirdTileChar <= 'Z' && 
        thirdTileChar >='A') || thirdTileChar=='-');

        //is it one of the values we meant to initialize
        assertTrue(isValidFirst);
        assertTrue(isValidSecond);
        assertTrue(isValidThird);
    }
    @Test
    void testNotAllTheSameTile() {
        List<Character> letters = new ArrayList<>();
        LetterTile curLetter;
        // 'E' is the most common character, and it occurs 12 times
        for (int i = 0; i < 13; i++) {
            curLetter = testBag.drawTile();
            letters.add(curLetter.getCharacter());
        }
        set = new HashSet<>(letters);
        // At least two non duplicate characters must exist
        assertTrue(set.size() > 1);
        assertEquals(testBag.numTilesRemaining(), 100-13);
    }
}

