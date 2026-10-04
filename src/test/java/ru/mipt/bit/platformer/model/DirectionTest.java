package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @Test
    void shiftsCoordinatesToTheNeighbourTile() {
        Coordinates tile = new Coordinates(3, 4);

        assertEquals(new Coordinates(3, 5), Direction.UP.shift(tile));
        assertEquals(new Coordinates(3, 3), Direction.DOWN.shift(tile));
        assertEquals(new Coordinates(2, 4), Direction.LEFT.shift(tile));
        assertEquals(new Coordinates(4, 4), Direction.RIGHT.shift(tile));
    }

    @Test
    void hasRotationAngleForEveryDirection() {
        assertEquals(90f, Direction.UP.getAngle());
        assertEquals(-180f, Direction.LEFT.getAngle());
        assertEquals(-90f, Direction.DOWN.getAngle());
        assertEquals(0f, Direction.RIGHT.getAngle());
    }
}
