package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mipt.bit.platformer.model.TestTanks.HEALTH;
import static ru.mipt.bit.platformer.model.TestTanks.RELOAD_SECONDS;
import static ru.mipt.bit.platformer.model.TestTanks.SECONDS_PER_TILE;
import static ru.mipt.bit.platformer.model.TestTanks.settings;

class TankTest {

    private final List<Bullet> fired = new ArrayList<>();
    private Tank tank;

    @BeforeEach
    void createTank() {
        tank = new Tank(new Coordinates(1, 1), Direction.RIGHT, settings(), fired::add);
    }

    @Test
    void standsStillAfterCreation() {
        assertFalse(tank.isMoving());
        assertEquals(new Coordinates(1, 1), tank.getPosition());
        assertEquals(new Coordinates(1, 1), tank.getDestination());
        assertEquals(Direction.RIGHT, tank.getDirection());
        assertEquals(HEALTH, tank.getHealth());
    }

    @Test
    void startsMovingToTheNeighbourTile() {
        tank.move(Direction.UP);

        assertTrue(tank.isMoving());
        assertEquals(new Coordinates(1, 1), tank.getPosition());
        assertEquals(new Coordinates(1, 2), tank.getDestination());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void reachesDestinationWhenMovementIsOver() {
        tank.move(Direction.UP);

        tank.live(SECONDS_PER_TILE);

        assertFalse(tank.isMoving());
        assertEquals(new Coordinates(1, 2), tank.getPosition());
    }

    @Test
    void isStillMovingInTheMiddleOfTheStep() {
        tank.move(Direction.UP);

        tank.live(SECONDS_PER_TILE / 2);

        assertTrue(tank.isMoving());
        assertEquals(0.5f, tank.getMovementProgress(), 0.001f);
        assertEquals(new Coordinates(1, 1), tank.getPosition());
    }

    @Test
    void slowerTankNeedsMoreTimeForTheSameStep() {
        TankSettings slow = new TankSettings(SECONDS_PER_TILE * 2, HEALTH, RELOAD_SECONDS,
                new BulletSettings(0.1f, 1));
        Tank slowTank = new Tank(new Coordinates(1, 1), Direction.RIGHT, slow, fired::add);

        slowTank.move(Direction.UP);
        slowTank.live(SECONDS_PER_TILE);

        assertTrue(slowTank.isMoving());
        assertEquals(new Coordinates(1, 1), slowTank.getPosition());
    }

    @Test
    void ignoresNewMoveWhileMoving() {
        tank.move(Direction.UP);

        tank.move(Direction.RIGHT);

        assertEquals(new Coordinates(1, 2), tank.getDestination());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void rotatesWithoutChangingPosition() {
        tank.rotate(Direction.DOWN);

        assertEquals(Direction.DOWN, tank.getDirection());
        assertEquals(-90f, tank.getRotation());
        assertEquals(new Coordinates(1, 1), tank.getPosition());
        assertFalse(tank.isMoving());
    }

    @Test
    void occupiesOnlyItsTileWhenStandingStill() {
        assertTrue(tank.occupies(new Coordinates(1, 1)));
        assertFalse(tank.occupies(new Coordinates(1, 2)));
    }

    @Test
    void occupiesBothTilesWhileMoving() {
        tank.move(Direction.UP);

        assertTrue(tank.occupies(new Coordinates(1, 1)));
        assertTrue(tank.occupies(new Coordinates(1, 2)));
    }

    @Test
    void stopsOccupyingLeftTileAfterArrival() {
        tank.move(Direction.UP);
        tank.live(SECONDS_PER_TILE);

        assertFalse(tank.occupies(new Coordinates(1, 1)));
        assertTrue(tank.occupies(new Coordinates(1, 2)));
    }

    @Test
    void firesBulletIntoTheNextTileInFrontOfIt() {
        tank.rotate(Direction.UP);

        tank.shoot();

        assertEquals(1, fired.size());
        assertEquals(new Coordinates(1, 2), fired.get(0).getPosition());
        assertEquals(new Coordinates(1, 3), fired.get(0).getDestination());
    }

    @Test
    void needsToReloadBetweenShots() {
        tank.shoot();
        tank.shoot();

        assertEquals(1, fired.size());
        assertFalse(tank.isLoaded());
    }

    @Test
    void isDestroyedWhenHealthRunsOut() {
        tank.takeDamage(HEALTH);

        assertTrue(tank.isDestroyed());
    }
}
