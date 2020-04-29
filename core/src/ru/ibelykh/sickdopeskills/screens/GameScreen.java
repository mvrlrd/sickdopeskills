package ru.ibelykh.sickdopeskills.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import ru.ibelykh.sickdopeskills.GamePresenter;
import ru.ibelykh.sickdopeskills.base.Base2DScreen;
import ru.ibelykh.sickdopeskills.base.Font;
import ru.ibelykh.sickdopeskills.buttons.ButtonGameSoundOffOn;
import ru.ibelykh.sickdopeskills.buttons.ButtonPause;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.sprites.*;

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
    private TextureAtlas allSprites;
    private static Font font;
    private static Music music;
    private static Sound soundCheck;
    private static Preferences prefs;
    private static ShapeRenderer shapeRenderer;
    private static Shouting shouting;
    private static YouCool youCool;
    private GamePresenter gamePresenter;
//   private Matrix4 mx4Font;
//    private SpriteBatch spriteFont;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        worldBounds = getWorldBounds();
        gamePresenter = new GamePresenter(getWorldBounds());
        allSprites = new TextureAtlas("images/sprites/allSprites.atlas");
        shouting = new Shouting(allSprites, worldBounds);
        youCool = new YouCool(allSprites, worldBounds);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.RED);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/bad_guy.mp3"));
        music.setLooping(true);
        soundCheck = Gdx.audio.newSound(Gdx.files.internal("sounds/pau.wav"));
        //preferences (music, high score)
        prefs = Gdx.app.getPreferences("high score and sound");
        if (prefs.getBoolean("soundOff")) {
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
//        mx4Font.setToRotation(fontRotationVector, 180);
//        spriteFont.setTransformMatrix(mx4Font);
        Gdx.input.setCatchBackKey(true); // ловим касание по системной кнопке "Назад
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK) {
            game.setScreen(new MenuScreen(game));
            music.dispose();
            gameOver();
            isItNeedToShout = false;
        }
        return false;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        deleteAllDestroyed();
        draw();
    }

    public void update(float delta) {
        gamePresenter.update(delta);
        if (((shouting.getFrame() == Shouting.DOPE) ||
                (shouting.getFrame() == Shouting.WHOA)) && (isItNeedToShout)) {
            interval += delta;
        }
        if (interval > 1.2f) {
            isItNeedToShout = false;
            interval = 0;
        }
        if (points > prefs.getInteger("pts")) {
            prefs.putInteger("pts", points);
            prefs.flush();
            highScore = points;
        }
        if (music.isPlaying()) {
            prefs.putBoolean("soundOff", false);
            prefs.flush();
        } else if (!music.isPlaying()) {
            prefs.putBoolean("soundOff", true);
            prefs.flush();
        }
        youCool.update(delta);
        shouting.update(delta);
        checkCollisions();
//        System.out.println("tree  "+treePool.getActiveObjects().size()+" free: "+treePool.getFreeObjects().size());
//        System.out.println("flag  "+flagPool.getActiveObjects().size()+" free: "+flagPool.getFreeObjects().size());
    }

    private void draw() {
        batch.begin();
        Gdx.gl.glClearColor(.8f, .8f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gamePresenter.draw(batch);
        if ((points == highScore) && (points != 0)) {
            youCool.draw(batch);
        }
        if ((isItNeedToShout)) {
            shouting.draw(batch);
        }
        batch.end();
        printInfo2();
//        shapeRenderer.setProjectionMatrix(matrixForSnowboard);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.rect(rider.getBoard2().getLeft(),rider.getBoard2().getBottom(),rider.getBoard2().getWidth(),rider.getBoard2().getHeight());
//        shapeRenderer.end();
//Это все показывает прямоугольники сноуборда и флажков, которые нужны для рассчета коллизий
//        shapeRenderer.setProjectionMatrix(worldToGl);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.rect(rider.getBoard2().getLeft(),rider.getBoard2().getBottom(),rider.getBoard2().getWidth(),rider.getBoard2().getHeight());
////        for (Flag flag : flagList) {
////            shapeRenderer.rect(flag.getCrashArea().getLeft(),flag.getCrashArea().getBottom(), flag.getCrashArea().getWidth(), flag.getCrashArea().getHeight());
////        }
////        for (Tree tree : treeList) {
////            shapeRenderer.rect(tree.getTrunk().getLeft(),tree.getTrunk().getBottom(),tree.getTrunk().getWidth(),tree.getTrunk().getHeight());
////        }
//        shapeRenderer.end();
    }

    private void printInfo2() {
        sbFrags.setLength(0);
        sbFrags.append(points);
        float otstup; // для того чтобы центрировать надпись с очками
        if (points < 10) {
            otstup = FONT_SIZE / 2f;
        } else {
            otstup = FONT_SIZE;
        }
        batchFont.begin();
        font.draw(batchFont,
                sbFrags,
                worldBounds.getLeft() + worldBounds.getHalfWidth() - otstup, worldBounds.getTop() - 0.3f);
        if (isGameOver) {
            sbHeighPts.setLength(0);
            sbHeighPts.append(highScore);
            font.draw(batchFont,
                    sbHeighPts,
                    worldBounds.getLeft() + worldBounds.getHalfWidth() - FONT_SIZE, worldBounds.getTop() - 0.8f);
        }
        batchFont.end();
    }
//    private void printInfo() {
//        sbDist.setLength(0);
//        sbHeighPts.setLength(0);
//        sbFrags.setLength(0);
//sbFrags.append(points);
//        font.draw(batch,
//                sbHeighPts.append(highScore),
//                worldBounds.getLeft()+FONT_SIZE, worldBounds.getTop()
//                );
//    }

    private void deleteAllDestroyed() {
        gamePresenter.deleteAllDestroyed();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        gamePresenter.resize(worldBounds);
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
        soundCheck.dispose();
        music.dispose();
        allSprites.dispose();
        gamePresenter.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if ((!gamePresenter.getGameSpritesModel().getButtonGameSoundOffOn().isMe(touch)) && (!gamePresenter.getGameSpritesModel().getButtonPause().isMe(touch)) && (!onPause)) {
            countClicks++;
            if (countClicks == 1) {
                startNewGame();
            }
            gamePresenter.touchDown(touch, pointer);
        }
        if ((onPause) || (isPlaying)) {
            gamePresenter.getGameSpritesModel().getButtonPause().touchDown(touch, pointer);
        }
        if ((gamePresenter.getGameSpritesModel().getButtonPause().getFrame() == 1) && (!gamePresenter.getGameSpritesModel().getButtonPause().isMe(touch)) && (!gamePresenter.getGameSpritesModel().getButtonGameSoundOffOn().isMe(touch))) {
            gamePresenter.getGameSpritesModel().getButtonPause().onPlay();
        }
        gamePresenter.getGameSpritesModel().getButtonGameSoundOffOn().touchDown(touch, pointer);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if ((!gamePresenter.getGameSpritesModel().getButtonGameSoundOffOn().isMe(touch)) && (!gamePresenter.getGameSpritesModel().getButtonPause().isMe(touch))) {
            gamePresenter.touchUp(touch, pointer);
        }
        gamePresenter.getGameSpritesModel().getButtonGameSoundOffOn().touchUp(touch, pointer);
        gamePresenter.getGameSpritesModel().getButtonPause().touchUp(touch, pointer);
        return super.touchUp(touch, pointer);
    }

    private void checkCollisions() {
        for (Flag flag : gamePresenter.getGameSpritesModel().getFlagList()) {
            if ((isAccident(flag)) && (isPlaying)) {
                shouting.setFrame(1);
                gameOver();
                System.out.println("flag Crush");
//                flag.setDestroyed(true);
            }
            if ((isWrongWay(flag)) && (isPlaying)) {
                shouting.setFrame(3);
                shouting.setSick(true);
//                flag.setDestroyed(true);
                System.out.println("wrong way");
                gameOver();
            }
            if ((isOnRightWay(flag)) && (isPlaying)) {
                setShoutFrame(true, flag);
                if (!flag.isDestroyed()) {
                    isItNeedToShout = true;
                    System.out.println("right way");
                }
            }
        }
        for (Tree tree : gamePresenter.getGameSpritesModel().getTreeList()) {
            if (((isAccident(tree))) && (isPlaying)) {
                shouting.setFrame(1);
//tree.setDestroyed(true);
                System.out.println("tree Crush");
                gameOver();
            }
        }
    }

    private void setShoutFrame(boolean isEverythingOk, Flag flag) {
        if (isEverythingOk) {
            int[] frames = {0, 2};
            shouting.setFrame(frames[flag.getShoutFrame()]);
        } else {
            shouting.setFrame(1);
        }
        shouting.setSick(true);
    }

    private boolean isOnRightWay(Flag flag) {
        return ((gamePresenter.getRider().getBoardNose() > flag.getLeft())
                && (((flag.isItRed())
                && (gamePresenter.getRider().getBoardTop() < flag.getBottom())) //ниже красного
                || ((!flag.isItRed())
                && (gamePresenter.getRider().getBoardBottom() > flag.getTop()))));//выше синего
    }

    private boolean isAccident(Flag flag) {
        return !flag.isOutside(gamePresenter.getRider().getBoard2());
    }

    private boolean isAccident(Tree tree) {
//        return !rider.getBoard2().isOutside(tree.getTrunk());
        return !tree.isOutside(gamePresenter.getRider().getBoard2());
    }
    // WRONGWAY
    private boolean isWrongWay(Flag flag) {
        if (!flag.isDestroyed())
            return ((gamePresenter.getRider().getBoardNose() > flag.getRight())
                    && (gamePresenter.getRider().getBoardNose() - flag.getLeft() < gamePresenter.getRider().getBoard().width)
                    && (((flag.isItRed())
                    && (gamePresenter.getRider().getBoardBottom() > flag.getBottom())) //Выше красного
                    || ((!flag.isItRed())
                    && (gamePresenter.getRider().getBoardTop() < flag.getTop()))));//ниже синего
        return false;
    }

    private void gameOver() {
        isGameOver = true;
        countClicks = 0;
        isItNeedToShout = true;
        music.setVolume(0.3f);
        setIsPlaying(false);
        gamePresenter.getRider().gameOver();
    }

    private void startNewGame() {
        for (Flag flag : gamePresenter.getGameSpritesModel().getFlagList()) {
            flag.setDestroyed(true);
        }
        for (Tree tree : gamePresenter.getGameSpritesModel().getTreeList()) {
            tree.setDestroyed(true);
        }
        setIsPlaying(true);
        isItNeedToShout = false;
        points = 0;
        shouting.setSick(false);
        music.setVolume(1f);
        gamePresenter.setTheNewGame();
        isGameOver = false;
        isPlaying = true;
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
        } else {
            music.pause();
        }
    }

    public static int getCountClicks() {
        return countClicks;
    }

}
