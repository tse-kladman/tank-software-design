package ru.mipt.bit.platformer.control.ai;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RandomDirectionStrategyTest {

    private static final float SECONDS_PER_TILE = 0.4f;
    private static final Coordinates START = new Coordinates(5, 5);

    private Tank createTank() {
        return new Tank(START, Direction.RIGHT, SECONDS_PER_TILE);
    }

    private GameLevel createLevel(Tank tank) {
        return new GameLevel(10, 8, tank, Collections.emptyList(), Collections.emptyList());
    }

    @Test
    void movesTankToOneOfTheNeighbourTiles() {
        Tank tank = createTank();
        GameLevel level = createLevel(tank);

        new RandomDirectionStrategy(level, new Random(1)).nextCommand(tank).execute();
        level.update(SECONDS_PER_TILE);

        assertNotEquals(START, tank.getPosition());
        int distance = Math.abs(tank.getPosition().getX() - START.getX())
                + Math.abs(tank.getPosition().getY() - START.getY());
        assertEquals(1, distance);
    }

    @Test
    void soonerOrLaterChoosesEveryDirection() {
        Tank tank = createTank();
        GameLevel level = createLevel(tank);
        RandomDirectionStrategy strategy = new RandomDirectionStrategy(level, new Random(1));

        Set<Direction> chosen = new HashSet<>();
        for (int attempt = 0; attempt < 50; attempt++) {
            strategy.nextCommand(tank).execute();
            chosen.add(tank.getDirection());
            level.update(SECONDS_PER_TILE);
        }

        assertEquals(Direction.values().length, chosen.size());
    }

    @Test
    void sameSeedGivesSameChoice() {
        Tank first = createTank();
        new RandomDirectionStrategy(createLevel(first), new Random(123)).nextCommand(first).execute();

        Tank second = createTank();
        new RandomDirectionStrategy(createLevel(second), new Random(123)).nextCommand(second).execute();

        assertEquals(first.getDirection(), second.getDirection());
        assertEquals(first.getDestination(), second.getDestination());
    }
}
