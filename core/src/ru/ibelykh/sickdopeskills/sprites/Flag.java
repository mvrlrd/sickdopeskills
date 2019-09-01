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
    private Rectangle collisionInvisibleSquare;  //An area on a flag which makes crushing with alike rider's area
    private Rect worldBounds;

    private Rect crashArea;
    private Vector2 crushAreaPos;



    public Flag (Rect worldBounds) {
        this.worldBounds = worldBounds;
        float X_FLAG_SPEED = -0.55f;
        this.flagSpeed = new Vector2(X_FLAG_SPEED,0f);
        this.collisionInvisibleSquare = new Rectangle();

        this.crashArea = new Rect();


       this. crushAreaPos = new Vector2(-100,100);
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

            crushAreaPos.set(getLeft()+getHalfWidth()*1.6f,getBottom()+getHalfHeight());
            crashArea.setHeight(getHalfHeight());
            crashArea.setWidth(getWidth()/4f);
            crashArea.setPos(crushAreaPos);


            pos.mulAdd(flagSpeed, delta);
            // видимо изза поворота на 90 getRight остался тем которое было до поворота/ для плавного ухода елок с экрана;
            if (getRight() < worldBounds.getLeft()) {
                GameScreen.setCountPoints(GameScreen.getCountPoints() + 1);  //increase points on 1;
                setDestroyed(true);
                GameScreen.setIsItNeedToShout(false);
                Shouting.framer(Rnd.nextInt(0, 4));  //there are just 3 types of shouting frames ("sick","dope","whoa");
            }
            if (!isDestroyed()) {
                if (isItRed()) {
                    collisionInvisibleSquare.set(getRight() - getWidth() / 4.27f, getBottom(),
                            getWidth() / 4.6f, getHeight());
                }
                if ((!isItRed())) {
                    collisionInvisibleSquare.set(getRight() - getWidth() / 4.27f, getBottom(),
                            getWidth() / 4.6f, getHeight());
                }
            } else {
                collisionInvisibleSquare.set(worldBounds.getRight(), 0f, 0f, 0f);
            }
        }else { //if the rider crushes the flags stop
            pos.mulAdd(new Vector2(0f,0f), delta);
        }
        }

    public float getBigRectLeft(){
        return collisionInvisibleSquare.x;
    }
    public float getBigRectRight(){
        return collisionInvisibleSquare.x+collisionInvisibleSquare.getWidth();
    }
    public float getBigRectTop(){
        return collisionInvisibleSquare.y+collisionInvisibleSquare.getHeight();
    }
    public float getBigRectBot(){
        return collisionInvisibleSquare.y;
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
//        this.collisionInvisibleSquare.set(worldBounds.getRight(),0f,0f,0f);
    }

    public Rect getCrashArea() {
        return crashArea;
    }
}

