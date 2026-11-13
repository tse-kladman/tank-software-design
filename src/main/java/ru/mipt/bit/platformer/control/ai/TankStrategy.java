package ru.mipt.bit.platformer.control.ai;

import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.model.Tank;

/** Port: decides what an ai tank does next. */
public interface TankStrategy {

    Command nextCommand(Tank tank);
}
