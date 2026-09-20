package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank {

    private static final float MOVEMENT_SPEED = 0.4f;

    private final GridPoint2 position;
    private final GridPoint2 destination;
    private Direction direction;
    private float movementProgress = 1f;

    public Tank(GridPoint2 startPosition, Direction direction) {
        this.position = new GridPoint2(startPosition);
        this.destination = new GridPoint2(startPosition);
        this.direction = direction;
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    public void rotate(Direction direction) {
        this.direction = direction;
    }

    public void move(Direction direction) {
        if (isMoving()) {
            return;
        }
        rotate(direction);
        destination.set(direction.shift(position));
        movementProgress = 0f;
    }

    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (!isMoving()) {
            position.set(destination);
        }
    }

    public GridPoint2 getPosition() {
        return new GridPoint2(position);
    }

    public GridPoint2 getDestination() {
        return new GridPoint2(destination);
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public Direction getDirection() {
        return direction;
    }
}
