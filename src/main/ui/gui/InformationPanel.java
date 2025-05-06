package ui.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Player;
import model.ScrabbleGame;
import model.exceptions.InvalidLetterException;
import model.move.Move;
import model.move.MoveType;

public class InformationPanel extends JPanel {

    private static final int FRAME_SIDE_LENGTH = 1000;
    private static final int INFO_TABS_WIDTH = 175;
    private static final String SEARCH_WORDS_DEFAULT_DISPLAY_TEXT = "";//"Enter a letter, then press 'search' " 
                   // + " to \ndisplay all words with that letter";
    private static final String SEARCH_REMAINING_COUNTS_DEFAULT_DISPLAY_TEXT = "";
    //"Enter a letter to get its remaining "+ "count in draw pile and opponent racks\n or blank to see all";
    private static final Font MOVE_FONT = new Font("Arial", Font.ITALIC, 12);
    private static final int MOVE_SUMMARY_PADDING = 25;
    
    private JTabbedPane infoTabs;
    private JPanel movesPanel;
    private JPanel wordTab;
    private JPanel wordPanelButtons;
    private JPanel wordsPanel;
    private JButton searchWordsButton;
    private JTextField wordFilterField;


    private JPanel letterDistributionPanel;
    private JButton searchLetterCountsButton;
    private JTextField searchLetterCountsField;

    private JScrollPane wordsScrollPane;
    private JScrollPane letterScrollPane;
    private JPanel letterSearchPanel;
    private JPanel letterTab;

    public InformationPanel(ScrabbleGame game) {
        setLayout(new CardLayout());
        setPreferredSize(new Dimension(INFO_TABS_WIDTH, FRAME_SIDE_LENGTH));
        infoTabs = new JTabbedPane();
    }

    public void updateInfoPanel(ScrabbleGame game) {
        infoTabs.removeAll();
        this.removeAll();
        addMovesTab(game);
        addWordsTab(game);
        addLetterDistributionTab(game);
        add(infoTabs);
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
    }

    private void addMovesTab(ScrabbleGame game) {
        movesPanel = new JPanel();
        movesPanel.setLayout(new BoxLayout(movesPanel, BoxLayout.Y_AXIS));
        String summary = "";
        Player player = game.getCurrentPlayer();
        for (Move move : player) {
            summary = game.getMoveDescription(move);
            JTextArea moveSummary = getFormattedTextArea(summary, MOVE_FONT);
            movesPanel.add(moveSummary);
            movesPanel.setMaximumSize(new Dimension(200,500));
           // movesPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        }
        JScrollPane movesScrollPane = new JScrollPane(movesPanel);
        movesScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        movesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        movesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        infoTabs.add(movesScrollPane, "My Move History");
    }

    private void addWordsTab(ScrabbleGame game) {
        wordFilterField = new JTextField(SEARCH_WORDS_DEFAULT_DISPLAY_TEXT, 2);
        searchWordsButton = new JButton("Search");
       
        wordPanelButtons = new JPanel();
        wordPanelButtons.add(wordFilterField);
        wordPanelButtons.add(searchWordsButton);

        wordsPanel = new JPanel();
        wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.Y_AXIS));
        wordsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, wordsPanel.getPreferredSize().height));
        wordsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        wordsScrollPane = new JScrollPane(wordsPanel);

        wordsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        wordsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        wordsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        wordTab = new JPanel(new BorderLayout());
        wordTab.add(wordPanelButtons, BorderLayout.NORTH);
        wordTab.add(wordsScrollPane, BorderLayout.CENTER);

        infoTabs.add(wordTab, "Word Filter");

        addFilteredWordsListeners(game);
    }

    private void addFilteredWordsListeners(ScrabbleGame game) {
        Player player = game.getCurrentPlayer();
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
        wordFilterField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                wordFilterField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
    }

    private void addLetterDistributionTab(ScrabbleGame game) {
        searchLetterCountsButton = new JButton("Search");
        searchLetterCountsField = new JTextField(SEARCH_REMAINING_COUNTS_DEFAULT_DISPLAY_TEXT, 2);
        letterSearchPanel = new JPanel();
        letterSearchPanel.add(searchLetterCountsField);
        letterSearchPanel.add(searchLetterCountsButton);

        letterDistributionPanel = new JPanel();
        letterDistributionPanel.setLayout(new BoxLayout(letterDistributionPanel, BoxLayout.Y_AXIS));

        letterScrollPane = new JScrollPane(letterDistributionPanel);
        letterScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        letterScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        letterTab = new JPanel(new BorderLayout());
        letterTab.add(letterSearchPanel, BorderLayout.NORTH);
        letterTab.add(letterScrollPane, BorderLayout.CENTER);
        addLetterDistributionListeners(game);
        infoTabs.add(letterTab, "Letter Distribution");
    }

    private void addLetterDistributionListeners(ScrabbleGame game) {
        Map<Character, Integer> distributionMap = game.getNumEachCharInBagAndOpponents(game.getCurrentPlayer());
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

    // EFFECTS: returns JTextArea with given text and standardized formatting
    private JTextArea getFormattedTextArea(String text, Font font) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(MOVE_FONT);
        int height = getTextboxHeight(textArea, text, INFO_TABS_WIDTH - MOVE_SUMMARY_PADDING, font);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        //textArea.setCaretPosition(0);
        textArea.setEditable(false);
        textArea.setFocusable(false); 
        
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

    // MODIFIES: panel
    // EFFECTS: repaints and revalidates the panel
    private void revalidateAndRepaint(JComponent component) {
        component.revalidate();
        component.repaint();
    }    
}
