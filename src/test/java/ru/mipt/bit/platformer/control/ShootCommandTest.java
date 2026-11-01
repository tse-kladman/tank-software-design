package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TankSettings;
import ru.mipt.bit.platformer.model.BulletSettings;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShootCommandTest {

    private static final TankSettings SETTINGS =
            new TankSettings(0.4f, 3, 0.5f, new BulletSettings(0.1f, 1));

    @Test
    void asksItsOwnTankToShoot() {
        GameLevel level = new GameLevel(10, 8);
        Tank shooter = new Tank(new Coordinates(1, 1), Direction.RIGHT, SETTINGS, level);
        Tank other = new Tank(new Coordinates(5, 5), Direction.RIGHT, SETTINGS, level);
        level.addPlayerTank(shooter);
        level.addTank(other);

        new ShootCommand(shooter).execute();

        assertEquals(1, level.getBullets().size());
        assertEquals(new Coordinates(2, 1), level.getBullets().get(0).getPosition());
    }
}
