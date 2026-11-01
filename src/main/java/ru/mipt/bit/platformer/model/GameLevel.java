package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameLevel implements BulletRegistry {

    private final int width;
    private final int height;
    private final List<GameObject> objects = new ArrayList<>();
    private final List<Tank> tanks = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<GameLevelListener> listeners = new ArrayList<>();
    private Tank playerTank;

    public GameLevel(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addListener(GameLevelListener listener) {
        listeners.add(listener);
    }

    public void addPlayerTank(Tank tank) {
        playerTank = tank;
        addTank(tank);
    }

    public void addTank(Tank tank) {
        tanks.add(tank);
        addObject(tank);
    }

    public void addObstacle(GameObject obstacle) {
        addObject(obstacle);
    }

    @Override
    public void register(Bullet bullet) {
        bullets.add(bullet);
        addObject(bullet);
        resolveHit(bullet);
        removeDestroyed();
    }

    public void removeObject(GameObject object) {
        if (!objects.remove(object)) {
            return;
        }
        tanks.remove(object);
        bullets.remove(object);
        if (object == playerTank) {
            playerTank = null;
        }
        for (GameLevelListener listener : listeners) {
            listener.objectRemoved(object);
        }
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
            if (object != mover && object.blocksMovement() && object.occupies(tile)) {
                return false;
            }
        }
        return true;
    }

    public void live(float deltaTime) {
        float step = shortestTileTime();
        float remaining = deltaTime;
        while (remaining > 0f) {
            float tick = Math.min(step, remaining);
            liveOnce(tick);
            remaining -= tick;
        }
    }

    private void liveOnce(float deltaTime) {
        for (GameObject object : new ArrayList<>(objects)) {
            object.live(deltaTime);
        }
        resolveBulletHits();
        removeDestroyed();
    }

    private float shortestTileTime() {
        float shortest = Float.MAX_VALUE;
        for (Bullet bullet : bullets) {
            shortest = Math.min(shortest, bullet.getSecondsPerTile());
        }
        return shortest > 0f ? shortest : Float.MAX_VALUE;
    }

    private void resolveBulletHits() {
        for (Bullet bullet : new ArrayList<>(bullets)) {
            resolveHit(bullet);
        }
    }

    private void resolveHit(Bullet bullet) {
        if (bullet.isDestroyed()) {
            return;
        }
        if (isOutside(bullet.getPosition())) {
            bullet.destroy();
            return;
        }
        for (GameObject object : new ArrayList<>(objects)) {
            if (object == bullet || object.isDestroyed()) {
                continue;
            }
            if (object.occupies(bullet.getPosition())) {
                object.takeDamage(bullet.getDamage());
                bullet.destroy();
                return;
            }
        }
    }

    private void removeDestroyed() {
        for (GameObject object : new ArrayList<>(objects)) {
            if (object.isDestroyed()) {
                removeObject(object);
            }
        }
    }

    private void addObject(GameObject object) {
        objects.add(object);
        for (GameLevelListener listener : listeners) {
            listener.objectAdded(object);
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

    public List<Tank> getTanks() {
        return Collections.unmodifiableList(new ArrayList<>(tanks));
    }

    public List<Tank> getAiTanks() {
        List<Tank> aiTanks = new ArrayList<>(tanks);
        aiTanks.remove(playerTank);
        return Collections.unmodifiableList(aiTanks);
    }

    public List<Bullet> getBullets() {
        return Collections.unmodifiableList(new ArrayList<>(bullets));
    }

    public List<GameObject> getObjects() {
        return Collections.unmodifiableList(new ArrayList<>(objects));
    }
}
