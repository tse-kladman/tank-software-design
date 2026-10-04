package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static final float SECONDS_PER_TILE = 0.4f;

    private Tank tank;

    @BeforeEach
    void createTank() {
        tank = new Tank(new Coordinates(1, 1), Direction.RIGHT, SECONDS_PER_TILE);
    }

    @Test
    void standsStillAfterCreation() {
        assertFalse(tank.isMoving());
        assertEquals(new Coordinates(1, 1), tank.getPosition());
        assertEquals(new Coordinates(1, 1), tank.getDestination());
        assertEquals(Direction.RIGHT, tank.getDirection());
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

        tank.update(SECONDS_PER_TILE);

        assertFalse(tank.isMoving());
        assertEquals(new Coordinates(1, 2), tank.getPosition());
    }

    @Test
    void isStillMovingInTheMiddleOfTheStep() {
        tank.move(Direction.UP);

        tank.update(SECONDS_PER_TILE / 2);

        assertTrue(tank.isMoving());
        assertEquals(0.5f, tank.getMovementProgress(), 0.001f);
        assertEquals(new Coordinates(1, 1), tank.getPosition());
    }

    @Test
    void slowerTankNeedsMoreTimeForTheSameStep() {
        Tank slowTank = new Tank(new Coordinates(1, 1), Direction.RIGHT, SECONDS_PER_TILE * 2);

        slowTank.move(Direction.UP);
        slowTank.update(SECONDS_PER_TILE);

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
}
