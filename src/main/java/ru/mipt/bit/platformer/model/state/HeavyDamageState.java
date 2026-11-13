package ru.mipt.bit.platformer.model.state;

public class HeavyDamageState implements TankState {

    @Override
    public float getSecondsPerTileFactor() {
        return 3f;
    }

    @Override
    public float getReloadFactor() {
        return 1f;
    }

    @Override
    public boolean canShoot() {
        return false;
    }
}
