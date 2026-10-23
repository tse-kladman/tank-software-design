package ru.mipt.bit.platformer.control;

import java.util.List;

public interface CommandSource {

    List<Command> commands();
}
