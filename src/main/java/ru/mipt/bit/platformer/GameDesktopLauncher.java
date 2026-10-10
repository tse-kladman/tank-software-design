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
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.model.level.GameLevelFactory;
import ru.mipt.bit.platformer.model.level.LevelSource;
import ru.mipt.bit.platformer.model.level.RandomLevelSource;
import ru.mipt.bit.platformer.model.level.TextLevelSource;
import ru.mipt.bit.platformer.util.GdxTextFiles;

import java.util.Arrays;
import java.util.Random;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String TILED_MAP_FILE = "level.tmx";
    private static final String LEVEL_LAYOUT_FILE = "level.txt";
    private static final String RANDOM_LEVEL_ARGUMENT = "random";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final Color BACKGROUND = new Color(0f, 0f, 0.2f, 1f);
    private static final float TANK_SECONDS_PER_TILE = 0.4f;
    private static final Direction TANK_INITIAL_DIRECTION = Direction.RIGHT;
    private static final int RANDOM_TREES_COUNT = 12;
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private final boolean randomLevel;

    private TiledMap tiledMap;
    private GameLevel gameLevel;
    private TextureLibrary textures;
    private LevelRenderer levelRenderer;
    private InputController controller;

    public GameDesktopLauncher(boolean randomLevel) {
        this.randomLevel = randomLevel;
    }

    @Override
    public void create() {
        // load level tiles
        tiledMap = new TmxMapLoader().load(TILED_MAP_FILE);
        TiledMapTileLayer groundLayer = getSingleLayer(tiledMap);

        GameLevelFactory levelFactory = new GameLevelFactory(TANK_SECONDS_PER_TILE, TANK_INITIAL_DIRECTION);
        gameLevel = levelFactory.create(createLevelSource(groundLayer.getWidth(), groundLayer.getHeight()));

        textures = new TextureLibrary();
        textures.register(Tank.class, TANK_TEXTURE);
        textures.register(Tree.class, TREE_TEXTURE);

        levelRenderer = new LevelRenderer(gameLevel, tiledMap, groundLayer, textures, BACKGROUND);
        controller = createController();
    }

    private LevelSource createLevelSource(int width, int height) {
        if (randomLevel) {
            return new RandomLevelSource(width, height, RANDOM_TREES_COUNT, new Random());
        }
        return new TextLevelSource(GdxTextFiles.readLines(LEVEL_LAYOUT_FILE));
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
        tiledMap.dispose();
    }

    public static void main(String[] args) {
        boolean randomLevel = Arrays.asList(args).contains(RANDOM_LEVEL_ARGUMENT);
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        new Lwjgl3Application(new GameDesktopLauncher(randomLevel), config);
    }
}
