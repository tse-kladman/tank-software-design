package ru.mipt.bit.platformer.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public abstract class GameEntity {

    private final TextureRegion texture;
    protected final Rectangle bounds;
    protected final GridPoint2 position;

    protected GameEntity(TiledMapTileLayer tileLayer, TextureRegion texture, GridPoint2 startPosition) {
        this.texture = texture;
        this.bounds = createBoundingRectangle(texture);
        this.position = new GridPoint2(startPosition);
        moveRectangleAtTileCenter(tileLayer, bounds, position);
    }

    public void draw(Batch batch) {
        drawTextureRegionUnscaled(batch, texture, bounds, getRotation());
    }

    protected float getRotation() {
        return 0f;
    }

    public GridPoint2 getPosition() {
        // Возвращаем копию, чтобы избежать случайного изменения координат объекта извне
        return new GridPoint2(position);
    }
}
