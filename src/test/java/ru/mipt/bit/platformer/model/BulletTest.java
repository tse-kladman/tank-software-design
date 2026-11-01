package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mipt.bit.platformer.model.TestTanks.BULLET_SECONDS_PER_TILE;

class BulletTest {

    private static final BulletSettings SETTINGS = new BulletSettings(BULLET_SECONDS_PER_TILE, 1);

    private Bullet bullet() {
        return new Bullet(new Coordinates(2, 2), Direction.RIGHT, SETTINGS);
    }

    @Test
    void fliesTileByTileInTheSameDirection() {
        Bullet bullet = bullet();

        bullet.live(BULLET_SECONDS_PER_TILE);
        assertEquals(new Coordinates(3, 2), bullet.getPosition());

        bullet.live(BULLET_SECONDS_PER_TILE);
        assertEquals(new Coordinates(4, 2), bullet.getPosition());
    }

    @Test
    void doesNotBlockTankMovement() {
        assertFalse(bullet().blocksMovement());
    }

    @Test
    void isDestroyedByAnyDamage() {
        Bullet bullet = bullet();
        assertFalse(bullet.isDestroyed());

        bullet.takeDamage(1);

        assertTrue(bullet.isDestroyed());
    }
}
