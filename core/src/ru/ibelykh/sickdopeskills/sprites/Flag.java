package ru.ibelykh.sickdopeskills.sprites;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.screens.GameScreen;

public class Flag extends Sprite {
    private int shoutFrame;
    private Vector2 flagSpeed;
    private Rect worldBounds;




    public Flag (Rect worldBounds) {
        this.worldBounds = worldBounds;
        float X_FLAG_SPEED = -0.55f;
        this.flagSpeed = new Vector2(X_FLAG_SPEED,0f);
    }

    public int getShoutFrame() {
        return shoutFrame;
    }
    public void setShoutFrame(int shoutFrame) {
        this.shoutFrame = shoutFrame;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (GameScreen.getIsPlaying()) {




            pos.mulAdd(flagSpeed, delta);
            // видимо изза поворота на 90 getRight остался тем которое было до поворота/ для плавного ухода елок с экрана;
            if (getRight() < worldBounds.getLeft()) {
                GameScreen.setCountPoints(GameScreen.getCountPoints() + 1);  //increase points on 1;
                setDestroyed(true);
                GameScreen.setIsItNeedToShout(false);
                Shouting.framer(Rnd.nextInt(0, 4));  //there are just 3 types of shouting frames ("sick","dope","whoa");
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
            float height
    ){
        this.regions = regions;
        setHeightProportion(height);
    }



    public boolean isItRed() {
        return (this.getFrame()==1);
    }


    @Override
    public void setDestroyed(boolean _destroyed) {
        super.setDestroyed(_destroyed);
    }



    @Override
    public boolean isOutside(Rect other) {

        if (this.isItRed()) {
            return     this.getLeft() + 0.9f * getWidth() > other.getRight()
                    || this.getRight() < other.getLeft()
                    || this.getBottom() + 0.2f * getHalfHeight() > other.getTop()
                    || this.getTop() - 0.1f * getHalfHeight() < other.getBottom();
        } else {
            return     this.getLeft() + 0.9f * getWidth() > other.getRight()
                    || this.getRight() < other.getLeft()
                    || this.getBottom() + 0.1f * getHalfHeight() > other.getTop()
                    || this.getTop() - 0.2f * getHalfHeight() < other.getBottom();
        }

    }
}

