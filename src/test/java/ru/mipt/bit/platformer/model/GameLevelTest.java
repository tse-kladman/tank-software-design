package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameLevelTest {

    private static final float SECONDS_PER_TILE = 0.4f;

    private Tank tank;
    private GameLevel gameLevel;

    private Tank tankAt(int x, int y) {
        return new Tank(new Coordinates(x, y), Direction.RIGHT, SECONDS_PER_TILE);
    }

    @BeforeEach
    void createLevel() {
        tank = tankAt(1, 1);
        gameLevel = new GameLevel(10, 8, tank, Collections.emptyList(),
                Arrays.asList(new Tree(new Coordinates(1, 2)), new Tree(new Coordinates(5, 5))));
    }

    @Test
    void emptyTileIsFree() {
        assertTrue(gameLevel.isFree(new Coordinates(4, 4)));
    }

    @Test
    void tileWithObstacleIsNotFree() {
        assertFalse(gameLevel.isFree(new Coordinates(1, 2)));
        assertFalse(gameLevel.isFree(new Coordinates(5, 5)));
    }

    @Test
    void tileOutsideOfLevelIsNotFree() {
        assertFalse(gameLevel.isFree(new Coordinates(-1, 3)));
        assertFalse(gameLevel.isFree(new Coordinates(3, -1)));
        assertFalse(gameLevel.isFree(new Coordinates(10, 3)));
        assertFalse(gameLevel.isFree(new Coordinates(3, 8)));
    }

    @Test
    void blocksTileOfAnyObstacleKind() {
        GameLevel level = new GameLevel(10, 8, tank, Collections.emptyList(),
                Collections.singletonList(new Wall(new Coordinates(2, 2))));

        assertFalse(level.isFree(new Coordinates(2, 2)));
        assertTrue(level.isFree(new Coordinates(2, 3)));
    }

    @Test
    void movesTankToFreeTile() {
        gameLevel.moveTank(tank, Direction.RIGHT);
        gameLevel.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(2, 1), tank.getPosition());
    }

    @Test
    void rotatesTankButKeepsItInPlaceWhenObstacleBlocksTheWay() {
        gameLevel.moveTank(tank, Direction.UP);
        gameLevel.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(1, 1), tank.getPosition());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void doesNotLetTankLeaveTheLevel() {
        Tank corner = tankAt(0, 0);
        GameLevel level = new GameLevel(10, 8, corner, Collections.emptyList(), Collections.emptyList());

        level.moveTank(corner, Direction.LEFT);
        level.update(SECONDS_PER_TILE);
        assertEquals(new Coordinates(0, 0), corner.getPosition());
        assertEquals(Direction.LEFT, corner.getDirection());

        level.moveTank(corner, Direction.DOWN);
        level.update(SECONDS_PER_TILE);
        assertEquals(new Coordinates(0, 0), corner.getPosition());
    }

    @Test
    void doesNotLetTankLeaveTheLevelThroughFarBorders() {
        Tank corner = tankAt(9, 7);
        GameLevel level = new GameLevel(10, 8, corner, Collections.emptyList(), Collections.emptyList());

        level.moveTank(corner, Direction.RIGHT);
        level.update(SECONDS_PER_TILE);
        assertEquals(new Coordinates(9, 7), corner.getPosition());

        level.moveTank(corner, Direction.UP);
        level.update(SECONDS_PER_TILE);
        assertEquals(new Coordinates(9, 7), corner.getPosition());
    }

    @Test
    void ignoresCommandsWhileTankIsMoving() {
        gameLevel.moveTank(tank, Direction.RIGHT);

        gameLevel.moveTank(tank, Direction.DOWN);
        gameLevel.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(2, 1), tank.getPosition());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void tankCanNotDriveIntoAnotherTank() {
        Tank ai = tankAt(2, 1);
        GameLevel level = new GameLevel(10, 8, tank, Collections.singletonList(ai), Collections.emptyList());

        level.moveTank(tank, Direction.RIGHT);
        level.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(1, 1), tank.getPosition());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void movingTankKeepsBothItsTilesOccupied() {
        Tank ai = tankAt(5, 5);
        GameLevel level = new GameLevel(10, 8, tank, Collections.singletonList(ai), Collections.emptyList());

        level.moveTank(ai, Direction.UP);

        assertFalse(level.isFree(new Coordinates(5, 5)));
        assertFalse(level.isFree(new Coordinates(5, 6)));
    }

    @Test
    void noOtherTankMayEnterTileLeftByMovingTank() {
        Tank mover = tankAt(5, 5);
        Tank follower = tankAt(4, 5);
        GameLevel level = new GameLevel(10, 8, mover, Collections.singletonList(follower), Collections.emptyList());

        level.moveTank(mover, Direction.UP);
        level.moveTank(follower, Direction.RIGHT);
        level.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(5, 6), mover.getPosition());
        assertEquals(new Coordinates(4, 5), follower.getPosition());
    }

    @Test
    void noOtherTankMayEnterTileAimedAtByMovingTank() {
        Tank mover = tankAt(5, 5);
        Tank rival = tankAt(6, 6);
        GameLevel level = new GameLevel(10, 8, mover, Collections.singletonList(rival), Collections.emptyList());

        level.moveTank(mover, Direction.UP);
        level.moveTank(rival, Direction.LEFT);
        level.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(5, 6), mover.getPosition());
        assertEquals(new Coordinates(6, 6), rival.getPosition());
    }

    @Test
    void tileIsFreeAgainWhenTankHasLeftIt() {
        Tank mover = tankAt(5, 5);
        GameLevel level = new GameLevel(10, 8, mover, Collections.emptyList(), Collections.emptyList());

        level.moveTank(mover, Direction.UP);
        level.update(SECONDS_PER_TILE);

        assertTrue(level.isFree(new Coordinates(5, 5)));
        assertFalse(level.isFree(new Coordinates(5, 6)));
    }

    @Test
    void updatesEveryTank() {
        Tank ai = tankAt(5, 5);
        GameLevel level = new GameLevel(10, 8, tank, Collections.singletonList(ai), Collections.emptyList());

        level.moveTank(tank, Direction.RIGHT);
        level.moveTank(ai, Direction.UP);
        level.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(2, 1), tank.getPosition());
        assertEquals(new Coordinates(5, 6), ai.getPosition());
    }

    @Test
    void showsEveryTankAndObstacleAsObjectToDraw() {
        Tank ai = tankAt(5, 5);
        GameLevel level = new GameLevel(10, 8, tank, Collections.singletonList(ai),
                Collections.singletonList(new Tree(new Coordinates(3, 3))));

        assertEquals(Arrays.asList(tank, ai), level.getTanks());
        assertEquals(3, level.getObjects().size());
        assertTrue(level.getObjects().containsAll(Arrays.asList(tank, ai)));
    }

    private static class Wall implements GameObject {

        private final Coordinates position;

        Wall(Coordinates position) {
            this.position = position;
        }

        @Override
        public Coordinates getPosition() {
            return position;
        }

        @Override
        public Coordinates getDestination() {
            return position;
        }

        @Override
        public float getMovementProgress() {
            return 1f;
        }

        @Override
        public float getRotation() {
            return 0f;
        }

        @Override
        public boolean occupies(Coordinates tile) {
            return position.equals(tile);
        }
    }
}
