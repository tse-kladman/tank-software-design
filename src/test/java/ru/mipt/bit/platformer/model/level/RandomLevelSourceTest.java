package ru.mipt.bit.platformer.model.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelSourceTest {

    private static final int WIDTH = 10;
    private static final int HEIGHT = 8;
    private static final int TREES = 12;

    private LevelLayout generate(long seed) {
        return new RandomLevelSource(WIDTH, HEIGHT, TREES, new Random(seed)).load();
    }

    @Test
    void keepsRequestedLevelSize() {
        LevelLayout layout = generate(1);

        assertEquals(WIDTH, layout.getWidth());
        assertEquals(HEIGHT, layout.getHeight());
    }

    @Test
    void generatesRequestedNumberOfTrees() {
        assertEquals(TREES, generate(1).getTreePositions().size());
    }

    @Test
    void placesEveryTreeOnItsOwnTile() {
        List<Coordinates> trees = generate(42).getTreePositions();

        assertEquals(trees.size(), new HashSet<>(trees).size());
    }

    @Test
    void neverPlacesTankOnATree() {
        for (long seed = 0; seed < 50; seed++) {
            LevelLayout layout = generate(seed);

            assertFalse(layout.getTreePositions().contains(layout.getTankPosition()));
        }
    }

    @Test
    void placesEverythingInsideTheLevel() {
        LevelLayout layout = generate(7);

        Set<Coordinates> placed = new HashSet<>(layout.getTreePositions());
        placed.add(layout.getTankPosition());
        for (Coordinates tile : placed) {
            assertTrue(tile.getX() >= 0 && tile.getX() < WIDTH);
            assertTrue(tile.getY() >= 0 && tile.getY() < HEIGHT);
        }
    }

    @Test
    void differentSeedsGiveDifferentLevels() {
        assertFalse(generate(1).getTreePositions().equals(generate(2).getTreePositions()));
    }

    @Test
    void rejectsMoreTreesThanTilesAvailable() {
        RandomLevelSource source = new RandomLevelSource(2, 2, 4, new Random(1));

        assertThrows(IllegalArgumentException.class, source::load);
    }
}
