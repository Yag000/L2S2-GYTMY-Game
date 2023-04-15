package com.gytmy.labyrinth.controller;

import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFrame;

import com.gytmy.labyrinth.model.Direction;
import com.gytmy.labyrinth.model.GameData;
import com.gytmy.labyrinth.model.LabyrinthModel;
import com.gytmy.labyrinth.model.LabyrinthModelFactory;
import com.gytmy.labyrinth.model.player.Player;
import com.gytmy.labyrinth.model.score.ScoreCalculator;
import com.gytmy.labyrinth.model.score.ScoreType;
import com.gytmy.labyrinth.view.game.LabyrinthView;
import com.gytmy.labyrinth.view.game.LabyrinthViewFactory;
import com.gytmy.sound.AudioRecorder;
import com.gytmy.sound.RecordObserver;
import com.gytmy.utils.Coordinates;
import com.gytmy.utils.HotkeyAdder;

public class LabyrinthControllerImplementation implements LabyrinthController, RecordObserver {

    private GameData gameData;
    private LabyrinthModel model;
    private LabyrinthView view;
    private JFrame frame;
    private boolean hasCountdownEnded = false;
    private static String AUDIO_GAME_PATH = "src/resources/audioFiles/client/audio/currentGameAudio.wav";

    private MovementControllerType selectedMovementControllerType = MovementControllerType.KEYBOARD;

    public enum MovementControllerType {
        KEYBOARD
    }

    public LabyrinthControllerImplementation(GameData gameData, JFrame frame) {
        this.gameData = gameData;
        this.frame = frame;
        initGame();
        initializeMovementController();
        initializeVoiceRecorder();
    }

    private void initGame() {
        initScoreType();
        model = LabyrinthModelFactory.createLabyrinth(gameData);
        initPlayersInitialCell(model.getPlayers());
        view = LabyrinthViewFactory.createLabyrinthView(gameData, model, frame, this);
    }

    private void initScoreType() {
        switch (selectedMovementControllerType) {
            case KEYBOARD:
                gameData.setScoreType(ScoreType.SIMPLE_KEYBOARD);
                break;
            default:
                break;
        }
    }

    private void initPlayersInitialCell(Player[] players) {
        Coordinates initialCell = model.getInitialCell();
        Player.initAllPlayersCoordinates(initialCell, players);
    }

    private void initializeMovementController() {
        // Switch statement used in place of an if-then-else statement because it is
        // more readable and allows for more than two conditions (future implementations
        // of different controllers)
        switch (selectedMovementControllerType) {
            case KEYBOARD:
                initializeKeyboardMovementController();
                break;
            default:
                break;
        }
    }

    private void initializeKeyboardMovementController() {
        MovementController movementController = new KeyboardMovementController(this);
        movementController.setup();
    }

    private void initializeVoiceRecorder() {

        AudioRecorder recorder = AudioRecorder.getInstance();
        AudioRecorder.addObserver(this);
        HotkeyAdder.addHotkey(view, KeyEvent.VK_SPACE, () -> {

            if (AudioRecorder.isRecording()) {
                recorder.finish();
                return;
            }

            new Thread(() -> {
                recorder.start(AUDIO_GAME_PATH);
            }).start();

        }, "Record Audio In Game");
    }

    private void compareAudioWithModel() {
        // TODO : @selvakum - @gdudilli - compare with model
        new File(AUDIO_GAME_PATH).delete();
    }

    @Override
    public LabyrinthView getView() {
        return view;
    }

    @Override
    public Player[] getPlayers() {
        return model.getPlayers();
    }

    @Override
    public void movePlayer(Player player, Direction direction) {
        if (!isMovementAllowed() || !canPlayerMove(player)) {
            return;
        }
        if (model.movePlayer(player, direction)) {
            view.update(player, direction);
        }

        handlePlayersAtExit(player);
    }

    private boolean canPlayerMove(Player player) {
        return !model.isPlayerAtExit(player);
    }

    private boolean isMovementAllowed() {
        if (model.isGameOver()) {
            view.stopTimer();
            return false;
        }
        return hasCountdownEnded;
    }

    /**
     * Takes care of the players that have reached the exit cell. If the player has
     * reached the exit cell, the player's time is saved. If all players have
     * reached the exit cell, the game is over.
     * 
     * @param player
     */
    private void handlePlayersAtExit(Player player) {
        if (!canPlayerMove(player)) {
            player.setTimePassedInSeconds(view.getTimerCounterInSeconds());
        }

        if (model.isGameOver()) {
            view.stopTimer();
            view.notifyGameOver();
        }
    }

    @Override
    public void addKeyController(KeyboardMovementController controller) {
        view.addKeyController(controller);
    }

    @Override
    public ScoreCalculator getScoreCalculator(ScoreType scoreType, Player player) {
        return model.getScoreCalculator(scoreType, player);
    }

    @Override
    public void notifyGameStarted() {
        hasCountdownEnded = true;
        view.notifyGameStarted();
    }

    @Override
    public void update() {
        compareAudioWithModel();
    }
}
