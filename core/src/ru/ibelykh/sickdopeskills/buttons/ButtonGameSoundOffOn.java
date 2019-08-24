package ru.ibelykh.sickdopeskills.buttons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.ibelykh.sickdopeskills.base.ScaledButton;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.screens.GameScreen;

public class ButtonGameSoundOffOn extends ScaledButton {

    public ButtonGameSoundOffOn(TextureAtlas atlas) {
        super(atlas.findRegion("buttonSound"),2,1,2);
        setHeightProportion(0.15f);
        setFrame(1);
    }

    @Override
    public void actionPerformed() {
        if (frame==0){
            setFrame(1);
            if (!GameScreen.getMusic().isPlaying()) {
                GameScreen.getMusic().play();
            }
        } else {
            setFrame(0);
            if (GameScreen.getMusic().isPlaying()) {
                GameScreen.getMusic().pause();
            }
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setAngle(90);
        setBottom(worldBounds.getBottom());
        setRight(worldBounds.getRight()-0.04f);

    }
}
