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

   static float posX = -0.67f;
   static float posY = 0f;
    private Rider rider = GameScreen.getRider();

    public Splash(TextureAtlas atlas) {
        super(atlas.findRegion("snow"));

        setHeightProportion(Rnd.nextFloat(0.02f,0.025f));
        pos.set(posX,posY);
        v.set(Rnd.nextFloat(-0.01f,-0.4f),Rnd.nextFloat(-0.02f,-0.5f));
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
//        float posX = -0.67f;
//        float posY = 0f;
//        pos.set(posX,rider.getBoardBottom());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v,delta);
        checkAndHandleBounds();
    }
    private void checkAndHandleBounds(){
        if (getRight() < worldBounds.getLeft()) setLeft(rider.getBoardBack());
        if (getLeft() > rider.getBoardNose()) setRight(rider.getBoardBack());
         setBottom(rider.getBoardBottom());

    }

}
