package ru.mipt.bit.platformer.control.keyboard;

import com.badlogic.gdx.Gdx;

/** Adapter (infrastructure): libGDX input behind {@link KeyboardState}. */
public class GdxKeyboardState implements KeyboardState {

    @Override
    public boolean isKeyPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }
}
