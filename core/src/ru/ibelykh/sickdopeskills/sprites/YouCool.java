package ru.ibelykh.sickdopeskills.sprites;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;


public class YouCool extends Sprite {
    private Rect worldBounds;
    private Sound youCool;




    public YouCool(TextureAtlas atlas, Rect worldBounds) {
        super(atlas.findRegion("youCool"));
        setHeightProportion(0.5f);
        this.worldBounds = worldBounds;

    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);



    }

    @Override
    public void update(float delta) {

        super.update(delta);
        setAngle(90);
        pos.set(worldBounds.getRight()-getHalfWidth(),worldBounds.getBottom()+getHalfHeight());


//        this.coolMoments++;
//			System.out.println(coolMoments+"  COooll");setAngle(90);



    }
    @Override
    public void draw(SpriteBatch batch) {
//        if (!Shouting.isSick()){
            super.draw(batch);
//        }
    }



}
