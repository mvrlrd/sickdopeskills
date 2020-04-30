package ru.ibelykh.sickdopeskills.pools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.ibelykh.sickdopeskills.base.SpritesPool;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.sprites.Brizgi;
import ru.ibelykh.sickdopeskills.sprites.Flag;
import ru.ibelykh.sickdopeskills.sprites.Rider;

public class BrizgiPool extends SpritesPool<Brizgi> {
    private Rect worldBounds;


    public BrizgiPool( Rect worldBounds) {
        this.worldBounds=worldBounds;

    }

    @Override
    protected Brizgi newObject() {
        return new Brizgi(worldBounds);
    }
}