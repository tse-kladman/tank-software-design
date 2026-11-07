package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.GameObject;

public class HealthBarGraphics implements ObjectView {

    private static final float BAR_WIDTH_RATIO = 0.8f;
    private static final float BAR_HEIGHT = 10f;
    private static final float BAR_OFFSET = 6f;

    private final ObjectView view;
    private final GameObject object;
    private final TextureRegion bar;

    public HealthBarGraphics(ObjectView view, GameObject object, TextureRegion bar) {
        this.view = view;
        this.object = object;
        this.bar = bar;
    }

    @Override
    public void update() {
        view.update();
    }

    @Override
    public void draw(Batch batch) {
        view.draw(batch);

        Rectangle bounds = view.getBounds();
        float width = bounds.width * BAR_WIDTH_RATIO;
        float x = bounds.x + (bounds.width - width) / 2f;
        float y = bounds.y + bounds.height + BAR_OFFSET;

        Color previous = batch.getColor().cpy();
        batch.setColor(Color.RED);
        batch.draw(bar, x, y, width, BAR_HEIGHT);
        batch.setColor(Color.GREEN);
        batch.draw(bar, x, y, width * healthLeft(), BAR_HEIGHT);
        batch.setColor(previous);
    }

    @Override
    public Rectangle getBounds() {
        return view.getBounds();
    }

    private float healthLeft() {
        if (object.getMaxHealth() <= 0) {
            return 0f;
        }
        return Math.max(0f, (float) object.getHealth() / object.getMaxHealth());
    }
}
