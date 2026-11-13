package ru.mipt.bit.platformer.model.state;

public class DamageStates implements TankStates {

    private static final float LIGHT_DAMAGE_HEALTH = 0.7f;
    private static final float MEDIUM_DAMAGE_HEALTH = 0.15f;

    private final TankState light = new LightDamageState();
    private final TankState medium = new MediumDamageState();
    private final TankState heavy = new HeavyDamageState();

    @Override
    public TankState stateFor(float healthLeft) {
        if (healthLeft > LIGHT_DAMAGE_HEALTH) {
            return light;
        }
        if (healthLeft >= MEDIUM_DAMAGE_HEALTH) {
            return medium;
        }
        return heavy;
    }
}
