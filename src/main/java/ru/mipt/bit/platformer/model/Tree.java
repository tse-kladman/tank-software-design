package ru.mipt.bit.platformer.model;

public class Tree implements GameObject {

    private final Coordinates position;

    public Tree(Coordinates position) {
        this.position = position;
    }

    @Override
    public Coordinates getPosition() {
        return position;
    }

    @Override
    public Coordinates getDestination() {
        return position;
    }

    @Override
    public float getMovementProgress() {
        return 1f;
    }

    @Override
    public boolean occupies(Coordinates tile) {
        return position.equals(tile);
    }

    @Override
    public float getRotation() {
        return 0f;
    }
}
