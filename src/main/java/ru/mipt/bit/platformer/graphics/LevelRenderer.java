package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameLevelListener;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

public class LevelRenderer implements GameLevelListener, Disposable {

    private final Batch batch;
    private final MapRenderer mapRenderer;
    private final TileMovement tileMovement;
    private final Textures textures;
    private final Color background;
    private final Map<GameObject, ObjectGraphics> graphics = new LinkedHashMap<>();

    public LevelRenderer(TiledMap level, TiledMapTileLayer groundLayer, Textures textures, Color background) {
        this.batch = new SpriteBatch();
        this.mapRenderer = createSingleLayerMapRenderer(level, batch);
        this.tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        this.textures = textures;
        this.background = background;
    }

    @Override
    public void objectAdded(GameObject object) {
        graphics.put(object, new ObjectGraphics(object, textures.textureFor(object), tileMovement));
    }

    @Override
    public void objectRemoved(GameObject object) {
        graphics.remove(object);
    }

    public void render() {
        Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        mapRenderer.render();

        batch.begin();
        for (ObjectGraphics object : graphics.values()) {
            object.update();
            object.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
