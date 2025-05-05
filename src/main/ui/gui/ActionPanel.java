package ui.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.Direction;
import model.ScrabbleGame;

public class ActionPanel extends JPanel {


    private JPanel moveOptionButtons;
    private JPanel otherOptionButtons;
    private JButton previewButton;
    private JButton playButton;
    private JButton swapButton;
    private JButton skipButton;
    private JButton terminalUIButton;
    private JButton directionToggle;
    private JButton clearSelections;
    private JButton saveAndQuit;

    private JButton confirm;
    private JButton cancel;

    private JPanel saveSaveAsPanel;
    private JPanel quitOrCancelPanel;

    private JButton saveAsAndQuit;
    private JButton quitWithoutSaving; 


    private GuiListener listener;

    public ActionPanel(ScrabbleGame game, GuiListener listener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        updateActionPanel(game);
        this.listener = listener;
    }

    public void updateActionPanel(ScrabbleGame game) {
        this.removeAll();
        addActionButtons(game);
    }

    public void updateActionPanelToSaveAndQuit() {
        this.removeAll();
        addSaveAndQuitActionButtons();
    }

    public void updateToPreviewPanel() {
        this.removeAll();
        addPreviewOptionsButtons();
    }

     // MODIFIES: this
    // EFFECTS: loads panel with available action buttons
    private void addActionButtons(ScrabbleGame game) {
        moveOptionButtons = new JPanel();
        moveOptionButtons.setLayout(new BoxLayout(moveOptionButtons, BoxLayout.X_AXIS));
        moveOptionButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        otherOptionButtons = new JPanel();
        otherOptionButtons.setLayout(new BoxLayout(otherOptionButtons, BoxLayout.X_AXIS));
        otherOptionButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        initializeOptionButtons(game.getDirection());
        addOptionButtonsToContainers();

        addMoveOptionListeners();
        addOtherOptionsListeners();
    }

    private void initializeOptionButtons(Direction dir) {
        previewButton = new JButton("Preview");
        playButton = new JButton("Play");
        swapButton = new JButton("Swap");
        skipButton = new JButton("Skip");
        terminalUIButton = new JButton("Terminal UI");
        String directionString = (dir == Direction.DOWN) ? "Down" : "Right";
        directionToggle = new JButton(directionString);
        clearSelections = new JButton("Clear");
        saveAndQuit = new JButton("Save and Quit");
    }

    // MODIFIES: this
    // EFFECTS: puts available action buttons into appropriate panels
    private void addOptionButtonsToContainers() {
        moveOptionButtons.add(playButton);
        moveOptionButtons.add(swapButton);
        moveOptionButtons.add(skipButton);
        moveOptionButtons.add(previewButton);
        
        otherOptionButtons.add(directionToggle);
        otherOptionButtons.add(clearSelections);
        otherOptionButtons.add(terminalUIButton);
        otherOptionButtons.add(saveAndQuit);
    
        this.add(moveOptionButtons);
        this.add(otherOptionButtons);
    }

    private void addMoveOptionListeners() {
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listener.confirmedWordPlacementActionListener();
            }
        });
        swapButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listener.swapButtonActionListener();
            }
        });
        skipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listener.skipButtonActionListener();
            }
        });
        previewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> { 
                    listener.previewButtonActionListener();
                });   
            }
        });
    }

    private void addOtherOptionsListeners() {
        terminalUIButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listener.terminalUIButtonActionListener();
            }
        });
        directionToggle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listener.directionToggleActionListener();
            }
        });
        clearSelections.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listener.clearSelectionsActionListener();              
            }
        });

        saveAndQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    listener.openSaveMenuActionListener(); 
                });
            }
        });
    }


    private void addPreviewOptionsButtons() {
        confirm = new JButton("Confirm");
        cancel = new JButton("Cancel");
        this.add(confirm);
        this.add(cancel);

        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listener.confirmedWordPlacementActionListener();
                // confirmWordPlacement(player);
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    listener.cancelButtonActionListener();
                });
            }
        });
    }

    private void addSaveAndQuitActionButtons() {
        saveSaveAsPanel = new JPanel(new BoxLayout(saveSaveAsPanel, BoxLayout.X_AXIS));
        saveSaveAsPanel.setLayout(new BoxLayout(saveSaveAsPanel, BoxLayout.X_AXIS));
        saveSaveAsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        quitOrCancelPanel = new JPanel();
        quitOrCancelPanel.setLayout(new BoxLayout(quitOrCancelPanel, BoxLayout.X_AXIS));
        quitOrCancelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        initializeSaveMenuButtons();
        addSaveMenuButtonsToContainers();
        addSaveMenuButtonListeners();
    }

    private void initializeSaveMenuButtons() {
        saveAndQuit = new JButton("Save & Quit");
        saveAsAndQuit = new JButton("Save as & Quit");
        quitWithoutSaving = new JButton("Quit without Saving");
        cancel = new JButton("Cancel");
    }

    private void addSaveMenuButtonsToContainers() {
        saveSaveAsPanel.add(saveAndQuit);
        saveSaveAsPanel.add(saveAsAndQuit);
        quitOrCancelPanel.add(quitWithoutSaving);
        quitOrCancelPanel.add(cancel);
        this.add(saveSaveAsPanel);
        this.add(quitOrCancelPanel);
    }

    private void addSaveMenuButtonListeners() {
        saveAndQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listener.saveAndQuitActionListener();
            }
        });
        saveAsAndQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    listener.saveAsAndQuitActionListener();
                });
            }
        });
        quitWithoutSaving.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listener.quitWithoutSavingActionListener();
            }
        });
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    listener.cancelButtonActionListener();
                });
            }
        });
    }

    public void updateDirectionToggle(String newDirection) {
        directionToggle.setText(newDirection);
    }
}

