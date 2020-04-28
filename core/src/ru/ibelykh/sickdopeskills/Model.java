package ru.ibelykh.sickdopeskills;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.sprites.Rider;
import ru.ibelykh.sickdopeskills.sprites.Snow;
import ru.ibelykh.sickdopeskills.sprites.Splash;
import ru.ibelykh.sickdopeskills.sprites.StartGates;

public class Model {
    private final TextureAtlas ALL_SPRITES_TEXTURE_ATLAS = new TextureAtlas("images/sprites/allSprites.atlas");
    private static final int SPLASH_COUNT = 20;
    private static final int SNOW_COUNT = Rnd.nextInt(500, 10000);
    private Rect worldBounds;

    private StartGates startGates;
    private Rider rider;
    private Splash[] splashes;
    private Snow[] snows;




    public Model(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    public void init(){
        this.rider = new Rider(ALL_SPRITES_TEXTURE_ATLAS,worldBounds);
        this.startGates = new StartGates(ALL_SPRITES_TEXTURE_ATLAS,worldBounds);

        //splashes
        this.splashes = new Splash[SPLASH_COUNT];
        for (int i = 0; i < splashes.length; i++) {
            splashes[i] = new Splash(ALL_SPRITES_TEXTURE_ATLAS,rider);
        }
        //snow
        snows = new Snow[SNOW_COUNT];
        float windDirectionX = Rnd.nextFloat(-0.7f, 0.7f); //wind speed on X
        float windDirectionY = Rnd.nextFloat(-0.7f, 0.7f); //wind speed on Y
        for (int i = 0; i < snows.length; i++) {
            Vector2 snowSpeed = new Vector2();
            snowSpeed.set(Rnd.nextFloat(windDirectionX - 0.1f, windDirectionX + 0.1f), Rnd.nextFloat(windDirectionY - 0.1f, windDirectionY + 0.1f));
            snows[i] = new Snow(ALL_SPRITES_TEXTURE_ATLAS, snowSpeed);
        }


    }

    public Rider getRider() {
        return rider;
    }

    public StartGates getStartGates() {
        return startGates;
    }

    public Splash[] getSplash() {
        return splashes;
    }

    public Snow[] getSnows() {
        return snows;
    }
}
