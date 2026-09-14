package ru.mipt.bit.platformer.entity.obstacle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.entity.GameEntity;

public class Tree extends GameEntity {

    public Tree(TiledMapTileLayer tileLayer, TextureRegion texture, GridPoint2 position) {
        super(tileLayer, texture, position);
    }
}
