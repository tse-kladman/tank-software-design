package ru.mipt.bit.platformer.model.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DamageStatesTest {

    private final TankStates states = new DamageStates();

    @Test
    void undamagedTankKeepsSpeedAndFire() {
        TankState state = states.stateFor(1f);

        assertEquals(1f, state.getSecondsPerTileFactor());
        assertEquals(1f, state.getReloadFactor());
        assertTrue(state.canShoot());
    }

    @Test
    void moderatelyDamagedTankMovesTwiceSlower() {
        assertEquals(2f, states.stateFor(0.7f).getSecondsPerTileFactor());
        assertEquals(2f, states.stateFor(0.5f).getSecondsPerTileFactor());
        assertEquals(2f, states.stateFor(0.15f).getSecondsPerTileFactor());
        assertTrue(states.stateFor(0.5f).canShoot());
    }

    @Test
    void heavilyDamagedTankMovesThreeTimesSlowerAndHoldsFire() {
        TankState state = states.stateFor(0.1f);

        assertEquals(3f, state.getSecondsPerTileFactor());
        assertFalse(state.canShoot());
    }
}
