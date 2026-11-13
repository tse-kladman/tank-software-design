package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.control.HealthBarSwitch;
import ru.mipt.bit.platformer.model.GameLevelListener;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

/** Adapter (infrastructure): draws the level with libGDX, follows it as {@link GameLevelListener}. */
public class LevelRenderer implements GameLevelListener, HealthBarSwitch, Disposable {

    private final Batch batch;
    private final MapRenderer mapRenderer;
    private final TileMovement tileMovement;
    private final Textures textures;
    private final Color background;
    private final Texture barTexture;
    private final TextureRegion bar;
    private final Map<GameObject, ObjectView> views = new LinkedHashMap<>();

    private boolean healthBarsShown;

    public LevelRenderer(TiledMap level, TiledMapTileLayer groundLayer, Textures textures, Color background) {
        this.batch = new SpriteBatch();
        this.mapRenderer = createSingleLayerMapRenderer(level, batch);
        this.tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        this.textures = textures;
        this.background = background;
        this.barTexture = createBarTexture();
        this.bar = new TextureRegion(barTexture);
    }

    @Override
    public void objectAdded(GameObject object) {
        views.put(object, createView(object));
    }

    @Override
    public void objectRemoved(GameObject object) {
        views.remove(object);
    }

    @Override
    public void toggleHealthBars() {
        healthBarsShown = !healthBarsShown;
        for (Map.Entry<GameObject, ObjectView> view : views.entrySet()) {
            view.setValue(createView(view.getKey()));
        }
    }

    public void render() {
        Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        mapRenderer.render();

        batch.begin();
        for (ObjectView object : views.values()) {
            object.update();
            object.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        barTexture.dispose();
        batch.dispose();
    }

    private ObjectView createView(GameObject object) {
        ObjectView view = new ObjectGraphics(object, textures.textureFor(object), tileMovement);
        if (healthBarsShown && object.getMaxHealth() > 0) {
            return new HealthBarGraphics(view, object, bar);
        }
        return view;
    }

    private static Texture createBarTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
