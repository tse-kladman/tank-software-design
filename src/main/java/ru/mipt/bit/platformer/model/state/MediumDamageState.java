package ru.mipt.bit.platformer.model.state;

public class MediumDamageState implements TankState {

    @Override
    public float getSecondsPerTileFactor() {
        return 2f;
    }

    @Override
    public float getReloadFactor() {
        return 1f;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
