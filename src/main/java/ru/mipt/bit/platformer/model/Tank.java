package ru.mipt.bit.platformer.model;

public class Tank implements GameObject {

    private static final float MOVEMENT_FINISHED = 1f;

    private final TankSettings settings;
    private final BulletRegistry bulletRegistry;
    private Coordinates position;
    private Coordinates destination;
    private Direction direction;
    private float movementProgress = MOVEMENT_FINISHED;
    private float timeSinceShot;
    private int health;

    public Tank(Coordinates position, Direction direction, TankSettings settings, BulletRegistry bulletRegistry) {
        this.position = position;
        this.destination = position;
        this.direction = direction;
        this.settings = settings;
        this.bulletRegistry = bulletRegistry;
        this.health = settings.getHealth();
        this.timeSinceShot = settings.getReloadSeconds();
    }

    public boolean isMoving() {
        return movementProgress < MOVEMENT_FINISHED;
    }

    public boolean isLoaded() {
        return timeSinceShot >= settings.getReloadSeconds();
    }

    public void rotate(Direction direction) {
        if (isDestroyed()) {
            return;
        }
        this.direction = direction;
    }

    public void move(Direction direction) {
        if (isDestroyed() || isMoving()) {
            return;
        }
        rotate(direction);
        destination = direction.shift(position);
        movementProgress = 0f;
    }

    public void shoot() {
        if (isDestroyed() || !isLoaded()) {
            return;
        }
        timeSinceShot = 0f;
        bulletRegistry.register(new Bullet(direction.shift(destination), direction, settings.getBulletSettings()));
    }

    public int getHealth() {
        return health;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void live(float deltaTime) {
        timeSinceShot += deltaTime;
        movementProgress = Math.min(movementProgress + deltaTime / settings.getSecondsPerTile(), MOVEMENT_FINISHED);
        if (!isMoving()) {
            position = destination;
        }
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
    }

    @Override
    public boolean isDestroyed() {
        return health <= 0;
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
        return position.equals(tile) || destination.equals(tile);
    }

    @Override
    public boolean blocksMovement() {
        return true;
    }
}
