package com.gytmy.labyrinth;

import java.awt.Color;

import com.gytmy.utils.Vector2;

public class PlayerImplementation implements Player {
    private Vector2 coordinates;
    private String name;
    private Color color;
    private boolean ready;

    public PlayerImplementation(Vector2 coordinates, String name, Color color, boolean ready) {
        this.coordinates = coordinates;
        this.name = name;
        this.color = color;
        this.ready = ready;
    }

    public PlayerImplementation(Vector2 coordinates) {
        this(coordinates.copy(), "NO NAME", Color.MAGENTA, false);
    }

    public PlayerImplementation(int x, int y) {
        this(new Vector2(x, y), "NO NAME", Color.MAGENTA, false);
    }

    public PlayerImplementation() {
        this(new Vector2(), "NO NAME", Color.MAGENTA, false);
    }

    @Override
    public int getX() {
        return coordinates.getX();
    }

    @Override
    public int getY() {
        return coordinates.getY();
    }

    @Override
    public Vector2 getCoordinates() {
        return coordinates.copy();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void setX(int x) {
        coordinates.setX(x);
    }

    @Override
    public void setY(int y) {
        coordinates.setY(y);
    }

    @Override
    public void setCoordinates(Vector2 coordinates) {
        this.coordinates = coordinates.copy();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public void move(Direction direction)
            throws IllegalArgumentException {
        switch (direction) {
            case LEFT:
            case RIGHT:
                int newHorizontalPosition = getX() + direction.getStep();
                setX(newHorizontalPosition);
                break;
            case UP:
            case DOWN:
                int newVerticalPosition = getY() + direction.getStep();
                setY(newVerticalPosition);
                break;
            default:
                throw new IllegalArgumentException("Direction " + direction + " is not supported");
        }
    }

}
