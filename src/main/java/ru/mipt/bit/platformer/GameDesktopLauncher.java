package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.entity.Tank;
import ru.mipt.bit.platformer.entity.obstacle.Tree;
import ru.mipt.bit.platformer.movement.Direction;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;

    // Disposables
    private Texture blueTankTexture;
    private Texture greenTreeTexture;

    private Tank player;
    private Tree tree;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);

        // Create game objects
        blueTankTexture = new Texture("images/tank_blue.png");
        player = new Tank(groundLayer, new TextureRegion(blueTankTexture), new GridPoint2(1, 1));

        greenTreeTexture = new Texture("images/greenTree.png");
        tree = new Tree(groundLayer, new TextureRegion(greenTreeTexture), new GridPoint2(1, 3));
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        processInput();

        // get time passed since the last render and update player
        float deltaTime = Gdx.graphics.getDeltaTime();
        player.update(deltaTime);

        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();
        // render game objects
        player.draw(batch);
        tree.draw(batch);
        // submit all drawing requests
        batch.end();
    }

    private void processInput() {
        if (player.isMoving()) {
            return;
        }
        Direction direction = getPressedDirection();
        if (direction == null) {
            return;
        }

        player.rotate(direction);
        // check potential player destination for collision with obstacles
        GridPoint2 nextPosition = direction.shift(player.getPosition());
        if (!nextPosition.equals(tree.getPosition())) {
            player.move(direction);
        }
    }

    private Direction getPressedDirection() {
        for (Direction direction : Direction.values()) {
            for (int key : direction.getKeys()) {
                if (Gdx.input.isKeyPressed(key)) {
                    // A move has been initiated, no need to check other keys in this frame
                    return direction;
                }
            }
        }
        return null;
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
