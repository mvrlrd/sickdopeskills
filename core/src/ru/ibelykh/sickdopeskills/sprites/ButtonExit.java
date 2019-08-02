package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.ibelykh.sickdopeskills.base.ScaledButton;
import ru.ibelykh.sickdopeskills.math.Rect;

public  class ButtonExit extends ScaledButton {

    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("sick"));
        setHeightProportion(0.3f);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);

        setBottom(worldBounds.getBottom());
        setRight(worldBounds.getRight()-0.04f);

    }

    @Override
    public void actionPerformed() {
        Gdx.app.exit();
    }
}
