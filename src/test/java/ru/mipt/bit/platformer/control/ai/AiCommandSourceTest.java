package ru.mipt.bit.platformer.control.ai;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.model.BulletSettings;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TankSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiCommandSourceTest {

    private static final TankSettings SETTINGS =
            new TankSettings(0.4f, 3, 0.5f, new BulletSettings(0.1f, 1));

    private final List<Tank> asked = new ArrayList<>();

    private final TankStrategy recordingStrategy = tank -> {
        asked.add(tank);
        return () -> {
        };
    };

    private Tank tankAt(GameLevel level, int x, int y) {
        return new Tank(new Coordinates(x, y), Direction.RIGHT, SETTINGS, level);
    }

    @Test
    void asksStrategyForEveryAiTank() {
        GameLevel level = new GameLevel(10, 8);
        Tank player = tankAt(level, 1, 1);
        Tank firstAi = tankAt(level, 3, 3);
        Tank secondAi = tankAt(level, 5, 5);
        level.addPlayerTank(player);
        level.addTank(firstAi);
        level.addTank(secondAi);

        List<Command> commands = new AiCommandSource(level, recordingStrategy).commands();

        assertEquals(2, commands.size());
        assertEquals(Arrays.asList(firstAi, secondAi), asked);
    }

    @Test
    void neverAsksForThePlayerTank() {
        GameLevel level = new GameLevel(10, 8);
        Tank player = tankAt(level, 1, 1);
        level.addPlayerTank(player);

        assertTrue(new AiCommandSource(level, recordingStrategy).commands().isEmpty());
        assertFalse(asked.contains(player));
    }

    @Test
    void stopsAskingForTankWhichLeftTheLevel() {
        GameLevel level = new GameLevel(10, 8);
        Tank player = tankAt(level, 1, 1);
        Tank ai = tankAt(level, 3, 3);
        level.addPlayerTank(player);
        level.addTank(ai);
        AiCommandSource source = new AiCommandSource(level, recordingStrategy);

        assertEquals(1, source.commands().size());

        level.removeObject(ai);

        assertTrue(source.commands().isEmpty());
    }
}
