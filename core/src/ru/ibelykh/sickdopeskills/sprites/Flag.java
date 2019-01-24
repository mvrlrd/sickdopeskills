package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.screens.GameScreen;


public class Flag extends Sprite {

    private boolean isItRed;
    private Vector2 v = new Vector2();
    private Rider rider;


    private Rect worldBounds;

    public Flag( Rider rider, Rect worldBounds) {

        this.rider=rider;
        this.worldBounds = worldBounds;
        this.v.set(-0.28f,0f);

//        rectangle= new Rectangle();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (GameScreen.getIsPlaying()) {



            pos.mulAdd(v, delta);

//            if (isOutside(worldBounds)) {
//                setDestroyed(true);
//
//            }
            // видимо изза поворота на 90 getRight остался тем которое было до поворота/ для плавного ухода елок с экрана
            if (getRight()<worldBounds.getLeft()) {
                setDestroyed(true);
                GameScreen.setCountPoints(GameScreen.getCountPoints()+1);
            }
//            if (isItRed) {
//               rectangle.set(getLeft()+getWidth()*0.4f,getBottom()+getHalfWidth()/1.5f,
//                       getWidth()/1.7f,getHeight()/3f);
//            }
        }
    }


    public void resize(Rect worldBounds, float x, float y) {
        super.resize(worldBounds);
        pos.set(x,y);

    }

    public void set(
            TextureRegion[]regions,
            float height

    ){
        this.regions = regions;
        setHeightProportion(height);
    }

//    public int getFlagCounter() {
//        return flagCounter;
//    }

    public void setItRed(boolean itRed) {
        isItRed = itRed;
    }

    public boolean isItRed() {
        return isItRed;
    }


}

