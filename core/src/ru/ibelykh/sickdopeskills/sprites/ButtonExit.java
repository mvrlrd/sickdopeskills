package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.ibelykh.sickdopeskills.base.ScaledButton;
import ru.ibelykh.sickdopeskills.math.Rect;

public  class ButtonExit extends ScaledButton {

    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("labels"),2,2,4);
        setHeightProportion(0.2f);
        setFrame(1);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom());
        setRight(worldBounds.getRight()+getHalfHeight());
        setAngle(90f);
    }

    @Override
    public void actionPerformed() {
        Gdx.app.exit();
    }
}
