package ru.mipt.bit.platformer.model;

public interface GameObject {

    Coordinates getPosition();

    Coordinates getDestination();

    float getMovementProgress();

    float getRotation();
}
