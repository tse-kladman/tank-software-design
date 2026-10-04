package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.graphics.TextureLibrary;
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.input.MoveCommand;
import ru.mipt.bit.platformer.input.keyboard.GdxKeyboardState;
import ru.mipt.bit.platformer.input.keyboard.KeyboardController;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Collections;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_FILE = "level.tmx";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final Color BACKGROUND = new Color(0f, 0f, 0.2f, 1f);
    private static final float TANK_SECONDS_PER_TILE = 0.4f;
    private static final Coordinates TANK_SPAWN = new Coordinates(1, 1);
    private static final Coordinates TREE_TILE = new Coordinates(1, 3);
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private TiledMap level;
    private GameLevel gameLevel;
    private TextureLibrary textures;
    private LevelRenderer levelRenderer;
    private InputController controller;

    @Override
    public void create() {
        // load level tiles
        level = new TmxMapLoader().load(LEVEL_FILE);
        TiledMapTileLayer groundLayer = getSingleLayer(level);

        gameLevel = new GameLevel(groundLayer.getWidth(), groundLayer.getHeight(),
                new Tank(TANK_SPAWN, Direction.RIGHT, TANK_SECONDS_PER_TILE),
                Collections.singletonList(new Tree(TREE_TILE)));

        textures = new TextureLibrary();
        textures.register(Tank.class, TANK_TEXTURE);
        textures.register(Tree.class, TREE_TEXTURE);

        levelRenderer = new LevelRenderer(gameLevel, level, groundLayer, textures, BACKGROUND);
        controller = createController();
    }

    private InputController createController() {
        KeyboardController keyboardController = new KeyboardController(new GdxKeyboardState());
        keyboardController.bind(new MoveCommand(gameLevel, Direction.UP), Keys.UP, Keys.W);
        keyboardController.bind(new MoveCommand(gameLevel, Direction.LEFT), Keys.LEFT, Keys.A);
        keyboardController.bind(new MoveCommand(gameLevel, Direction.DOWN), Keys.DOWN, Keys.S);
        keyboardController.bind(new MoveCommand(gameLevel, Direction.RIGHT), Keys.RIGHT, Keys.D);
        return keyboardController;
    }

    @Override
    public void render() {
        controller.handleInput();
        gameLevel.update(Gdx.graphics.getDeltaTime());
        levelRenderer.render();
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
        levelRenderer.dispose();
        textures.dispose();
        level.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
