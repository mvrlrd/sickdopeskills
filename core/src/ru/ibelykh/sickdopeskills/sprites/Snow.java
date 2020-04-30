package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;

public class Snow extends Sprite {
    private Rect worldBounds;
    private Vector2 snowFlakeSpeed = new Vector2();

    public Snow(TextureAtlas atlas, Vector2 snowSpeed) {
        super(atlas.findRegion("snow"));
        setHeightProportion(Rnd.nextFloat(0.001f,0.006f));
        this.snowFlakeSpeed.set(snowSpeed);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(posX,posY);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(snowFlakeSpeed,delta);
        checkAndHandleBounds();
    }
    //if a snowflake get off the screen it will appear on the contrary side
    private void checkAndHandleBounds(){
        if (getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        if (getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if (getTop() < worldBounds.getBottom()) setBottom(worldBounds.getTop());
        if (getBottom() > worldBounds.getTop()) setBottom(worldBounds.getBottom());
    }

}
