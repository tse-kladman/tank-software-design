package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.model.state.TankState;

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
    private TankState state;

    public Tank(Coordinates position, Direction direction, TankSettings settings, BulletRegistry bulletRegistry) {
        this.position = position;
        this.destination = position;
        this.direction = direction;
        this.settings = settings;
        this.bulletRegistry = bulletRegistry;
        this.health = settings.getHealth();
        this.timeSinceShot = settings.getReloadSeconds();
        this.state = settings.getStates().stateFor(healthLeft());
    }

    public boolean isMoving() {
        return movementProgress < MOVEMENT_FINISHED;
    }

    public boolean isLoaded() {
        return timeSinceShot >= settings.getReloadSeconds() * state.getReloadFactor();
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
        if (isDestroyed() || !state.canShoot() || !isLoaded()) {
            return;
        }
        timeSinceShot = 0f;
        bulletRegistry.register(new Bullet(direction.shift(position), direction, settings.getBulletSettings(), this));
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return settings.getHealth();
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void live(float deltaTime) {
        timeSinceShot += deltaTime;
        movementProgress = Math.min(movementProgress + deltaTime / secondsPerTile(), MOVEMENT_FINISHED);
        if (!isMoving()) {
            position = destination;
        }
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
        state = settings.getStates().stateFor(healthLeft());
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

    private float secondsPerTile() {
        return settings.getSecondsPerTile() * state.getSecondsPerTileFactor();
    }

    private float healthLeft() {
        return (float) health / settings.getHealth();
    }
}
