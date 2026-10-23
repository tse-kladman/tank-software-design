package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GameLevel {

    private final int width;
    private final int height;
    private final Tank playerTank;
    private final List<Tank> aiTanks;
    private final List<Tank> tanks;
    private final List<GameObject> obstacles;
    private final List<GameObject> objects;

    public GameLevel(int width, int height, Tank playerTank, Collection<Tank> aiTanks,
                     Collection<? extends GameObject> obstacles) {
        this.width = width;
        this.height = height;
        this.playerTank = playerTank;
        this.aiTanks = Collections.unmodifiableList(new ArrayList<>(aiTanks));
        this.obstacles = Collections.unmodifiableList(new ArrayList<GameObject>(obstacles));

        List<Tank> allTanks = new ArrayList<>();
        allTanks.add(playerTank);
        allTanks.addAll(this.aiTanks);
        this.tanks = Collections.unmodifiableList(allTanks);

        List<GameObject> allObjects = new ArrayList<GameObject>(allTanks);
        allObjects.addAll(this.obstacles);
        this.objects = Collections.unmodifiableList(allObjects);
    }

    public void moveTank(Tank tank, Direction direction) {
        if (tank.isMoving()) {
            return;
        }
        tank.rotate(direction);
        if (isFreeFor(tank, direction.shift(tank.getPosition()))) {
            tank.move(direction);
        }
    }

    public boolean isFree(Coordinates tile) {
        return isFreeFor(null, tile);
    }

    public boolean isFreeFor(GameObject mover, Coordinates tile) {
        if (isOutside(tile)) {
            return false;
        }
        for (GameObject object : objects) {
            if (object != mover && object.occupies(tile)) {
                return false;
            }
        }
        return true;
    }

    public void update(float deltaTime) {
        for (Tank tank : tanks) {
            tank.update(deltaTime);
        }
    }

    private boolean isOutside(Coordinates tile) {
        return tile.getX() < 0 || tile.getY() < 0 || tile.getX() >= width || tile.getY() >= height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tank getPlayerTank() {
        return playerTank;
    }

    public List<Tank> getAiTanks() {
        return aiTanks;
    }

    public List<Tank> getTanks() {
        return tanks;
    }

    public List<GameObject> getObstacles() {
        return obstacles;
    }

    public List<GameObject> getObjects() {
        return objects;
    }
}
