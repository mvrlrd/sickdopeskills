package ru.ibelykh.sickdopeskills.utils;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.pools.FlagPool;
import ru.ibelykh.sickdopeskills.screens.GameScreen;
import ru.ibelykh.sickdopeskills.sprites.Flag;


public class FlagEmitter {

    private static final float FLAG_HEIGHT = 0.08f;
    private Rect worldBounds;
    private float generateInterval = 0f;
    private float generateTimer;
    private int flagCount=1;
    private TextureRegion[] flagRedRegion;
    private TextureRegion[] flagBlueRegion;
    private FlagPool flagPool;

    public FlagEmitter(Rect worldBounds, FlagPool flagPool, TextureAtlas atlas) {
        this.worldBounds = worldBounds;
        this.flagPool = flagPool;
        TextureRegion textureRegion0 = atlas.findRegion("redFlag");
        this.flagRedRegion = Regions.split(textureRegion0, 1, 1, 1);
        TextureRegion textureRegion1 = atlas.findRegion("blueFlag");
        this.flagBlueRegion = Regions.split(textureRegion1, 1, 1, 1);


    }

    public void generateFlags(float delta) {

        if (GameScreen.getIsPlaying()) {
            generateInterval = 2.1f;
            generateTimer += delta;
            if (generateTimer >= generateInterval) {
                generateTimer = 0f;
                Flag flag = flagPool.obtain();
                if (flagCount%2==0){
                    flag.set(
                            flagBlueRegion,
                            FLAG_HEIGHT
                    );
                    flag.setItRed(false);
//                    flag.setAngle(90);
                    flag.pos.y = Rnd.nextFloat(0.03f,0.2f);
                    flag.setLeft(worldBounds.getRight());
                    flagCount++;
                }
                else {
                    flag.set(
                            flagRedRegion,
                            FLAG_HEIGHT
                    );
//                    flag.setAngle(90);
                    flag.pos.y = Rnd.nextFloat(-0.03f,-0.2f);
                    flag.setLeft(worldBounds.getRight());
                    flagCount++;
                    flag.setItRed(true);
                }
            }
        }

    }


}