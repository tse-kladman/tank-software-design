package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.mipt.bit.platformer.model.Tree;

import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TreeGraphics extends ObjectGraphics {

    public TreeGraphics(TextureRegion texture, TiledMapTileLayer tileLayer, Tree tree) {
        super(texture);
        moveRectangleAtTileCenter(tileLayer, bounds, tree.getPosition());
    }
}
