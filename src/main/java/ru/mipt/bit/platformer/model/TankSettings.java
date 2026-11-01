package ru.mipt.bit.platformer.model;

public class TankSettings {

    private final float secondsPerTile;
    private final int health;
    private final float reloadSeconds;
    private final BulletSettings bulletSettings;

    public TankSettings(float secondsPerTile, int health, float reloadSeconds, BulletSettings bulletSettings) {
        this.secondsPerTile = secondsPerTile;
        this.health = health;
        this.reloadSeconds = reloadSeconds;
        this.bulletSettings = bulletSettings;
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
}
