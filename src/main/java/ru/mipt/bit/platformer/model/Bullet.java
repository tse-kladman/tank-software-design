package ru.mipt.bit.platformer.model;

public class Bullet implements GameObject {

    private final Direction direction;
    private final BulletSettings settings;
    private Coordinates position;
    private Coordinates destination;
    private float movementProgress;
    private boolean destroyed;

    public Bullet(Coordinates position, Direction direction, BulletSettings settings) {
        this.position = position;
        this.destination = direction.shift(position);
        this.direction = direction;
        this.settings = settings;
    }

    public float getSecondsPerTile() {
        return settings.getSecondsPerTile();
    }

    public int getDamage() {
        return settings.getDamage();
    }

    public void destroy() {
        destroyed = true;
    }

    @Override
    public void live(float deltaTime) {
        movementProgress += deltaTime / settings.getSecondsPerTile();
        while (movementProgress >= 1f) {
            movementProgress -= 1f;
            position = destination;
            destination = direction.shift(position);
        }
    }

    @Override
    public void takeDamage(int damage) {
        destroy();
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
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
    public float getRotation() {
        return direction.getAngle();
    }

    @Override
    public boolean occupies(Coordinates tile) {
        return position.equals(tile);
    }

    @Override
    public boolean blocksMovement() {
        return false;
    }
}
