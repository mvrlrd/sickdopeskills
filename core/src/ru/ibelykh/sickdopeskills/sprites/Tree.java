package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.screens.GameScreen;


public class Tree extends Sprite {
    private boolean isPlaying;
    private Vector2 v = new Vector2();
    private Rider rider;
    private Rect worldBounds;

    public Tree( Rider rider, Rect worldBounds) {
        this.rider=rider;
        this.worldBounds = worldBounds;
        this.v.set(-0.45f,0f);
        isPlaying = GameScreen.getIsPlaying();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (isPlaying) {
            this.setAngle(90);
            pos.mulAdd(v, delta);
            // видимо изза поворота на 90 getRight остался тем которое было до поворота/ для плавного ухода елок с экрана
            if (getRight()+getHalfHeight()<worldBounds.getLeft()) {
                setDestroyed(true);
            }
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
}
