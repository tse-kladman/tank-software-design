package ru.mipt.bit.platformer.input.keyboard;

import ru.mipt.bit.platformer.input.Command;
import ru.mipt.bit.platformer.input.InputController;

import java.util.LinkedHashMap;
import java.util.Map;

public class KeyboardController implements InputController {

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

    @Override
    public void handleInput() {
        for (Map.Entry<Integer, Command> binding : commands.entrySet()) {
            if (keyboard.isKeyPressed(binding.getKey())) {
                binding.getValue().execute();
            }
        }
    }
}
