package ru.mipt.bit.platformer.model.state;

public interface TankState {

    float getSecondsPerTileFactor();

    float getReloadFactor();

    boolean canShoot();
}
