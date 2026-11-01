package ru.mipt.bit.platformer.model;

public final class TestTanks {

    public static final float SECONDS_PER_TILE = 0.4f;
    public static final float BULLET_SECONDS_PER_TILE = 0.1f;
    public static final float RELOAD_SECONDS = 0.5f;
    public static final int HEALTH = 3;
    public static final int DAMAGE = 1;

    private TestTanks() {
    }

    public static TankSettings settings() {
        return settings(HEALTH, DAMAGE);
    }

    public static TankSettings settings(int health, int damage) {
        return new TankSettings(SECONDS_PER_TILE, health, RELOAD_SECONDS,
                new BulletSettings(BULLET_SECONDS_PER_TILE, damage));
    }

    public static Tank tankAt(BulletRegistry registry, int x, int y) {
        return new Tank(new Coordinates(x, y), Direction.RIGHT, settings(), registry);
    }

    public static BulletRegistry noRegistry() {
        return bullet -> {
        };
    }
}
