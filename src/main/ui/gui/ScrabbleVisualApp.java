package ui.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import model.*;
import model.exceptions.BoardSectionUnavailableException;
import ui.ScrabbleConsoleApp;
import ui.ScrabbleUserInterface;

import persistance.JsonReader;
import persistance.JsonWriter;

// A Graphical user interface for Scrabble
public class ScrabbleVisualApp extends ScrabbleUserInterface implements GuiListener {
    
    private static final String JSON_STORE = "./data/savedgames/defaultSaveFile.json";
    private static final String REQUEST_PLAYER_NAME_TEXT = "Type player name then click add";
    private static final int FRAME_SIDE_LENGTH = 1000;
    
    private static final int REQUEST_NAMES_FRAME_WIDTH = 1000 * 2 / 3;
    private static final int REQUEST_NAMES_FRAME_HEIGHT = 100;
    //private static final int REMAINING_TILE_PRINTOUT_HEIGHT = 20;
    
    private JLabel coverPhoto;
    private JFrame loadOrPlayFrame;
    private JButton newGame;
    private JButton continueGame;
    private JButton loadSelected;

    private JFrame playerNameFrame;
    private JPanel addPlayerPanel;
    private JTextField nameInput;
    private JButton addPlayerButton;
    private JButton startButton;

    private BoardPanel boardPanel;
    private RackPanel rackPanel;
    private ScorePanel scorePanel;
    private InformationPanel infoPanel;

    private JFrame frame;
    private JFileChooser fileChooser;
    private JComboBox<String> saveGameDropdown;
    private String selectedFileName;
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
                loadOrPlayFrame.dispose();
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
        frame.add(rackPanel, BorderLayout.SOUTH);
        frame.add(scorePanel, BorderLayout.WEST);
        frame.add(infoPanel, BorderLayout.EAST); 

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
        handleGame();
    }

    private void initializePanels() {
        scorePanel = new ScorePanel(game);
        boardPanel = new BoardPanel(game);
        rackPanel = new RackPanel(game, this);
        infoPanel = new InformationPanel(game);
    }

    // MODIFIES: this
    // EFFECTS: Prompts player to enter players' names in the desired order of play.
    private void requestPlayerNames() {
        this.game = new ScrabbleGame();
        this.numPlayers = 0;
        playerNameFrame = new JFrame("Request Player Names");
        playerNameFrame.setSize(REQUEST_NAMES_FRAME_WIDTH, REQUEST_NAMES_FRAME_HEIGHT);
        
        nameInput = new JTextField(REQUEST_PLAYER_NAME_TEXT);
        addPlayerButton = new JButton("Add player with name in textbox");
        startButton = new JButton("Start Game");
        
        addRequestNamesListeners();

        addPlayerPanel = new JPanel();
        addPlayerPanel.add(nameInput);
        addPlayerPanel.add(addPlayerButton);
        addPlayerPanel.add(startButton);

        playerNameFrame.add(addPlayerPanel);
        playerNameFrame.setVisible(true);
    }

    // MODIFIES: add Player and confirm Players buttons, input player name textfield
    // EFFECTS: adds ActionListeners to addPlayerButton and confirmAllPlayersButton,
    //         and adds FocusListener to nameInput textfield
    private void addRequestNamesListeners() {
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
        game.drawTiles(game.getCurrentPlayer()); 
        scorePanel.updateScorePanel(game); 
        boardPanel.updateBoard(game);
        rackPanel.updateRackPanel(game, true);
        infoPanel.updateInfoPanel(game);

        frame.revalidate();
        frame.repaint();
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
        showWaitingScreen();
        handleGame();
    }

    @Override
    public void skipButtonActionListener() {
        game.logSkippedTurn();
        game.nextPlayer();
        showWaitingScreen();
        handleGame();
    }

    @Override
    public void previewButtonActionListener() {
        SwingUtilities.invokeLater(() -> {
            try {
                boardPanel.updateToPreviewBoard(game);
                SwingUtilities.invokeLater(() -> {
                    rackPanel.updateToPreviewPanel(game);
                });
            } catch (BoardSectionUnavailableException exception) {
                System.out.println("\n" + exception.getMessage());
                SwingUtilities.invokeLater(() -> {
                    game.getCurrentPlayer().clearSelectedTiles();
                    boardPanel.updateBoard(game);
                    rackPanel.updateRackPanel(game, true);
                });
            } finally {
                revalidateAndRepaint(rackPanel);
                revalidateAndRepaint(boardPanel);
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
                showWaitingScreen();
                handleGame();
            }
        } catch (BoardSectionUnavailableException e) {
            System.out.println("\n" + e.getMessage());
            SwingUtilities.invokeLater(() -> {
                player.clearSelectedTiles();
                rackPanel.updateRackPanel(game, true);
                boardPanel.updateBoard(game);
                revalidateAndRepaint(rackPanel);
                revalidateAndRepaint(boardPanel);
            });
        }
    }

    // MODIFIES: panel
    // EFFECTS: repaints and revalidates the panel
    private void revalidateAndRepaint(JComponent component) {
        component.revalidate();
        component.repaint();
    }

    private void showWaitingScreen() {
        String waitingMessage = game.getCensoredLastMoveDescription();
        BlurOverlayUtil.showBlurOverlay(frame, waitingMessage);
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

    