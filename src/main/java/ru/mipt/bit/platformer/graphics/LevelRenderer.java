package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

public class LevelRenderer implements Disposable {

    private final Batch batch;
    private final MapRenderer mapRenderer;
    private final Texture blueTankTexture;
    private final Texture greenTreeTexture;
    private final List<ObjectGraphics> objects = new ArrayList<>();

    public LevelRenderer(GameLevel gameLevel, TiledMap level, TiledMapTileLayer groundLayer) {
        batch = new SpriteBatch();
        mapRenderer = createSingleLayerMapRenderer(level, batch);
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        blueTankTexture = new Texture("images/tank_blue.png");
        objects.add(new TankGraphics(new TextureRegion(blueTankTexture), tileMovement, gameLevel.getPlayerTank()));

        greenTreeTexture = new Texture("images/greenTree.png");
        for (Tree tree : gameLevel.getTrees()) {
            objects.add(new TreeGraphics(new TextureRegion(greenTreeTexture), groundLayer, tree));
        }
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        mapRenderer.render();

        batch.begin();
        for (ObjectGraphics object : objects) {
            object.update();
            object.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        batch.dispose();
    }
}
