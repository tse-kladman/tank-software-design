package ru.mipt.bit.platformer.control.ai;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiCommandSourceTest {

    private static final float SECONDS_PER_TILE = 0.4f;

    private final List<Tank> asked = new ArrayList<>();

    private final TankStrategy recordingStrategy = tank -> {
        asked.add(tank);
        return () -> {
        };
    };

    private Tank tankAt(int x, int y) {
        return new Tank(new Coordinates(x, y), Direction.RIGHT, SECONDS_PER_TILE);
    }

    @Test
    void asksStrategyForEveryIdleTank() {
        Tank first = tankAt(1, 1);
        Tank second = tankAt(2, 2);

        List<Command> commands = new AiCommandSource(Arrays.asList(first, second), recordingStrategy).commands();

        assertEquals(2, commands.size());
        assertEquals(Arrays.asList(first, second), asked);
    }

    @Test
    void doesNotDisturbTankWhichIsAlreadyMoving() {
        Tank moving = tankAt(1, 1);
        moving.move(Direction.UP);

        List<Command> commands = new AiCommandSource(Collections.singletonList(moving), recordingStrategy).commands();

        assertTrue(commands.isEmpty());
        assertTrue(asked.isEmpty());
    }

    @Test
    void returnsNothingWhenThereAreNoAiTanks() {
        assertTrue(new AiCommandSource(Collections.emptyList(), recordingStrategy).commands().isEmpty());
    }
}
