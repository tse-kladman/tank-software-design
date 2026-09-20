package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Tree {

    private final GridPoint2 position;

    public Tree(GridPoint2 position) {
        this.position = new GridPoint2(position);
    }

    public boolean isAt(GridPoint2 tile) {
        return position.equals(tile);
    }

    public GridPoint2 getPosition() {
        return new GridPoint2(position);
    }
}
