package ru.mipt.bit.platformer.model.level;

import ru.mipt.bit.platformer.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class TextLevelSource implements LevelSource {

    private static final char TREE = 'T';
    private static final char TANK = 'X';
    private static final char EMPTY = '_';

    private final List<String> lines;

    public TextLevelSource(List<String> lines) {
        this.lines = new ArrayList<>(lines);
    }

    @Override
    public LevelLayout load() {
        List<String> rows = meaningfulRows();
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Level description is empty");
        }

        int height = rows.size();
        int width = rows.get(0).length();
        List<Coordinates> trees = new ArrayList<>();
        Coordinates tankPosition = null;

        for (int row = 0; row < height; row++) {
            String line = rows.get(row);
            if (line.length() != width) {
                throw new IllegalArgumentException("Level rows have different length: " + width + " and "
                        + line.length());
            }
            for (int column = 0; column < width; column++) {
                Coordinates tile = new Coordinates(column, height - 1 - row);
                switch (line.charAt(column)) {
                    case TREE:
                        trees.add(tile);
                        break;
                    case TANK:
                        if (tankPosition != null) {
                            throw new IllegalArgumentException("Level describes more than one tank");
                        }
                        tankPosition = tile;
                        break;
                    case EMPTY:
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected symbol in level description: "
                                + line.charAt(column));
                }
            }
        }

        if (tankPosition == null) {
            throw new IllegalArgumentException("Level does not describe a tank position");
        }
        return new LevelLayout(width, height, tankPosition, trees);
    }

    private List<String> meaningfulRows() {
        List<String> rows = new ArrayList<>();
        for (String line : lines) {
            String row = line.trim();
            if (!row.isEmpty()) {
                rows.add(row);
            }
        }
        return rows;
    }
}
