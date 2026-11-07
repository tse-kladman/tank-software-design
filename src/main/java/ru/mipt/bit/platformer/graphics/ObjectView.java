package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public interface ObjectView {

    void update();

    void draw(Batch batch);

    Rectangle getBounds();
}
