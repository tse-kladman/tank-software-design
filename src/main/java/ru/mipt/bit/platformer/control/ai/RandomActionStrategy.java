package ru.mipt.bit.platformer.control.ai;

import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.control.ShootCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Random;

public class RandomActionStrategy implements TankStrategy {

    private static final Direction[] DIRECTIONS = Direction.values();

    private final GameLevel gameLevel;
    private final Random random;
    private final float shootProbability;

    public RandomActionStrategy(GameLevel gameLevel, Random random, float shootProbability) {
        this.gameLevel = gameLevel;
        this.random = random;
        this.shootProbability = shootProbability;
    }

    @Override
    public Command nextCommand(Tank tank) {
        if (random.nextFloat() < shootProbability) {
            return new ShootCommand(tank);
        }
        return new MoveCommand(gameLevel, tank, DIRECTIONS[random.nextInt(DIRECTIONS.length)]);
    }
}
