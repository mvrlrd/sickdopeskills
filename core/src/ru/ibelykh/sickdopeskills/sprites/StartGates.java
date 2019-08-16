package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;

public class StartGates extends Sprite {

    private Rect worldBounds;
    private Vector2 speed=new Vector2(-0.0f,0f);
    private static final float proportion = 0.2f;
    private static final String REGION = "start";
    private static final Vector2 SPEED = new Vector2(-0.45f, 0.0f);


    public StartGates(TextureAtlas atlas, Rect worldBounds) {
        super(atlas.findRegion(REGION), 1, 1, 1);
        setHeightProportion(proportion);
        this.setAngle(90f);
        this.worldBounds = worldBounds;
//        this.pos.set(worldBounds.getLeft()+0.1f,0f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(speed,delta);

    }

    public void resize(Rect worldBounds, float x, float y) {
        super.resize(worldBounds);
        pos.set(x,y);


    }



    @Override
    public boolean isDestroyed() {
        super.isDestroyed();
        return true;
    }

    public void setTheNewGame(){
        speed.set(SPEED);
        pos.set(worldBounds.getLeft()+0.1f,0f);
    }

//    public void gameOver(){
//        v.set(-0.15f,0f);
//        velocity.set(0.000f,-0.0f);
//    }




    public Vector2 getSpeed() {
        return speed;
    }
}
