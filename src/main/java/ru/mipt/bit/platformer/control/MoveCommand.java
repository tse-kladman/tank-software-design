package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;

public class MoveCommand implements Command {

    private final GameLevel gameLevel;
    private final Tank tank;
    private final Direction direction;

    public MoveCommand(GameLevel gameLevel, Tank tank, Direction direction) {
        this.gameLevel = gameLevel;
        this.tank = tank;
        this.direction = direction;
    }

    @Override
    public void execute() {
        gameLevel.moveTank(tank, direction);
    }
}
