package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CoordinatesTest {

    @Test
    void shiftReturnsNewCoordinates() {
        Coordinates origin = new Coordinates(2, 3);

        Coordinates shifted = origin.shift(1, -1);

        assertEquals(new Coordinates(3, 2), shifted);
        assertEquals(new Coordinates(2, 3), origin);
    }

    @Test
    void equalCoordinatesHaveEqualHashCodes() {
        assertEquals(new Coordinates(4, 5), new Coordinates(4, 5));
        assertEquals(new Coordinates(4, 5).hashCode(), new Coordinates(4, 5).hashCode());
    }

    @Test
    void differentCoordinatesAreNotEqual() {
        assertNotEquals(new Coordinates(4, 5), new Coordinates(5, 4));
    }
}
