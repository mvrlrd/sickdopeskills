package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;

public class StartGates extends Sprite {

    private Rect worldBounds;
    private Vector2 speed=new Vector2(-0.0f,0f);


    public StartGates(TextureAtlas atlas, Rect worldBounds) {
        super(atlas.findRegion("start"), 1, 1, 1);
        setHeightProportion(0.2f);
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
        speed.set(-0.13f, 0.0f);
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
