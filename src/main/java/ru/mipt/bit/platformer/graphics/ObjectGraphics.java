package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Coordinates;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class ObjectGraphics {

    private final GameObject object;
    private final TextureRegion texture;
    private final TileMovement tileMovement;
    private final Rectangle bounds;

    public ObjectGraphics(GameObject object, TextureRegion texture, TileMovement tileMovement) {
        this.object = object;
        this.texture = texture;
        this.tileMovement = tileMovement;
        this.bounds = createBoundingRectangle(texture);
    }

    public void update() {
        tileMovement.moveRectangleBetweenTileCenters(bounds, toGridPoint(object.getPosition()),
                toGridPoint(object.getDestination()), object.getMovementProgress());
    }

    public void draw(Batch batch) {
        drawTextureRegionUnscaled(batch, texture, bounds, object.getRotation());
    }

    private static GridPoint2 toGridPoint(Coordinates coordinates) {
        return new GridPoint2(coordinates.getX(), coordinates.getY());
    }
}
