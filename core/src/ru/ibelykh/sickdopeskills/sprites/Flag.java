package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.screens.GameScreen;


public class Flag extends Sprite {

    private boolean isItRed;
    private Vector2 v = new Vector2();
    private Rider rider;
    private Rectangle bigRect;



    private Rect worldBounds;

    public Flag( Rider rider, Rect worldBounds) {

        this.rider=rider;
        this.worldBounds = worldBounds;
        this.v.set(-0.3f,0f);
        bigRect=new Rectangle();


    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
//        if (GameScreen.getIsPlaying()) {





//            if (isOutside(worldBounds)) {
//                setDestroyed(true);
//
//            }
            // видимо изза поворота на 90 getRight остался тем которое было до поворота/ для плавного ухода елок с экрана
            if (getRight()<worldBounds.getLeft()) {



                GameScreen.setCountPoints(GameScreen.getCountPoints()+1);

                setDestroyed(true);
                GameScreen.setIsItNeedToShout(false);
                Shouting.framer(Rnd.nextInt(0,3));

            }
            if (!isDestroyed()) {
                if ((isItRed)) {
                    bigRect.set(getRight() - getWidth() / 4.27f, getBottom(),
                            getWidth() / 4.6f, getHeight());
                }
                if ((!isItRed)) {
                    bigRect.set(getRight() - getWidth() / 4.27f, getBottom(),
                            getWidth() / 4.6f, getHeight());
                }
            }else {
                bigRect.set(worldBounds.getRight(),0f,0f,0f);}



        }


//        }

    public Rectangle getBigRect() {
        return bigRect;
    }

    public float getBigRectLeft(){
        return bigRect.x;
    }
    public float getBigRectRight(){
        return bigRect.x+bigRect.getWidth();
    }
    public float getBigRectTop(){
        return bigRect.y+bigRect.getHeight();
    }
    public float getBigRectBot(){
        return bigRect.y;
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

    @Override
    public void setDestroyed(boolean destroyed) {
        super.setDestroyed(destroyed);
        this.bigRect.set(worldBounds.getRight(),0f,0f,0f);
    }


}

