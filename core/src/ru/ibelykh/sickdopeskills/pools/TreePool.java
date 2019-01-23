package ru.ibelykh.sickdopeskills.pools;


import ru.ibelykh.sickdopeskills.base.SpritesPool;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.sprites.Rider;
import ru.ibelykh.sickdopeskills.sprites.Tree;

public class TreePool extends SpritesPool<Tree> {


    private Rider rider;
    private Rect worldBounds;


    public TreePool(Rider rider, Rect worldBounds) {

        this.rider = rider;
        this.worldBounds=worldBounds;

    }

    @Override
    protected Tree newObject() {
        return new Tree(rider,worldBounds);
    }
}

