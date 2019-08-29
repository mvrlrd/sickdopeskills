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
    private static int frm=0;



    public Shouting(TextureAtlas atlas, Rect worldBounds) {
        super(atlas.findRegion("labels"), 2, 2, 4);
        setHeightProportion(0.15f);
        this.worldBounds = worldBounds;
    }


    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        this.setAngle(90);


        if (!isSick){
            setFrame(frm);

        }

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


    public static void framer(int a){
        if (a==1){
            frm = 0;
        }else {
            frm = a;
        }
    }
}