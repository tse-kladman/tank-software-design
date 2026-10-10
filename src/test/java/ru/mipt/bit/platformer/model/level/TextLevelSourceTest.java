package ru.mipt.bit.platformer.model.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextLevelSourceTest {

    private static final List<String> EXAMPLE = Arrays.asList(
            "___T__T___",
            "__TT__TTTT",
            "_________T",
            "TTTT__T__T",
            "_____X____",
            "__________");

    @Test
    void readsLevelSize() {
        LevelLayout layout = new TextLevelSource(EXAMPLE).load();

        assertEquals(10, layout.getWidth());
        assertEquals(6, layout.getHeight());
    }

    @Test
    void readsTankPositionCountingRowsFromTheBottom() {
        LevelLayout layout = new TextLevelSource(EXAMPLE).load();

        assertEquals(new Coordinates(5, 1), layout.getTankPosition());
    }

    @Test
    void readsEveryTree() {
        LevelLayout layout = new TextLevelSource(EXAMPLE).load();

        assertEquals(15, layout.getTreePositions().size());
        assertTrue(layout.getTreePositions().contains(new Coordinates(3, 5)));
        assertTrue(layout.getTreePositions().contains(new Coordinates(9, 3)));
        assertTrue(layout.getTreePositions().contains(new Coordinates(0, 2)));
    }

    @Test
    void placesNothingOnEmptyTiles() {
        LevelLayout layout = new TextLevelSource(Arrays.asList("__X", "___")).load();

        assertEquals(new Coordinates(2, 1), layout.getTankPosition());
        assertTrue(layout.getTreePositions().isEmpty());
    }

    @Test
    void ignoresBlankLines() {
        LevelLayout layout = new TextLevelSource(Arrays.asList("__X", "", "T__", "   ")).load();

        assertEquals(2, layout.getHeight());
        assertEquals(Collections.singletonList(new Coordinates(0, 0)), layout.getTreePositions());
    }

    @Test
    void rejectsLevelWithoutTank() {
        TextLevelSource source = new TextLevelSource(Arrays.asList("___", "T__"));

        assertThrows(IllegalArgumentException.class, source::load);
    }

    @Test
    void rejectsLevelWithTwoTanks() {
        TextLevelSource source = new TextLevelSource(Arrays.asList("X__", "__X"));

        assertThrows(IllegalArgumentException.class, source::load);
    }

    @Test
    void rejectsRowsOfDifferentLength() {
        TextLevelSource source = new TextLevelSource(Arrays.asList("X___", "___"));

        assertThrows(IllegalArgumentException.class, source::load);
    }

    @Test
    void rejectsUnknownSymbol() {
        TextLevelSource source = new TextLevelSource(Arrays.asList("X_?", "___"));

        assertThrows(IllegalArgumentException.class, source::load);
    }

    @Test
    void rejectsEmptyDescription() {
        TextLevelSource source = new TextLevelSource(Collections.emptyList());

        assertThrows(IllegalArgumentException.class, source::load);
    }
}
