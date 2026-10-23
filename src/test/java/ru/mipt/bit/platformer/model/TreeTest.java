package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeTest {

    @Test
    void neverMovesAndIsNotRotated() {
        Tree tree = new Tree(new Coordinates(2, 3));

        assertEquals(new Coordinates(2, 3), tree.getPosition());
        assertEquals(tree.getPosition(), tree.getDestination());
        assertEquals(1f, tree.getMovementProgress());
        assertEquals(0f, tree.getRotation());
        assertTrue(tree.occupies(new Coordinates(2, 3)));
        assertFalse(tree.occupies(new Coordinates(2, 4)));
    }
}
