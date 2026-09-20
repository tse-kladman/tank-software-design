package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GameLevel {

    private final int width;
    private final int height;
    private final Tank playerTank;
    private final List<Tree> trees;

    public GameLevel(int width, int height, Tank playerTank, Collection<Tree> trees) {
        this.width = width;
        this.height = height;
        this.playerTank = playerTank;
        this.trees = Collections.unmodifiableList(new ArrayList<>(trees));
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

    public boolean isFree(GridPoint2 tile) {
        if (tile.x < 0 || tile.y < 0 || tile.x >= width || tile.y >= height) {
            return false;
        }
        for (Tree tree : trees) {
            if (tree.isAt(tile)) {
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

    public List<Tree> getTrees() {
        return trees;
    }
}
