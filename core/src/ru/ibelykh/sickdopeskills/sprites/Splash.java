package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.screens.GameScreen;

public class Splash extends Sprite {

    private Rect worldBounds;
    private Vector2 v = new Vector2();
    private Rider rider = GameScreen.getRider();

    public Splash(TextureAtlas atlas) {
        super(atlas.findRegion("snow"));
        setHeightProportion(Rnd.nextFloat(0.02f,0.025f));
        v.set(Rnd.nextFloat(-0.18f,-0.36f),Rnd.nextFloat(-0.5f,-0.001f));
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        float posX = -0.67f;
        float posY = rider.getBoardTop();
        pos.set(posX,posY);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v,delta);
        checkAndHandleBounds();
    }
    private void checkAndHandleBounds(){
        if (getRight() < worldBounds.getLeft()) setLeft(rider.getBoardBack());
//        if (getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
         setBottom(rider.getBoardBottom());

    }

}
