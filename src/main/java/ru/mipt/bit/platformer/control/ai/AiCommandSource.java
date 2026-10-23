package ru.mipt.bit.platformer.control.ai;

import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.CommandSource;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AiCommandSource implements CommandSource {

    private final List<Tank> tanks;
    private final TankStrategy strategy;

    public AiCommandSource(Collection<Tank> tanks, TankStrategy strategy) {
        this.tanks = Collections.unmodifiableList(new ArrayList<>(tanks));
        this.strategy = strategy;
    }

    @Override
    public List<Command> commands() {
        List<Command> commands = new ArrayList<>();
        for (Tank tank : tanks) {
            if (!tank.isMoving()) {
                commands.add(strategy.nextCommand(tank));
            }
        }
        return commands;
    }
}
