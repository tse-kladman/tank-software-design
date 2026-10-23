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
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

public class LevelRenderer implements Disposable {

    private final Batch batch;
    private final MapRenderer mapRenderer;
    private final Color background;
    private final List<ObjectGraphics> graphics = new ArrayList<>();

    public LevelRenderer(GameLevel gameLevel, TiledMap level, TiledMapTileLayer groundLayer, Textures textures,
                         Color background) {
        this.batch = new SpriteBatch();
        this.mapRenderer = createSingleLayerMapRenderer(level, batch);
        this.background = background;

        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        for (GameObject object : gameLevel.getObjects()) {
            graphics.add(new ObjectGraphics(object, textures.textureFor(object), tileMovement));
        }
    }

    public void render() {
        Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        mapRenderer.render();

        batch.begin();
        for (ObjectGraphics object : graphics) {
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
