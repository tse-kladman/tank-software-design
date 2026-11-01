package ru.mipt.bit.platformer.model.level;

import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.GameLevelListener;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.TankSettings;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Collection;
import java.util.Collections;

public class GameLevelFactory {

    private final TankSettings tankSettings;
    private final Direction tankInitialDirection;

    public GameLevelFactory(TankSettings tankSettings, Direction tankInitialDirection) {
        this.tankSettings = tankSettings;
        this.tankInitialDirection = tankInitialDirection;
    }

    public GameLevel create(LevelSource levelSource) {
        return create(levelSource, Collections.<GameLevelListener>emptyList());
    }

    public GameLevel create(LevelSource levelSource, Collection<GameLevelListener> listeners) {
        LevelLayout layout = levelSource.load();
        GameLevel gameLevel = new GameLevel(layout.getWidth(), layout.getHeight());
        for (GameLevelListener listener : listeners) {
            gameLevel.addListener(listener);
        }

        gameLevel.addPlayerTank(createTank(gameLevel, layout.getPlayerTankPosition()));
        for (Coordinates position : layout.getAiTankPositions()) {
            gameLevel.addTank(createTank(gameLevel, position));
        }
        for (Coordinates position : layout.getTreePositions()) {
            gameLevel.addObstacle(new Tree(position));
        }
        return gameLevel;
    }

    private Tank createTank(GameLevel gameLevel, Coordinates position) {
        return new Tank(position, tankInitialDirection, tankSettings, gameLevel);
    }
}
