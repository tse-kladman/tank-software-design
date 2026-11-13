package ru.mipt.bit.platformer.model;

public class Bullet implements GameObject {

    private final Direction direction;
    private final BulletSettings settings;
    private final GameObject owner;
    private Coordinates position;
    private Coordinates destination;
    private float movementProgress;
    private boolean destroyed;

    public Bullet(Coordinates position, Direction direction, BulletSettings settings) {
        this(position, direction, settings, null);
    }

    public Bullet(Coordinates position, Direction direction, BulletSettings settings, GameObject owner) {
        this.position = position;
        this.destination = direction.shift(position);
        this.direction = direction;
        this.settings = settings;
        this.owner = owner;
    }

    public GameObject getOwner() {
        return owner;
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
    public int getHealth() {
        return 0;
    }

    @Override
    public int getMaxHealth() {
        return 0;
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
