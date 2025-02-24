package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

public class HistoryTest {
    
    private Board board;
    private TileBag testBag;
    private Set<Character> set;
    private Player testPlayer;
    private Player testPlayer2;
    private Board testBoard;
    private List<LetterTile> p1Letters;
    private List<LetterTile> p2Letters;
    private History history;
    private Move moveToAdd;
    private Move otherMoveToAdd;
    private LetterTile B; 
    private LetterTile A;
    private LetterTile Z;
    

    @BeforeEach
    void runBefore() {
        testBag = new TileBag();
        board =  new Board();
        testPlayer = new Player("Trevor", testBoard,testBag);
        testPlayer2 = new Player("Rovert", testBoard,testBag);
        p1Letters = testPlayer.getTilesOnRack();
        p2Letters = testPlayer2.getTilesOnRack();
        B = new LetterTile('B',3);
        A = new LetterTile('A',1);
        Z = new LetterTile('Z',10);
        history = new History("Trevor");
        moveToAdd = new Move(testPlayer, board, p1Letters, 1,8, 10, Direction.DOWN);
        otherMoveToAdd = new Move(testPlayer2, board, true, p1Letters, 7);

    }

    @Test
    void testConstructor() {
        assertEquals(history.getName(), "Trevor");
        assertTrue(history.getMoves().isEmpty());
        assertTrue(history.getListOfMovesContainingLetter('A').isEmpty());
    }
    @Test
    void testAddMoveOneMove() {
        assertEquals(history.getName(), "Trevor");
        assertTrue(history.getMoves().isEmpty());
        history.addMove(moveToAdd);
        assertEquals(history.getMoves().size(), 1);
        assertEquals(history.getMoves().get(0), moveToAdd);

    }
    @Test
    void testAddTwoMoves() {
        assertEquals(history.getName(), "Trevor");
        assertTrue(history.getMoves().isEmpty());
        history.addMove(otherMoveToAdd);
        history.addMove(moveToAdd);
        assertEquals(history.getMoves().size(), 2);
        assertEquals(history.getMoves().get(0), otherMoveToAdd);
        assertEquals(history.getMoves().get(1), moveToAdd);
    }

    @Test
    void testGetListOfMoveContainingLetterOneMove() {
        List<LetterTile> playedLetters = new ArrayList<>();
        playedLetters.add(A);
        playedLetters.add(Z);
        Move doesntHaveB = new Move(testPlayer, board, playedLetters, 3, 5, 13, Direction.DOWN);
        history.addMove(doesntHaveB);
        assertEquals(history.getMoves().size(), 1);
        assertEquals(0, history.getListOfMovesContainingLetter('B').size());
        assertEquals(1, history.getListOfMovesContainingLetter('A').size());
        assertEquals(1, history.getListOfMovesContainingLetter('Z').size());
    }
    @Test
    void testGetListOfMoveContainingLetterTwoMoves() {
        List<LetterTile> playedLetters = new ArrayList<>();
        playedLetters.add(A);
        playedLetters.add(B); // AB
        Move doesntHaveZ = new Move(testPlayer, board, playedLetters, 7,9, 8,Direction.RIGHT);
        history.addMove(doesntHaveZ);

        List<LetterTile> playedLetters2 = new ArrayList<>();
        playedLetters2.add(A);
        playedLetters2.add(Z); //AZ

        Move doesntHaveB = new Move(testPlayer, board, playedLetters2, 3, 5, 13, Direction.DOWN);
        history.addMove(doesntHaveB);
        assertEquals(history.getMoves().size(), 2);
        //BOTH HAVE A
        assertEquals(history.getListOfMovesContainingLetter('A').size(), 2);
        //One has B and One has Z
        assertEquals(history.getListOfMovesContainingLetter('B').size(),1);
        assertEquals(history.getListOfMovesContainingLetter('Z').size(),1);
        // Neither have Q
        assertEquals(history.getListOfMovesContainingLetter('Q').size(),0);

        assertEquals(doesntHaveB, history.getListOfMovesContainingLetter('A').get(1));
        assertEquals(doesntHaveZ, history.getListOfMovesContainingLetter('A').get(0));

        assertEquals(history.getListOfMovesContainingLetter('B').get(0), doesntHaveZ);

    }

    @Test
    void testGetAllWordsPlayed() {
        List<Move> wordsPlayed = testPlayer.getHistory().getMovesWithWordPlayed();
        assertTrue(wordsPlayed.isEmpty());
        // uses selected tiles within player, doesn't need non null tiles selected to make a move
        testPlayer.makeWord(board, 7, 7, 10, Direction.RIGHT);
        wordsPlayed = testPlayer.getHistory().getMovesWithWordPlayed();
        assertEquals(wordsPlayed.size(), 1);

        testPlayer.makeWord(board, 0, 4, 15, Direction.RIGHT);

        testBag.drawTiles(testPlayer);
        testPlayer.selectTile(1);

        testPlayer.makeWord(board, 0, 0, 5, Direction.DOWN);
        wordsPlayed = testPlayer.getHistory().getMovesWithWordPlayed();
        assertEquals(wordsPlayed.size(), 3);
        assertEquals(wordsPlayed.get(0).getMoveType(), MoveType.PLAY_WORD);
        assertEquals(wordsPlayed.get(0).getPointsForMove(), 10);

        assertEquals(wordsPlayed.get(1).getMoveType(), MoveType.PLAY_WORD);
        assertEquals(wordsPlayed.get(1).getPointsForMove(), 15);
        
        assertEquals(wordsPlayed.get(2).getMoveType(), MoveType.PLAY_WORD);
        assertEquals(wordsPlayed.get(2).getPointsForMove(), 5);
    }

    
}