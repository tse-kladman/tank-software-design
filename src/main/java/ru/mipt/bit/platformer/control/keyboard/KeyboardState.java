package ru.mipt.bit.platformer.control.keyboard;

/** Port: which keys are pressed right now. */
public interface KeyboardState {

    boolean isKeyPressed(int keyCode);
}
