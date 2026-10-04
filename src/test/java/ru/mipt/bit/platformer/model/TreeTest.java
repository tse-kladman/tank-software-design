package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTest {

    @Test
    void neverMovesAndIsNotRotated() {
        Tree tree = new Tree(new Coordinates(2, 3));

        assertEquals(new Coordinates(2, 3), tree.getPosition());
        assertEquals(tree.getPosition(), tree.getDestination());
        assertEquals(1f, tree.getMovementProgress());
        assertEquals(0f, tree.getRotation());
    }
}
