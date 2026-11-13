package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.mipt.bit.platformer.model.GameObject;

/** Port: a texture for a game object. */
public interface Textures {

    TextureRegion textureFor(GameObject object);
}
