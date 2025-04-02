package ui.gui;

import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import model.*;
import model.board.*;
import model.tile.*;
import model.move.*;
import ui.ScrabbleConsoleApp;
import ui.ScrabbleUserInterface;

import persistance.JsonReader;
import persistance.JsonWriter;

// A Graphical user interface for Scrabble
public class ScrabbleVisualApp extends ScrabbleUserInterface {
    
    //private static final String JSON_STORE = "./data/savedgames/gameToPlayTest.json";
    private static final String JSON_STORE = "./data/savedgames/defaultSaveFile.json";

    private static final String REQUEST_PLAYER_NAME_TEXT = "Type player name then click add";
    private static final int FRAME_SIDE_LENGTH = 1000;
    private static final int TILE_SIZE = 30;
    private static final int BOARD_PANEL_LENGTH = Board.BOARD_LENGTH * TILE_SIZE;
    private static final int REQUEST_NAMES_FRAME_WIDTH = 1000 * 2 / 3;
    private static final int REQUEST_NAMES_FRAME_HEIGHT = 100;
    private static final Font TILE_FONT = new Font("Arial", Font.BOLD, 14);
    //private static final int REMAINING_TILE_PRINTOUT_HEIGHT = 20;
    private static final Color DOUBLE_LETTER_COLOR = new Color(173, 216, 230);
    private static final Color TRIPLE_LETTER_COLOR = new Color(5, 127, 187);
    private static final Color DOUBLE_WORD_COLOR =  Color.PINK;
    private static final Color TRIPLE_WORD_COLOR = new Color(255, 0, 0);
    private static final Color DEFAULT_BOARD_SPACE_COLOR = new Color(190, 171, 141);
    private static final Color DEFAULT_LETTER_TILE_COLOR = new Color(244, 217, 138);
    private static final Color SELECTED_TILE_BORDER_COLOR = new Color(128, 0, 128);
    private static final String SEARCH_WORDS_DEFAULT_DISPLAY_TEXT = "";//"Enter a letter, then press 'search' " 
               // + " to \ndisplay all words with that letter";
    private static final String SEARCH_REMAINING_COUNTS_DEFAULT_DISPLAY_TEXT = "";
    //"Enter a letter to get its remaining "+ "count in draw pile and opponent racks\n or blank to see all";

    
    private JPanel boardPanel;
    private JPanel rackPanel;
    private JPanel scorePanel;

    private JPanel infoPanel;
    private JPanel movesPanel;
    private JPanel wordsPanel;
    private JButton searchWordsButton;
    private JTextField wordFilterField;
    private JPanel letterDistributionPanel;
    private JButton searchLetterCountsButton;
    private JTextField searchLetterCountsField;
    private JTabbedPane infoTabs;

    private JFrame frame;
    
    private JButton saveAndQuit;
    private JButton saveAsAndQuit;
    private JButton quitWithoutSaving;
    private JPanel saveSaveAsPanel;
    private JPanel quitOrCancelPanel;
    private JFileChooser fileChooser;

    private JLabel coverPhoto;
    private JFrame loadOrPlayFrame;
    private JButton newGame;
    private JButton continueGame;
    private JButton loadSelected;
    private JComboBox saveGameDropdown;

    private String selectedFileName;

    private JFrame playerNameFrame;
    private JPanel addPlayerPanel;
    private JTextField nameInput;
    private JButton addPlayerButton;
    private JButton startButton;

    private JButton confirm;
    private JButton cancel;

    private JPanel moveButtons;
    private JPanel otherOptionButtons;
    private JButton playButton;
    private JButton swapButton;
    private JButton skipButton;
    private JButton previewButton;
    private JButton terminalUIButton;
    
    private JButton directionToggle;
    private JButton clearSelections;
    private JPanel actionPanel;
    private int startRow;
    private int startCol;
    private Direction dir;

    private String lastGamePath;

    // EFFECTS: Loads start menu frame to user screen.
    public ScrabbleVisualApp() {
        super();
        dir = Direction.DOWN;
        initializeStartMenu();
    }

    public ScrabbleVisualApp(ScrabbleGame game) {
        super();
        this.game = game;
        this.numPlayers = game.getNumPlayers();
        this.gameRunning = true;
        dir = Direction.DOWN;
        handleGame(game.getCurrentPlayerIndex());
    }

    // MODIFIES: this
    // EFFECTS: Creates start menu frame for player to select
    // whether they would like to play a new game or load 
    // a saved game.
    private void initializeStartMenu() {
        loadOrPlayFrame = new JFrame("Start Menu");
        loadOrPlayFrame.setSize(FRAME_SIDE_LENGTH, FRAME_SIDE_LENGTH);

        Image originalImage = new ImageIcon("./data/startMenuBackgroundPhoto.jpg").getImage();
        Image scaledImage = originalImage.getScaledInstance(FRAME_SIDE_LENGTH, FRAME_SIDE_LENGTH, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(scaledImage);

        coverPhoto = new JLabel(newIcon);
        coverPhoto.setLayout(new BoxLayout(coverPhoto, BoxLayout.Y_AXIS));
        newGame = new JButton("New Game");
        continueGame = new JButton("Continue Game");
        loadSelected = new JButton("Load Selected Game");
        saveGameDropdown = new JComboBox<>(SavedGameManager.getAllSaveFiles());
        
        addStartMenuListeners();
        Box.Filler verticalFiller = new Box.Filler(
                new Dimension(400, 850), // Horizontal size (0), Vertical size (100 pixels)
                new Dimension(400, 850),
                new Dimension(400, 850)
        );
        coverPhoto.add(verticalFiller);
        coverPhoto.add(newGame, new GridBagConstraints());
        coverPhoto.add(continueGame, new GridBagConstraints());
        coverPhoto.add(loadSelected, new GridBagConstraints());
        coverPhoto.add(saveGameDropdown, new GridBagConstraints());


        loadOrPlayFrame.add(coverPhoto);
        loadOrPlayFrame.repaint();
        loadOrPlayFrame.setVisible(true);
    }



    //REQUIRES: newGameButton and loadGameButton have been initialized
    //MODIFIES: newGameButton and loadGameButton 
    //EFFECTS: adds action listeners for newGameButton and loadGameButton 
    private void addStartMenuListeners() {
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadOrPlayFrame.setVisible(false);
                initializeNewGame();
            }
        });
        continueGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadOrPlayFrame.setVisible(false);
                loadLastSavedGame(); 
            }
        });
        loadSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadSelectedGame();
            }
        });
    }

    // MODIFIES: scrabbleGame, players, numPlayers
    // EFFECTS: loads assets from previously saved game
    private void loadLastSavedGame() {
        this.gameRunning = true;
        try {
            lastGamePath = SavedGameManager.getLastGamePath();
            JsonReader jsonReader = new JsonReader(lastGamePath);
            game = jsonReader.read();
            this.numPlayers = game.getNumPlayers();
            handleGame(game.getCurrentPlayerIndex());
        } catch (IOException e) {
            System.out.println("Unable to read game from file: " + lastGamePath);
        }
    }

    // MODIFIES: scrabbleGame, players, numPlayers
    // EFFECTS: loads assets from previously saved game
    private void loadSelectedGame() {
        this.gameRunning = true;
        try {
            selectedFileName = SavedGameManager.getSavedGamesDirectory() + (String) saveGameDropdown.getSelectedItem();
            JsonReader jsonReader = new JsonReader(selectedFileName);
            game = jsonReader.read();
            this.numPlayers = game.getNumPlayers();
            handleGame(game.getCurrentPlayerIndex());
        } catch (IOException e) {
            System.out.println("Unable to read game from file: " + selectedFileName);
        }
    }

    // MODIFIES: scrabbleGame
    // EFFECTS: creates assets for a new game and prompts user input
    // for setup parameters
    private void initializeNewGame() {
        this.game = new ScrabbleGame("game");
        this.numPlayers = 0;
        requestPlayerNames();
        // !!! TODO add exception handling
    }

    // MODIFIES: this
    // EFFECTS: Prompts player to enter players' names
    //         in the desired order of play.
    private void requestPlayerNames() {
        playerNameFrame = new JFrame("Request Player Names");
        playerNameFrame.setSize(REQUEST_NAMES_FRAME_WIDTH, REQUEST_NAMES_FRAME_HEIGHT);
        
        //playerNameFrame.addKeyListener()
        addPlayerPanel = new JPanel();
        nameInput = new JTextField(REQUEST_PLAYER_NAME_TEXT);
        nameInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addPlayer();
                    nameInput.setText("");
                }
            }
        });

        
        addPlayerButton = new JButton("Add player with name in textbox");
        startButton = new JButton("Start Game");
        addNameActionListeners();
        addNameFocusListeners();
        addPlayerPanel.add(nameInput);
        addPlayerPanel.add(addPlayerButton);
        addPlayerPanel.add(startButton);
        playerNameFrame.add(addPlayerPanel);
        playerNameFrame.setVisible(true);
    }

    // MODIFIES: addPlayerButton, confirmAllPlayersButton
    // EFFECTS: adds ActionListeners to addPlayerButton and confirmAllPlayersButton
    private void addNameActionListeners() {
        addPlayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPlayer();
                nameInput.setText(REQUEST_PLAYER_NAME_TEXT);
            }
        });

        /* addPlayerButton.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addPlayer();
                    nameInput.setText(REQUEST_PLAYER_NAME_TEXT);
                }
            }
        }); */
        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playerNameFrame.setVisible(false);
                gameRunning = true;
                handleGame(0);
            }
        });
    }

    // MODIFIES: requestPlayerNameText
    // EFFECTS: adds FocusListener to requestPlayerNameText
    private void addNameFocusListeners() {
        nameInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameInput.getText().equals(REQUEST_PLAYER_NAME_TEXT)) {
                    nameInput.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameInput.getText().isEmpty()) {
                    nameInput.setText(REQUEST_PLAYER_NAME_TEXT);
                }
            }
        });
    }

    // MODIFIES: player
    // EFFECTS: Manages order of turn taking, 
    // and ensures player's draw new tiles
    // when they are supposed to
    private void handleGame(int nextPlayer) {
        int index = nextPlayer % numPlayers;
        frame = new JFrame("Scrabble Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        updateScorePanel(game);
        Double scorePanelWidth = scorePanel.getPreferredSize().getWidth();
        frame.setSize(FRAME_SIDE_LENGTH - 15 + scorePanelWidth.intValue(), FRAME_SIDE_LENGTH);
        Player playerToPlayNext = game.getPlayerByIndex(index); //curPlayer
        game.drawTiles(playerToPlayNext); //curPlayer
        
        frame.add(getBoardPanel(), BorderLayout.CENTER);
        frame.add(getRackPanel(playerToPlayNext), BorderLayout.SOUTH); //curPlayer
        frame.add(scorePanel, BorderLayout.WEST);
        frame.add(getInfoPanel(playerToPlayNext), BorderLayout.EAST); //curPlayer
        
        frame.repaint();
        frame.setVisible(true);
    }

    // EFFECTS: adds players' names to along with their score
    // to scorePanel in the format name: score
    private void updateScorePanel(ScrabbleGame scrabbleGame) {
        scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        JLabel scoreLabel = new JLabel("<html><u>Scoreboard</u></html>");
        scoreLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        scorePanel.add(scoreLabel);
        Map<String, Integer> playerScoreMap = game.getPlayerScoreMap();
        for (Map.Entry<String, Integer> entry : playerScoreMap.entrySet()) {
            JLabel playerLabel = new JLabel(entry.getKey() + ": " + entry.getValue());
            scorePanel.add(playerLabel);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads panel with player's tiles,
    // along with their available action buttons
    private JPanel getRackPanel(Player player) {
        rackPanel = new JPanel();
        rackPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        addActionButtons(player);
        List<LetterTile> letters = player.getTilesOnRack();
        int numLetters = letters.size();
        for (int i = 0; i < numLetters; i++) {
            rackPanel.add(createTilePanel(player, letters.get(i), i));
        }
        return rackPanel;
    }

    // MODIFIES: this
    // EFFECTS: loads panel with available action buttons
    private void addActionButtons(Player player) {
        moveButtons = new JPanel();
        otherOptionButtons = new JPanel();

        moveButtons.setLayout(new BoxLayout(moveButtons, BoxLayout.X_AXIS));
        otherOptionButtons.setLayout(new BoxLayout(otherOptionButtons, BoxLayout.X_AXIS));
        moveButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
        otherOptionButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
        previewButton = new JButton("Preview");
        playButton = new JButton("Play");
        swapButton = new JButton("Swap");
        skipButton = new JButton("Skip");

        terminalUIButton = new JButton("Terminal UI");
        String directionString = (dir == Direction.DOWN) ? "Down" : "Right";
        directionToggle = new JButton(directionString);
        clearSelections = new JButton("Clear");

        saveAndQuit = new JButton("Save and Quit");
        saveAndQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    frame.remove(rackPanel);
                    frame.add(getSaveAndQuitRackPanel(player), BorderLayout.SOUTH);
                    repaintAndRevalidate(rackPanel);
                    repaintAndRevalidate(boardPanel);
                });
                //handleSave(player);
                //printEventLog();
                //System.exit(0);
            }
        });
        addMoveListeners(player);
        addOtherOptionsListeners(player);
        addButtonsToActionPanel();

    }

    // MODIFIES: this
    // EFFECTS: puts available action buttons into appropriate panels
    private void addButtonsToActionPanel() {
        
        moveButtons.add(playButton);
        moveButtons.add(swapButton);
        moveButtons.add(skipButton);
        moveButtons.add(previewButton);

        
        otherOptionButtons.add(directionToggle);
        otherOptionButtons.add(clearSelections);
        otherOptionButtons.add(terminalUIButton);
        otherOptionButtons.add(saveAndQuit);
    
        actionPanel.add(moveButtons);
        actionPanel.add(otherOptionButtons);
        
        rackPanel.add(actionPanel);
    }

    // REQUIRES: playWordButton, swapTilesButton, skipTurnButton
    // have been initialized
    // MODIFIES: this
    // EFFECTS: adds play, swap, and skip action listeners
    private void addMoveListeners(Player player) {
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.playWord(player, startRow, startCol, dir);
                frame.setVisible(false);
                if (player.outOfTiles()) {
                    handleEndGame(player);
                } else {
                    handleGame(game.getPlayerIndex(player) + 1);
                }
            }
        });
        swapButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.swapTiles(player);
                frame.setVisible(false);
                handleGame(game.getPlayerIndex(player) + 1);
            }
        });
        skipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.logSkippedTurn(player);
                frame.setVisible(false);
                handleGame(game.getPlayerIndex(player) + 1);
            }
        });
        
        previewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    frame.remove(boardPanel);
                    frame.add(getPreviewBoardPanel(player), BorderLayout.CENTER);
                    repaintAndRevalidate(boardPanel);
                    SwingUtilities.invokeLater(() -> {
                        frame.remove(rackPanel);
                        frame.add(getConfirmOrCancelRackPanel(player), BorderLayout.SOUTH);
                        repaintAndRevalidate(rackPanel);
                        repaintAndRevalidate(boardPanel);
                    });
                });   
            }
        });
    }

    private JPanel getConfirmOrCancelRackPanel(Player player) {
        rackPanel = new JPanel();
        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        confirm = new JButton("Confirm");
        cancel = new JButton("Cancel");

        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.playWord(player, startRow, startCol, dir);
                frame.setVisible(false);
                if (player.outOfTiles()) {
                    handleEndGame(player);
                } else {
                    handleGame(game.getPlayerIndex(player) + 1);
                }
            }
        });
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    frame.remove(rackPanel);
                    frame.remove(boardPanel);
                    player.clearSelectedTiles();
                    frame.add(getRackPanel(player), BorderLayout.SOUTH);
                    frame.add(getBoardPanel(), BorderLayout.CENTER);
                    repaintAndRevalidate(rackPanel);
                    repaintAndRevalidate(boardPanel);
                });
            }
        });
        actionPanel.add(confirm);
        actionPanel.add(cancel);
        rackPanel.add(actionPanel);
        List<LetterTile> letters = player.getTilesOnRack();
        for (int i = 0; i < letters.size(); i++) {
            rackPanel.add(createTilePanelNotClickable(player, letters.get(i), i));
        }
        return rackPanel;
    }

    // REQUIRES: toggleDirectionButton, and clearSelectionsButton 
    // have been initialized
    // MODIFIES: this
    // EFFECTS: adds clear selected tiles, direction, save and quit,
    // and quit without saving action listeners.
    private void addOtherOptionsListeners(Player player) {
        terminalUIButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.setCurrentPlayer(player);
                frame.setVisible(false);
                new ScrabbleConsoleApp(game);
                gameRunning = false;
            }
        });
        directionToggle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Direction newDirection = (dir == Direction.DOWN) ? Direction.RIGHT : Direction.DOWN;
                dir = newDirection;
                String newText = (dir == Direction.DOWN) ? "Down" : "Right";
                directionToggle.setText(newText);
            }
        });
        clearSelections.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                player.clearSelectedTiles();
                SwingUtilities.invokeLater(() -> {
                    frame.remove(rackPanel);
                    frame.add(getRackPanel(player), BorderLayout.SOUTH);
                    repaintAndRevalidate(rackPanel);
                });                
            }
        });
        //addSaveAndQuitActionListeners(player);
    }

    // MODIFIES: this
    // EFFECTS: adds save and quit, and quit without saving action listeners.
    private void addSaveAndQuitActionListeners(Player player) {
        saveAndQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSave(player);
                printEventLog();
                System.exit(0);
            }
        });
        saveAsAndQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    fileChooser = new JFileChooser(SavedGameManager.getSavedGamesDirectory());
                    fileChooser.setDialogTitle(("Save game as"));
                    int result = fileChooser.showSaveDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        if (!selectedFile.getName().endsWith(".json")) {
                            selectedFile = new File(selectedFile.getAbsolutePath() + ".json");
                        }
                        handleSave(player, selectedFile.getAbsolutePath());
                        System.out.println("Saving Game as : " + selectedFile.getName());
                        printEventLog();
                        System.exit(0);
                    }
                });
                
            }
        });
        quitWithoutSaving.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printEventLog();
                System.exit(0);
            }
        });
    }

    private JPanel getSaveAndQuitRackPanel(Player player) {
        rackPanel = new JPanel();
        rackPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
       // saveSaveAsPanel = new JPanel(new BoxLayout(saveSaveAsPanel, BoxLayout.X_AXIS));
        saveSaveAsPanel = new JPanel();
        quitOrCancelPanel = new JPanel();
        saveSaveAsPanel.setLayout(new BoxLayout(saveSaveAsPanel, BoxLayout.X_AXIS));
        quitOrCancelPanel.setLayout(new BoxLayout(quitOrCancelPanel, BoxLayout.X_AXIS));

        saveSaveAsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitOrCancelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        saveAndQuit = new JButton("Save & Quit");
        saveAsAndQuit = new JButton("Save as & Quit");
        quitWithoutSaving = new JButton("Quit without Saving");
        addSaveAndQuitActionListeners(player);
        cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    frame.remove(rackPanel);
                    frame.remove(boardPanel);
                    player.clearSelectedTiles();
                    frame.add(getRackPanel(player), BorderLayout.SOUTH);
                    frame.add(getBoardPanel(), BorderLayout.CENTER);
                    repaintAndRevalidate(rackPanel);
                    repaintAndRevalidate(boardPanel);
                });
            }
        });
        saveSaveAsPanel.add(saveAndQuit);
        saveSaveAsPanel.add(saveAsAndQuit);
        quitOrCancelPanel.add(quitWithoutSaving);
        quitOrCancelPanel.add(cancel);
        actionPanel.add(saveSaveAsPanel);
        actionPanel.add(quitOrCancelPanel);
        rackPanel.add(actionPanel);
        List<LetterTile> letters = player.getTilesOnRack();
        for (int i = 0; i < letters.size(); i++) {
            rackPanel.add(createTilePanelNotClickable(player, letters.get(i), i));
        }
        return rackPanel;
    }

    // MODIFIES: scrabbleGame
    // EFFECTS: Saves game to file
    private void handleSave(Player player) {
        try {
            JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
            game.setCurrentPlayer(player);
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();
            // !!! ToDo Add "Okay" button to click before closing
        } catch (IOException e) {
            System.out.println("Unable to write to file " + JSON_STORE);
        }
    }

    // MODIFIES: scrabbleGame
    // EFFECTS: Saves game to file
    private void handleSave(Player player, String filePath) {
        try {
            JsonWriter jsonWriter = new JsonWriter(filePath);
            game.setCurrentPlayer(player);
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();
            // !!! ToDo Add "Okay" button to click before closing
        } catch (IOException e) {
            System.out.println("Unable to write to file " + filePath);
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
                tileButton.setBorder(BorderFactory.createLineBorder(SELECTED_TILE_BORDER_COLOR));
            }
        });
        return tileButton;
    }

    // MODIFIES: this
    // EFFECTS: Adds panel representing player's tiles
    // to the frame
    private JButton createTilePanelNotClickable(Player player, LetterTile letter, int letterIndex) {
        JButton tileButton = new JButton(letter.toDisplay());
        tileButton.setPreferredSize(new Dimension(40, 40));
        tileButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tileButton.setBackground(DEFAULT_LETTER_TILE_COLOR);
        tileButton.setFont(new Font("Arial", Font.BOLD, 16));
        
        return tileButton;
    }
    
    // MODIFIES: this
    // EFFECTS: Adds panel representing the current board
    // to the frame
    private JPanel getBoardPanel() {
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(Board.BOARD_LENGTH, Board.BOARD_LENGTH));
        boardPanel.setPreferredSize(new Dimension(BOARD_PANEL_LENGTH, BOARD_PANEL_LENGTH));
        for (int i = 0; i < Board.BOARD_LENGTH; i++) {
            for (int j = 0; j < Board.BOARD_LENGTH; j++) {
                String toDisplay = game.getTileAtPositionOnBoard(i, j).toDisplay();
                JButton tile = new JButton(toDisplay);
                tile.setBackground(selectBoardTileColor(toDisplay));
                tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                tile.setFont(TILE_FONT);
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
        return boardPanel;
    }

    // MODIFIES: this
    // EFFECTS: Adds panel representing the current board
    // to the frame
    private JPanel getPreviewBoardPanel(Player player) {
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(Board.BOARD_LENGTH, Board.BOARD_LENGTH));
        boardPanel.setPreferredSize(new Dimension(BOARD_PANEL_LENGTH, BOARD_PANEL_LENGTH));
        String[][] previewBoardDisplay = game.previewBoardDisplay(player, startRow, startCol, dir);
        for (int i = 0; i < Board.BOARD_LENGTH; i++) {
            for (int j = 0; j < Board.BOARD_LENGTH; j++) {
                String toDisplay = previewBoardDisplay[i][j];
                
                JButton tile = new JButton(toDisplay);
                tile.setBackground(selectBoardTileColor(toDisplay));
                tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                if (!(toDisplay.equals(game.getTileAtPositionOnBoard(i, j).toDisplay()))) {
                    tile.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
                }
                tile.setFont(TILE_FONT);
                boardPanel.add(tile);
            }
        }
        return boardPanel;
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

    // MODIFIES: this
    // EFFECTS: adds tabbed information panel to frame
    private JPanel getInfoPanel(Player player) {
        infoPanel = new JPanel();
        infoPanel.setLayout(new CardLayout());
        infoPanel.setPreferredSize(new Dimension(175, FRAME_SIDE_LENGTH));
        infoTabs = new JTabbedPane();
        addMovesTab(player);
        addWordsTab(player);
        addLetterDistributionTab(player);

        infoTabs.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = infoTabs.getSelectedIndex();
                if (selectedIndex == 2) {
                    resetTab(letterDistributionPanel);
                } else if (selectedIndex == 1) {
                    resetTab(wordsPanel);
                } 
            }
        });
        infoPanel.add(infoTabs);
        return infoPanel;
    }

    // MODIFIES: infoTabs       
    // EFFECTs: adds a panel to show summary of all moves
    // to the information tabbed pane
    private void addMovesTab(Player player) {
        movesPanel = new JPanel();
        String summary = "";
        for (Move move : player.getHistory().getMoves()) {
            switch (move.getMoveType()) {
                case PLAY_WORD:
                    summary = game.getWordDescription(move, player);
                    break;
                case SWAP_TILES:
                    summary = game.getSwapDescription(move, player);
                    break;
                case SKIP:
                    summary = game.getSkipDescription(move, player);
                    break;
                case END_GAME_ADJUSTMENT:
                    summary = game.getEndGameDescription(move, player);
                    break;
            }
            JTextArea moveSummary = getFormattedTextArea(summary, 100);
            
            movesPanel.add(moveSummary);
            movesPanel.setMaximumSize(new Dimension(200,500));
            movesPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        }
        infoTabs.add(movesPanel, "My Move History");
    }

    // EFFECTS: returns JTextArea with given text and 
    // standardized formatting
    private JTextArea getFormattedTextArea(String text, int height) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(new Font("Arial", Font.ITALIC, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setCaretPosition(0);
        textArea.setEditable(false);
        textArea.setPreferredSize(new Dimension(150, height));
        return textArea;
    }

    // MODIFIES: infoTabs       
    // EFFECTs: adds a panel to show filtered word summary
    // to the information tabbed pane
    private void addWordsTab(Player player) {
        wordsPanel = new JPanel();
        searchWordsButton = new JButton("Search");
        wordFilterField = new JTextField(SEARCH_WORDS_DEFAULT_DISPLAY_TEXT, 2);
        addSearchWordsButtonListener(player);   
        wordFilterField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                wordFilterField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        wordsPanel.add(wordFilterField);
        wordsPanel.add(searchWordsButton);
        infoTabs.add(wordsPanel, "Word Filter");
    }

    // MODIFIES: searchWordsButton
    // EFFECTS: adds action listener to the button in Filtered Words tab
    private void addSearchWordsButtonListener(Player player) {
        searchWordsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetTab(wordsPanel);
                if (wordFilterField.getText().trim().isEmpty()) {
                    for (Move move : player.getMoves()) {
                        if (move.getMoveType() == MoveType.PLAY_WORD) {
                            wordsPanel.add(getFormattedTextArea(game.getWordDescription(move, player), 100));
                        }
                    }
                } else {
                    char letter = wordFilterField.getText().trim().toUpperCase().charAt(0);
                    List<Move> words = player.getHistory().getWordsContainingLetter(letter);
                    if (words.isEmpty()) {
                        wordsPanel.add(getFormattedTextArea("You haven't played a word with that letter", 100));
                    } else {
                        for (Move word : words) {
                            wordsPanel.add(getFormattedTextArea(game.getWordDescription(word, player), 100));
                        }
                    }
                    repaintAndRevalidate(wordsPanel);
                }
            }
        });
    }

    // MODIFIES: panel
    // EFFECTS: removes all JTextArea components
    // from panel
    private void resetTab(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JTextArea) {
                panel.remove(component);
            }
        }
        repaintAndRevalidate(panel);
    }

    // MODIFIES: infoTabs
    // EFFECTs: adds a panel to show remaining tile counts
    // to the information tabbed pane
    private void addLetterDistributionTab(Player player) {
        letterDistributionPanel = new JPanel();
        Map<Character, Integer> distributionMap = game.getNumEachCharInBagAndOpponents(player);
        searchLetterCountsButton = new JButton("Search");
        searchLetterCountsField = new JTextField(SEARCH_REMAINING_COUNTS_DEFAULT_DISPLAY_TEXT, 2);
        searchLetterCountsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetTab(letterDistributionPanel);
                if (searchLetterCountsField.getText().trim().isEmpty()) {
                    for (Map.Entry<Character, Integer> entry : distributionMap.entrySet()) {
                        letterDistributionPanel.add(getFormattedTextArea(entry.getKey() + " : " 
                                + entry.getValue(), 20));
                    }
                } else {
                    char key = searchLetterCountsField.getText().toUpperCase().trim().charAt(0);
                    letterDistributionPanel.add(getFormattedTextArea(key + " : " + distributionMap.get(key), 20));
                    repaintAndRevalidate(letterDistributionPanel);
                }
            }
        });
        addLetterDistributionFocusListener();
        letterDistributionPanel.add(searchLetterCountsField);
        letterDistributionPanel.add(searchLetterCountsButton);
        infoTabs.add(letterDistributionPanel, "Letter Distribution");
    }

    // MODIFIES: searchRemainingCountsTextField
    // EFFECTS: searchRemainingCountsTextField has been initialized
    private void addLetterDistributionFocusListener() {
        searchLetterCountsField.addFocusListener(new FocusListener() {
            @Override
            // MODIFIES: searchRemainingCountsTextField
            // EFFECTS: sets searchRemainingCountsTextField text to empty string
            public void focusGained(FocusEvent e) {
                searchLetterCountsField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
    }

    // MODIFIES: panel
    // EFFECTS: repaints and revalidates the panel
    private void repaintAndRevalidate(JComponent component) {
        component.repaint();
        component.revalidate();
    }

    // MODIFIES: this
    // EFFECTS: adds a new player to the game
    // who's name is the text currently in 
    // requestPlayerNameText
    private void addPlayer() {
        game.addPlayer(new Player(nameInput.getText()));
        numPlayers++;
    }

    // EFFECTS: Start the Graphical User Interface
    public static void main(String[] args) {
        new ScrabbleVisualApp();
    }
}
