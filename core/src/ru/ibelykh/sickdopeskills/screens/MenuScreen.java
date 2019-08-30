package ru.ibelykh.sickdopeskills.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.ibelykh.sickdopeskills.base.Base2DScreen;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.sprites.*;

public class MenuScreen extends Base2DScreen {
    private static final int STAR_COUNT = 3000;

    private static TextureAtlas allSprites;
    private static Music menuMusic;
    private static Snow[] snow;
    private static ButtonExit buttonExit;
    private static ButtonGetRider btnGetRider;
    private static ButtonNewGame buttonNewGame;


    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        allSprites = new TextureAtlas("images/sprites/allSprites.atlas");

        float musicVolume = 0.5f;
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/strange.mp3"));
        menuMusic.setVolume(musicVolume);
        menuMusic.setLooping(true);

        snow = new Snow[STAR_COUNT];
        for (int i = 0; i <snow.length ; i++) {
            Vector2 v = new Vector2();
            v.set(Rnd.nextFloat(-0.18f,-0.36f),Rnd.nextFloat(-0.5f,-0.001f));
            snow[i]= new Snow(allSprites,v);
        }

        buttonExit = new ButtonExit(allSprites);
        btnGetRider = new ButtonGetRider(allSprites.findRegion("riders"),3,2,6);
        buttonNewGame = new ButtonNewGame(allSprites,game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    private void update(float delta) {
        for (int i = 0; i <snow.length ; i++) {
            snow[i].update(delta);
        }
    }

    private void draw() {
        batch.begin();
        Gdx.gl.glClearColor(0.1f,0.1f,1f,2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (int i = 0; i <snow.length ; i++) {  //Star 5
            snow[i].draw(batch);
        }
        buttonExit.draw(batch);
        btnGetRider.draw(batch);
        buttonNewGame.draw(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        for (int i = 0; i <snow.length ; i++) {  //Star 6
            snow[i].resize(worldBounds);
        }
        buttonExit.resize(worldBounds);
        btnGetRider.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
        allSprites.dispose();
        menuMusic.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        buttonExit.touchDown(touch,pointer);
        btnGetRider.touchDown(touch, pointer);
        buttonNewGame.touchDown(touch,pointer);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        buttonExit.touchUp(touch,pointer);
        btnGetRider.touchUp(touch, pointer);
        buttonNewGame.touchUp(touch,pointer);
        return super.touchUp(touch, pointer);
    }

    public static ButtonGetRider getBtnGetRider() {
        return btnGetRider;
    }
}
