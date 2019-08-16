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
    private static final float MININTERVAL = 1.1f;
    private static final float MAXINTERVAL = 1.4f;
    private static final String REDFLAGREGION = "redFlag";
    private static final String BLUEFLAGREGION = "blueFlag";
    private static final float MINPOSY = 0.03f;
    private static final float MAXPOSY = 0.2f;

    private Rect worldBounds;
    private float generateTimer;
    private int flagCount = 1;
    private TextureRegion[] flagRedRegion;
    private TextureRegion[] flagBlueRegion;
    private FlagPool flagPool;

    public FlagEmitter(Rect worldBounds, FlagPool flagPool, TextureAtlas atlas) {
        this.worldBounds = worldBounds;
        this.flagPool = flagPool;
        TextureRegion textureRegion0 = atlas.findRegion(REDFLAGREGION);
        this.flagRedRegion = Regions.split(textureRegion0, 1, 1, 1);
        TextureRegion textureRegion1 = atlas.findRegion(BLUEFLAGREGION);
        this.flagBlueRegion = Regions.split(textureRegion1, 1, 1, 1);
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
                            flagBlueRegion,
                            FLAG_HEIGHT
                    );
                    flag.setItRed(false);
                    flag.pos.y = Rnd.nextFloat(MINPOSY,MAXPOSY);
                    flag.setLeft(worldBounds.getRight());
                    flagCount++;
                }
                else {
                    flag.set(
                            flagRedRegion,
                            FLAG_HEIGHT
                    );
                    flag.pos.y = Rnd.nextFloat(-MINPOSY,-MAXPOSY);
                    flag.setLeft(worldBounds.getRight());
                    flagCount++;
                    flag.setItRed(true);
                }
            }
        }
    }
}