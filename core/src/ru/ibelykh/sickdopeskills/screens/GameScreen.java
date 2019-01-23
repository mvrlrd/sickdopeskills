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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;


import java.util.List;

import javax.swing.plaf.FontUIResource;

import ru.ibelykh.sickdopeskills.base.Base2DScreen;
import ru.ibelykh.sickdopeskills.base.Font;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.pools.FlagPool;
import ru.ibelykh.sickdopeskills.pools.TreePool;
import ru.ibelykh.sickdopeskills.sprites.Flag;
import ru.ibelykh.sickdopeskills.sprites.Rider;
import ru.ibelykh.sickdopeskills.sprites.Shouting;
import ru.ibelykh.sickdopeskills.sprites.Snow;
import ru.ibelykh.sickdopeskills.sprites.YouCool;
import ru.ibelykh.sickdopeskills.utils.FlagEmitter;
import ru.ibelykh.sickdopeskills.utils.TreeEmitter;


public class GameScreen extends Base2DScreen {

    private int countPoints;
    private boolean gotPoint;
    private boolean what;

    private static final String FRAGS = "Frags: ";
    //	private static final String HP = "HP: ";
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
    private boolean isItNeedToShout;
    private float interval = 0f;

    float inter = 0f;
    int a;

    private TreePool treePool;
    private TreeEmitter treeEmitter;
    private TextureAtlas treeAtlas;

    private YouCool youCool;
    private int coolMoments;

    private TextureAtlas alenaAtlas;

    private int frags;

    private Font font;
    private StringBuilder sbFrags = new StringBuilder();
    private StringBuilder sbHp = new StringBuilder();
    private StringBuilder sbLvl = new StringBuilder();

//	private Splash[] splash;

    public GameScreen(Game game) {
        super(game);

    }

    @Override
    public void show() {
        super.show();

        textureAtlas = new TextureAtlas("images/star.atlas");

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/strange.mp3"));
        soundCheck = Gdx.audio.newSound(Gdx.files.internal("sounds/pau.wav"));

        worldBounds = getWorldBounds();


//		splash = new Splash[500];
//		for (int i = 0; i <splash.length ; i++) {
//			splash[i]= new Splash(textureAtlas);
//		}
        dogHouseAtl = new TextureAtlas("images/alenaSprites.atlas");

        rider = new Rider(dogHouseAtl, worldBounds);

        //STAR

        snow = new Snow[SNOW_COUNT];
        for (int i = 0; i < snow.length; i++) {
            snow[i] = new Snow(textureAtlas);
        }

//		rectangle = dogHouse1.getPath();

        alenaAtlas = new TextureAtlas("images/alena.atlas");

        flagPool = new FlagPool(rider, worldBounds);
        flagEmitter = new FlagEmitter(worldBounds, flagPool, alenaAtlas);

        spriteBatch = new SpriteBatch();

        music.play();
        music.setLooping(true);

        shapeRenderer = new ShapeRenderer();


        shapeRenderer.setColor(Color.LIME);
        shapeRenderer.rotate(0, 0, 0, 0);


        shoutingAtlas = new TextureAtlas("images/shoutingAtlas.atlas");
        shouting = new Shouting(shoutingAtlas, worldBounds);

        treeAtlas = new TextureAtlas("images/trees.atlas");
        treePool = new TreePool(rider, worldBounds);
        treeEmitter = new TreeEmitter(worldBounds, treePool, treeAtlas);

        youCool = new YouCool(alenaAtlas, worldBounds);

        font = new Font("font/font.fnt", "font/font.png");
        font.setFontSize(FONT_SIZE);
        font.setColor(Color.BLUE);
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
        Gdx.gl.glClearColor(1, 1, 1, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        flagPool.drawActiveSprites(batch);
        treePool.drawActiveSprites(batch);
//		for (int i = 0; i <splash.length ; i++) {
////			splash[i].draw(batch);
//		}

        if (coolMoments > 3) {

            youCool.draw(batch);

        }


        rider.draw(batch);


        for (int i = 0; i < snow.length; i++) {
            snow[i].draw(batch);
        }
        if (isItNeedToShout) {

            shouting.draw(batch);

        }
        printInfo();
        batch.end();


        shapeRenderer.setProjectionMatrix(worldToGl);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(rider.getBoard().x, rider.getBoard().y, rider.getBoard().width,
                rider.getBoard().height);


        shapeRenderer.end();
        spriteBatch.begin();
        spriteBatch.end();

    }

    public void printInfo() {
        sbHp.setLength(0);
        sbLvl.setLength(0);
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(countPoints), worldBounds.getLeft(), worldBounds.getTop());     // font.draw(batch, "Frags:"+ frags) --- так плохо потому что будет создаваться каждый раз новая строка для frags и для "frags" итого 120 строк в сек

    }

    public void deleteAllDestroyed() {
        flagPool.freeAllDestroyedActiveSprites();
        treePool.freeAllDestroyedActiveSprites();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        rider.resize(worldBounds, worldBounds.getLeft() + 1.2f * rider.getWidth(), 0f);
        //STAR
        for (int i = 0; i < snow.length; i++) {
            snow[i].resize(worldBounds);
        }

//		for (int i = 0; i <splash.length ; i++) {
//			splash[i].resize(worldBounds,dogHouse1.getLeft(),dogHouse1.getTop());
//		}
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
        List<Flag> flagList = flagPool.getActiveObjects();
//        float minDist = rider.getHalfHeight();
        for (Flag flag : flagList) {

//            if (isWrongWay(flag)) {
//                countPoints = 0;
////                gotPoint = false;
////                isItNeedToShout = false;
////                shouting.setSick(false);
//                shouting.setFrame(1);
//                isItNeedToShout = true;
////                shouting.setSick(true);
//            }
                if ((isAccident(flag))||(isWrongWay(flag))) {


                    shouting.setFrame(1);
                    shouting.setSick(true);
                    countPoints = 0;
                    isItNeedToShout = true;
                    rider.isDestroyed();
                    music.setVolume(0.3f);
                    setIsPlaying(false);
                    countClicks = 0;
                    rider.gameOver();
                    flag.setDestroyed(true);

                }
            if (isOnRightWay(flag)){
                shouting.setFrame(2);
                isItNeedToShout=true;


            }



//            if ((flag.pos.dst2(rider.pos) < minDist)) {

//
//
//				inter+=delta;
////				System.out.println(inter+"  INTER");
//				if ((inter>0.8f)){
//					coolMoments++;
//					inter=0f;
//					System.out.println(coolMoments+"  COooll");
//					a = Rnd.nextInt(0, 3);
//					if (a == 1){
//						a=2;
//
//					}
//
//					gotPoint=true;
//
//
//					inter=0f;
//					shouting.setFrame(a);
//					isItNeedToShout=true;
//					shouting.setSick(false);
//				}



//			if (isRightWay(flag)) {

//				ifCloseToFlag(flag, minDist, delta);

//				what = gotPoint;
//				pointsCounter(flag);
//				gotPoint = false;
//			}

                //если неправильно объехал флажок
//			else {
////				isItWrongWay = true;
//				countPoints = 0;
//				gotPoint = false;
////				isItNeedToShout = false;
////				shouting.setSick(false);
//				shouting.setFrame(1);
//				isItNeedToShout = true;
//				shouting.setSick(true);
//			}

//		ifWasAnAccident(flag);


            }
//            what = gotPoint;
//			pointsCounter();
//            gotPoint = false;

            //если неправильно объехал флажок




//
//
//            if (
//                    !(flag.getRight() < (rider.getBoardLeft())
//                            || flag.getLeft() > (rider.getBoardRight())
//                            || flag.getBottom() > (rider.getBoardTop())
//                            || flag.getTop() < (rider.getBoardBottom()))
//
//            ) {
//                isItNeedToShout = false;
//                shouting.setFrame(1);
//                shouting.setSick(true);
//                countPoints = 0;
//                isItNeedToShout = true;
//                rider.isDestroyed();
//                music.setVolume(0.3f);
//                setIsPlaying(false);
//                countClicks = 0;
//                rider.gameOver();
//                flag.setDestroyed(true);
//            }
//        }
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
        return (!(rider.getBoardBack()>flag.getRight()
                || rider.getBoardNose()<flag.getLeft()
                || rider.getBoardTop()<flag.getBottom()
                || rider.getBoardBottom()>flag.getTop()));
    }

    // WRONGWAY
    public boolean isWrongWay(Flag flag){
        return ((rider.getBoardNose() > flag.getRight())
                &&
                (((flag.isItRed())
                        && (rider.getBoardBottom() > flag.getTop())) //Выше красного
                || ((!flag.isItRed())
                        && (rider.getBoardTop() < flag.getBottom()))));//ниже синего
    }
    /////




    public void startNewGame() {
        countPoints = 0;
        isItNeedToShout = false;
        shouting.setSick(false);
        music.setVolume(1f);
        isPlaying = true;
        rider.setTheNewGame();
    }

    public static boolean getIsPlaying() {
        return isPlaying;
    }

    private static void setIsPlaying(boolean isPlaying) {
        GameScreen.isPlaying = isPlaying;
    }


    private void pointsCounter() {
        if ((what == gotPoint) && (what == true)) {
            countPoints++;
        }





    }
}