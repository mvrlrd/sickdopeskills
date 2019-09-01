package ru.ibelykh.sickdopeskills.pools;


import ru.ibelykh.sickdopeskills.base.SpritesPool;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.sprites.Rider;
import ru.ibelykh.sickdopeskills.sprites.Tree;

public class TreePool extends SpritesPool<Tree> {



    private Rect worldBounds;


    public TreePool( Rect worldBounds) {


        this.worldBounds=worldBounds;

    }

    @Override
    protected Tree newObject() {

        return new Tree(worldBounds);
    }
}

