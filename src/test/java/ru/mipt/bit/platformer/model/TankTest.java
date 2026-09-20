package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private Tank tank;

    @BeforeEach
    void createTank() {
        tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
    }

    @Test
    void standsStillAfterCreation() {
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getPosition());
        assertEquals(new GridPoint2(1, 1), tank.getDestination());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void startsMovingToTheNeighbourTile() {
        tank.move(Direction.UP);

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getPosition());
        assertEquals(new GridPoint2(1, 2), tank.getDestination());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void reachesDestinationWhenMovementIsOver() {
        tank.move(Direction.UP);

        tank.update(0.4f);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getPosition());
    }

    @Test
    void isStillMovingInTheMiddleOfTheStep() {
        tank.move(Direction.UP);

        tank.update(0.2f);

        assertTrue(tank.isMoving());
        assertEquals(0.5f, tank.getMovementProgress(), 0.001f);
        assertEquals(new GridPoint2(1, 1), tank.getPosition());
    }

    @Test
    void ignoresNewMoveWhileMoving() {
        tank.move(Direction.UP);

        tank.move(Direction.RIGHT);

        assertEquals(new GridPoint2(1, 2), tank.getDestination());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void rotatesWithoutChangingPosition() {
        tank.rotate(Direction.DOWN);

        assertEquals(Direction.DOWN, tank.getDirection());
        assertEquals(new GridPoint2(1, 1), tank.getPosition());
        assertFalse(tank.isMoving());
    }

    @Test
    void keepsPositionWhenReturnedCopyIsChanged() {
        tank.getPosition().add(5, 5);

        assertEquals(new GridPoint2(1, 1), tank.getPosition());
    }
}
