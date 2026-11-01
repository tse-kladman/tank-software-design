package ru.mipt.bit.platformer.control.ai;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.BulletSettings;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TankSettings;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RandomActionStrategyTest {

    private static final float SECONDS_PER_TILE = 0.4f;
    private static final TankSettings SETTINGS =
            new TankSettings(SECONDS_PER_TILE, 3, 0f, new BulletSettings(0.1f, 1));
    private static final Coordinates START = new Coordinates(5, 5);
    private static final float NEVER_SHOOT = 0f;
    private static final float ALWAYS_SHOOT = 1f;

    private GameLevel level;
    private Tank tank;

    private void createLevel() {
        level = new GameLevel(10, 8);
        tank = new Tank(START, Direction.RIGHT, SETTINGS, level);
        level.addPlayerTank(tank);
    }

    @Test
    void movesTankToOneOfTheNeighbourTiles() {
        createLevel();

        new RandomActionStrategy(level, new Random(1), NEVER_SHOOT).nextCommand(tank).execute();
        level.live(SECONDS_PER_TILE);

        assertNotEquals(START, tank.getPosition());
        int distance = Math.abs(tank.getPosition().getX() - START.getX())
                + Math.abs(tank.getPosition().getY() - START.getY());
        assertEquals(1, distance);
    }

    @Test
    void soonerOrLaterChoosesEveryDirection() {
        createLevel();
        RandomActionStrategy strategy = new RandomActionStrategy(level, new Random(1), NEVER_SHOOT);

        Set<Direction> chosen = new HashSet<>();
        for (int attempt = 0; attempt < 50; attempt++) {
            strategy.nextCommand(tank).execute();
            chosen.add(tank.getDirection());
            level.live(SECONDS_PER_TILE);
        }

        assertEquals(Direction.values().length, chosen.size());
    }

    @Test
    void shootsWhenProbabilitySaysSo() {
        createLevel();

        new RandomActionStrategy(level, new Random(1), ALWAYS_SHOOT).nextCommand(tank).execute();

        assertEquals(1, level.getBullets().size());
        assertEquals(START, tank.getPosition());
    }
}
