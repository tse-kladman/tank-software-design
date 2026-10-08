package ru.mipt.bit.platformer.model.level;

import ru.mipt.bit.platformer.model.Coordinates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LevelLayout {

    private final int width;
    private final int height;
    private final Coordinates tankPosition;
    private final List<Coordinates> treePositions;

    public LevelLayout(int width, int height, Coordinates tankPosition, Collection<Coordinates> treePositions) {
        this.width = width;
        this.height = height;
        this.tankPosition = tankPosition;
        this.treePositions = Collections.unmodifiableList(new ArrayList<>(treePositions));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Coordinates getTankPosition() {
        return tankPosition;
    }

    public List<Coordinates> getTreePositions() {
        return treePositions;
    }
}
