package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextureLibrary implements Textures, Disposable {

    private final Map<Class<? extends GameObject>, TextureRegion> regions = new HashMap<>();
    private final List<Texture> textures = new ArrayList<>();

    public void register(Class<? extends GameObject> type, String fileName) {
        Texture texture = new Texture(fileName);
        textures.add(texture);
        regions.put(type, new TextureRegion(texture));
    }

    @Override
    public TextureRegion textureFor(GameObject object) {
        TextureRegion region = regions.get(object.getClass());
        if (region == null) {
            throw new IllegalArgumentException("No texture registered for " + object.getClass().getName());
        }
        return region;
    }

    @Override
    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
    }
}
