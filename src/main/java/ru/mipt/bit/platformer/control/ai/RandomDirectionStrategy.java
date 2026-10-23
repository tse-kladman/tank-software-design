package ru.mipt.bit.platformer.control.ai;

import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Random;

public class RandomDirectionStrategy implements TankStrategy {

    private static final Direction[] DIRECTIONS = Direction.values();

    private final GameLevel gameLevel;
    private final Random random;

    public RandomDirectionStrategy(GameLevel gameLevel, Random random) {
        this.gameLevel = gameLevel;
        this.random = random;
    }

    @Override
    public Command nextCommand(Tank tank) {
        return new MoveCommand(gameLevel, tank, DIRECTIONS[random.nextInt(DIRECTIONS.length)]);
    }
}
