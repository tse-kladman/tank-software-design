package ru.mipt.bit.platformer.model.state;

public interface TankStates {

    TankState stateFor(float healthLeft);
}
