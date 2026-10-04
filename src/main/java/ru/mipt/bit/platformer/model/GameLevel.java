package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GameLevel {

    private final int width;
    private final int height;
    private final Tank playerTank;
    private final List<GameObject> obstacles;
    private final List<GameObject> objects;

    public GameLevel(int width, int height, Tank playerTank, Collection<? extends GameObject> obstacles) {
        this.width = width;
        this.height = height;
        this.playerTank = playerTank;
        this.obstacles = Collections.unmodifiableList(new ArrayList<GameObject>(obstacles));

        List<GameObject> allObjects = new ArrayList<>();
        allObjects.add(playerTank);
        allObjects.addAll(this.obstacles);
        this.objects = Collections.unmodifiableList(allObjects);
    }

    public void moveTank(Direction direction) {
        if (playerTank.isMoving()) {
            return;
        }
        playerTank.rotate(direction);
        if (isFree(direction.shift(playerTank.getPosition()))) {
            playerTank.move(direction);
        }
    }

    public boolean isFree(Coordinates tile) {
        if (tile.getX() < 0 || tile.getY() < 0 || tile.getX() >= width || tile.getY() >= height) {
            return false;
        }
        for (GameObject obstacle : obstacles) {
            if (obstacle.getPosition().equals(tile)) {
                return false;
            }
        }
        return true;
    }

    public void update(float deltaTime) {
        playerTank.update(deltaTime);
    }

    public Tank getPlayerTank() {
        return playerTank;
    }

    public List<GameObject> getObstacles() {
        return obstacles;
    }

    public List<GameObject> getObjects() {
        return objects;
    }
}
