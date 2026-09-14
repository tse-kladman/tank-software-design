package ru.mipt.bit.platformer.movement;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.GridPoint2;

public enum Direction {
    UP(0, 1, 90f, Keys.UP, Keys.W),
    LEFT(-1, 0, -180f, Keys.LEFT, Keys.A),
    DOWN(0, -1, -90f, Keys.DOWN, Keys.S),
    RIGHT(1, 0, 0f, Keys.RIGHT, Keys.D);

    private final int dx;
    private final int dy;
    private final float angle;
    private final int[] keys;

    Direction(int dx, int dy, float angle, int... keys) {
        this.dx = dx;
        this.dy = dy;
        this.angle = angle;
        this.keys = keys;
    }

    public float getAngle() {
        return angle;
    }

    public int[] getKeys() {
        return keys;
    }

    public GridPoint2 shift(GridPoint2 point) {
        return new GridPoint2(point).add(dx, dy);
    }
}
