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
import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.CommandSource;
import ru.mipt.bit.platformer.control.CompositeCommandSource;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.control.ShootCommand;
import ru.mipt.bit.platformer.control.ai.AiCommandSource;
import ru.mipt.bit.platformer.control.ai.RandomActionStrategy;
import ru.mipt.bit.platformer.control.keyboard.GdxKeyboardState;
import ru.mipt.bit.platformer.control.keyboard.KeyboardCommandSource;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.graphics.TextureLibrary;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.BulletSettings;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.GameLevelListener;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TankSettings;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.model.level.GameLevelFactory;
import ru.mipt.bit.platformer.model.level.LevelSource;
import ru.mipt.bit.platformer.model.level.RandomLevelSource;
import ru.mipt.bit.platformer.model.level.TextLevelSource;
import ru.mipt.bit.platformer.util.GdxTextFiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String TILED_MAP_FILE = "level.tmx";
    private static final String LEVEL_LAYOUT_FILE = "level.txt";
    private static final String RANDOM_LEVEL_ARGUMENT = "random";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final String BULLET_TEXTURE = "images/bullet.png";
    private static final Color BACKGROUND = new Color(0f, 0f, 0.2f, 1f);
    private static final float TANK_SECONDS_PER_TILE = 0.4f;
    private static final int TANK_HEALTH = 3;
    private static final float TANK_RELOAD_SECONDS = 0.7f;
    private static final float BULLET_SECONDS_PER_TILE = 0.12f;
    private static final int BULLET_DAMAGE = 1;
    private static final float AI_SHOOT_PROBABILITY = 0.02f;
    private static final Direction TANK_INITIAL_DIRECTION = Direction.RIGHT;
    private static final int RANDOM_TREES_COUNT = 12;
    private static final int RANDOM_AI_TANKS_COUNT = 4;
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private final boolean randomLevel;

    private TiledMap tiledMap;
    private GameLevel gameLevel;
    private TextureLibrary textures;
    private LevelRenderer levelRenderer;
    private CommandSource commandSource;

    public GameDesktopLauncher(boolean randomLevel) {
        this.randomLevel = randomLevel;
    }

    @Override
    public void create() {
        // load level tiles
        tiledMap = new TmxMapLoader().load(TILED_MAP_FILE);
        TiledMapTileLayer groundLayer = getSingleLayer(tiledMap);

        textures = new TextureLibrary();
        textures.register(Tank.class, TANK_TEXTURE);
        textures.register(Tree.class, TREE_TEXTURE);
        textures.register(Bullet.class, BULLET_TEXTURE);

        levelRenderer = new LevelRenderer(tiledMap, groundLayer, textures, BACKGROUND);

        GameLevelFactory levelFactory = new GameLevelFactory(createTankSettings(), TANK_INITIAL_DIRECTION);
        List<GameLevelListener> listeners = Collections.<GameLevelListener>singletonList(levelRenderer);
        gameLevel = levelFactory.create(createLevelSource(groundLayer.getWidth(), groundLayer.getHeight()), listeners);

        commandSource = createCommandSource();
    }

    private TankSettings createTankSettings() {
        return new TankSettings(TANK_SECONDS_PER_TILE, TANK_HEALTH, TANK_RELOAD_SECONDS,
                new BulletSettings(BULLET_SECONDS_PER_TILE, BULLET_DAMAGE));
    }

    private LevelSource createLevelSource(int width, int height) {
        if (randomLevel) {
            return new RandomLevelSource(width, height, RANDOM_TREES_COUNT, RANDOM_AI_TANKS_COUNT, new Random());
        }
        return new TextLevelSource(GdxTextFiles.readLines(LEVEL_LAYOUT_FILE));
    }

    private CommandSource createCommandSource() {
        return new CompositeCommandSource(Arrays.asList(createKeyboardSource(), createAiSource()));
    }

    private CommandSource createKeyboardSource() {
        Tank playerTank = gameLevel.getPlayerTank();
        KeyboardCommandSource keyboard = new KeyboardCommandSource(new GdxKeyboardState());
        keyboard.bind(new MoveCommand(gameLevel, playerTank, Direction.UP), Keys.UP, Keys.W);
        keyboard.bind(new MoveCommand(gameLevel, playerTank, Direction.LEFT), Keys.LEFT, Keys.A);
        keyboard.bind(new MoveCommand(gameLevel, playerTank, Direction.DOWN), Keys.DOWN, Keys.S);
        keyboard.bind(new MoveCommand(gameLevel, playerTank, Direction.RIGHT), Keys.RIGHT, Keys.D);
        keyboard.bind(new ShootCommand(playerTank), Keys.SPACE);
        return keyboard;
    }

    private CommandSource createAiSource() {
        return new AiCommandSource(gameLevel,
                new RandomActionStrategy(gameLevel, new Random(), AI_SHOOT_PROBABILITY));
    }

    @Override
    public void render() {
        List<Command> commands = commandSource.commands();
        for (Command command : commands) {
            command.execute();
        }
        gameLevel.live(Gdx.graphics.getDeltaTime());
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
