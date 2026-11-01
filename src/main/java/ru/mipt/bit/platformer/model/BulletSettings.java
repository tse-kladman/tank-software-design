package ru.mipt.bit.platformer.model;

public class BulletSettings {

    private final float secondsPerTile;
    private final int damage;

    public BulletSettings(float secondsPerTile, int damage) {
        this.secondsPerTile = secondsPerTile;
        this.damage = damage;
    }

    public float getSecondsPerTile() {
        return secondsPerTile;
    }

    public int getDamage() {
        return damage;
    }
}
