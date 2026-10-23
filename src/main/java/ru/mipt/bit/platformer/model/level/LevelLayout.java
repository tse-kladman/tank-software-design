package ru.mipt.bit.platformer.model.level;

import ru.mipt.bit.platformer.model.Coordinates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LevelLayout {

    private final int width;
    private final int height;
    private final Coordinates playerTankPosition;
    private final List<Coordinates> aiTankPositions;
    private final List<Coordinates> treePositions;

    public LevelLayout(int width, int height, Coordinates playerTankPosition,
                       Collection<Coordinates> aiTankPositions, Collection<Coordinates> treePositions) {
        this.width = width;
        this.height = height;
        this.playerTankPosition = playerTankPosition;
        this.aiTankPositions = Collections.unmodifiableList(new ArrayList<>(aiTankPositions));
        this.treePositions = Collections.unmodifiableList(new ArrayList<>(treePositions));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Coordinates getPlayerTankPosition() {
        return playerTankPosition;
    }

    public List<Coordinates> getAiTankPositions() {
        return aiTankPositions;
    }

    public List<Coordinates> getTreePositions() {
        return treePositions;
    }
}
