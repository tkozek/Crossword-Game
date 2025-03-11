package persistance;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Direction;
import model.History;
import model.Player;
import model.ScrabbleGame;
import model.board.Board;
import model.move.Move;
import model.move.MoveType;
import model.tile.LetterTile;
import model.tile.TileBag;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonWriterTest extends JsonTest {
    
    private ScrabbleGame game;
    private Board board;
    private TileBag tileBag;
    private Player player;

    @BeforeEach
    void setup() {
        board = new Board();
        tileBag = new TileBag();
        game = new ScrabbleGame("Test", board, tileBag);
        player = new Player("Tester", game);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("FileNotFoundException expected for illegal file name");
        } catch (IOException e) {
            // passes test
        }
    }

    @Test
    void testWriterInitialGame() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterInitialGame.json");
            writer.open();
            writer.write(game);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterInitialGame.json");
            game = reader.read();
            assertEquals("Test", game.getName());
            assertTrue(game.getHistory().getMoves().isEmpty());
            TileBag bagThatWasReadFromJson = game.getTileBag();
            assertEquals(bagThatWasReadFromJson.getCurrentLetterFrequencies(), tileBag.getCurrentLetterFrequencies()); 
            // Should test board as well
           // Player playerAddedToGameFromJson = game.getPlayers().get(0);
           // assertEquals(playerAddedToGameFromJson.getPlayerName(), "Tester");
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterTwoMovesHaveBeenPlayed() {
        try {
            game.addPlayer(player);
            tileBag.drawTiles(player);
            List<LetterTile> originalLettersDrawn = player.getTilesOnRack();
            List<LetterTile> originalLetterCopies = new ArrayList<>();
            Map<Character, Integer> originalLetterCounts = new HashMap<>();
            for (LetterTile letter : originalLettersDrawn) {
                char character = letter.getCharacter();
                originalLetterCopies.add(new LetterTile(letter));
                originalLetterCounts.put(character, originalLetterCounts.getOrDefault(character, 0) + 1);
            }
            player.selectTile(0);
            List<LetterTile> selectedTiles = player.getSelectedTiles();
            LetterTile letter1 = new LetterTile(selectedTiles.get(0));
            // 
            int score = board.playWord(selectedTiles, 7, 7, Direction.DOWN);
            // log word logs it to player's game too
            game.logWord(player, getStringFromLetters(selectedTiles), 7, 7, score, Direction.DOWN);
            player.removeSelectedTiles();
            //player.logSkippedTurn(board);
            // should expect there to be one letters played there that match
            // and expect tileBag to be missing the 7 originally drawn tiles      
            JsonWriter writer = new JsonWriter("./data/testWriterOneWordPlayed.json");
            writer.open();
            writer.write(game);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterOneWordPlayed.json");
            game = reader.read();
            assertEquals(game.getTileBag().numTilesRemaining(), TileBag.TOTAL_LETTERS_INITIALLY - 7);
            Map<Character, Integer> afterReadingTileBagCounts = game.getTileBag().getCurrentLetterFrequencies();

            char letter1Char = letter1.getCharacter();

            int originalLetter1CountDrawn = originalLetterCounts.get(letter1.getCharacter());

            int initializedLetter1BagCount = tileBag.getInitialLetterFrequencies().get(letter1Char);
            //Check tilebag counts
            assertEquals(afterReadingTileBagCounts.getOrDefault(letter1Char, 0) + originalLetter1CountDrawn, initializedLetter1BagCount);
            Board newBoard = game.getBoard();
            List<LetterTile> letterToPlay = new ArrayList<>();
            letterToPlay.add(new LetterTile('A', 1));
            assertFalse(newBoard.sectionIsAvailable(letterToPlay, 7, 7, Direction.DOWN));
            LetterTile firstPlacedLetter = (LetterTile) newBoard.getTileAtPositionOnBoard(7,7);
            // Check board updated correctly
            assertEquals(firstPlacedLetter.getCharacter(), letter1Char);
            // 1 play 1 skip
            History newGameHistory = game.getHistory();
            assertEquals(newGameHistory.getMoves().size(), 1);
            Move firstMove = newGameHistory.getMoves().get(0);
            assertEquals(firstMove.getMoveType(), MoveType.PLAY_WORD);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterTwoPlayersEachSwapped() {
        try {
            Player player2 = new Player("John", game);
            game.addPlayer(player);
            game.addPlayer(player2);

            tileBag.drawTiles(player);
            tileBag.drawTiles(player2);

            // Deep copy of initial tiles
            List<LetterTile> p1InitLetters = player.copySelectedTiles();
            List<LetterTile> p2InitLetters = player2.copySelectedTiles();

            // Select all tiles
            for (int i = 0; i < 7; i++) {
                player.selectTile(i);
                player2.selectTile(i);
            }
            // Both swap all tiles
            game.swapTiles(player);
            game.swapTiles(player2);
            
            // Copy of final letters
            List<LetterTile> p1FinalLetters = player.copyLetterTiles(player.getTilesOnRack());
            List<LetterTile> p2FinalLetters = player2.copyLetterTiles(player2.getTilesOnRack());

            game.logSwap(player, getStringFromLetters(p1InitLetters), getStringFromLetters(p1FinalLetters));
            game.logSwap(player2, getStringFromLetters(p2InitLetters), getStringFromLetters(p2FinalLetters));
            JsonWriter writer = new JsonWriter("./data/testWriterTwoPlayersEachSwapped.json");
            writer.open();
            writer.write(game);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterTwoPlayersEachSwapped.json");
            game = reader.read();
            Player copyP1 = game.getPlayerByName("Tester");
            Player copyP2 = game.getPlayerByName("John");
            // All entries should preserve order and have their tile values maintained
            for (int i = 0; i < 7; i++) {
                
                assertEquals(player.getTilesOnRack().get(i).getCharacter(), copyP1.getTilesOnRack().get(i).getCharacter());
                assertEquals(player.getTilesOnRack().get(i).getLetterPoints(), copyP1.getTilesOnRack().get(i).getLetterPoints());
                
                assertEquals(player2.getTilesOnRack().get(i).getCharacter(), copyP2.getTilesOnRack().get(i).getCharacter());
                assertEquals(player2.getTilesOnRack().get(i).getLetterPoints(), copyP2.getTilesOnRack().get(i).getLetterPoints());
            }

            assertEquals(copyP1.getHistory().getMoves().size(), player.getHistory().getMoves().size());
            assertEquals(copyP2.getHistory().getMoves().size(), player2.getHistory().getMoves().size());

            assertEquals(copyP1.getHistory().getMoves().get(0).getDirection(), player.getHistory().getMoves().get(0).getDirection());
            assertEquals(copyP1.getHistory().getMoves().get(0).getPointsForMove(), player.getHistory().getMoves().get(0).getPointsForMove());
            //assertEquals(copyP1.getHistory().getMoves().get(0).getLettersInvolved(), player.getHistory().getMoves().get(0).getLettersInvolved());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }


    }

    @Test
    void testWriteMoveComprehensive() {
        try {
            Player player2 = new Player("John", game);
            game.addPlayer(player);
            game.addPlayer(player2);
    
            tileBag.drawTiles(player);
            tileBag.drawTiles(player2);
    
            // Deep copy of initial tiles
            for (int i = 0; i < 7; i++) {
                player.selectTile(i);
                player2.selectTile(i);
            }
            List<LetterTile> p1InitLetters = player.copySelectedTiles();
            List<LetterTile> p2InitLetters = player2.copySelectedTiles();

            game.swapTiles(player);  // first logged move
            game.swapTiles(player2); // first logged move
            for (int i = 0; i < 7; i++) {
                player.selectTile(i);
                player2.selectTile(i);
            }
            List<LetterTile> p1FinalLetters = player.copySelectedTiles();
            List<LetterTile> p2FinalLetters = player2.copySelectedTiles();

            game.logSwap(player, getStringFromLetters(p1InitLetters), getStringFromLetters(p1FinalLetters)); // second logged move
            game.logSwap(player2, getStringFromLetters(p2InitLetters), getStringFromLetters(p2FinalLetters)); // second logged move
            game.logSkippedTurn(player); // 3rd

            game.logWord(player2, getStringFromLetters(player2.getSelectedTiles()), 7, 7, 70, Direction.DOWN); //3rd
            game.logWord(player, getStringFromLetters(player.getSelectedTiles()), 6, 7, 60, Direction.RIGHT); //4th


            JsonWriter writer = new JsonWriter("./data/testWriteMoveComprehensive.json");
            writer.open();
            writer.write(game);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriteMoveComprehensive.json");
            game = reader.read();
            List<Move> p1Moves = game.getPlayerByName("Tester").getHistory().getMoves();
            List<Move> p2Moves = game.getPlayerByName("John").getHistory().getMoves();

            assertEquals(p1Moves.size(), 4);
            assertEquals(p1Moves.get(0).getMoveType(), MoveType.SWAP_TILES);
            assertEquals(p1Moves.get(1).getMoveType(), MoveType.SWAP_TILES);
            assertEquals(p1Moves.get(2).getMoveType(), MoveType.SKIP);
            assertEquals(p1Moves.get(3).getMoveType(), MoveType.PLAY_WORD);
            assertEquals(p1Moves.get(3).getDirection(), Direction.RIGHT);

            assertEquals(p2Moves.size(), 3);
            assertEquals(p2Moves.get(0).getMoveType(), MoveType.SWAP_TILES);
            assertEquals(p2Moves.get(1).getMoveType(), MoveType.SWAP_TILES);
            assertEquals(p2Moves.get(2).getMoveType(), MoveType.PLAY_WORD);
            assertEquals(p2Moves.get(2).getDirection(), Direction.DOWN);


        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    } 

    @Test
    void testWriteEndGame() {
        try {
            Player player1 = new Player("Player1", game);
            Player player2 = new Player("Player2", game);
            Player player3 = new Player("Player3", game);
            game.addPlayer(player1);
            game.addPlayer(player2);
            game.addPlayer(player3);
            game.drawTiles(player1);
            game.drawTiles(player2);
            game.drawTiles(player3);

            String p1Letters = getStringFromLetters(player1.getTilesOnRack());
            String p2Letters = getStringFromLetters(player2.getTilesOnRack());
            String p3Letters = getStringFromLetters(player3.getTilesOnRack());

            tileBag.emptyDrawPile();
            for (int i = 0; i < Player.MAX_NUM_TILES; i++) {
                player1.selectTile(i);
            } 
            
            int scoreForP1Word = game.playWord(player1, 7, 7, Direction.DOWN);
            int pointValOnP2Rack = getTotalValueFromLetters(player2.getTilesOnRack());
            int pointValOnP3Rack = getTotalValueFromLetters(player3.getTilesOnRack());
            
            game.performEndGameAdjustments(player1);
            assertEquals(player1.getHistory().getMoves().get(1).getMoveType(), MoveType.END_GAME_ADJUSTMENT);
            JsonWriter writer = new JsonWriter("./data/testWriterEndGame.json");
            writer.open();
            writer.write(game);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterEndGame.json");
            game = reader.read();
            Player p1 = game.getPlayerByName("Player1");
            Player p2 = game.getPlayerByName("Player2");
            Player p3 = game.getPlayerByName("Player3");

            List<Move> p1Moves = p1.getHistory().getMoves();
            List<Move> p2Moves = p2.getHistory().getMoves();
            List<Move> p3Moves = p3.getHistory().getMoves();

            // p1 played once, then each player had one end game adjustment
            assertEquals(p1Moves.size(), 2);
            assertEquals(p2Moves.size(), 1);
            assertEquals(p3Moves.size(), 1);


            assertEquals(p1Moves.get(0).getMoveType(), MoveType.PLAY_WORD);
            assertEquals(p1Moves.get(0).getPointsForMove(), scoreForP1Word);
            assertEquals(p1Moves.get(1).getMoveType(), MoveType.END_GAME_ADJUSTMENT);

            assertEquals(p2Moves.get(0).getMoveType(), MoveType.END_GAME_ADJUSTMENT);
            assertEquals(p2Moves.get(0).getPointsForMove(), -1 * pointValOnP2Rack);

            assertEquals(p3Moves.get(0).getMoveType(), MoveType.END_GAME_ADJUSTMENT);
            assertEquals(p3Moves.get(0).getPointsForMove(), -1 * pointValOnP3Rack);

            assertEquals(p1Moves.get(1).getPointsForMove(),  pointValOnP2Rack + pointValOnP3Rack);

            assertEquals(p1.getPointsThisGame(), scoreForP1Word + pointValOnP2Rack + pointValOnP3Rack);
            assertEquals(p2.getPointsThisGame(), -1 *  pointValOnP2Rack);
            assertEquals(p3.getPointsThisGame(), -1 * pointValOnP3Rack);

            assertEquals(p2Letters, p2Moves.get(0).getLettersInvolved());
            assertEquals(p3Letters, p3Moves.get(0).getLettersInvolved());

            assertEquals(p2Letters + p3Letters, p1Moves.get(1).getLettersInvolved());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    } 

    // EFFECTS: returns list of letter tiles
    // based on input string
    private String getStringFromLetters(List<LetterTile> letters) {
        String result = "";
        for (LetterTile letter : letters) {
            result += letter.getString();
        }
        return result;
    }  

    private int getTotalValueFromLetters(List<LetterTile> letters) {
        int total = 0;
        for (LetterTile letter : letters) {
            total += letter.getLetterPoints();
        }
        return total;
    }
 
    
}
