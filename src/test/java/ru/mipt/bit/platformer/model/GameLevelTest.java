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

    @BeforeEach
    void createLevel() {
        tank = new Tank(new Coordinates(1, 1), Direction.RIGHT, SECONDS_PER_TILE);
        gameLevel = new GameLevel(10, 8, tank,
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
        GameLevel level = new GameLevel(10, 8, tank, Collections.singletonList(new Wall(new Coordinates(2, 2))));

        assertFalse(level.isFree(new Coordinates(2, 2)));
        assertTrue(level.isFree(new Coordinates(2, 3)));
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
    }

    @Test
    void movesTankToFreeTile() {
        gameLevel.moveTank(Direction.RIGHT);
        gameLevel.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(2, 1), tank.getPosition());
    }

    @Test
    void rotatesTankButKeepsItInPlaceWhenObstacleBlocksTheWay() {
        gameLevel.moveTank(Direction.UP);
        gameLevel.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(1, 1), tank.getPosition());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void doesNotLetTankLeaveTheLevel() {
        GameLevel smallLevel = new GameLevel(2, 2, tank, Collections.emptyList());

        smallLevel.moveTank(Direction.RIGHT);
        smallLevel.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(1, 1), tank.getPosition());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void ignoresCommandsWhileTankIsMoving() {
        gameLevel.moveTank(Direction.RIGHT);

        gameLevel.moveTank(Direction.DOWN);
        gameLevel.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(2, 1), tank.getPosition());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void showsTankAndObstaclesAsObjectsToDraw() {
        assertEquals(3, gameLevel.getObjects().size());
        assertTrue(gameLevel.getObjects().contains(tank));
        assertTrue(gameLevel.getObjects().containsAll(gameLevel.getObstacles()));
    }
}
