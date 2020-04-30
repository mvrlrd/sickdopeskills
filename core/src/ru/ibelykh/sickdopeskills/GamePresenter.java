package ru.ibelykh.sickdopeskills;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.ibelykh.sickdopeskills.interfaces.GameProcess;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.sprites.Flag;
import ru.ibelykh.sickdopeskills.sprites.Rider;
import ru.ibelykh.sickdopeskills.sprites.Tree;

public class GamePresenter implements GameProcess {
    private GameSpritesModel gameSpritesModel;
    

    public GamePresenter(Rect worldBounds) {
        this.gameSpritesModel = new GameSpritesModel(worldBounds);
    }


    @Override
    public void draw(SpriteBatch batch) {
        gameSpritesModel.draw(batch);
    }

    @Override
    public void update(float delta) {

        gameSpritesModel.update(delta);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(Rect worldBounds) {
        gameSpritesModel.resize(worldBounds);
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
       gameSpritesModel.dispose();
    }

    @Override
    public void startNewGame() {

    }

    @Override
    public void deleteAllDestroyed() {
        gameSpritesModel.deleteAllDestroyed();
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
       gameSpritesModel.startNewGame();
    }


}
