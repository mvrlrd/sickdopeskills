package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.ibelykh.sickdopeskills.base.ScaledButton;
import ru.ibelykh.sickdopeskills.math.Rect;

public class ButtonGetRider extends ScaledButton {

    public ButtonGetRider(TextureAtlas atlas) {
        super(atlas.findRegion("yellowMove"));
        setHeightProportion(0.3f);

    }

    @Override
    public void actionPerformed() {

    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);

        setBottom(worldBounds.getBottom()+worldBounds.getHalfHeight()-getHalfHeight());
        setRight(worldBounds.getRight()-worldBounds.getHalfWidth());

    }
}
