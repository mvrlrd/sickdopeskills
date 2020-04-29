package ru.ibelykh.sickdopeskills;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.ibelykh.sickdopeskills.interfaces.GameProcess;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.screens.GameScreen;
import ru.ibelykh.sickdopeskills.sprites.Flag;
import ru.ibelykh.sickdopeskills.sprites.Rider;
import ru.ibelykh.sickdopeskills.sprites.Snow;
import ru.ibelykh.sickdopeskills.sprites.Splash;
import ru.ibelykh.sickdopeskills.sprites.Tree;

public class GamePresenter implements GameProcess {
    private GameSpritesModel gameSpritesModel;

    public GamePresenter(Rect worldBounds) {
        this.gameSpritesModel = new GameSpritesModel(worldBounds);
        show();
    }

    @Override
    public void show() {
        gameSpritesModel.initGameSpriteModels();
    }

    @Override
    public void draw(SpriteBatch batch) {
        //splashes(only if the rider is moving)
        if (GameScreen.getIsPlaying()){
            for (int i = 0; i < gameSpritesModel.getSplash().length; i++) {
                gameSpritesModel.getSplash()[i].draw(batch);
            }
        }
        gameSpritesModel.getRider().draw(batch);
        gameSpritesModel.getStartGates().draw(batch);

        gameSpritesModel.getFlagPool().drawActiveSprites(batch);
        gameSpritesModel.getTreePool().drawActiveSprites(batch);
        //snows
        for (int i = 0; i < gameSpritesModel.getSnows().length; i++) {
            gameSpritesModel.getSnows()[i].draw(batch);
        }




    }

    @Override
    public void update(float delta) {
        gameSpritesModel.getStartGates().update(delta);
        gameSpritesModel.getRider().update(delta);
        //splashes
        for (Splash oneSingleSplash : gameSpritesModel.getSplash()) {
            oneSingleSplash.update(delta);
        }
        //snows
        for (Snow oneSingleSnowflake : gameSpritesModel.getSnows()) {
            oneSingleSnowflake.update(delta);
        }

        gameSpritesModel.getTreePool().updateActiveSprites(delta);
        gameSpritesModel.getTreeEmitter().generateTreesTop(delta);
        gameSpritesModel.getTreeEmitter().generateTreesBottom(delta);

        gameSpritesModel.getFlagPool().updateActiveSprites(delta);
        gameSpritesModel.getFlagEmitter().generateFlags(delta);

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(Rect worldBounds) {
        gameSpritesModel.getStartGates().resize(worldBounds, worldBounds.getLeft() + 0.1f, 0f);
        gameSpritesModel.getRider().resize(worldBounds, worldBounds.getLeft() + 1.2f * gameSpritesModel.getRider().getWidth(), 0f);
        //splashes
        for (Splash oneSingleSplash : gameSpritesModel.getSplash()) {
            oneSingleSplash.resize(worldBounds);
        }
        //snows
        for (Snow oneSingleSnowflake : gameSpritesModel.getSnows()) {
            oneSingleSnowflake.resize(worldBounds);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameSpritesModel.getFlagPool().dispose();
        gameSpritesModel.getTreePool().dispose();
    }

    @Override
    public void startNewGame() {

    }

    @Override
    public void deleteAllDestroyed() {
        gameSpritesModel.getFlagPool().freeAllDestroyedActiveSprites();
        gameSpritesModel.getTreePool().freeAllDestroyedActiveSprites();
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public void printInfo2() {

    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        return gameSpritesModel.getRider().touchDown(touch,pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        return gameSpritesModel.getRider().touchUp(touch, pointer);
    }

    @Override
    public void checkCollisions() {

    }

    @Override
    public void setShoutFrame(boolean isEverythingOk, Flag flag) {

    }

    @Override
    public boolean isOnRightWay(Flag flag) {
        return false;
    }

    @Override
    public boolean isAccident(Flag flag) {
        return false;
    }

    @Override
    public boolean isAccident(Tree tree) {
        return false;
    }

    @Override
    public boolean isWrongWay(Flag flag) {
        return false;
    }

    @Override
    public void gameOver() {

    }

    public Rider getRider() {
        return gameSpritesModel.getRider();
    }

    public GameSpritesModel getGameSpritesModel() {
        return gameSpritesModel;
    }

    @Override
    public void setTheNewGame() {
        gameSpritesModel.getStartGates().setTheNewGame();
        gameSpritesModel.getRider().setTheNewGame();
    }
//    public void startNewGame() {
//        for (Flag flag : getGameSpritesModel().getFlagList()) {
//            flag.setDestroyed(true);
//        }
//        for (Tree tree : getGameSpritesModel().getTreeList()) {
//            tree.setDestroyed(true);
//        }
//        setIsPlaying(true);
//        isItNeedToShout = false;
//        points = 0;
//        shouting.setSick(false);
//        music.setVolume(1f);
//
//        gameSpritesModel.getStartGates().setTheNewGame();
//        gameSpritesModel.getRider().setTheNewGame();
//
//        isGameOver = false;
//        isPlaying = true;
//    }


}
