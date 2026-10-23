package ru.mipt.bit.platformer.model.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;

import java.util.ArrayList;
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
    private static final int AI_TANKS = 4;

    private LevelLayout generate(long seed) {
        return new RandomLevelSource(WIDTH, HEIGHT, TREES, AI_TANKS, new Random(seed)).load();
    }

    private List<Coordinates> everything(LevelLayout layout) {
        List<Coordinates> placed = new ArrayList<>();
        placed.add(layout.getPlayerTankPosition());
        placed.addAll(layout.getAiTankPositions());
        placed.addAll(layout.getTreePositions());
        return placed;
    }

    @Test
    void keepsRequestedLevelSize() {
        LevelLayout layout = generate(1);

        assertEquals(WIDTH, layout.getWidth());
        assertEquals(HEIGHT, layout.getHeight());
    }

    @Test
    void generatesRequestedNumberOfTreesAndTanks() {
        LevelLayout layout = generate(1);

        assertEquals(TREES, layout.getTreePositions().size());
        assertEquals(AI_TANKS, layout.getAiTankPositions().size());
    }

    @Test
    void putsEveryObjectOnItsOwnTile() {
        for (long seed = 0; seed < 50; seed++) {
            List<Coordinates> placed = everything(generate(seed));

            assertEquals(placed.size(), new HashSet<>(placed).size());
        }
    }

    @Test
    void neverPlacesPlayerTankOnATree() {
        for (long seed = 0; seed < 50; seed++) {
            LevelLayout layout = generate(seed);

            assertFalse(layout.getTreePositions().contains(layout.getPlayerTankPosition()));
        }
    }

    @Test
    void placesEverythingInsideTheLevel() {
        Set<Coordinates> placed = new HashSet<>(everything(generate(7)));

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
    void fillsLevelCompletelyWhenAskedForExactlyAllTiles() {
        LevelLayout layout = new RandomLevelSource(2, 2, 2, 1, new Random(1)).load();

        assertEquals(4, new HashSet<>(everything(layout)).size());
    }

    @Test
    void rejectsMoreObjectsThanTilesAvailable() {
        RandomLevelSource source = new RandomLevelSource(2, 2, 3, 2, new Random(1));

        assertThrows(IllegalArgumentException.class, source::load);
    }
}
