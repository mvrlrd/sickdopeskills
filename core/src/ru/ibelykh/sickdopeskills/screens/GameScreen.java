package ru.ibelykh.sickdopeskills.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.pools.FlagPool;
import ru.ibelykh.sickdopeskills.pools.TreePool;
import ru.ibelykh.sickdopeskills.sprites.*;
import ru.ibelykh.sickdopeskills.utils.FlagEmitter;
import ru.ibelykh.sickdopeskills.utils.TreeEmitter;


public class GameScreen extends Base2DScreen {

    private static final String HEIGHPTS = "top: ";
    private static final float FONT_SIZE = 3f;

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

    private static SpriteBatch spriteBatch;

    private static Rider rider;
    private static Shouting shouting;
    private static YouCool youCool;
    private static StartGates startGates;

    private static ButtonGameSoundOffOn buttonGameSoundOffOn;
    private static ButtonPause buttonPause;

    private static Snow[] snow;
    private static Splash[] splash;

    Matrix4 mx4Font = new Matrix4();
    BitmapFont fontt;
    SpriteBatch spriteFont;
    Vector3 position ;

    public GameScreen(Game game) {
        super(game);
    }



    @Override
    public void show() {
        super.show();
        spriteBatch = new SpriteBatch();
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
        shapeRenderer.setColor(Color.BLACK);

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/lord_of_boards.mp3"));
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

        font = new Font("font/snowCapFont.fnt", "font/snowCapFont.png");
        font.setColor(Color.GOLDENROD);
        font.setFontSize(FONT_SIZE);

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
//        for (Tree flag : treeList) {
//            shapeRenderer.rect(flag.getCollisionInvisibleSquare().x,
//                    flag.getCollisionInvisibleSquare().y,
//                    flag.getCollisionInvisibleSquare().width,
//                    flag.getCollisionInvisibleSquare().height);
//        }
//        shapeRenderer.end();
        spriteBatch.begin();
        spriteBatch.end();
    }

    private void printInfo() {
        sbDist.setLength(0);
        sbHeighPts.setLength(0);
        sbFrags.setLength(0);


sbFrags.append(points);


        spriteFont = new SpriteBatch();
        mx4Font.setToRotation(new Vector3(1, 1, 0), 180);
        spriteFont.setTransformMatrix(mx4Font);
        spriteFont.begin();
        font.draw(spriteFont, sbFrags, 0, 122);
        spriteFont.end();
//        font.draw(batch,
//                sbFrags.append(points),
//                0f,
//                0f,55);

        // font.draw(batch, "Frags:"+ frags) --- так плохо потому что будет создаваться каждый раз новая строка для frags и для "frags" итого 120 строк в сек
//        font.draw(batch,
//                sbDist.append(rider.getVelocityY()),
//                worldBounds.getLeft(),
//                worldBounds.getBottom()+0.1f + FONT_SIZE);



        if (isGameOver) {

//            if (prefs.getInteger("pts") != 0) {
//                font.draw(spriteFont, "hgfgh", 0, 300);

//            }
        }

    }

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
        fontt.dispose();

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
                isItNeedToShout=true;
                gameOver();
                System.out.println("flag Crush");
                flag.setDestroyed(true);

            }
            if ((isWrongWay(flag))) {
                shouting.setFrame(3);
                shouting.setSick(true);
                flag.setDestroyed(true);
                isItNeedToShout=true;
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
//                isItNeedToShout=true;
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
        return (!(rider.getBoardBack() > flag.getBigRectRight()
                || rider.getBoardNose() < flag.getBigRectLeft()
                || rider.getBoardTop() < flag.getBigRectBot()
                || rider.getBoardBottom() > flag.getBigRectTop()));
    }

    private boolean isAccident(Tree tree) {
        System.out.println(!(rider.getBoardBack() > (tree.getCollisionInvisibleSquare().x + tree.getCollisionInvisibleSquare().width))+"  1"+"  "+rider.getBoardBack());
        System.out.println((rider.getBoardNose() < tree.getCollisionInvisibleSquare().x)+"  2  "+rider.getBoardNose());
        System.out.println((rider.getBoardTop() < tree.getCollisionInvisibleSquare().y)+"  3  "+rider.getBoardTop());
        System.out.println((rider.getBoardBottom() > (tree.getCollisionInvisibleSquare().y + tree.getCollisionInvisibleSquare().height))+"  4  "+rider.getBoardBottom());
        return (!(rider.getBoardBack() > (tree.getCollisionInvisibleSquare().x + tree.getCollisionInvisibleSquare().width)
                || rider.getBoardNose() < tree.getCollisionInvisibleSquare().x
                || rider.getBoardTop() < tree.getCollisionInvisibleSquare().y
                || rider.getBoardBottom() > (tree.getCollisionInvisibleSquare().y + tree.getCollisionInvisibleSquare().height)));
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
//        isItNeedToShout = true;

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
}
