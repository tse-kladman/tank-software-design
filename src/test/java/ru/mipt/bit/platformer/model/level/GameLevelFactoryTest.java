package ru.mipt.bit.platformer.model.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.BulletSettings;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.GameLevelListener;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.TankSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameLevelFactoryTest {

    private static final TankSettings SETTINGS =
            new TankSettings(0.4f, 3, 0.5f, new BulletSettings(0.1f, 1));

    private final GameLevelFactory factory = new GameLevelFactory(SETTINGS, Direction.RIGHT);

    @Test
    void buildsLevelFromAnyLevelSource() {
        LevelSource source = () -> new LevelLayout(4, 3, new Coordinates(1, 1),
                Collections.singletonList(new Coordinates(2, 2)),
                Arrays.asList(new Coordinates(0, 0), new Coordinates(3, 2)));

        GameLevel level = factory.create(source);

        assertEquals(new Coordinates(1, 1), level.getPlayerTank().getPosition());
        assertEquals(1, level.getAiTanks().size());
        assertEquals(new Coordinates(2, 2), level.getAiTanks().get(0).getPosition());
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
    void reportsEveryInitialObjectToListener() {
        List<GameObject> added = new ArrayList<>();
        GameLevelListener listener = new GameLevelListener() {
            @Override
            public void objectAdded(GameObject object) {
                added.add(object);
            }

            @Override
            public void objectRemoved(GameObject object) {
                added.remove(object);
            }
        };

        GameLevel level = factory.create(new TextLevelSource(Arrays.asList("T_T", "EX_")),
                Collections.singletonList(listener));

        assertEquals(level.getObjects(), added);
        assertEquals(4, added.size());
    }
}
