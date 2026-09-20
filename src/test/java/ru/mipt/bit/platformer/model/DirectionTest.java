package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @Test
    void shiftsPointToTheNeighbourTile() {
        GridPoint2 point = new GridPoint2(3, 4);

        assertEquals(new GridPoint2(3, 5), Direction.UP.shift(point));
        assertEquals(new GridPoint2(3, 3), Direction.DOWN.shift(point));
        assertEquals(new GridPoint2(2, 4), Direction.LEFT.shift(point));
        assertEquals(new GridPoint2(4, 4), Direction.RIGHT.shift(point));
    }

    @Test
    void doesNotChangeGivenPoint() {
        GridPoint2 point = new GridPoint2(3, 4);

        Direction.UP.shift(point);

        assertEquals(new GridPoint2(3, 4), point);
    }

    @Test
    void hasRotationAngleForEveryDirection() {
        assertEquals(90f, Direction.UP.getAngle());
        assertEquals(-180f, Direction.LEFT.getAngle());
        assertEquals(-90f, Direction.DOWN.getAngle());
        assertEquals(0f, Direction.RIGHT.getAngle());
    }
}
