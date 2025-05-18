package ui.gui;

public interface GuiListener {

    void openSaveMenuActionListener();

    void saveAndQuitActionListener();

    void saveAsAndQuitActionListener();

    void quitWithoutSavingActionListener();

    void cancelButtonActionListener();

    void confirmedWordPlacementActionListener();

    void swapButtonActionListener();
    
    void skipButtonActionListener();

    void previewButtonActionListener();
    
    void terminalUIButtonActionListener();

    void directionToggleActionListener();

    void clearSelectionsActionListener();
}
