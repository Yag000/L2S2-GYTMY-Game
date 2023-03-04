package com.gytmy.labyrinth.view.settings.game_mode;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.gytmy.labyrinth.view.Cell;

public class GameModeSelectionPanel extends JPanel {

    public static final Color BACKGROUND_COLOR = Cell.WALL_COLOR;
    public static final Color FOREGROUND_COLOR = Cell.PATH_COLOR;

    private GameModeSelector gameModeSelector;
    protected JPanel gameModeSettingsPanel;

    public GameModeSelectionPanel() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new GridBagLayout());

        initGameModeSelector();
        initGameModeSettingsPanel();
    }

    private void initGameModeSelector() {
        gameModeSelector = new GameModeSelector();
        gameModeSelector.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.weightx = 1;
        gbc.weighty = 0.3;
        add(gameModeSelector, gbc);
    }

    private void initGameModeSettingsPanel() {
        gameModeSettingsPanel = new JPanel();
        gameModeSettingsPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0.7;
        add(gameModeSettingsPanel, gbc);

        firstGameModeSettingsPanelUpdate();
    }

    /**
     * We need to call this method only once, because we want to update the panel
     * with the first game mode. The issue is that the panel should be updated
     * only after the panel is added to the frame. If we call this method in the
     * constructor, the panel will be updated before it is added to the frame.
     * That's why we need to call this method only once, after the panel is added
     * to the frame.
     */
    private void firstGameModeSettingsPanelUpdate() {
        gameModeSelector.updateGameModeSettingsPanel((GameMode) gameModeSelector.getSelectedItem());
    }

    public GameModeData getGameModeData() {
        GameModeSettingsPanelHandler handler = GameModeSettingsPanelHandlerFactory
                .getGameModeSettingsPanel((GameMode) gameModeSelector.getSelectedItem());
        return handler.getSettingsData();
    }

    public GameMode getSelectedGameMode() {
        return (GameMode) gameModeSelector.getSelectedItem();
    }

    /**
     * This inner class represents a combo box with all available game modes. When
     * the user selects a game mode, the panel with the settings for this game mode
     * is updated. For this we use a factory that returns the appropriate handler
     * for the selected game mode. The handler is responsible for updating the panel
     * with the settings for the selected game mode. And it is also responsible for
     * cleaning the panel when the user selects another game mode.
     */
    private class GameModeSelector extends JComboBox<GameMode> {
        // We need to remember the last selected game mode, because we need to
        // clean the panel with the settings for this game mode.
        private GameMode lastSelectedGameMode;

        public GameModeSelector() {
            Font font = new Font("Arial", Font.BOLD, 15);
            setFont(font);

            for (GameMode gameMode : GameMode.values()) {
                addItem(gameMode);
            }

            addActionListener(e -> {
                GameMode gameMode = (GameMode) getSelectedItem();
                updateGameModeSettingsPanel(gameMode);
            });

            setBackground(BACKGROUND_COLOR);
            setForeground(FOREGROUND_COLOR);
        }

        private void updateGameModeSettingsPanel(GameMode gameMode) {
            cleanOldPanel();

            lastSelectedGameMode = (GameMode) getSelectedItem();
            GameModeSettingsPanelHandler handler = GameModeSettingsPanelHandlerFactory
                    .getGameModeSettingsPanel(gameMode);
            handler.initPanel(gameModeSettingsPanel);
        }

        private void cleanOldPanel() {
            if (lastSelectedGameMode == null) {
                return;
            }

            GameModeSettingsPanelHandler handler = GameModeSettingsPanelHandlerFactory
                    .getGameModeSettingsPanel(lastSelectedGameMode);
            handler.cleanPanel(gameModeSettingsPanel);
        }

    }

}
