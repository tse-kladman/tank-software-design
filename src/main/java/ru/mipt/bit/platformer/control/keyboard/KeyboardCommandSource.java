package ru.mipt.bit.platformer.control.keyboard;

import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.CommandSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KeyboardCommandSource implements CommandSource {

    private final KeyboardState keyboard;
    private final Map<Integer, Command> bindings = new LinkedHashMap<>();

    public KeyboardCommandSource(KeyboardState keyboard) {
        this.keyboard = keyboard;
    }

    public void bind(Command command, int... keyCodes) {
        for (int keyCode : keyCodes) {
            bindings.put(keyCode, command);
        }
    }

    @Override
    public List<Command> commands() {
        List<Command> pressed = new ArrayList<>();
        for (Map.Entry<Integer, Command> binding : bindings.entrySet()) {
            if (keyboard.isKeyPressed(binding.getKey()) && !pressed.contains(binding.getValue())) {
                pressed.add(binding.getValue());
            }
        }
        return pressed;
    }
}
