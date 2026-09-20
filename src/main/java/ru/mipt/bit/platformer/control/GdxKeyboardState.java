package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;

public class GdxKeyboardState implements KeyboardState {

    @Override
    public boolean isKeyPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }
}
