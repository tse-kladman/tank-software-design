package ru.mipt.bit.platformer.model;

public enum Direction {
    UP(0, 1, 90f),
    LEFT(-1, 0, -180f),
    DOWN(0, -1, -90f),
    RIGHT(1, 0, 0f);

    private final int dx;
    private final int dy;
    private final float angle;

    Direction(int dx, int dy, float angle) {
        this.dx = dx;
        this.dy = dy;
        this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }

    public Coordinates shift(Coordinates from) {
        return from.shift(dx, dy);
    }
}
