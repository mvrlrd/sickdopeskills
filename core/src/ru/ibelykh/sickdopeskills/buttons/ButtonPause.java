package ru.ibelykh.sickdopeskills.buttons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.ibelykh.sickdopeskills.base.ScaledButton;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.screens.GameScreen;

public class ButtonPause extends ScaledButton {
boolean musicStatus;

    public ButtonPause(TextureAtlas atlas) {
        super(atlas.findRegion("stopPlay"),2,1,2);
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
            if (!GameScreen.getMusic().isPlaying()) {
                GameScreen.setMusic(musicStatus);
            }
        }
//            System.out.println("play");
    }

    public void onPause(){
        if (GameScreen.isPlaying) {
            if (GameScreen.getMusic().isPlaying()) {
                musicStatus = true;
                GameScreen.setMusic(false);
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
