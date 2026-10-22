package ru.ibelykh.sickdopeskills.buttons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.ibelykh.sickdopeskills.audio.AudioPort;
import ru.ibelykh.sickdopeskills.base.ScaledButton;
import ru.ibelykh.sickdopeskills.math.Rect;

public class ButtonGameSoundOffOn extends ScaledButton {

    private final AudioPort audio;

    public ButtonGameSoundOffOn(TextureAtlas atlas, AudioPort audio) {
        super(atlas.findRegion("sound"),1,2,2);
        this.audio = audio;
        setHeightProportion(0.08f);
        setFrame(1);
    }

    @Override
    public void actionPerformed() {
        if (frame==0){
            setFrame(1);
            if (!audio.isPlaying()) {
                audio.play();

            }
        } else {
            setFrame(0);
            if (audio.isPlaying()) {
                audio.pause();
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

    @Override
    public void update(float delta) {
        super.update(delta);
        if(audio.isPlaying()){
            setFrame(1);
        }else {
            setFrame(0);
        }
    }
}
