package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.exceptions.InvalidLetterException;
import model.exceptions.MoveTypeMismatchException;
import model.move.*;
import model.tile.LetterTile;


import java.util.List;

public class MoveTest {
    
    private Player testPlayer;
    private Player testPlayer2;
    private List<LetterTile> p1Letters;
    private List<LetterTile> p2Letters;
    private Move playWord;
    private Move swap;
    private Move endGameLoser;
    private Move endGameWinner;
    private String alphabet;
    private ScrabbleGame game;

    @BeforeEach
    public void runBefore() {
        game = new ScrabbleGame();
        testPlayer = new Player("Trevor");
        testPlayer2 = new Player("Rovert");
        game.drawTiles(testPlayer);
        game.drawTiles(testPlayer2);
        p1Letters = testPlayer.getTilesOnRack();
        p2Letters = testPlayer2.getTilesOnRack();
    }

    @Test
    public void testConstructorForPlayedWord() {
        playWord = new Move(testPlayer.getPlayerName(), getStringFromLetters(p1Letters), 7,7, 10, Direction.DOWN);
        assertEquals(playWord.getMoveType(), MoveType.PLAY_WORD);
        assertEquals(playWord.getPointsForMove(), 10);
        assertEquals(playWord.getLettersInvolved(), getStringFromLetters(p1Letters));
        assertEquals(playWord.getDirection(),Direction.DOWN); 
        assertEquals(playWord.getStartColumn(), 7);
        assertEquals(playWord.getStartRow(), 7);
    }

    @Test
    public void testMoveContainsCharacter() {
        playWord = new Move(testPlayer.getPlayerName(), getStringFromLetters(p1Letters), 1,8, 10, Direction.DOWN);
        LetterTile letter = p1Letters.get(0);
        char firstChar = letter.getCharacter();
        assertEquals(playWord.getMoveType(), MoveType.PLAY_WORD);
        assertEquals(playWord.getPointsForMove(), 10);
        assertEquals(playWord.getLettersInvolved(), getStringFromLetters(p1Letters));
        assertEquals(playWord.getDirection(),Direction.DOWN);
        
        assertTrue(playWord.moveContainsLetter(firstChar)); 
        boolean allTrue = true;
        alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ-";
        for (int i = 0; i < 26; i++) {
            if (!playWord.moveContainsLetter(alphabet.charAt(i))) {
                allTrue = false;
                break;
            }
        }
        // Move cannot contain all possible letters since tileRack is less than the size of the alphabet
        assertFalse(allTrue);
        
    }
    
    @Test
    public void testConstructorForSwappedWord() {
        swap = new Move(testPlayer2.getPlayerName(), getStringFromLetters(p1Letters), getStringFromLetters(p2Letters));
        assertEquals(swap.getMoveType(), MoveType.SWAP_TILES);
        assertEquals(swap.getPointsForMove(), 0);
        p1Letters.addAll(p2Letters);
        assertEquals(swap.getLettersInvolved(), getStringFromLetters(p1Letters));
    }
    
    @Test
    public void testConstructorForEndGameAdjustment() {
        String p1Name = testPlayer.getPlayerName();
        String p2Name = testPlayer2.getPlayerName();

        endGameLoser = new Move(p1Name, p2Name, getStringFromLetters(p1Letters), -7);
        assertEquals(endGameLoser.getMoveType(), MoveType.END_GAME_ADJUSTMENT);

        endGameWinner = new Move(p2Name, p2Name, getStringFromLetters(p1Letters), 7);
        assertEquals(endGameWinner.getMoveType(),MoveType.END_GAME_ADJUSTMENT);;
    }

    @Test
    public void testConstructorSkip() {
        Move skip = new Move(testPlayer.getPlayerName());
        assertEquals(skip.getPointsForMove(), 0);
        assertEquals(skip.getMoveType(), MoveType.SKIP);
    }

    @Test
    public void testGetLastPlayer() {
        Move endGame = new Move(testPlayer.getPlayerName(), testPlayer2.getPlayerName(), "ADC", 6);
        assertEquals(testPlayer2.getPlayerName(), endGame.getLastPlayerName());
        assertFalse(testPlayer.getPlayerName().equals(endGame.getLastPlayerName()));
    }

    @Test
    public void testGetLastPlayerNameWrongMoveType() {
        Move skip = new Move(testPlayer.getPlayerName());
        try {
            skip.getLastPlayerName();
            fail();
        } catch (MoveTypeMismatchException e) {
            assertNotEquals(skip.getMoveType(), MoveType.END_GAME_ADJUSTMENT);
        }
    }

    

    @Test
    public void testGetDirectionWrongMoveType() {
        Move skip = new Move(testPlayer.getPlayerName());
        try {
            skip.getDirection();
            fail();
        } catch (MoveTypeMismatchException e) {
            assertNotEquals(skip.getMoveType(), MoveType.PLAY_WORD);
        }
    }

    @Test
    public void testMoveContainsLetterBlank() {
        playWord = new Move(testPlayer.getPlayerName(), "-", 7,7, 0, Direction.DOWN);
        char letter = '-';
        try {
            assertTrue(playWord.moveContainsLetter(letter));
        } catch (Exception e) {
            fail();
        } 
    }

    @Test
    public void testMoveContainsLetterInvalidMoveType() {
        Move skip = new Move(testPlayer.getPlayerName());
        char letter = 'A';
        try {
            skip.moveContainsLetter(letter);
            fail();
        } catch (InvalidLetterException e) {
            fail();
        } catch (MoveTypeMismatchException e) {
            assertNotEquals(skip.getMoveType(), MoveType.PLAY_WORD);
        }
    }
    
    @Test
    public void testMoveContainsLetterInvalidLetter() {
        playWord = new Move(testPlayer.getPlayerName(), getStringFromLetters(p1Letters), 1,8, 10, Direction.DOWN);
        char letter = '3';
        try {
            playWord.moveContainsLetter(letter);
            fail();
        } catch (InvalidLetterException e) {
            assertFalse(((letter >= 'A' && letter <= 'Z') || letter == '-'));
        } catch (MoveTypeMismatchException e) {
            fail();        
        }
    }

    @Test
    public void testMoveContainsLetterInvalidMoveTypeAndInvalidLetter() {
        Move skip = new Move(testPlayer.getPlayerName());
        char letter = '~';
        try {
            skip.moveContainsLetter(letter);
            fail("Should have thrown an Exception");
        } catch (InvalidLetterException e) {
            fail("Should have thrown InvalidLetterException first");
        } catch (MoveTypeMismatchException e) {
            assertNotEquals(skip.getMoveType(), MoveType.PLAY_WORD);
        }
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

    
}