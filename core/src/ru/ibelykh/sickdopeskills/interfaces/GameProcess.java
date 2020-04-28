package ru.ibelykh.sickdopeskills.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.sprites.Flag;
import ru.ibelykh.sickdopeskills.sprites.Tree;

public interface GameProcess {

    void show();
    void draw(SpriteBatch batch);
    void update(float delta);
    void render(float delta);
    void resize(Rect worldBounds);
    void pause();
    void resume();
    void hide();
    void dispose();
    void startNewGame();
    void deleteAllDestroyed();
    void setTheNewGame();

    boolean keyUp(int keycode);



    void printInfo2();



    boolean touchDown(Vector2 touch, int pointer);
    boolean touchUp(Vector2 touch, int pointer);
    void checkCollisions();
    void setShoutFrame(boolean isEverythingOk,Flag flag);
    boolean isOnRightWay(Flag flag);
    boolean isAccident(Flag flag);
    boolean isAccident(Tree tree);
    boolean isWrongWay(Flag flag);
    void gameOver();

}
