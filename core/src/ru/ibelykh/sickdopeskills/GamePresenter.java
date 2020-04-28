package ru.ibelykh.sickdopeskills;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.ibelykh.sickdopeskills.interfaces.GameProcess;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.screens.GameScreen;
import ru.ibelykh.sickdopeskills.sprites.Flag;
import ru.ibelykh.sickdopeskills.sprites.Rider;
import ru.ibelykh.sickdopeskills.sprites.Tree;

public class GamePresenter implements GameProcess {
    private Model model;


    public GamePresenter(Rect worldBounds) {
        this.model = new Model(worldBounds);
    }

    @Override
    public void show() {
        model.init();
    }

    @Override
    public void draw(SpriteBatch batch) {
        //splashes(only if the rider is moving)
        if (GameScreen.getIsPlaying()){
            for (int i = 0; i < model.getSplash().length; i++) {
                model.getSplash()[i].draw(batch);
            }
        }
        model.getRider().draw(batch);
        model.getStartGates().draw(batch);

        //snows
        for (int i = 0; i < model.getSnows().length; i++) {
            model.getSnows()[i].draw(batch);
        }


    }

    @Override
    public void update(float delta) {
        model.getStartGates().update(delta);
        model.getRider().update(delta);
        //splashes
        for (int i = 0; i < model.getSplash().length; i++) {
            model.getSplash()[i].update(delta);
        }
        //snows
        for (int i = 0; i < model.getSnows().length; i++) {
            model.getSnows()[i].update(delta);
        }
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(Rect worldBounds) {
        model.getStartGates().resize(worldBounds, worldBounds.getLeft() + 0.1f, 0f);
        model.getRider().resize(worldBounds, worldBounds.getLeft() + 1.2f * model.getRider().getWidth(), 0f);
        //splashes
        for (int i = 0; i < model.getSplash().length; i++) {
            model.getSplash()[i].resize(worldBounds);
        }
        //snow
        for (int i = 0; i < model.getSnows().length; i++) {
            model.getSnows()[i].resize(worldBounds);
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

    }

    @Override
    public void startNewGame() {

    }

    @Override
    public void deleteAllDestroyed() {

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
        return model.getRider().touchDown(touch,pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        return model.getRider().touchUp(touch, pointer);
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
        return model.getRider();
    }

    public Model getModel() {
        return model;
    }

    @Override
    public void setTheNewGame() {
        model.getStartGates().setTheNewGame();
        model.getRider().setTheNewGame();
    }
}
