package ru.mipt.bit.platformer.model;

public class Tank implements GameObject {

    private static final float MOVEMENT_FINISHED = 1f;

    private final float secondsPerTile;
    private Coordinates position;
    private Coordinates destination;
    private Direction direction;
    private float movementProgress = MOVEMENT_FINISHED;

    public Tank(Coordinates position, Direction direction, float secondsPerTile) {
        this.position = position;
        this.destination = position;
        this.direction = direction;
        this.secondsPerTile = secondsPerTile;
    }

    public boolean isMoving() {
        return movementProgress < MOVEMENT_FINISHED;
    }

    public void rotate(Direction direction) {
        this.direction = direction;
    }

    public void move(Direction direction) {
        if (isMoving()) {
            return;
        }
        rotate(direction);
        destination = direction.shift(position);
        movementProgress = 0f;
    }

    public void update(float deltaTime) {
        movementProgress = Math.min(movementProgress + deltaTime / secondsPerTile, MOVEMENT_FINISHED);
        if (!isMoving()) {
            position = destination;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public Coordinates getPosition() {
        return position;
    }

    @Override
    public Coordinates getDestination() {
        return destination;
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public boolean occupies(Coordinates tile) {
        return position.equals(tile) || destination.equals(tile);
    }

    @Override
    public float getRotation() {
        return direction.getAngle();
    }
}
