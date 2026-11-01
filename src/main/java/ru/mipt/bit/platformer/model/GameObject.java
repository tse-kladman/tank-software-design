package ru.mipt.bit.platformer.model;

public interface GameObject {

    Coordinates getPosition();

    Coordinates getDestination();

    float getMovementProgress();

    float getRotation();

    boolean occupies(Coordinates tile);

    boolean blocksMovement();

    void live(float deltaTime);

    void takeDamage(int damage);

    boolean isDestroyed();
}
