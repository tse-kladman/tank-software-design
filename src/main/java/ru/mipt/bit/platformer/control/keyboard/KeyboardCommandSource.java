package ru.mipt.bit.platformer.control.keyboard;

import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.CommandSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KeyboardCommandSource implements CommandSource {

    private final KeyboardState keyboard;
    private final Map<Integer, Binding> bindings = new LinkedHashMap<>();
    private final Set<Integer> heldKeys = new HashSet<>();

    public KeyboardCommandSource(KeyboardState keyboard) {
        this.keyboard = keyboard;
    }

    public void bind(Command command, int... keyCodes) {
        addBinding(new Binding(command, true), keyCodes);
    }

    public void bindOnPress(Command command, int... keyCodes) {
        addBinding(new Binding(command, false), keyCodes);
    }

    @Override
    public List<Command> commands() {
        List<Command> pressed = new ArrayList<>();
        for (Map.Entry<Integer, Binding> entry : bindings.entrySet()) {
            int keyCode = entry.getKey();
            if (!keyboard.isKeyPressed(keyCode)) {
                heldKeys.remove(keyCode);
                continue;
            }
            Binding binding = entry.getValue();
            boolean justPressed = heldKeys.add(keyCode);
            if ((binding.whilePressed || justPressed) && !pressed.contains(binding.command)) {
                pressed.add(binding.command);
            }
        }
        return pressed;
    }

    private void addBinding(Binding binding, int... keyCodes) {
        for (int keyCode : keyCodes) {
            bindings.put(keyCode, binding);
        }
    }

    private static class Binding {

        private final Command command;
        private final boolean whilePressed;

        Binding(Command command, boolean whilePressed) {
            this.command = command;
            this.whilePressed = whilePressed;
        }
    }
}
