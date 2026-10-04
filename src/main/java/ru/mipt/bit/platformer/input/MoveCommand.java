package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;

public class MoveCommand implements Command {

    private final GameLevel gameLevel;
    private final Direction direction;

    public MoveCommand(GameLevel gameLevel, Direction direction) {
        this.gameLevel = gameLevel;
        this.direction = direction;
    }

    @Override
    public void execute() {
        gameLevel.moveTank(direction);
    }
}
