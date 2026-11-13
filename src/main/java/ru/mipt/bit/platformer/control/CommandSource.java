package ru.mipt.bit.platformer.control;

import java.util.List;

/** Port: where the commands of one game cycle come from. */
public interface CommandSource {

    List<Command> commands();
}
