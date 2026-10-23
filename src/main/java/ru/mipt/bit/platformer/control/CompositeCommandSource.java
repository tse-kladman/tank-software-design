package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CompositeCommandSource implements CommandSource {

    private final List<CommandSource> sources;

    public CompositeCommandSource(Collection<CommandSource> sources) {
        this.sources = Collections.unmodifiableList(new ArrayList<>(sources));
    }

    @Override
    public List<Command> commands() {
        List<Command> commands = new ArrayList<>();
        for (CommandSource source : sources) {
            commands.addAll(source.commands());
        }
        return commands;
    }
}
