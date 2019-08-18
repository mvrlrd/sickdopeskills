package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.screens.GameScreen;

public class Tree extends Sprite{
    private static final float XSPEED = -0.45f;
    private static final float YSPEED = 0f;
    private static Vector2 treeSpeed = new Vector2(XSPEED,YSPEED);
    private static Vector2 stopSpeed = new Vector2(YSPEED,YSPEED);

    private Rider rider;
    private Rect worldBounds;

    private Rectangle collisionInvisibleSquare;

    public Tree(Rider rider, Rect worldBounds) {
        this.rider = rider;
        this.worldBounds = worldBounds;
        collisionInvisibleSquare = new Rectangle();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (GameScreen.getIsPlaying()) {
            this.setAngle(90); //turn trees in right direction
            pos.mulAdd(treeSpeed, delta);
            // видимо изза поворота на 90 getRight остался тем которое было до поворота/ для плавного ухода елок с экрана
            if (getRight()+getHalfHeight() < worldBounds.getLeft()) {
                setDestroyed(true);
            }
            if (!isDestroyed()){

                collisionInvisibleSquare.set(getRight() - getWidth() / 1f, getBottom(),
                        getWidth()/2.5f , getHeight()/20f);
            }
        }
        else { //if the rider crushes the trees stop
            pos.mulAdd(stopSpeed, delta);
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

    public Rectangle getCollisionInvisibleSquare() {
        return collisionInvisibleSquare;
    }


}
