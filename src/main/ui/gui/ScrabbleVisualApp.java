package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import model.Direction;
import model.Player;
import model.ScrabbleGame;
import model.board.Board;
import model.move.Move;
import model.tile.LetterTile;
import model.tile.TileBag;
import persistance.JsonReader;
import persistance.JsonWriter;

import java.util.List;

// A Graphical user interface for Scrabble
public class ScrabbleVisualApp {
    
    private static final String JSON_STORE = "./data/gameToPlayTest.json";
    private static final String REQUEST_PLAYER_NAME_TEXT = "Type player name then click add";
    private static final int FRAME_SIDE_LENGTH = 1000;
    private static final int TILE_SIZE = 30;

    private static final Color DOUBLE_LETTER_COLOR = new Color(173, 216, 230);
    private static final Color TRIPLE_LETTER_COLOR = new Color(5, 127, 187);
    private static final Color DOUBLE_WORD_COLOR =  Color.PINK;
    private static final Color TRIPLE_WORD_COLOR = new Color(255, 0, 0);
    private static final Color DEFAULT_BOARD_SPACE_COLOR = new Color(190, 171, 141);
    private static final Color DEFAULT_LETTER_TILE_COLOR = new Color(244, 217, 138);

    private Board board;
    private TileBag tileBag;
    private ScrabbleGame scrabbleGame;
    private boolean gameRunning;
    private JPanel boardPanel;
    private JPanel rackPanel;
    private ScorePanel scorePanel;
    private JPanel historyPanel;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private List<Player> players;
    private int numPlayers;
    private JFrame frame;

    private JPanel panel;
    
    private JButton saveAndQuitButton;
    private JButton quitWithoutSavingButton;
    private JLabel coverPhoto;
    private JFrame loadOrPlayFrame;
    private JButton newGameButton;
    private JButton loadGameButton;

    private JFrame getPlayerNamesFrame;
    private JPanel requestPlayerNamePanel;
    private JTextField requestPlayerNameText;
    private JButton addPlayerButton;
    private JButton confirmAllPlayersButton;

    private JPanel moveButtons;
    private JPanel otherOptionButtons;
    private JButton playWordButton;
    private JButton swapTilesButton;
    private JButton skipTurnButton;
    private JButton turnSelectionConfirmationButton;
    private JButton toggleDirectionButton;
    private JButton clearSelectionsButton;
    private JPanel actionPanel;
    private int startRow;
    private int startCol;
    private Direction dir;

    // EFFECTS: Loads start menu frame to user screen.
    public ScrabbleVisualApp() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        dir = Direction.DOWN;
        tileBag = new TileBag(); // this needs to be here due to static tilebag and nature of json reader
        initializeStartMenu();
    }

    // MODIFIES: this
    // EFFECTS: Creates start menu frame for player to select
    // whether they would like to play a new game or load 
    // a saved game.
    private void initializeStartMenu() {
        loadOrPlayFrame = new JFrame("Start Menu");
        loadOrPlayFrame.setSize(FRAME_SIDE_LENGTH, FRAME_SIDE_LENGTH);
        coverPhoto = new JLabel(new ImageIcon("./data/startMenuBackgroundPhoto.jpg"));
        coverPhoto.setLayout(new GridBagLayout());
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
        coverPhoto.add(newGameButton, new GridBagConstraints());
        coverPhoto.add(loadGameButton, new GridBagConstraints());
        
        loadOrPlayFrame.add(coverPhoto);
        loadOrPlayFrame.repaint();
        loadOrPlayFrame.setVisible(true);
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

    // MODIFIES: this
    // EFFECTS: Prompts player to enter players' names
    //         in the desired order of play.
    private void requestPlayerNames() {
        getPlayerNamesFrame = new JFrame("Request Player Names");
        getPlayerNamesFrame.setSize(FRAME_SIDE_LENGTH, FRAME_SIDE_LENGTH);
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
                confirmPlayersButtonActionListener();
            }
        });
        requestPlayerNamePanel.add(requestPlayerNameText);
        requestPlayerNamePanel.add(addPlayerButton);
        requestPlayerNamePanel.add(confirmAllPlayersButton);
        getPlayerNamesFrame.add(requestPlayerNamePanel);
        getPlayerNamesFrame.setVisible(true);
    }

    public void confirmPlayersButtonActionListener() {
        getPlayerNamesFrame.setVisible(false);
        players = scrabbleGame.getPlayers();
        gameRunning = true;
        handleGame(0);  
    }

    // MODIFIES: player, board, tileBag
    // EFFECTS: Manages order of turn taking, 
    // and ensures player's draw new tiles
    // when they are supposed to
    private void handleGame(int nextPlayer) {
        int index = nextPlayer % numPlayers;
        frame = new JFrame("Scrabble Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_SIDE_LENGTH, FRAME_SIDE_LENGTH);
        frame.setLayout(new BorderLayout());
        //Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        //frame.setLocation(mouseLocation.x - FRAME_SIDE_LENGTH / 4, mouseLocation.y - FRAME_SIDE_LENGTH / 4);
        getBoardPanel(board);
        Player playerToPlayNext = players.get(index);
        scrabbleGame.drawTiles(playerToPlayNext);
        getRackPanel(playerToPlayNext);
        scorePanel = new ScorePanel(scrabbleGame);
        getHistoryPanel(playerToPlayNext);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(rackPanel, BorderLayout.SOUTH);
        frame.add(scorePanel, BorderLayout.WEST);
        frame.add(historyPanel, BorderLayout.EAST);
        
        frame.repaint();
        frame.setVisible(true);
        frame.repaint();
    }

    // MODIFIES: this
    // EFFECTS: loads panel with player's tiles,
    // along with their available action buttons
    private void getRackPanel(Player player) {
        rackPanel = new JPanel();
        rackPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        addActionButtons(player);
        List<LetterTile> letters = player.getTilesOnRack();
        for (int i = 0; i < letters.size(); i++) {
            rackPanel.add(createTilePanel(player, letters.get(i), i));
        }
/* 
        for (LetterTile letter : player.getTilesOnRack()) {
            JPanel tilePanel = createTilePanel(letter);
            rackPanel.add(tilePanel);
        } */
    }
//

    // MODIFIES: this
    // EFFECTS: loads panel with available action buttons
    private void addActionButtons(Player player) {
        moveButtons = new JPanel();
        otherOptionButtons = new JPanel();
        moveButtons.setLayout(new BoxLayout(moveButtons, BoxLayout.X_AXIS));
        otherOptionButtons.setLayout(new BoxLayout(otherOptionButtons, BoxLayout.X_AXIS));
        moveButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
        otherOptionButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
        playWordButton = new JButton("Play");
        swapTilesButton = new JButton("Swap");
        skipTurnButton = new JButton("Skip");
        String directionString = (dir == Direction.DOWN) ? "Down" : "Right";
        toggleDirectionButton = new JButton(directionString);
        clearSelectionsButton = new JButton("Clear selection");
        saveAndQuitButton = new JButton("Save and Quit");
        quitWithoutSavingButton = new JButton("Quit without Saving");
        addActionButtonActionListeners(player);
        addActionButtonsToActionButtonPanels();

    }

    // MODIFIES: this
    // EFFECTS: puts available action buttons into appropriate panels
    private void addActionButtonsToActionButtonPanels() {
        moveButtons.add(playWordButton);
        moveButtons.add(swapTilesButton);
        moveButtons.add(skipTurnButton);

        otherOptionButtons.add(toggleDirectionButton);
        otherOptionButtons.add(clearSelectionsButton);
        otherOptionButtons.add(saveAndQuitButton);
        otherOptionButtons.add(quitWithoutSavingButton);

        actionPanel.add(moveButtons);
        actionPanel.add(otherOptionButtons);
        
        rackPanel.add(actionPanel);
    }
    
    // REQUIRES: playWordButton, swapTilesButton, skipTurnButton,
    // toggleDirectionButton, and clearSelectionsButton have been
    // initialized
    // MODIFIES: this
    // EFFECTS: adds play, swap, and skip action listeners,
    // as well as clear selected tiles, direction, save and quit,
    // and quit without saving action listeners.
    public void addActionButtonActionListeners(Player player) {
        addMoveButtonActionListeners(player);
        addOtherButtonOptionsActionListeners(player);
    }

    // REQUIRES: playWordButton, swapTilesButton, skipTurnButton
    // have been initialized
    // MODIFIES: this
    // EFFECTS: adds play, swap, and skip action listeners
    public void addMoveButtonActionListeners(Player player) {
        playWordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scrabbleGame.playWord(player, startRow, startCol, dir);
                frame.setVisible(false);
                handleGame(scrabbleGame.getPlayerIndex(player) + 1);
            }
        });
        swapTilesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scrabbleGame.swapTiles(player);
                frame.setVisible(false);
                handleGame(scrabbleGame.getPlayerIndex(player) + 1);
            }
        });
        skipTurnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scrabbleGame.logSkippedTurn(player);
                frame.setVisible(false);
                handleGame(scrabbleGame.getPlayerIndex(player) + 1);
            }
        });
    }

    // REQUIRES: toggleDirectionButton, and clearSelectionsButton 
    // have been initialized
    // MODIFIES: this
    // EFFECTS: adds clear selected tiles, direction, save and quit,
    // and quit without saving action listeners.
    public void addOtherButtonOptionsActionListeners(Player player) {
        toggleDirectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Direction newDirection = (dir == Direction.DOWN) ? Direction.RIGHT : Direction.DOWN;
                dir = newDirection;
                String newText = (dir == Direction.DOWN) ? "Down" : "Right";
                toggleDirectionButton.setText(newText);
            }
        });
        clearSelectionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                player.clearSelectedTiles();
                getRackPanel(player);
                rackPanel.repaint();
                rackPanel.revalidate();
                // getRackPanel(player);
                // frame.repaint();
            }
        });
        saveAndQuitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSave(player);
                System.exit(0);
            }
        });
        quitWithoutSavingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    // MODIFIES: scrabbleGame
    // EFFECTS: Saves game to file
    private void handleSave(Player player) {
        try {
            scrabbleGame.setFirstPlayer(player);
            jsonWriter.open();
            jsonWriter.write(scrabbleGame);
            jsonWriter.close();
            // !!! ToDo Add "Okay" button to click before closing
        } catch (IOException e) {
            System.out.println("Unable to write to file " + JSON_STORE);
        }
    }

    
    // MODIFIES: this
    // EFFECTS: Adds panel representing player's tiles
    // to the frame
    private JButton createTilePanel(Player player, LetterTile letter, int letterIndex) {
        JButton tileButton = new JButton(letter.toDisplay());
        tileButton.setPreferredSize(new Dimension(40, 40));
        tileButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tileButton.setBackground(DEFAULT_LETTER_TILE_COLOR);
        tileButton.setFont(new Font("Arial", Font.BOLD, 16));
        tileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                player.selectTile(letterIndex);
                tileButton.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            }
        });
        return tileButton;
    }
    
    // MODIFIES: this
    // EFFECTS: Adds panel representing the current board
    // to the frame
    public void getBoardPanel(Board board) {
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(Board.BOARD_LENGTH, Board.BOARD_LENGTH));
        boardPanel.setPreferredSize(new Dimension(Board.BOARD_LENGTH * TILE_SIZE, Board.BOARD_LENGTH * TILE_SIZE));
        for (int i = 0; i < Board.BOARD_LENGTH; i++) {
            for (int j = 0; j < Board.BOARD_LENGTH; j++) {
                String toDisplay = board.getTileAtPositionOnBoard(i, j).toDisplay();
                JButton tile = new JButton(toDisplay);
                tile.setBackground(selectBoardTileColor(toDisplay));
                tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                tile.setFont(new Font("Arial", Font.BOLD, 14));
                final int row = i;
                final int col = j;
                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        startRow = row;
                        startCol = col;
                    }
                });
                boardPanel.add(tile);
            }
        }
    }

    // EFFECTS: returns appropriate display color given
    // displayString which corresponds to a tile on the board
    private Color selectBoardTileColor(String displayString) {
        Color color;
        switch (displayString) {
            case "DLS":
                color = DOUBLE_LETTER_COLOR;
                break;
            case "DWS":
                color = DOUBLE_WORD_COLOR;
                break;
            case "TLS":
                color = TRIPLE_LETTER_COLOR;
                break;
            case "TWS":
                color = TRIPLE_WORD_COLOR;
                break;
            case " ":
                color = DEFAULT_BOARD_SPACE_COLOR;
                break;
            default:
                color = DEFAULT_LETTER_TILE_COLOR;
                break;
        }
        return color;
    }

    // MODIFIES: 
    // EFFECTS: 
    public void getHistoryPanel(Player player) {
        historyPanel = new JPanel();
        historyPanel.setLayout(new FlowLayout());
        historyPanel.setPreferredSize(new Dimension(175, FRAME_SIDE_LENGTH));
        String summary = "";
        for (Move move : player.getHistory().getMoves()) {
            switch (move.getMoveType()) {
                case PLAY_WORD:
                    summary = getWordString(move, player);
                    break;
                case SWAP_TILES:
                    summary = getSwapSummary(move, player);
                    break;
                case SKIP:
                    summary = getSkipSummary(move, player);
                    break;
                case END_GAME_ADJUSTMENT:
                    summary = getSingleEndGameAdjustmentSummary(move, player);
                    break;
            }
            JTextArea moveSummary = new JTextArea(summary);
            moveSummary.setFont(new Font("Arial", Font.ITALIC, 12));
            moveSummary.setLineWrap(true);
            moveSummary.setWrapStyleWord(true);
            moveSummary.setCaretPosition(0);
            moveSummary.setEditable(false);
            moveSummary.setPreferredSize(new Dimension(150, 75));

            JPanel movePanel = new JPanel();
            movePanel.add(moveSummary);
            movePanel.setMaximumSize(new Dimension(200,300));
            historyPanel.add(movePanel);
            movePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        }
    }

    // EFFECTS: Returns string summary of a word played
    public String getWordString(Move word, Player p) {
        String printout = "\n" + p.getPlayerName() + " played ";
        String wordString = word.getLettersInvolved();
        String startRow = String.valueOf(word.getStartRow());
        String startCol = String.valueOf(word.getStartColumn());
        String coordinates = "(" + startRow + "," + startCol + ")";
        String direction = (word.getDirection() == Direction.RIGHT) ? "to the right" : "down";
        String points = String.valueOf(word.getPointsForMove());
        printout += wordString + " starting at " + coordinates + " and moving " 
                + direction + " earning " + points + " points.";
        return printout;
    } 

    //EFFECTS: Returns string summary of a player swap
    public String getSwapSummary(Move swap, Player p) {
        String printout = "\n" + p.getPlayerName() + " swapped tiles. ";
        String preAndPostLetters = swap.getLettersInvolved();
        int halfLength = preAndPostLetters.length() / 2;
        String preSwapLetters = preAndPostLetters.substring(0, halfLength);
        String postSwapLetters = preAndPostLetters.substring(halfLength);
        String points = String.valueOf(swap.getPointsForMove());
        printout += "Their tiles before swapping were: " + preSwapLetters + " and their tiles after swapping were " 
                 + postSwapLetters + ", earning " + points + " points.";
        return printout;
    }

    // EFFECTS: Returns string summary of a skipped turn
    public String getSkipSummary(Move skip, Player p) {
        return p.getPlayerName() + " skipped their turn";
    }

    // EFFECTS: Returns string summary of an end game adjustment
    public String getSingleEndGameAdjustmentSummary(Move move, Player player) {
        String playerName = player.getPlayerName();
        String lastPlayer = move.getLastPlayer().getPlayerName();
        int pointChange = move.getPointsForMove();
        int absolutePointChange = Math.abs(pointChange);
        String gainOrLoss = (pointChange >= 0) ? " gained " : " lost ";
        String pluralOrNot = (absolutePointChange != 1) ? "s." : ".";
        return lastPlayer + " used all their tiles first. " + playerName + gainOrLoss 
                + absolutePointChange + " point" + pluralOrNot;
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

    // EFFECTS: Start the Graphical User Interface
    public static void main(String[] args) {
        new ScrabbleVisualApp();
        
    }
}
