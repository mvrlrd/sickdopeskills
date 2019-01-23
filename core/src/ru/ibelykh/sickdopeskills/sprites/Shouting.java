package ru.ibelykh.sickdopeskills.sprites;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;


public class Shouting extends Sprite {

    private static boolean isSick;
    private Rect worldBounds;
    private Sound Sick, dope, youCool;

    Rider rider;


    public Shouting(TextureAtlas atlas, Rect worldBounds) {
        super(atlas.findRegion("shout"), 3, 1, 3);
        setHeightProportion(0.15f);
        this.worldBounds = worldBounds;

//        this.dogHouse = dogHouse;

    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);

    }

    @Override
    public void update(float delta) {

        super.update(delta);
        this.setAngle(90);
//        if (GameScreen.getIsPlaying()){
//            setFrame(1);
//        }


    }
    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    public static boolean isSick() {
        return isSick;
    }

    public  void setSick(boolean sick) {
        isSick = sick;
    }
}