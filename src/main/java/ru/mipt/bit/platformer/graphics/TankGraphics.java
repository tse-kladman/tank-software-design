package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

public class TankGraphics extends ObjectGraphics {

    private final Tank tank;
    private final TileMovement tileMovement;

    public TankGraphics(TextureRegion texture, TileMovement tileMovement, Tank tank) {
        super(texture);
        this.tileMovement = tileMovement;
        this.tank = tank;
    }

    @Override
    public void update() {
        tileMovement.moveRectangleBetweenTileCenters(bounds, tank.getPosition(), tank.getDestination(),
                tank.getMovementProgress());
    }

    @Override
    protected float getAngle() {
        return tank.getDirection().getAngle();
    }
}
