package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.model.state.TankStates;

public class TankSettings {

    private final float secondsPerTile;
    private final int health;
    private final float reloadSeconds;
    private final BulletSettings bulletSettings;
    private final TankStates states;

    public TankSettings(float secondsPerTile, int health, float reloadSeconds, BulletSettings bulletSettings,
                        TankStates states) {
        this.secondsPerTile = secondsPerTile;
        this.health = health;
        this.reloadSeconds = reloadSeconds;
        this.bulletSettings = bulletSettings;
        this.states = states;
    }

    public float getSecondsPerTile() {
        return secondsPerTile;
    }

    public int getHealth() {
        return health;
    }

    public float getReloadSeconds() {
        return reloadSeconds;
    }

    public BulletSettings getBulletSettings() {
        return bulletSettings;
    }

    public TankStates getStates() {
        return states;
    }
}
