package ru.mipt.bit.platformer.model.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameLevelFactoryTest {

    private final GameLevelFactory factory = new GameLevelFactory(0.4f, Direction.RIGHT);

    @Test
    void buildsLevelFromAnyLevelSource() {
        LevelSource source = () -> new LevelLayout(4, 3, new Coordinates(1, 1),
                Collections.singletonList(new Coordinates(2, 2)),
                Arrays.asList(new Coordinates(0, 0), new Coordinates(3, 2)));

        GameLevel level = factory.create(source);

        assertEquals(new Coordinates(1, 1), level.getPlayerTank().getPosition());
        assertEquals(1, level.getAiTanks().size());
        assertEquals(new Coordinates(2, 2), level.getAiTanks().get(0).getPosition());
        assertEquals(2, level.getObstacles().size());
        assertFalse(level.isFree(new Coordinates(0, 0)));
        assertFalse(level.isFree(new Coordinates(4, 1)));
        assertTrue(level.isFree(new Coordinates(2, 1)));
    }

    @Test
    void buildsPlayableLevelFromTextDescription() {
        GameLevel level = factory.create(new TextLevelSource(Arrays.asList("__T", "EX_")));

        assertEquals(new Coordinates(1, 0), level.getPlayerTank().getPosition());
        assertEquals(Direction.RIGHT, level.getPlayerTank().getDirection());
        assertEquals(1, level.getAiTanks().size());
        assertFalse(level.isFree(new Coordinates(2, 1)));
        assertEquals(3, level.getObjects().size());
    }

    @Test
    void givesEveryTankTheSameSpeed() {
        GameLevel level = new GameLevelFactory(0.2f, Direction.UP)
                .create(new TextLevelSource(Arrays.asList("_E_", "_X_")));

        for (Tank tank : level.getTanks()) {
            assertEquals(Direction.UP, tank.getDirection());
        }

        Tank aiTank = level.getAiTanks().get(0);
        level.moveTank(aiTank, Direction.LEFT);
        level.update(0.2f);

        assertFalse(aiTank.isMoving());
    }
}
