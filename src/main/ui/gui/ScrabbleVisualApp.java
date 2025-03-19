package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import model.Player;
import model.ScrabbleGame;
import model.board.Board;
import model.tile.TileBag;
import persistance.JsonReader;
import persistance.JsonWriter;

import java.util.List;
import java.util.ArrayList;

// A Graphical user interface for Scrabble
public class ScrabbleVisualApp {
    
    private static final String JSON_STORE = "./data/gameToPlayTest.json";
    private static final String REQUEST_PLAYER_NAME_TEXT = "Type player name then click add";
    private Board board;
    private TileBag tileBag;
    private ScrabbleGame scrabbleGame;
    private boolean gameRunning;
    private BoardPanel boardPanel;
    private RackPanel rackPanel;
    private ScorePanel scorePanel;
    private HistoryPanel historyPanel;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private List<Player> players;
    private int numPlayers;
    private JFrame frame;

    private JPanel panel;
    
    private JButton saveButton;

    private JFrame loadOrPlayFrame;
    private JButton newGameButton;
    private JButton loadGameButton;

    private JFrame getPlayerNamesFrame;
    private JPanel requestPlayerNamePanel;
    private JTextField requestPlayerNameText;
    private JButton addPlayerButton;
    private JButton confirmAllPlayersButton;

    private JButton playWordButton;
    private JButton swapTilesButton;
    private JButton skipTurnButton;
    private JButton turnSelectionConfirmationButton;

    public ScrabbleVisualApp() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        initializeMainMenu();
    }

    private void initializeMainMenu() {
        loadOrPlayFrame = new JFrame("Start Menu");
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        loadOrPlayFrame.setLocation(mouseLocation.x, mouseLocation.y);
        loadOrPlayFrame.setSize(1000,1000);
        panel = new JPanel();
       // panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        newGameButton = new JButton("New Game");
        loadGameButton = new JButton("Load Game");
        

        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadOrPlayFrame.setVisible(false);
                initializeNewGame();
            }
        });

        loadGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadOrPlayFrame.setVisible(false);
                loadOldGame();
                
            }
        });

        panel.add(newGameButton);
        panel.add(loadGameButton);
        loadOrPlayFrame.add(panel);
        loadOrPlayFrame.repaint();
        loadOrPlayFrame.setVisible(true);
        loadOrPlayFrame.repaint();

    }

    // MODIFIES: scrabbleGame, players, tileBag, board, numPlayers
    // EFFECTS: loads assets from previously saved game
    private void loadOldGame() {
        this.gameRunning = true;
        try {
            scrabbleGame = jsonReader.read();
            this.players = scrabbleGame.getPlayers();
            this.board = scrabbleGame.getBoard();
            this.tileBag = scrabbleGame.getTileBag();
            this.numPlayers = players.size();
            handleGame(0);
            // System.out.println("Loaded " + scrabbleGame.getName() + " with " + String.valueOf(numPlayers) 
            //         + " players from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read game from file: " + JSON_STORE);
        }
    }

    // MODIFIES: scrabbleGame, board, tileBag
    // EFFECTS: creates assets for a new game and prompts user input
    // for setup parameters
    private void initializeNewGame() {
        this.board = new Board();
        this.tileBag = new TileBag();
        this.scrabbleGame = new ScrabbleGame("game", board, tileBag);
        this.numPlayers = 0;
        requestPlayerNames();
        //scrabbleGame.setFirstPlayer(players.get(0)); // !!! Todo add exception handling
        
    }

    private void requestPlayerNames() {
        getPlayerNamesFrame = new JFrame("Request Player Names");
        getPlayerNamesFrame.setSize(1000,1000);
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        getPlayerNamesFrame.setLocation(mouseLocation.x, mouseLocation.y);
        // !!! ToDO Add some text box with instructions to hover above these buttons
        requestPlayerNamePanel = new JPanel();

        requestPlayerNameText = new JTextField(REQUEST_PLAYER_NAME_TEXT);
        addPlayerButton = new JButton("Add player with name in text field");
        confirmAllPlayersButton = new JButton("Start game with players entered so far");

        addPlayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPlayer();
                requestPlayerNameText.setText(REQUEST_PLAYER_NAME_TEXT);
            }
        });

        confirmAllPlayersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getPlayerNamesFrame.setVisible(false);
                players = scrabbleGame.getPlayers();
                gameRunning = true;
                handleGame(0);  
            }
        });
        requestPlayerNamePanel.add(requestPlayerNameText);
        requestPlayerNamePanel.add(addPlayerButton);
        requestPlayerNamePanel.add(confirmAllPlayersButton);
        getPlayerNamesFrame.add(requestPlayerNamePanel);

        getPlayerNamesFrame.setVisible(true);

    }

    // MODIFIES: player, board, tileBag
    // EFFECTS: Manages order of turn taking, 
    // and ensures player's draw new tiles
    // when they are supposed to
    private void handleGame(int nextPlayer) {
        int index = nextPlayer % numPlayers;
        frame = new JFrame("Scrabble Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,1000);
        frame.setLayout(new BorderLayout());
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        frame.setLocation(mouseLocation.x, mouseLocation.y);
        boardPanel = new BoardPanel(board);
        Player playerToPlayNext = players.get(index);
        scrabbleGame.drawTiles(playerToPlayNext);
        rackPanel = new RackPanel(playerToPlayNext);
        scorePanel = new ScorePanel(scrabbleGame);
        historyPanel = new HistoryPanel(playerToPlayNext);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(rackPanel, BorderLayout.SOUTH);
        frame.add(scorePanel, BorderLayout.WEST);
        frame.add(historyPanel, BorderLayout.EAST);
        frame.repaint();
        frame.setVisible(true);
        frame.repaint();
            
    }
    

    // MODIFIES: this
    // EFFECTS: adds a new player to the game
    // who's name is the text currently in 
    // requestPlayerNameText
    private void addPlayer() {
        String name = requestPlayerNameText.getText();
        Player player = new Player(name, scrabbleGame);
        scrabbleGame.addPlayer(player);
        numPlayers++;
    }

    public static void main(String[] args) {
        new ScrabbleVisualApp();
        
    }
}
