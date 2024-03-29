package ru.ibelykh.sickdopeskills.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;


import java.util.List;

import javax.swing.plaf.FontUIResource;

import ru.ibelykh.sickdopeskills.base.Base2DScreen;
import ru.ibelykh.sickdopeskills.base.Font;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.pools.FlagPool;
import ru.ibelykh.sickdopeskills.pools.TreePool;
import ru.ibelykh.sickdopeskills.sprites.*;
import ru.ibelykh.sickdopeskills.utils.FlagEmitter;
import ru.ibelykh.sickdopeskills.utils.TreeEmitter;


public class GameScreen extends Base2DScreen {

    private static int countPoints;

    private static final String POINTS = "pts: ";
    	private static final String SPEED = "spd: ";
//	private static final String LVL = "level: ";
    private static final int SNOW_COUNT = 10000;
    private static final float FONT_SIZE = 0.08f;

    private TextureAtlas textureAtlas;
    private Snow[] snow;

    private boolean state;
    private boolean isAllRight;

    private Rider rider;
    private TextureAtlas dogHouseAtl;

    private static boolean isPlaying;
    private int countClicks;

    private FlagPool flagPool;
    private FlagEmitter flagEmitter;

    private Music music;
    private Sound soundCheck;

    private SpriteBatch spriteBatch;

    private ShapeRenderer shapeRenderer;


    private TextureAtlas shoutingAtlas;
    private Shouting shouting;
    private static boolean isItNeedToShout;
    private float interval = 0f;

    float inter = 0f;
    int a;

    private TreePool treePool;
    private TreeEmitter treeEmitter;
    private TextureAtlas treeAtlas;

    private YouCool youCool;
    private int coolMoments;

    private TextureAtlas alenaAtlas;

    private StartGates startGates;

    private int frags;

    private Font font;
    private StringBuilder sbFrags = new StringBuilder();
    private StringBuilder sbHp = new StringBuilder();
    private StringBuilder sbLvl = new StringBuilder();

    List<Flag> flagList;

//	private Splash[] splash;

    public GameScreen(Game game) {
        super(game);

    }

    @Override
    public void show() {
        super.show();


        shapeRenderer = new ShapeRenderer();

        textureAtlas = new TextureAtlas("images/snow.atlas");
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/lord_of_boards.mp3"));
        soundCheck = Gdx.audio.newSound(Gdx.files.internal("sounds/pau.wav"));
        worldBounds = getWorldBounds();
        dogHouseAtl = new TextureAtlas("images/alenaSprites.atlas");
        rider = new Rider(dogHouseAtl, worldBounds);

        startGates = new StartGates(dogHouseAtl,worldBounds);
        //STAR

        snow = new Snow[SNOW_COUNT];

        for (int i = 0; i < snow.length; i++) {
            Vector2 v = new Vector2();
            v.set(Rnd.nextFloat(-0.18f,-0.36f),Rnd.nextFloat(-0.5f,-0.001f));
            snow[i] = new Snow(textureAtlas, v);
        }

        alenaAtlas = new TextureAtlas("images/alena.atlas");

        flagPool = new FlagPool( worldBounds);
        flagEmitter = new FlagEmitter(worldBounds, flagPool, alenaAtlas);

        spriteBatch = new SpriteBatch();

        music.play();
        music.setLooping(true);




//        shapeRenderer.setColor(Color.ROYAL);


        shoutingAtlas = new TextureAtlas("images/shoutingAtlas.atlas");
        shouting = new Shouting(shoutingAtlas, worldBounds);

        treeAtlas = new TextureAtlas("images/trees.atlas");
        treePool = new TreePool(rider, worldBounds);
        treeEmitter = new TreeEmitter(worldBounds, treePool, treeAtlas);

        youCool = new YouCool(alenaAtlas, worldBounds);

        font = new Font("font/font.fnt", "font/font.png");
        font.setFontSize(FONT_SIZE);
        font.setColor(Color.BLUE);
     flagList = flagPool.getActiveObjects();

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        deleteAllDestroyed();
        draw();

    }

    public void update(float delta) {
        //STAR
        for (int i = 0; i < snow.length; i++) {
            snow[i].update(delta);
        }

        startGates.update(delta);

        rider.update(delta);

//		for (int i = 0; i <splash.length ; i++) {
//
////			splash[i].update(delta, dogHouse1.getBoardLeft(), Rnd.nextFloat(dogHouse1.getBoardBottom(),
////					dogHouse1.getBoardTop()),dogHouse1.getState());
//		}

        if (((shouting.getFrame() == 0) || (shouting.getFrame() == 2)) && (isItNeedToShout)) {
            interval += delta;
        }

        if (interval > 1.2f) {
            isItNeedToShout = false;
            interval = 0;
        }

        treePool.updateActiveSprites(delta);
        treeEmitter.generateTreesTop(delta);
        treeEmitter.generateTreesBottom(delta);

        flagPool.updateActiveSprites(delta);
        flagEmitter.generateFlags(delta);

        youCool.update(delta);

        shouting.update(delta);
        checkCollisions(delta);
    }

    public void draw() {
        batch.begin();
        Gdx.gl.glClearColor(.8f, .8f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        flagPool.drawActiveSprites(batch);
        treePool.drawActiveSprites(batch);
        if ((countPoints%10==0)&&(countPoints!=0)) {
            youCool.draw(batch);
        }
        startGates.draw(batch);


        rider.draw(batch);
        for (int i = 0; i < snow.length; i++) {
            snow[i].draw(batch);
        }
        if (isItNeedToShout) {
            shouting.draw(batch);
        }
        printInfo();
        batch.end();

//Это все показывает прямоугольники сноуборда и флажков, которые нужны для рассчета коллизий
//        shapeRenderer.setProjectionMatrix(worldToGl);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.rect(rider.getBoard().x, rider.getBoard().y, rider.getBoard().width,
//                rider.getBoard().height);

//        for (Flag flag : flagList) {
//            shapeRenderer.rect(flag.getBigRectLeft(), flag.getBigRectBot(),
//                    flag.getBigRect().width, flag.getBigRect().height);
//        }
//        shapeRenderer.end();
        spriteBatch.begin();
        spriteBatch.end();
    }

    private void printInfo() {
        sbHp.setLength(0);
        sbLvl.setLength(0);
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(POINTS).append(countPoints), worldBounds.getLeft(), worldBounds.getTop());     // font.draw(batch, "Frags:"+ frags) --- так плохо потому что будет создаваться каждый раз новая строка для frags и для "frags" итого 120 строк в сек
//        font.draw(batch, sbFrags.append(SPEED).append(rider.getSpeed().y), worldBounds.getLeft(), worldBounds.getBottom()+0.2f);
    }

    private void deleteAllDestroyed() {
        flagPool.freeAllDestroyedActiveSprites();
        treePool.freeAllDestroyedActiveSprites();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);

        startGates.resize(worldBounds,worldBounds.getLeft()+0.1f,0f);


        rider.resize(worldBounds, worldBounds.getLeft() + 1.2f * rider.getWidth(), 0f);
        //STAR
        for (int i = 0; i < snow.length; i++) {
            snow[i].resize(worldBounds);
        }
    }


    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
        treeAtlas.dispose();
        dogHouseAtl.dispose();
        flagPool.dispose();
        soundCheck.dispose();
        music.dispose();
        textureAtlas.dispose();
        alenaAtlas.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        countClicks++;
        if (countClicks == 1) {
            startNewGame();
        }
        rider.touchDown(touch, pointer);
        return super.touchDown(touch, pointer);
    }


    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        rider.touchUp(touch, pointer);
        return super.touchUp(touch, pointer);
    }

    private void checkCollisions(float delta) {
//        List<Flag> flagList = flagPool.getActiveObjects();

//        float minDist = rider.getHalfHeight();


        for (Flag flag : flagList) {
              if ((isAccident(flag))) {
                    setShoutFrame(false,flag);
                    countPoints = 0;
                    isItNeedToShout = true;
                    rider.isDestroyed();
                    music.setVolume(0.3f);
                    setIsPlaying(false);
                    countClicks = 0;
                    rider.gameOver();
                    flag.setDestroyed(true);
                }
                if ((isWrongWay(flag))){
                    shouting.setFrame(1);
                    shouting.setSick(true);

                    isItNeedToShout = true;

                }

            if (isOnRightWay(flag)) {
                setShoutFrame(true,flag);

if (!flag.isDestroyed()) {
    isItNeedToShout = true;
}

            }

            }
    }

    private void setShoutFrame(boolean isEverithingOk,Flag flag){
        if (isEverithingOk) {
            int[] frames = {0, 2};
            shouting.setFrame(frames[flag.getShoutFrame()]);

        }
        else {
            shouting.setFrame(1);


        }
        shouting.setSick(true);
    }




public boolean isOnRightWay(Flag flag){
    return ((rider.getBoardNose() > flag.getLeft())
            &&
            (((flag.isItRed())
                    && (rider.getBoardTop() < flag.getBottom())) //ниже красного
                    || ((!flag.isItRed())
                    && (rider.getBoardBottom() > flag.getTop()))));//выше синего

}

    public boolean isAccident(Flag flag) {

        return (!(
                rider.getBoardBack()>flag.getBigRectRight()
                || rider.getBoardNose()<flag.getBigRectLeft()

                || rider.getBoardTop()<flag.getBigRectBot()
                || rider.getBoardBottom()>flag.getBigRectTop()));

    }
    // WRONGWAY
    public boolean isWrongWay(Flag flag){
        return ((rider.getBoardNose() > flag.getRight())&&(rider.getBoardNose()-flag.getLeft()<
                rider.getBoard().width)
                &&
                (((flag.isItRed())
                        && (rider.getBoardBottom() > flag.getBottom())) //Выше красного
                || ((!flag.isItRed())
                        && (rider.getBoardTop() < flag.getTop()))));//ниже синего
    }
    /////




    public void startNewGame() {
        countPoints = 0;
        isItNeedToShout = false;
        shouting.setSick(false);
        music.setVolume(1f);
        isPlaying = true;
        rider.setTheNewGame();
        startGates.setTheNewGame();
    }

    public static boolean getIsPlaying() {
        return isPlaying;
    }

    private static void setIsPlaying(boolean isPlaying) {
        GameScreen.isPlaying = isPlaying;
    }

    public static   void setCountPoints(int a) {
        countPoints = a;
    }

    public static int getCountPoints() {
        return countPoints;
    }

    public static boolean isItNeedToShout() {
        return isItNeedToShout;
    }

    public static void setIsItNeedToShout(boolean isItNeedToShout) {
        GameScreen.isItNeedToShout = isItNeedToShout;
    }
}
