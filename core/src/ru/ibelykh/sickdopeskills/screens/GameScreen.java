package ru.ibelykh.sickdopeskills.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import ru.ibelykh.sickdopeskills.base.Base2DScreen;
import ru.ibelykh.sickdopeskills.base.Font;
import ru.ibelykh.sickdopeskills.buttons.ButtonGameSoundOffOn;
import ru.ibelykh.sickdopeskills.buttons.ButtonPause;
import ru.ibelykh.sickdopeskills.math.MatrixUtils;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.pools.FlagPool;
import ru.ibelykh.sickdopeskills.pools.TreePool;
import ru.ibelykh.sickdopeskills.sprites.*;
import ru.ibelykh.sickdopeskills.utils.FlagEmitter;
import ru.ibelykh.sickdopeskills.utils.TreeEmitter;


public class GameScreen extends Base2DScreen {

    private static final String HEIGHPTS = "top: ";
    private static final float FONT_SIZE = 0.1f;

    public static boolean isPlaying;
    public static boolean isGameOver;
    private static boolean onPause;
    private static boolean isItNeedToShout;

    private static int points;
    private static int highScore;
    private static int countClicks;

    private static float interval = 0f;

    private static StringBuilder sbFrags = new StringBuilder();
    private static StringBuilder sbDist = new StringBuilder();
    private static StringBuilder sbHeighPts = new StringBuilder();

    private static List<Flag> flagList;
    private static List<Tree> treeList;

    private TextureAtlas allSprites;
    private static Font font;
    private static Music music;
    private static Sound soundCheck;

    private static Preferences prefs;
    private static ShapeRenderer shapeRenderer;

    private static FlagPool flagPool;
    private static FlagEmitter flagEmitter;
    private static TreePool treePool;
    private static TreeEmitter treeEmitter;



    private static Rider rider;
    private static Shouting shouting;
    private static YouCool youCool;
    private static StartGates startGates;

    private static ButtonGameSoundOffOn buttonGameSoundOffOn;
    private static ButtonPause buttonPause;

    private static Snow[] snow;
    private static Splash[] splash;
//
//   private Matrix4 mx4Font;
//    private SpriteBatch spriteFont;


    public GameScreen(Game game) {
        super(game);
    }



    @Override
    public void show() {
        super.show();

        worldBounds = getWorldBounds();

        allSprites = new TextureAtlas("images/sprites/allSprites.atlas");
        startGates = new StartGates(allSprites, worldBounds);
        rider = new Rider(allSprites, worldBounds);
        shouting = new Shouting(allSprites, worldBounds);
        youCool = new YouCool(allSprites, worldBounds);

        treePool = new TreePool( worldBounds);
        flagPool = new FlagPool(worldBounds);
        flagList = flagPool.getActiveObjects();
        treeList = treePool.getActiveObjects();
        flagEmitter = new FlagEmitter(worldBounds, flagPool, allSprites);
        treeEmitter = new TreeEmitter(worldBounds, treePool, allSprites);

        buttonGameSoundOffOn = new ButtonGameSoundOffOn(allSprites);
        buttonPause = new ButtonPause(allSprites);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.RED);

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/bad_guy.mp3"));
        music.setLooping(true);
        soundCheck = Gdx.audio.newSound(Gdx.files.internal("sounds/pau.wav"));

        //preferences (music, high score)
        prefs=Gdx.app.getPreferences("high score and sound");
        if (prefs.getBoolean("soundOff")){
            music.pause();
        } else {
            music.play();
        }
        highScore = prefs.getInteger("pts");

        font = new Font("font/mario_font.fnt", "font/mario_font.png");
        font.setColor(Color.BLACK);
        font.setFontSize(FONT_SIZE);


//        mx4Font = new Matrix4();
//        spriteFont = new SpriteBatch();
//        Vector3 fontRotationVector = new Vector3(1, 1, 0);
//
//
//
//
//        mx4Font.setToRotation(fontRotationVector, 180);
//        spriteFont.setTransformMatrix(mx4Font);


        //SPLASH
        int SPLASH_COUNT = 20;
        splash = new Splash[SPLASH_COUNT];
        for (int i = 0; i < splash.length; i++) {
            splash[i] = new Splash(allSprites);
        }
        //SNOW
        int SNOW_COUNT = Rnd.nextInt(500, 10000);
        snow = new Snow[SNOW_COUNT];
        float windDirectionX = Rnd.nextFloat(-0.7f, 0.7f); //wind speed on X
        float windDirectionY = Rnd.nextFloat(-0.7f, 0.7f); //wind speed on Y
        for (int i = 0; i < snow.length; i++) {
            Vector2 snowSpeed = new Vector2();
            snowSpeed.set(Rnd.nextFloat(windDirectionX - 0.1f, windDirectionX + 0.1f), Rnd.nextFloat(windDirectionY - 0.1f, windDirectionY + 0.1f));
            snow[i] = new Snow(allSprites, snowSpeed);
        }

//        fontt = new BitmapFont(Gdx.files.internal("font/snowCapFont.fnt"),
//                Gdx.files.internal("font/snowCapFont.png"), true);
//        fontt.setColor(Color.BLACK);
//        fontt.getData().setScale(4f);
//
//        position = new Vector3(400, 400, 0f);
    }


    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        deleteAllDestroyed();
        draw();




    }


    public void update(float delta) {
        startGates.update(delta);
        rider.update(delta);
        for (int i = 0; i < snow.length; i++) {
            snow[i].update(delta);
        }
        for (int i = 0; i < splash.length; i++) {
            splash[i].update(delta);
        }


        if (((shouting.getFrame() == Shouting.DOPE) ||
                (shouting.getFrame() == Shouting.WHOA)) && (isItNeedToShout)) {
            interval += delta;
        }
        if (interval > 1.2f) {
            isItNeedToShout = false;
            interval = 0;
        }

        if (points>prefs.getInteger("pts")){
            prefs.putInteger("pts",points);
            prefs.flush();
            highScore=points;
        }
        if (music.isPlaying()){
            prefs.putBoolean("soundOff",false);
            prefs.flush();
        } else if(!music.isPlaying()){
            prefs.putBoolean("soundOff",true);
            prefs.flush();
        }
        treePool.updateActiveSprites(delta);
        treeEmitter.generateTreesTop(delta);
        treeEmitter.generateTreesBottom(delta);
        flagPool.updateActiveSprites(delta);
        flagEmitter.generateFlags(delta);
        youCool.update(delta);

        shouting.update(delta);
        checkCollisions();
        buttonGameSoundOffOn.update(delta);
        buttonPause.update(delta);
//        System.out.println("tree  "+treePool.getActiveObjects().size()+" free: "+treePool.getFreeObjects().size());
//        System.out.println("flag  "+flagPool.getActiveObjects().size()+" free: "+flagPool.getFreeObjects().size());
    }

    private void draw() {
        batch.begin();
        Gdx.gl.glClearColor(.8f, .8f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        flagPool.drawActiveSprites(batch);
        if (isPlaying) {
            for (int i = 0; i < splash.length; i++) {
                splash[i].draw(batch);
            }
        }
        rider.draw(batch);
        treePool.drawActiveSprites(batch);
        if ((points % 10 == 0) && (points != 0)) {
            youCool.draw(batch);
        }
        startGates.draw(batch);
        if ((isItNeedToShout)){

            shouting.draw(batch);
        }
        for (int i = 0; i < snow.length; i++) {
            snow[i].draw(batch);
        }
        buttonGameSoundOffOn.draw(batch);
        buttonPause.draw(batch);


//        if (isGameOver) {
//
//            if (prefs.getInteger("pts") != 0) {
//                font.draw(batch, "gdf", 0f, 0f);
//
//            }
//        }


//        printInfo();
        batch.end();
        printInfo2();

//Это все показывает прямоугольники сноуборда и флажков, которые нужны для рассчета коллизий
//        shapeRenderer.setProjectionMatrix(worldToGl);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//
//
//        shapeRenderer.rect(rider.getBoard2().getLeft(),rider.getBoard2().getBottom(),rider.getBoard2().getWidth(),rider.getBoard2().getHeight());

//        for (Flag flag : flagList) {
//            shapeRenderer.rect(flag.getCrashArea().getLeft(),flag.getCrashArea().getBottom(), flag.getCrashArea().getWidth(), flag.getCrashArea().getHeight());
//        }
//        for (Tree tree : treeList) {
//            shapeRenderer.rect(tree.getTrunk().getLeft(),tree.getTrunk().getBottom(),tree.getTrunk().getWidth(),tree.getTrunk().getHeight());
//
//        }
//        shapeRenderer.end();


    }
    private void printInfo2() {


        sbFrags.setLength(0);
        sbFrags.append(points);



        batchFont.begin();

        font.draw(batchFont,
                sbFrags,
                worldBounds.getLeft()+worldBounds.getHalfWidth(), worldBounds.getTop());



        if (isGameOver){
            sbHeighPts.setLength(0);
            sbHeighPts.append(highScore);
            font.draw(batchFont,
                    sbHeighPts,
                    worldBounds.getLeft()+worldBounds.getHalfWidth()-FONT_SIZE/2, worldBounds.getTop()-1f);
        }
        batchFont.end();
    }
//    private void printInfo() {
//        sbDist.setLength(0);
//        sbHeighPts.setLength(0);
//        sbFrags.setLength(0);
//
//
//sbFrags.append(points);
//
//        font.draw(batch,
//                sbHeighPts.append(highScore),
//                worldBounds.getLeft()+FONT_SIZE, worldBounds.getTop()
//                );
//    }

    private void deleteAllDestroyed() {
        flagPool.freeAllDestroyedActiveSprites();
        treePool.freeAllDestroyedActiveSprites();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        startGates.resize(worldBounds, worldBounds.getLeft() + 0.1f, 0f);
        rider.resize(worldBounds, worldBounds.getLeft() + 1.2f * rider.getWidth(), 0f);
        //snow
        for (int i = 0; i < snow.length; i++) {
            snow[i].resize(worldBounds);
        }
        //splash
        for (int i = 0; i < splash.length; i++) {
            splash[i].resize(worldBounds);
        }
        buttonGameSoundOffOn.resize(worldBounds);
        buttonPause.resize(worldBounds);
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
        font.dispose();
        flagPool.dispose();
        soundCheck.dispose();
        music.dispose();
        allSprites.dispose();


    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {

        if((!buttonGameSoundOffOn.isMe(touch))&&(!buttonPause.isMe(touch))&&(!onPause)) {
            countClicks++;
            if (countClicks == 1) {
                startNewGame();
            }
            rider.touchDown(touch, pointer);
        }
        if ((onPause)||(isPlaying)) {
            buttonPause.touchDown(touch, pointer);
        }

        if ((buttonPause.getFrame()==1)&&(!buttonPause.isMe(touch))&&(!buttonGameSoundOffOn.isMe(touch))){
          buttonPause.onPlay();
        }


        buttonGameSoundOffOn.touchDown(touch, pointer);

        return super.touchDown(touch, pointer);
    }


    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if((!buttonGameSoundOffOn.isMe(touch))&&(!buttonPause.isMe(touch))) {
            rider.touchUp(touch, pointer);
        }

        buttonGameSoundOffOn.touchUp(touch, pointer);
        buttonPause.touchUp(touch, pointer);
        return super.touchUp(touch, pointer);
    }

    private void checkCollisions() {

        for (Flag flag : flagList) {
            if ((isAccident(flag))) {
                shouting.setFrame(1);

                gameOver();
                System.out.println("flag Crush");
//                flag.setDestroyed(true);

            }
            if ((isWrongWay(flag))) {
                shouting.setFrame(3);
                shouting.setSick(true);
//                flag.setDestroyed(true);

                System.out.println("wrong way");
                gameOver();

            }
            if (isOnRightWay(flag)) {
                setShoutFrame(true, flag);
                if (!flag.isDestroyed()) {
                    isItNeedToShout = true;
                    System.out.println("right way");
                }
            }
        }
        for (Tree tree : treeList) {
            if ((isAccident(tree))) {
                shouting.setFrame(1);
//tree.setDestroyed(true);
                System.out.println("tree Crush");
                gameOver();
            }
        }
    }

    private void setShoutFrame(boolean isEverythingOk,Flag flag){
        if (isEverythingOk) {
            int[] frames = {0,2};
            shouting.setFrame(frames[flag.getShoutFrame()]);
        }
        else {
            shouting.setFrame(1);
        }
        shouting.setSick(true);
    }


    private boolean isOnRightWay(Flag flag) {

            return ((rider.getBoardNose() > flag.getLeft())
                    && (((flag.isItRed())
                    && (rider.getBoardTop() < flag.getBottom())) //ниже красного
                    || ((!flag.isItRed())
                    && (rider.getBoardBottom() > flag.getTop()))));//выше синего

    }

    private boolean isAccident(Flag flag) {
        return !flag.isOutside(rider.getBoard2());
    }

    private boolean isAccident(Tree tree) {
//        return !rider.getBoard2().isOutside(tree.getTrunk());
        return !tree.isOutside(rider.getBoard2());

    }



    // WRONGWAY
    private boolean isWrongWay(Flag flag) {

            return ((rider.getBoardNose() > flag.getRight())
                    && (rider.getBoardNose() - flag.getLeft() < rider.getBoard().width)
                    && (((flag.isItRed())
                    && (rider.getBoardBottom() > flag.getBottom())) //Выше красного
                    || ((!flag.isItRed())
                    && (rider.getBoardTop() < flag.getTop()))));//ниже синего

    }

    private void gameOver() {
isGameOver=true;
        countClicks = 0;
        isItNeedToShout = true;

        music.setVolume(0.3f);
        setIsPlaying(false);

        rider.gameOver();
    }

    private void startNewGame() {
        isGameOver=false;
        for (Flag flag : flagList) {
            flag.setDestroyed(true);
        }
        for (Tree tree : treeList) {
            tree.setDestroyed(true);
        }

        setIsPlaying(true);
        isItNeedToShout = false;
        points = 0;

        shouting.setSick(false);
        music.setVolume(1f);
        isPlaying = true;
        rider.setTheNewGame();
        startGates.setTheNewGame();
    }

    public static Music getMusic() {
        return music;
    }

    public static boolean getIsPlaying() {
        return isPlaying;
    }

    private static void setIsPlaying(boolean isPlaying) {
        GameScreen.isPlaying = isPlaying;
    }

    public static void setCountPoints(int a) {
        points = a;
    }

    public static int getCountPoints() {
        return points;
    }


    public static void setIsItNeedToShout(boolean isItNeedToShout) {
        GameScreen.isItNeedToShout = isItNeedToShout;
    }


    public static boolean isOnPause() {
        return onPause;
    }

    public static void setOnPause(boolean onPause) {
        GameScreen.onPause = onPause;
    }

    public static void setMusic(Boolean status) {
        if (status) {
            music.play();
        }else {
            music.pause();
        }
    }

    public static Rider getRider() {
        return rider;
    }

    public static int getCountClicks() {
        return countClicks;
    }
}
