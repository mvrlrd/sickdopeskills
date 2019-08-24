package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.ibelykh.sickdopeskills.base.ScaledButton;
import ru.ibelykh.sickdopeskills.math.Rect;

public class ButtonGetRider extends ScaledButton {




    public ButtonGetRider(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
        setHeightProportion(0.3f);
    }



    @Override
    public void actionPerformed() {
        if (frame==0){
            setFrame(1);
        } else {
            setFrame(0);
        }
    }




    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom()+worldBounds.getHalfHeight()-getHalfHeight());
        setRight(worldBounds.getRight()-worldBounds.getHalfWidth());

    }
}
