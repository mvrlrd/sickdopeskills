package ru.ibelykh.sickdopeskills.buttons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.ibelykh.sickdopeskills.audio.AudioPort;
import ru.ibelykh.sickdopeskills.base.ScaledButton;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.screens.GameScreen;

public class ButtonPause extends ScaledButton {
boolean musicStatus;
    private final AudioPort audio;

    public ButtonPause(TextureAtlas atlas, AudioPort audio) {
        super(atlas.findRegion("stopPlay"),2,1,2);
        this.audio = audio;
        setHeightProportion(0.08f);
        setFrame(0);
    }

    @Override
    public void actionPerformed() {
        if (frame==1){
            onPlay();
        } else if (frame == 0){
           onPause();
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setAngle(90);
        setTop(worldBounds.getTop());
        setRight(worldBounds.getRight()-0.04f);

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(GameScreen.getIsPlaying()){
            setFrame(0);
        }else if (GameScreen.isOnPause()){
            setFrame(1);
        }
    }

    public void onPlay(){

            if (GameScreen.isOnPause()){
            setFrame(0);
            GameScreen.setOnPause(false);
            GameScreen.getRider().resume();
            GameScreen.isPlaying = true;
            if (!audio.isPlaying()) {
                // Прежний GameScreen.setMusic(musicStatus) разворачивался ровно в это:
                // true -> play, false -> pause. Звук восстанавливается в то состояние,
                // в котором был до паузы.
                if (musicStatus) {
                    audio.play();
                } else {
                    audio.pause();
                }
            }
        }
//            System.out.println("play");
    }

    public void onPause(){
        if (GameScreen.isPlaying) {
            if (audio.isPlaying()) {
                musicStatus = true;
                audio.pause();
            } else {
                musicStatus = false;
            }
            setFrame(1);
            GameScreen.setOnPause(true);
            GameScreen.getRider().pause();
            GameScreen.isPlaying = false;

//            System.out.println("pause");
        }
    }



}
