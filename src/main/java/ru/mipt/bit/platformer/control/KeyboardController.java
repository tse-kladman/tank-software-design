package ru.mipt.bit.platformer.control;

import java.util.LinkedHashMap;
import java.util.Map;

public class KeyboardController {

    private final KeyboardState keyboard;
    private final Map<Integer, Command> commands = new LinkedHashMap<>();

    public KeyboardController(KeyboardState keyboard) {
        this.keyboard = keyboard;
    }

    public void bind(Command command, int... keyCodes) {
        for (int keyCode : keyCodes) {
            commands.put(keyCode, command);
        }
    }

    public void handleInput() {
        for (Map.Entry<Integer, Command> binding : commands.entrySet()) {
            if (keyboard.isKeyPressed(binding.getKey())) {
                binding.getValue().execute();
            }
        }
    }
}
