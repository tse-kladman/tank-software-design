package ru.mipt.bit.platformer.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.movement.Direction;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank extends GameEntity {

    private static final float MOVEMENT_SPEED = 0.4f;

    private final TileMovement tileMovement;
    private final GridPoint2 destination;
    private float movementProgress = 1f;
    private float rotation = 0f;

    public Tank(TiledMapTileLayer tileLayer, TextureRegion texture, GridPoint2 startPosition) {
        super(tileLayer, texture, startPosition);
        destination = new GridPoint2(startPosition);
        tileMovement = new TileMovement(tileLayer, Interpolation.smooth);
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    public void rotate(Direction direction) {
        rotation = direction.getAngle();
    }

    public void move(Direction direction) {
        if (isMoving()) {
            return;
        }
        rotate(direction);
        destination.set(direction.shift(position));
        movementProgress = 0f;
    }

    public void update(float deltaTime) {
        tileMovement.moveRectangleBetweenTileCenters(bounds, position, destination, movementProgress);

        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (!isMoving()) {
            position.set(destination);
        }
    }

    @Override
    protected float getRotation() {
        return rotation;
    }
}
