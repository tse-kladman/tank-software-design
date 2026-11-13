package ru.mipt.bit.platformer.model.state;

public class LightDamageState implements TankState {

    @Override
    public float getSecondsPerTileFactor() {
        return 1f;
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
