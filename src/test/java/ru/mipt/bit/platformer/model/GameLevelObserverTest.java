package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mipt.bit.platformer.model.TestTanks.HEALTH;
import static ru.mipt.bit.platformer.model.TestTanks.RELOAD_SECONDS;
import static ru.mipt.bit.platformer.model.TestTanks.settings;

class GameLevelObserverTest {

    private final List<String> events = new ArrayList<>();

    private final GameLevelListener listener = new GameLevelListener() {
        @Override
        public void objectAdded(GameObject object) {
            events.add("added " + object.getClass().getSimpleName() + " " + object.getPosition());
        }

        @Override
        public void objectRemoved(GameObject object) {
            events.add("removed " + object.getClass().getSimpleName() + " " + object.getPosition());
        }
    };

    @Test
    void tellsListenerAboutBulletCreatedDuringTheGame() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = new Tank(new Coordinates(1, 1), Direction.RIGHT, settings(), level);
        level.addPlayerTank(tank);
        level.addListener(listener);

        tank.shoot();

        assertEquals(1, events.size());
        assertTrue(events.get(0).startsWith("added Bullet"));
    }

    @Test
    void tellsListenerAboutDestroyedTank() {
        GameLevel level = new GameLevel(10, 8);
        Tank shooter = new Tank(new Coordinates(1, 1), Direction.RIGHT, settings(), level);
        Tank victim = new Tank(new Coordinates(2, 1), Direction.RIGHT, settings(), level);
        level.addPlayerTank(shooter);
        level.addTank(victim);
        level.addListener(listener);

        for (int shot = 0; shot < HEALTH; shot++) {
            shooter.shoot();
            level.live(RELOAD_SECONDS);
        }

        assertTrue(events.contains("removed Tank " + new Coordinates(2, 1)));
    }
}
