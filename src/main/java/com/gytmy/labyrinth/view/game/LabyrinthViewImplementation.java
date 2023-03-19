package com.gytmy.labyrinth.view.game;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import com.gytmy.labyrinth.model.Direction;
import com.gytmy.labyrinth.model.LabyrinthModel;
import com.gytmy.labyrinth.model.player.Player;
import com.gytmy.labyrinth.view.GameFrameHandler;
import com.gytmy.labyrinth.view.GameOverPanel;
import com.gytmy.labyrinth.view.TimerPanel;

public class LabyrinthViewImplementation extends LabyrinthView {
    protected LabyrinthModel model;
    protected LabyrinthPanel labyrinthPanel;
    protected TimerPanel timerPanel;
    private JFrame frame;

    private static final Color BACKGROUND_COLOR = Cell.WALL_COLOR;

    protected LabyrinthViewImplementation(LabyrinthModel model, JFrame frame) {
        this.model = model;
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);
        labyrinthPanel = new LabyrinthPanel(model);
    }

    public void startTimer() {
        timerPanel.start();
    }

    @Override
    public void stopTimer() {
        timerPanel.stop();
    }

    public int getTimerCounterInSeconds() {
        return timerPanel.getCounterInSeconds();
    }

    @Override
    public void update(Player player, Direction direction) {
        labyrinthPanel.update(player, direction);
    }

    @Override
    public LabyrinthPanel getLabyrinthPanel() {
        return labyrinthPanel;
    }

    @Override
    public boolean isTimerCounting() {
        return timerPanel.isCounting();
    }

    @Override
    public void showGameOverPanel() {
        frame.setContentPane(new GameOverPanel(model, frame));
        GameFrameHandler.frameUpdate("Game Over");
    }

    @Override
    public void notifyGameStarted() {
        // For this view, nothing needs to be done.
    }

}
