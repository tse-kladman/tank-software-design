package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyboardControllerTest {

    private static final int FIRST_KEY = 1;
    private static final int SECOND_KEY = 2;
    private static final int UNKNOWN_KEY = 3;

    private final List<String> executed = new ArrayList<>();

    private Command command(String name) {
        return () -> executed.add(name);
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
    void executesCommandBoundToPressedKey() {
        KeyboardController controller = new KeyboardController(new PressedKeys(FIRST_KEY));
        controller.bind(command("move"), FIRST_KEY);

        controller.handleInput();

        assertEquals(Arrays.asList("move"), executed);
    }

    @Test
    void executesSameCommandForEveryBoundKey() {
        KeyboardController controller = new KeyboardController(new PressedKeys(SECOND_KEY));
        controller.bind(command("move"), FIRST_KEY, SECOND_KEY);

        controller.handleInput();

        assertEquals(Arrays.asList("move"), executed);
    }

    @Test
    void executesNothingWhenNoBoundKeyIsPressed() {
        KeyboardController controller = new KeyboardController(new PressedKeys(UNKNOWN_KEY));
        controller.bind(command("move"), FIRST_KEY);

        controller.handleInput();

        assertTrue(executed.isEmpty());
    }

    @Test
    void executesEveryCommandWhoseKeyIsPressed() {
        KeyboardController controller = new KeyboardController(new PressedKeys(FIRST_KEY, SECOND_KEY));
        controller.bind(command("move"), FIRST_KEY);
        controller.bind(command("shoot"), SECOND_KEY);

        controller.handleInput();

        assertEquals(Arrays.asList("move", "shoot"), executed);
    }
}
