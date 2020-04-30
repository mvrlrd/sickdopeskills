package ru.ibelykh.sickdopeskills.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.pools.BrizgiPool;
import ru.ibelykh.sickdopeskills.screens.GameScreen;
import ru.ibelykh.sickdopeskills.sprites.Brizgi;
import ru.ibelykh.sickdopeskills.sprites.Flag;
import ru.ibelykh.sickdopeskills.sprites.Rider;

public class BrizgiEmitter {


    private static final float MINTREEINTERVAL = 0.005f;
    private static final float MAXTREEINTERVAL = 0.009f;
    private Rect worldBounds;

    private float generateTimer;
    private TextureRegion[] brizgiRegion;
    private BrizgiPool brizgiPool;
    private Rider rider;


    public BrizgiEmitter(Rect worldBounds, BrizgiPool brizgiPool, TextureAtlas atlas, Rider rider) {
        this.worldBounds = worldBounds;
        this.brizgiPool = brizgiPool;
        this.brizgiRegion = Regions.split(atlas.findRegion("snow"), 1, 1, 1);
        this.rider = rider;
    }

    public void generateBrizgi(float delta) {
        brizgiSet(delta, 0.4f, worldBounds.getTop());
    }


    private void brizgiSet(float delta, float minY, float maxY) {
        if (GameScreen.getIsPlaying()) {
            float generateInterval = Rnd.nextFloat(MINTREEINTERVAL, MAXTREEINTERVAL);
            generateTimer += delta;
            if (generateTimer >= generateInterval) {
                generateTimer = 0f;
                Brizgi brizgi = brizgiPool.obtain();
                brizgi.set(
                        brizgiRegion,
//                            flagBlueRegion,
                        0.03f,
                        rider
                );

                    brizgi.setFrame(0);
                    brizgi.pos.y = rider.getBoardBottom();
                    brizgi.pos.x = rider.getBoardBack();
//                    brizgi.setLeft(rider.getBoardBottom());
                }


//                Brizgi brizgi = brizgiPool.obtain();
//                brizgi.set(brizgiRegion,0.2f);
//
////                    brizgi.setFrame(0);
//
//                brizgi.pos.y = Rnd.nextFloat(minY, maxY);
//                brizgi.pos.x = (worldBounds.getRight() + brizgi.getHalfHeight());
            }
        }


}