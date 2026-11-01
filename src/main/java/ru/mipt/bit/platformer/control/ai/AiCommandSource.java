package ru.mipt.bit.platformer.control.ai;

import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.CommandSource;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;

public class AiCommandSource implements CommandSource {

    private final GameLevel gameLevel;
    private final TankStrategy strategy;

    public AiCommandSource(GameLevel gameLevel, TankStrategy strategy) {
        this.gameLevel = gameLevel;
        this.strategy = strategy;
    }

    @Override
    public List<Command> commands() {
        List<Command> commands = new ArrayList<>();
        for (Tank tank : gameLevel.getAiTanks()) {
            commands.add(strategy.nextCommand(tank));
        }
        return commands;
    }
}
