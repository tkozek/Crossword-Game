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
import model.exceptions.BoardSectionUnavailableException;
import model.exceptions.InvalidLetterException;
import model.move.*;
import ui.ScrabbleConsoleApp;
import ui.ScrabbleUserInterface;

import persistance.JsonReader;
import persistance.JsonWriter;

// A Graphical user interface for Scrabble
public class ScrabbleVisualApp extends ScrabbleUserInterface implements GUIListener {
    
    //private static final String JSON_STORE = "./data/savedgames/gameToPlayTest.json";
    private static final String JSON_STORE = "./data/savedgames/defaultSaveFile.json";

    private static final String REQUEST_PLAYER_NAME_TEXT = "Type player name then click add";
    private static final int FRAME_SIDE_LENGTH = 1000;
    
    private static final int REQUEST_NAMES_FRAME_WIDTH = 1000 * 2 / 3;
    private static final int REQUEST_NAMES_FRAME_HEIGHT = 100;
    private static final int INFO_TABS_WIDTH = 175;
    private static final int MOVE_SUMMARY_PADDING = 25;
    private static final Font MOVE_FONT = new Font("Arial", Font.ITALIC, 12);
    //private static final int REMAINING_TILE_PRINTOUT_HEIGHT = 20;
    
    private static final String SEARCH_WORDS_DEFAULT_DISPLAY_TEXT = "";//"Enter a letter, then press 'search' " 
               // + " to \ndisplay all words with that letter";
    private static final String SEARCH_REMAINING_COUNTS_DEFAULT_DISPLAY_TEXT = "";
    //"Enter a letter to get its remaining "+ "count in draw pile and opponent racks\n or blank to see all";

    private BoardPanel boardPanel;
    private RackPanel rackPanel;
    private ScorePanel scorePanel;

    private JPanel infoPanel;
    private JPanel movesPanel;
    private JPanel wordsPanel;
    private JButton searchWordsButton;
    private JTextField wordFilterField;
    private JScrollPane wordsScrollPane;
    private JPanel wordTab;
    private JPanel letterDistributionPanel;
    private JButton searchLetterCountsButton;
    private JTextField searchLetterCountsField;
    private JTabbedPane infoTabs;

    private JFrame frame;
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
    private JButton directionToggle;

    private String lastGamePath;

    // EFFECTS: Loads start menu frame to user screen.
    public ScrabbleVisualApp() {
        super();
        initializeStartMenu();
    }

    public ScrabbleVisualApp(ScrabbleGame game) {
        super();
        this.game = game;
        this.numPlayers = game.getNumPlayers();
        this.gameRunning = true;
        initializeNewGame();
    }

    // MODIFIES: this
    // EFFECTS: Creates start menu frame for player to select
    // whether they would like to play a new game or load a saved game.
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

    //REQUIRES: NewGame and LoadGame buttons have been initialized
    //MODIFIES: NewGame and LoadGame buttons
    //EFFECTS: adds action listeners for NewGame and LoadGame buttons
    private void addStartMenuListeners() {
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadOrPlayFrame.dispose();
                requestPlayerNames();
            }
        });
        continueGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadOrPlayFrame.dispose();
                loadLastSavedGame(); 
            }
        });
        loadSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadSelectedGame();
            }
        });
    }

    // MODIFIES: this, scrabbleGame
    // EFFECTS: loads assets from previously saved game
    private void loadLastSavedGame() {
        this.gameRunning = true;
        try {
            lastGamePath = SavedGameManager.getLastGamePath();
            JsonReader jsonReader = new JsonReader(lastGamePath);
            game = jsonReader.read();
            this.numPlayers = game.getNumPlayers();
            initializeNewGame();
        } catch (IOException e) {
            System.out.println("Unable to read game from file: " + lastGamePath);
        }
    }

    // MODIFIES: this, scrabbleGame
    // EFFECTS: loads assets from previously saved game
    private void loadSelectedGame() {
        this.gameRunning = true;
        try {
            selectedFileName = SavedGameManager.getSavedGamesDirectory() + (String) saveGameDropdown.getSelectedItem();
            JsonReader jsonReader = new JsonReader(selectedFileName);
            game = jsonReader.read();
            this.numPlayers = game.getNumPlayers();
            initializeNewGame();
        } catch (IOException e) {
            System.out.println("Unable to read game from file: " + selectedFileName);
        }
    }

    // MODIFIES: scrabbleGame
    // EFFECTS: creates assets for a new game and prompts user input for setup parameters
    private void initializeNewGame() {
        frame = new JFrame("Scrabble Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        initializePanels();

        Double scorePanelWidth = scorePanel.getPreferredSize().getWidth();
        frame.setSize(FRAME_SIDE_LENGTH - 15 + scorePanelWidth.intValue(), FRAME_SIDE_LENGTH);

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(rackPanel, BorderLayout.SOUTH); //curPlayer
        frame.add(scorePanel, BorderLayout.WEST);
        frame.add(getInfoPanel(game.getCurrentPlayer()), BorderLayout.EAST); //curPlayer
        ActionPanel actionPanel = rackPanel.getActionPanel();
        revalidateAndRepaint(actionPanel);
        revalidateAndRepaint(rackPanel);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
        handleGame();
    }

    private void initializePanels() {
        scorePanel = new ScorePanel(game);
        boardPanel = new BoardPanel(game);
        rackPanel = new RackPanel(game, this);
    }

    // MODIFIES: this
    // EFFECTS: Prompts player to enter players' names in the desired order of play.
    private void requestPlayerNames() {
        this.game = new ScrabbleGame();
        this.numPlayers = 0;
        playerNameFrame = new JFrame("Request Player Names");
        playerNameFrame.setSize(REQUEST_NAMES_FRAME_WIDTH, REQUEST_NAMES_FRAME_HEIGHT);
        
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

    // MODIFIES: add Player and confirm Players buttons
    // EFFECTS: adds ActionListeners to addPlayerButton and confirmAllPlayersButton
    private void addNameActionListeners() {
        addPlayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPlayer();
                nameInput.setText(REQUEST_PLAYER_NAME_TEXT);
            }
        });        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playerNameFrame.dispose();
                gameRunning = true;
                initializeNewGame();
            }
        });
    }

    // MODIFIES: input player name textfield
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
    // EFFECTS: Manages order of turn taking, and ensures player's draw new tiles
    // when they are supposed to
    private void handleGame() {   
        Player playerToPlayNext = game.getCurrentPlayer(); //curPlayer
        game.drawTiles(playerToPlayNext); //curPlayer
        scorePanel.updateScorePanel(game); 
        boardPanel.updateBoard(game);
        rackPanel.updateRackPanel(game, true);
        frame.remove(infoPanel);
        frame.add(getInfoPanel(playerToPlayNext), BorderLayout.EAST); //curPlayer

        //
        
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void openSaveMenuActionListener() {
        SwingUtilities.invokeLater(() -> {
            rackPanel.updateToSaveAndQuitRackPanel(game);
            revalidateAndRepaint(rackPanel);
        });
    }

    @Override
    public void saveAndQuitActionListener() {
        SwingUtilities.invokeLater(() -> {
            handleSave();
            printEventLog();
            System.exit(0);
        });
    }



    @Override
    public void saveAsAndQuitActionListener() {
        SwingUtilities.invokeLater(() -> {
            fileChooser = new JFileChooser(SavedGameManager.getSavedGamesDirectory());
            fileChooser.setDialogTitle(("Save game as"));
            int result = fileChooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().endsWith(".json")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".json");
                }
                handleSave(selectedFile.getAbsolutePath());
                System.out.println("Saving Game as : " + selectedFile.getName());
                printEventLog();
                System.exit(0);
            }
        });
    }

    @Override
    public void quitWithoutSavingActionListener() {
        printEventLog();
        System.exit(0);
    }

    @Override
    public void cancelButtonActionListener() {
        SwingUtilities.invokeLater(() -> {
            game.getCurrentPlayer().clearSelectedTiles();
            rackPanel.updateRackPanel(game, true);
            boardPanel.updateBoard(game);
            revalidateAndRepaint(rackPanel);
            revalidateAndRepaint(boardPanel);
        });
    }

    @Override
    public void confirmedWordPlacementActionListener() {
        confirmWordPlacement(game.getCurrentPlayer());
    }

    @Override
    public void swapButtonActionListener() {
        game.swapTiles();
        game.nextPlayer();
        handleGame();
    }

    @Override
    public void skipButtonActionListener() {
        game.logSkippedTurn();
        game.nextPlayer();
        handleGame();
    }

    @Override
    public void previewButtonActionListener() {
        SwingUtilities.invokeLater(() -> {
            try {
                revalidateAndRepaint(boardPanel);
                boardPanel.updateToPreviewBoard(game);
                SwingUtilities.invokeLater(() -> {
                    rackPanel.updateToPreviewPanel(game);
                    revalidateAndRepaint(rackPanel);
                    revalidateAndRepaint(boardPanel);
                });
            } catch (BoardSectionUnavailableException exception) {
                System.out.println("\n" + exception.getMessage());
                SwingUtilities.invokeLater(() -> {
                    game.getCurrentPlayer().clearSelectedTiles();
                    boardPanel.updateBoard(game);
                    rackPanel.updateRackPanel(game, true);
                    revalidateAndRepaint(rackPanel);
                    revalidateAndRepaint(boardPanel);
                });
            }
        });   
    }

    @Override
    public void terminalUIButtonActionListener() {
        frame.dispose();
        new ScrabbleConsoleApp(game);
        gameRunning = false;
    }

    @Override
    public void directionToggleActionListener() {
        Direction oldDirection = game.getDirection();
        Direction newDirection = (oldDirection == Direction.DOWN) ? Direction.RIGHT : Direction.DOWN;
        game.setDirection(newDirection);
        String newText = (newDirection == Direction.DOWN) ? "Down" : "Right";
        rackPanel.updateDirection(newText);
    }

    @Override
    public void clearSelectionsActionListener() {
        game.getCurrentPlayer().clearSelectedTiles();
        SwingUtilities.invokeLater(() -> {
            rackPanel.updateRackPanel(game, true);
            revalidateAndRepaint(rackPanel);
        });                
    }

    private void confirmWordPlacement(Player player) {
        try {
            game.playWord(player);
            if (player.outOfTiles()) {
                handleEndGame(player);
            } else {
                game.nextPlayer();
                handleGame();
            }
        } catch (BoardSectionUnavailableException e) {
            // System.out.println("\n" + e.getMessage());
            // handleGame(game.getPlayerIndex(player));
            System.out.println("\n" + e.getMessage());
            SwingUtilities.invokeLater(() -> {
                player.clearSelectedTiles();
                rackPanel.updateRackPanel(game, false);
                revalidateAndRepaint(rackPanel);
                boardPanel.updateBoard(game);
                revalidateAndRepaint(boardPanel);
            });
        }
    }

    // MODIFIES: scrabbleGame
    // EFFECTS: Saves game to file
    private void handleSave() {
        try {
            JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
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
    private void handleSave(String filePath) {
        try {
            JsonWriter jsonWriter = new JsonWriter(filePath);
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();
            // !!! ToDo Add "Okay" button to click before closing
        } catch (IOException e) {
            System.out.println("Unable to write to file " + filePath);
        }
    }
    
    // MODIFIES: this
    // EFFECTS: adds tabbed information panel to frame
    private JPanel getInfoPanel(Player player) {
        infoPanel = new JPanel();
        infoPanel.setLayout(new CardLayout());
        infoPanel.setPreferredSize(new Dimension(INFO_TABS_WIDTH, FRAME_SIDE_LENGTH));
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

    // MODIFIES: Tabbed Information Pane      
    // EFFECTs: adds a panel to show summary of all moves to the information tabbed pane
    private void addMovesTab(Player player) {
        movesPanel = new JPanel();
        movesPanel.setLayout(new BoxLayout(movesPanel, BoxLayout.Y_AXIS));
        String summary = "";
        for (Move move : player) {
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
            JTextArea moveSummary = getFormattedTextArea(summary, MOVE_FONT);
            movesPanel.add(moveSummary);
            movesPanel.setMaximumSize(new Dimension(200,500));
           // movesPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        }
        JScrollPane scrollPane = new JScrollPane(movesPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        infoTabs.add(scrollPane, "My Move History");
    }

    // EFFECTS: returns JTextArea with given text and standardized formatting
    private JTextArea getFormattedTextArea(String text, Font font) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(MOVE_FONT);
        int height = getTextboxHeight(textArea, text, INFO_TABS_WIDTH - MOVE_SUMMARY_PADDING, font);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        //textArea.setCaretPosition(0);
        textArea.setEditable(false);
        textArea.setPreferredSize(new Dimension(INFO_TABS_WIDTH - MOVE_SUMMARY_PADDING, height + 10));
        textArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, height + 10));
        //textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        return textArea;
    }

    // EFFECTS: returns textbox height required based on length of input text and font used
    private int getTextboxHeight(JComponent component, String text, int containerWidth, Font font) {
        FontMetrics metric = component.getFontMetrics(font);
        int width = metric.charWidth('A');
        int h = metric.getHeight();
        double textBoxHeight = (text.length() * width * h / containerWidth);
        return Math.max(h - 5, (int) textBoxHeight);
    }

    // MODIFIES: Tabbed Information Pane      
    // EFFECTs: adds a panel to show filtered word summary to the information tabbed pane
    private void addWordsTab(Player player) {
        wordTab = new JPanel(new BorderLayout());
        JPanel wordPanelButtons = new JPanel();

        wordsPanel = new JPanel();

        wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.Y_AXIS));
        wordsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, wordsPanel.getPreferredSize().height));
        wordsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

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
        wordPanelButtons.add(wordFilterField);
        wordPanelButtons.add(searchWordsButton);

        wordsScrollPane = new JScrollPane(wordsPanel);
        wordsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        wordsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        wordTab.add(wordPanelButtons, BorderLayout.NORTH);
        wordTab.add(wordsScrollPane, BorderLayout.CENTER);

        infoTabs.add(wordTab, "Word Filter");
    }

    // MODIFIES: search filtered words button
    // EFFECTS: adds action listener to the button in Filtered Words tab
    private void addSearchWordsButtonListener(Player player) {
        searchWordsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetTab(wordsPanel);
                if (wordFilterField.getText().trim().isEmpty()) {
                    for (Move move : player) {
                        if (move.getMoveType() == MoveType.PLAY_WORD) {
                            wordsPanel.add(getFormattedTextArea(game.getWordDescription(move, player), MOVE_FONT));
                        }
                    }
                } else {
                    char letter = wordFilterField.getText().trim().toUpperCase().charAt(0);
                    List<Move> words;
                    try {
                        words = player.getWordsContainingLetter(letter);
                        if (words.isEmpty()) {
                            wordsPanel.add(getFormattedTextArea("You haven't played a word with that letter", MOVE_FONT));
                        } else {
                            for (Move word : words) {
                                wordsPanel.add(getFormattedTextArea(game.getWordDescription(word, player), MOVE_FONT));
                            }
                        }
                    } catch (InvalidLetterException exception) {
                        wordsPanel.add(getFormattedTextArea(exception.getMessage(), MOVE_FONT));
                    }
                    revalidateAndRepaint(wordsPanel);
                    SwingUtilities.invokeLater(() -> {
                        SwingUtilities.invokeLater(() -> {
                            JScrollBar verticalBar = wordsScrollPane.getVerticalScrollBar();
                            verticalBar.setValue(verticalBar.getMaximum());
                        });
                    });
                }
            }
        });
    }

    // MODIFIES: panel
    // EFFECTS: removes all JTextArea components from panel
    private void resetTab(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JTextArea) {
                panel.remove(component);
            }
        }
        revalidateAndRepaint(panel);
    }

    // MODIFIES: Tabbed Information Pane      
    // EFFECTs: adds a panel to show remaining tile counts to the information tabbed pane
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
                                + entry.getValue(), MOVE_FONT));
                    }
                } else {
                    char key = searchLetterCountsField.getText().toUpperCase().trim().charAt(0);
                    String count = String.valueOf(distributionMap.get(key));
                    if (count == "null") {
                        letterDistributionPanel.add(getFormattedTextArea("Invalid letter: " + key, MOVE_FONT));
                    } else {
                        letterDistributionPanel.add(getFormattedTextArea(key + " : " + count, MOVE_FONT));
                    }
                    revalidateAndRepaint(letterDistributionPanel);
                }
            }
        });
        addLetterDistributionFocusListener();
        letterDistributionPanel.add(searchLetterCountsField);
        letterDistributionPanel.add(searchLetterCountsButton);
        infoTabs.add(letterDistributionPanel, "Letter Distribution");
    }

    // MODIFIES: remaining tile counts textfield
    // EFFECTS: searchRemainingCountsTextField has been initialized
    private void addLetterDistributionFocusListener() {
        searchLetterCountsField.addFocusListener(new FocusListener() {
            @Override
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
    private void revalidateAndRepaint(JComponent component) {
        component.revalidate();
        component.repaint();
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