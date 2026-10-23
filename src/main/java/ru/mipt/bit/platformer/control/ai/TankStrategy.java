package ru.mipt.bit.platformer.control.ai;

import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.model.Tank;

public interface TankStrategy {

    Command nextCommand(Tank tank);
}
