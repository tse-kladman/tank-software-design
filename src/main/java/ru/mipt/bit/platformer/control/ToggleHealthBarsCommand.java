package ru.mipt.bit.platformer.control;

public class ToggleHealthBarsCommand implements Command {

    private final HealthBarSwitch healthBars;

    public ToggleHealthBarsCommand(HealthBarSwitch healthBars) {
        this.healthBars = healthBars;
    }

    @Override
    public void execute() {
        healthBars.toggleHealthBars();
    }
}
