package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeTest {

    @Test
    void occupiesOnlyItsOwnTile() {
        Tree tree = new Tree(new GridPoint2(2, 3));

        assertTrue(tree.isAt(new GridPoint2(2, 3)));
        assertFalse(tree.isAt(new GridPoint2(2, 4)));
    }

    @Test
    void keepsPositionWhenReturnedCopyIsChanged() {
        Tree tree = new Tree(new GridPoint2(2, 3));

        tree.getPosition().add(1, 1);

        assertEquals(new GridPoint2(2, 3), tree.getPosition());
    }
}
