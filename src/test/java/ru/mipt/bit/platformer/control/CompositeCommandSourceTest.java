package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompositeCommandSourceTest {

    private final List<String> executed = new ArrayList<>();

    private CommandSource source(String... names) {
        List<Command> commands = new ArrayList<>();
        for (String name : names) {
            commands.add(() -> executed.add(name));
        }
        return () -> commands;
    }

    @Test
    void collectsCommandsOfEverySourceKeepingTheirOrder() {
        CompositeCommandSource composite = new CompositeCommandSource(
                Arrays.asList(source("player"), source("first ai", "second ai")));

        for (Command command : composite.commands()) {
            command.execute();
        }

        assertEquals(Arrays.asList("player", "first ai", "second ai"), executed);
    }

    @Test
    void returnsNothingWhenThereAreNoSources() {
        assertTrue(new CompositeCommandSource(Collections.emptyList()).commands().isEmpty());
    }
}
