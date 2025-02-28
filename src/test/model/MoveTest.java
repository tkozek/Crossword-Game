package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.board.Board;
import model.move.Move;
import model.move.MoveType;
import model.tile.LetterTile;
import model.tile.TileBag;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MoveTest {
    
    private Board board;
    private TileBag testBag;
    private Player testPlayer;
    private Player testPlayer2;
    private List<LetterTile> p1Letters;
    private List<LetterTile> p2Letters;
    private Board testBoard;
    private Move playWord;
    private Move swap;
    private Move endGameWinner;
    private Move endGameLoser;
    private String alphabet;
    private ScrabbleGame game;

    @BeforeEach
    void runBefore() {
        testBag = new TileBag();
        board =  new Board();
        game = new ScrabbleGame("alphabet", board, testBag);
        testPlayer = new Player("Trevor", testBoard,testBag, game);
        testPlayer2 = new Player("Rovert", testBoard,testBag, game);
        testBag.drawTiles(testPlayer);
        testBag.drawTiles(testPlayer2);
        p1Letters = testPlayer.getTilesOnRack();
        p2Letters = testPlayer2.getTilesOnRack();
        
    }

    @Test
    void testConstructorForPlayedWord() {
        playWord = new Move(testPlayer, board, p1Letters, 7,7, 10, Direction.DOWN);
        assertEquals(playWord.getMoveType(), MoveType.PLAY_WORD);
        assertEquals(playWord.getPlayer(), testPlayer);
        assertEquals(playWord.getPointsForMove(), 10);
        assertEquals(playWord.getLettersInvolved(), p1Letters);
        assertEquals(playWord.getDirection(),Direction.DOWN); 
        assertEquals(playWord.getStartColumn(), 7);
        assertEquals(playWord.getStartRow(), 7);
        assertEquals(playWord.getBoard(),board);

    }
    @Test
    void testMoveContainsCharacter() {
        playWord = new Move(testPlayer, board, p1Letters, 1,8, 10, Direction.DOWN);
        LetterTile letter = p1Letters.get(0);
        char firstChar = letter.getCharacter();
        assertEquals(playWord.getMoveType(), MoveType.PLAY_WORD);
        assertEquals(playWord.getPointsForMove(), 10);
        assertEquals(playWord.getLettersInvolved(), p1Letters);
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
    void testConstructorForSwappedWord() {
        swap = new Move(testPlayer2, board, p1Letters, p2Letters);
        assertEquals(swap.getPlayer(), testPlayer2);
        assertEquals(swap.getMoveType(), MoveType.SWAP_TILES);
        assertEquals(swap.getPointsForMove(), 0);
        p1Letters.addAll(p2Letters);
        assertEquals(swap.getLettersInvolved(),p1Letters);
        assertEquals(swap.getBoard(),board);
    }
    @Test
    void testConstructorForEndGameAdjustment() {
        endGameLoser = new Move(testPlayer, board, false, p1Letters, 7);
        assertEquals(endGameLoser.getPlayer(), testPlayer);
        assertEquals(endGameLoser.getMoveType(), MoveType.END_GAME_LOSER);
        assertEquals(endGameLoser.getBoard(),board);
        endGameWinner = new Move(testPlayer2, board, true, p1Letters, 7);
        assertEquals(endGameWinner.getPlayer(), testPlayer2);
        assertEquals(endGameWinner.getMoveType(),MoveType.END_GAME_WINNER);
        assertEquals(endGameWinner.getBoard(),board);
    }

    @Test
    void testConstructorSkip() {
        Move skip = new Move(testPlayer, testBoard);
        assertEquals(skip.getPointsForMove(), 0);
        assertEquals(skip.getMoveType(), MoveType.SKIP);
        assertEquals(skip.getBoard(), testBoard);
        assertEquals(skip.getPlayer(),testPlayer);
    }
    
}