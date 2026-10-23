package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveCommandTest {

    private static final float SECONDS_PER_TILE = 0.4f;

    @Test
    void movesItsOwnTankInItsOwnDirection() {
        Tank playerTank = new Tank(new Coordinates(1, 1), Direction.RIGHT, SECONDS_PER_TILE);
        Tank aiTank = new Tank(new Coordinates(5, 5), Direction.RIGHT, SECONDS_PER_TILE);
        GameLevel gameLevel = new GameLevel(10, 8, playerTank, Collections.singletonList(aiTank),
                Collections.emptyList());

        new MoveCommand(gameLevel, aiTank, Direction.UP).execute();
        gameLevel.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(5, 6), aiTank.getPosition());
        assertEquals(new Coordinates(1, 1), playerTank.getPosition());
    }

    @Test
    void doesNothingWhenTargetTileIsTakenByAnotherTank() {
        Tank playerTank = new Tank(new Coordinates(1, 1), Direction.RIGHT, SECONDS_PER_TILE);
        Tank aiTank = new Tank(new Coordinates(2, 1), Direction.RIGHT, SECONDS_PER_TILE);
        GameLevel gameLevel = new GameLevel(10, 8, playerTank, Collections.singletonList(aiTank),
                Collections.emptyList());

        new MoveCommand(gameLevel, playerTank, Direction.RIGHT).execute();
        gameLevel.update(SECONDS_PER_TILE);

        assertEquals(new Coordinates(1, 1), playerTank.getPosition());
    }
}
