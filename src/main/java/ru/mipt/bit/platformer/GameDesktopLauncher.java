package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.GdxKeyboardState;
import ru.mipt.bit.platformer.control.KeyboardController;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Collections;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameDesktopLauncher implements ApplicationListener {

    private TiledMap level;
    private GameLevel gameLevel;
    private LevelRenderer levelRenderer;
    private KeyboardController controller;

    @Override
    public void create() {
        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        TiledMapTileLayer groundLayer = getSingleLayer(level);

        gameLevel = new GameLevel(groundLayer.getWidth(), groundLayer.getHeight(),
                new Tank(new GridPoint2(1, 1), Direction.RIGHT),
                Collections.singletonList(new Tree(new GridPoint2(1, 3))));

        levelRenderer = new LevelRenderer(gameLevel, level, groundLayer);
        controller = createController();
    }

    private KeyboardController createController() {
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
        level.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
