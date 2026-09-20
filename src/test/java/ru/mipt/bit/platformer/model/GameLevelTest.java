package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameLevelTest {

    private Tank tank;
    private GameLevel gameLevel;

    @BeforeEach
    void createLevel() {
        tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        gameLevel = new GameLevel(10, 8, tank,
                Arrays.asList(new Tree(new GridPoint2(1, 2)), new Tree(new GridPoint2(5, 5))));
    }

    @Test
    void emptyTileIsFree() {
        assertTrue(gameLevel.isFree(new GridPoint2(4, 4)));
    }

    @Test
    void tileWithTreeIsNotFree() {
        assertFalse(gameLevel.isFree(new GridPoint2(1, 2)));
        assertFalse(gameLevel.isFree(new GridPoint2(5, 5)));
    }

    @Test
    void tileOutsideOfLevelIsNotFree() {
        assertFalse(gameLevel.isFree(new GridPoint2(-1, 3)));
        assertFalse(gameLevel.isFree(new GridPoint2(3, -1)));
        assertFalse(gameLevel.isFree(new GridPoint2(10, 3)));
        assertFalse(gameLevel.isFree(new GridPoint2(3, 8)));
    }

    @Test
    void movesTankToFreeTile() {
        gameLevel.moveTank(Direction.RIGHT);
        gameLevel.update(0.4f);

        assertEquals(new GridPoint2(2, 1), tank.getPosition());
    }

    @Test
    void rotatesTankButKeepsItInPlaceWhenTreeBlocksTheWay() {
        gameLevel.moveTank(Direction.UP);
        gameLevel.update(0.4f);

        assertEquals(new GridPoint2(1, 1), tank.getPosition());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void doesNotLetTankLeaveTheLevel() {
        GameLevel smallLevel = new GameLevel(2, 2, tank, Collections.emptyList());

        smallLevel.moveTank(Direction.RIGHT);
        smallLevel.update(0.4f);

        assertEquals(new GridPoint2(1, 1), tank.getPosition());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void ignoresCommandsWhileTankIsMoving() {
        gameLevel.moveTank(Direction.RIGHT);

        gameLevel.moveTank(Direction.DOWN);
        gameLevel.update(0.4f);

        assertEquals(new GridPoint2(2, 1), tank.getPosition());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }
}
