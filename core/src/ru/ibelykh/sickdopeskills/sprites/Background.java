package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;


import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;


public class Background extends Sprite {


    public Background(TextureAtlas atlas, String nameOfPic) {
        super(atlas.findRegion(nameOfPic));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        pos.set(worldBounds.pos);
    }
}
