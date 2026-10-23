package ru.mipt.bit.platformer.control.keyboard;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.control.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyboardCommandSourceTest {

    private static final int FIRST_KEY = 1;
    private static final int SECOND_KEY = 2;
    private static final int UNKNOWN_KEY = 3;

    private final List<String> executed = new ArrayList<>();

    private Command command(String name) {
        return () -> executed.add(name);
    }

    private void executeAll(List<Command> commands) {
        for (Command command : commands) {
            command.execute();
        }
    }

    private static class PressedKeys implements KeyboardState {

        private final Set<Integer> pressed;

        PressedKeys(Integer... keyCodes) {
            this.pressed = new HashSet<>(Arrays.asList(keyCodes));
        }

        @Override
        public boolean isKeyPressed(int keyCode) {
            return pressed.contains(keyCode);
        }
    }

    @Test
    void returnsCommandBoundToPressedKey() {
        KeyboardCommandSource source = new KeyboardCommandSource(new PressedKeys(FIRST_KEY));
        source.bind(command("move"), FIRST_KEY);

        executeAll(source.commands());

        assertEquals(Arrays.asList("move"), executed);
    }

    @Test
    void returnsSameCommandOnceWhenSeveralOfItsKeysArePressed() {
        KeyboardCommandSource source = new KeyboardCommandSource(new PressedKeys(FIRST_KEY, SECOND_KEY));
        source.bind(command("move"), FIRST_KEY, SECOND_KEY);

        assertEquals(1, source.commands().size());
    }

    @Test
    void returnsNothingWhenNoBoundKeyIsPressed() {
        KeyboardCommandSource source = new KeyboardCommandSource(new PressedKeys(UNKNOWN_KEY));
        source.bind(command("move"), FIRST_KEY);

        assertTrue(source.commands().isEmpty());
    }

    @Test
    void returnsEveryCommandWhoseKeyIsPressed() {
        KeyboardCommandSource source = new KeyboardCommandSource(new PressedKeys(FIRST_KEY, SECOND_KEY));
        source.bind(command("move"), FIRST_KEY);
        source.bind(command("shoot"), SECOND_KEY);

        executeAll(source.commands());

        assertEquals(Arrays.asList("move", "shoot"), executed);
    }
}
