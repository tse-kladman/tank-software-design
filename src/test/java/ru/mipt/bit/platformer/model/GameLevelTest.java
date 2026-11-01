package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mipt.bit.platformer.model.TestTanks.BULLET_SECONDS_PER_TILE;
import static ru.mipt.bit.platformer.model.TestTanks.HEALTH;
import static ru.mipt.bit.platformer.model.TestTanks.SECONDS_PER_TILE;
import static ru.mipt.bit.platformer.model.TestTanks.settings;

class GameLevelTest {

    private GameLevel level;
    private Tank tank;

    private Tank tankAt(int x, int y) {
        return new Tank(new Coordinates(x, y), Direction.RIGHT, settings(), level);
    }

    @BeforeEach
    void createLevel() {
        level = new GameLevel(10, 8);
        tank = tankAt(1, 1);
        level.addPlayerTank(tank);
        level.addObstacle(new Tree(new Coordinates(1, 2)));
        level.addObstacle(new Tree(new Coordinates(5, 5)));
    }

    @Test
    void emptyTileIsFree() {
        assertTrue(level.isFree(new Coordinates(4, 4)));
    }

    @Test
    void tileWithObstacleIsNotFree() {
        assertFalse(level.isFree(new Coordinates(1, 2)));
        assertFalse(level.isFree(new Coordinates(5, 5)));
    }

    @Test
    void tileOutsideOfLevelIsNotFree() {
        assertFalse(level.isFree(new Coordinates(-1, 3)));
        assertFalse(level.isFree(new Coordinates(3, -1)));
        assertFalse(level.isFree(new Coordinates(10, 3)));
        assertFalse(level.isFree(new Coordinates(3, 8)));
    }

    @Test
    void movesTankToFreeTile() {
        level.moveTank(tank, Direction.RIGHT);
        level.live(SECONDS_PER_TILE);

        assertEquals(new Coordinates(2, 1), tank.getPosition());
    }

    @Test
    void rotatesTankButKeepsItInPlaceWhenObstacleBlocksTheWay() {
        level.moveTank(tank, Direction.UP);
        level.live(SECONDS_PER_TILE);

        assertEquals(new Coordinates(1, 1), tank.getPosition());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void doesNotLetTankLeaveTheLevel() {
        GameLevel small = new GameLevel(10, 8);
        Tank corner = new Tank(new Coordinates(0, 0), Direction.RIGHT, settings(), small);
        small.addPlayerTank(corner);

        small.moveTank(corner, Direction.LEFT);
        small.live(SECONDS_PER_TILE);
        assertEquals(new Coordinates(0, 0), corner.getPosition());

        small.moveTank(corner, Direction.DOWN);
        small.live(SECONDS_PER_TILE);
        assertEquals(new Coordinates(0, 0), corner.getPosition());
    }

    @Test
    void tankCanNotDriveIntoAnotherTank() {
        level.addTank(tankAt(2, 1));

        level.moveTank(tank, Direction.RIGHT);
        level.live(SECONDS_PER_TILE);

        assertEquals(new Coordinates(1, 1), tank.getPosition());
    }

    @Test
    void movingTankKeepsBothItsTilesOccupied() {
        Tank ai = tankAt(7, 1);
        level.addTank(ai);

        level.moveTank(ai, Direction.UP);

        assertFalse(level.isFree(new Coordinates(7, 1)));
        assertFalse(level.isFree(new Coordinates(7, 2)));
    }

    @Test
    void noOtherTankMayEnterTileAimedAtByMovingTank() {
        Tank mover = tankAt(7, 1);
        Tank rival = tankAt(8, 2);
        level.addTank(mover);
        level.addTank(rival);

        level.moveTank(mover, Direction.UP);
        level.moveTank(rival, Direction.LEFT);
        level.live(SECONDS_PER_TILE);

        assertEquals(new Coordinates(7, 2), mover.getPosition());
        assertEquals(new Coordinates(8, 2), rival.getPosition());
    }

    @Test
    void bulletDisappearsWhenItLeavesTheLevel() {
        Tank shooter = tankAt(9, 4);
        level.addTank(shooter);
        shooter.shoot();

        level.live(BULLET_SECONDS_PER_TILE);

        assertTrue(level.getBullets().isEmpty());
    }

    @Test
    void bulletDisappearsOnATree() {
        Tank shooter = tankAt(5, 4);
        level.addTank(shooter);
        shooter.rotate(Direction.UP);
        shooter.shoot();

        level.live(BULLET_SECONDS_PER_TILE / 2);

        assertTrue(level.getBullets().isEmpty());
        assertEquals(4, level.getObjects().size());
    }

    @Test
    void bulletDamagesTankItHits() {
        Tank shooter = tankAt(3, 4);
        Tank victim = tankAt(6, 4);
        level.addTank(shooter);
        level.addTank(victim);

        shooter.shoot();
        for (int tick = 0; tick < 10; tick++) {
            level.live(BULLET_SECONDS_PER_TILE / 2);
        }

        assertEquals(HEALTH - 1, victim.getHealth());
        assertTrue(level.getBullets().isEmpty());
    }

    @Test
    void bulletHitsEnemyStandingRightNextToTheShooter() {
        Tank shooter = tankAt(3, 4);
        Tank victim = tankAt(4, 4);
        level.addTank(shooter);
        level.addTank(victim);

        shooter.shoot();
        level.live(BULLET_SECONDS_PER_TILE / 4);

        assertEquals(HEALTH - 1, victim.getHealth());
        assertTrue(level.getBullets().isEmpty());
    }

    @Test
    void tankIsRemovedFromLevelWhenItsHealthRunsOut() {
        Tank shooter = tankAt(3, 4);
        Tank victim = tankAt(4, 4);
        level.addTank(shooter);
        level.addTank(victim);

        for (int shot = 0; shot < HEALTH; shot++) {
            shooter.shoot();
            level.live(TestTanks.RELOAD_SECONDS);
        }

        assertTrue(victim.isDestroyed());
        assertFalse(level.getObjects().contains(victim));
        assertFalse(level.getTanks().contains(victim));
    }
}
