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
import ru.ibelykh.sickdopeskills.sprites.Background;
import ru.ibelykh.sickdopeskills.sprites.ButtonExit;
import ru.ibelykh.sickdopeskills.sprites.ButtonNewGame;
import ru.ibelykh.sickdopeskills.sprites.Snow;

public class MenuScreen extends Base2DScreen {



    //STAR
    private static final int STAR_COUNT = 3000;
    private TextureAtlas textureAtlas;
    private Snow[] snow;
    //BACKGROUND
    private Background background;
    private TextureAtlas bg;
    //Buttons
    private ButtonExit buttonExit;
    private  TextureAtlas btAtlas;


    private Music menuMusic;

    private ButtonNewGame buttonNewGame;

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();



        //BACKGROUND SHOW
        bg = new TextureAtlas("backgrounds/backgrounds.atlas");
        background = new Background(bg,"menubg");

        //STAR_SHOW
        textureAtlas = new TextureAtlas("images/snow.atlas");
        snow = new Snow[STAR_COUNT];

        for (int i = 0; i <snow.length ; i++) {
            Vector2 v = new Vector2();
            v.set(Rnd.nextFloat(-0.18f,-0.36f),Rnd.nextFloat(-0.5f,-0.001f));
            snow[i]= new Snow(textureAtlas,v);
        }
        btAtlas = new TextureAtlas("buttons/buttons.atlas");
        buttonExit = new ButtonExit(btAtlas);
        buttonNewGame = new ButtonNewGame(btAtlas,game);
//
        float musicVolume = 0.5f;
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/strange.mp3"));
        menuMusic.setVolume(musicVolume);
        menuMusic.setLooping(true);
        menuMusic.play();


    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    public void update(float delta) {
        //STAR UPDATE
        for (int i = 0; i <snow.length ; i++) {
            snow[i].update(delta);
        }
    }

    public void draw() {
        batch.begin();

        Gdx.gl.glClearColor(0.1f,0.1f,1f,2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //BACKGROUND DRAW

//        background.draw(batch);
        //STAR DRAW
        for (int i = 0; i <snow.length ; i++) {  //Star 5
            snow[i].draw(batch);
        }
        // Buttons
        buttonExit.draw(batch);
        buttonNewGame.draw(batch);



        batch.end();

    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        //STAR RESIZE
        for (int i = 0; i <snow.length ; i++) {  //Star 6
            snow[i].resize(worldBounds);
        }
        //BACKGROUND RESIZE
        background.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
//        bg.dispose();
        textureAtlas.dispose(); //star
        btAtlas.dispose();
        menuMusic.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        buttonExit.touchDown(touch,pointer);
        buttonNewGame.touchDown(touch,pointer);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        buttonExit.touchUp(touch,pointer);
        buttonNewGame.touchUp(touch,pointer);
        return super.touchUp(touch, pointer);
    }
}
