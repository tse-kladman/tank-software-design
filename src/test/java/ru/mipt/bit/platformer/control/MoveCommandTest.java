package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveCommandTest {

    @Test
    void movesTankInItsOwnDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT);
        GameLevel gameLevel = new GameLevel(10, 8, tank, Collections.emptyList());

        new MoveCommand(gameLevel, Direction.UP).execute();
        gameLevel.update(0.4f);

        assertEquals(new GridPoint2(1, 2), tank.getPosition());
    }
}
