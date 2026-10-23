package ru.mipt.bit.platformer.model.level;

import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.List;

public class GameLevelFactory {

    private final float tankSecondsPerTile;
    private final Direction tankInitialDirection;

    public GameLevelFactory(float tankSecondsPerTile, Direction tankInitialDirection) {
        this.tankSecondsPerTile = tankSecondsPerTile;
        this.tankInitialDirection = tankInitialDirection;
    }

    public GameLevel create(LevelSource levelSource) {
        LevelLayout layout = levelSource.load();

        List<Tree> trees = new ArrayList<>();
        for (Coordinates position : layout.getTreePositions()) {
            trees.add(new Tree(position));
        }

        List<Tank> aiTanks = new ArrayList<>();
        for (Coordinates position : layout.getAiTankPositions()) {
            aiTanks.add(createTank(position));
        }

        return new GameLevel(layout.getWidth(), layout.getHeight(), createTank(layout.getPlayerTankPosition()),
                aiTanks, trees);
    }

    private Tank createTank(Coordinates position) {
        return new Tank(position, tankInitialDirection, tankSecondsPerTile);
    }
}
