package ru.ibelykh.sickdopeskills.pools;


import ru.ibelykh.sickdopeskills.base.SpritesPool;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.sprites.Flag;
import ru.ibelykh.sickdopeskills.sprites.Rider;

public class FlagPool extends SpritesPool<Flag> {


    private Rider rider;
    private Rect worldBounds;


    public FlagPool(Rider rider, Rect worldBounds) {

        this.rider = rider;
        this.worldBounds=worldBounds;

    }

    @Override
    protected Flag newObject() {
        return new Flag(rider,worldBounds);
    }
}
