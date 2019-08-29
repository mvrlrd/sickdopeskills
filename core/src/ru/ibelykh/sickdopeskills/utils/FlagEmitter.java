package ru.ibelykh.sickdopeskills.utils;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.pools.FlagPool;
import ru.ibelykh.sickdopeskills.screens.GameScreen;
import ru.ibelykh.sickdopeskills.sprites.Flag;


public class FlagEmitter {

    private static final float FLAG_HEIGHT = 0.05f;
    private static final float MININTERVAL = 0.99f;
    private static final float MAXINTERVAL = 1f;

    private static final float MINPOSY = 0.08f;
    private static final float MAXPOSY = 0.2f;

    private Rect worldBounds;
    private float generateTimer;
    private int flagCount = 1;
    private TextureRegion[] flagTextureRegion;

    private FlagPool flagPool;

    public FlagEmitter(Rect worldBounds, FlagPool flagPool, TextureAtlas atlas) {
        this.worldBounds = worldBounds;
        this.flagPool = flagPool;
        TextureRegion textureRegion = atlas.findRegion("flags");
        this.flagTextureRegion = Regions.split(textureRegion, 1, 2, 2);


    }

    public void generateFlags(float delta) {
        if (GameScreen.getIsPlaying()) {
            float generateInterval = Rnd.nextFloat(MININTERVAL,MAXINTERVAL);
            generateTimer += delta;
            if (generateTimer >= generateInterval) {
                generateTimer = 0f;
                Flag flag = flagPool.obtain();
                flag.setShoutFrame((int)(Math.random()*2));
                if (flagCount%2==0){
                    flag.set(
                            flagTextureRegion,
//                            flagBlueRegion,
                            FLAG_HEIGHT
                    );
                    flag.setAngle(90f);
                    flag.setFrame(0);
                    flag.pos.y = Rnd.nextFloat(MINPOSY,MAXPOSY);
                    flag.setLeft(worldBounds.getRight());
                    flagCount++;
                }
                else {
                    flag.set(
                            flagTextureRegion,
//                            flagRedRegion,
                            FLAG_HEIGHT
                    );
                    flag.setAngle(90f);
                    flag.setFrame(1);
                    flag.pos.y = Rnd.nextFloat(-MINPOSY,-MAXPOSY);
                    flag.setLeft(worldBounds.getRight());
                    flagCount++;
                }
            }
        }
    }
}