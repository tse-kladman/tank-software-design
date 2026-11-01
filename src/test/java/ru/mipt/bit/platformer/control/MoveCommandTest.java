package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.BulletSettings;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TankSettings;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveCommandTest {

    private static final float SECONDS_PER_TILE = 0.4f;
    private static final TankSettings SETTINGS =
            new TankSettings(SECONDS_PER_TILE, 3, 0.5f, new BulletSettings(0.1f, 1));

    private Tank tankAt(GameLevel level, int x, int y) {
        return new Tank(new Coordinates(x, y), Direction.RIGHT, SETTINGS, level);
    }

    @Test
    void movesItsOwnTankInItsOwnDirection() {
        GameLevel level = new GameLevel(10, 8);
        Tank playerTank = tankAt(level, 1, 1);
        Tank aiTank = tankAt(level, 5, 5);
        level.addPlayerTank(playerTank);
        level.addTank(aiTank);

        new MoveCommand(level, aiTank, Direction.UP).execute();
        level.live(SECONDS_PER_TILE);

        assertEquals(new Coordinates(5, 6), aiTank.getPosition());
        assertEquals(new Coordinates(1, 1), playerTank.getPosition());
    }

    @Test
    void doesNothingWhenTargetTileIsTakenByAnotherTank() {
        GameLevel level = new GameLevel(10, 8);
        Tank playerTank = tankAt(level, 1, 1);
        Tank aiTank = tankAt(level, 2, 1);
        level.addPlayerTank(playerTank);
        level.addTank(aiTank);

        new MoveCommand(level, playerTank, Direction.RIGHT).execute();
        level.live(SECONDS_PER_TILE);

        assertEquals(new Coordinates(1, 1), playerTank.getPosition());
    }
}
