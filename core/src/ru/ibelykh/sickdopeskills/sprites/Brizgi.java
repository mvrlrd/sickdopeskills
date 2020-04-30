package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.screens.GameScreen;

public class Brizgi extends Sprite {
    private Vector2 flagSpeed;
    private Rect worldBounds;




    public Brizgi (Rect worldBounds) {
        this.worldBounds = worldBounds;
        float X_FLAG_SPEED = -0.55f;
        this.flagSpeed = new Vector2(X_FLAG_SPEED,0f);
    }


    @Override
    public void update(float delta) {
        super.update(delta);
        if (GameScreen.getIsPlaying()) {
            pos.mulAdd(flagSpeed, delta);
            // видимо изза поворота на 90 getRight остался тем которое было до поворота/ для плавного ухода елок с экрана;
            if (getRight() < worldBounds.getLeft()) {
                setDestroyed(true);
            }
        }else { //if the rider crushes the flags stop
            pos.mulAdd(new Vector2(0f,0f), delta);
        }
    }



    public void resize(Rect _worldBounds, float x, float y) {
        super.resize(_worldBounds);
        pos.set(x,y);
    }

    public void set(
            TextureRegion[]regions,
            float height,
            Rider rider
    ){
        this.regions = regions;
        pos.set(rider.pos.x,rider.pos.y);
        setHeightProportion(height);
    }






    @Override
    public void setDestroyed(boolean _destroyed) {
        super.setDestroyed(_destroyed);
    }




}

