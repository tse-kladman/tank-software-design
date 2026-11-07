package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToggleHealthBarsCommandTest {

    private int toggles;

    @Test
    void togglesHealthBarsOnEveryExecution() {
        HealthBarSwitch healthBars = () -> toggles++;
        Command command = new ToggleHealthBarsCommand(healthBars);

        command.execute();
        command.execute();

        assertEquals(2, toggles);
    }
}
