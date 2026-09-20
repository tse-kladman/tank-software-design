package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public abstract class ObjectGraphics {

    private final TextureRegion texture;
    protected final Rectangle bounds;

    protected ObjectGraphics(TextureRegion texture) {
        this.texture = texture;
        this.bounds = createBoundingRectangle(texture);
    }

    public void update() {
    }

    public void draw(Batch batch) {
        drawTextureRegionUnscaled(batch, texture, bounds, getAngle());
    }

    protected float getAngle() {
        return 0f;
    }
}
