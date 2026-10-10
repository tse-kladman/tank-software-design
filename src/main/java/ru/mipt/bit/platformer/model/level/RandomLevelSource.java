package ru.mipt.bit.platformer.model.level;

import ru.mipt.bit.platformer.model.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomLevelSource implements LevelSource {

    private final int width;
    private final int height;
    private final int treesCount;
    private final Random random;

    public RandomLevelSource(int width, int height, int treesCount, Random random) {
        this.width = width;
        this.height = height;
        this.treesCount = treesCount;
        this.random = random;
    }

    @Override
    public LevelLayout load() {
        List<Coordinates> tiles = allTiles();
        if (treesCount + 1 > tiles.size()) {
            throw new IllegalArgumentException("Level of " + width + "x" + height
                    + " tiles has no room for " + treesCount + " trees and a tank");
        }
        Collections.shuffle(tiles, random);

        Coordinates tankPosition = tiles.get(0);
        List<Coordinates> trees = new ArrayList<>(tiles.subList(1, treesCount + 1));
        return new LevelLayout(width, height, tankPosition, trees);
    }

    private List<Coordinates> allTiles() {
        List<Coordinates> tiles = new ArrayList<>(width * height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles.add(new Coordinates(x, y));
            }
        }
        return tiles;
    }
}
