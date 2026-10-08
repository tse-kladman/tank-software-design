package ru.mipt.bit.platformer.model.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameLevelFactoryTest {

    private final GameLevelFactory factory = new GameLevelFactory(0.4f, Direction.RIGHT);

    @Test
    void buildsLevelFromAnyLevelSource() {
        LevelSource source = () -> new LevelLayout(4, 3, new Coordinates(1, 1),
                Arrays.asList(new Coordinates(0, 0), new Coordinates(3, 2)));

        GameLevel level = factory.create(source);

        assertEquals(new Coordinates(1, 1), level.getPlayerTank().getPosition());
        assertEquals(2, level.getObstacles().size());
        assertFalse(level.isFree(new Coordinates(0, 0)));
        assertFalse(level.isFree(new Coordinates(4, 1)));
        assertTrue(level.isFree(new Coordinates(2, 1)));
    }

    @Test
    void buildsPlayableLevelFromTextDescription() {
        GameLevel level = factory.create(new TextLevelSource(Arrays.asList("__T", "_X_")));

        assertEquals(new Coordinates(1, 0), level.getPlayerTank().getPosition());
        assertEquals(Direction.RIGHT, level.getPlayerTank().getDirection());
        assertFalse(level.isFree(new Coordinates(2, 1)));
        assertEquals(2, level.getObjects().size());
    }
}
