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
public class ScrabbleVisualApp extends JFrame {
    
    private static final String JSON_STORE = "./data/gameToPlayTest.json";
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

    private JPanel panel;
    private JButton newGame;
    private JButton loadGame;
    private JButton saveButton;

    public ScrabbleVisualApp() {
        super("Scrabble Game");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        tileBag = new TileBag();
        loadOldGame();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,1000);
        setLayout(new BorderLayout());

        boardPanel = new BoardPanel(board);
        rackPanel = new RackPanel(players.get(0));
        scorePanel = new ScorePanel(scrabbleGame);
        historyPanel = new HistoryPanel(players.get(0));

        add(boardPanel, BorderLayout.CENTER);
        add(rackPanel, BorderLayout.SOUTH);
        add(scorePanel, BorderLayout.WEST);
        add(historyPanel, BorderLayout.EAST);
        repaint();
        setVisible(true);
        repaint();
        setLocationRelativeTo(null);
    }

    private void initializeMainMenu() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        newGame = new JButton("New Game");
        loadGame = new JButton("Load Game");
        

        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initializeNewGame();
            }
        });

        loadGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadOldGame();
                
            }
        });

        panel.add(newGame);
        panel.add(loadGame);
        add(panel);
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
        //requestPlayerNames();
    }

    public static void main(String[] args) {
        new ScrabbleVisualApp();
        
    }
}
