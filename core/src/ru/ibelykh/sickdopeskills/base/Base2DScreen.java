package ru.ibelykh.sickdopeskills.base;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import ru.ibelykh.sickdopeskills.math.MatrixUtils;
import ru.ibelykh.sickdopeskills.math.Rect;


public class Base2DScreen implements Screen, InputProcessor {


    protected SpriteBatch batch;

    private Rect screenBounds;  //для расчета соотношений сторон экрана
    protected Rect worldBounds;    // наша координатная сетка где высота 1f а ширина плавающая (1f*aspect)
    private Rect glBounds;  // координатная сетки openGl 2f*2f

    protected Matrix4 worldToGl;
    protected Matrix3 screenToWorld;

    protected Game game;

    private Vector2 touch = new Vector2();


    public Base2DScreen(Game game) {
        this.game = game;
        this.screenBounds = new Rect();
        this.worldBounds = new Rect();
        this.glBounds = new Rect(0, 0, 1f, 1f);
        this.worldToGl = new Matrix4();
        this.screenToWorld = new Matrix3();

    }

    public Rect getWorldBounds() {
        return worldBounds;
    }

    @Override
    public void show() {
//            System.out.println("show");
        batch = new SpriteBatch();
        batch.getProjectionMatrix().idt();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
//            System.out.println("resize");
        screenBounds.setSize(width, height);
        screenBounds.setLeft(0);
        screenBounds.setBottom(0);

        float aspect = width / (float) height;
        worldBounds.setHeight(1f);
        worldBounds.setWidth(1f * aspect);

        MatrixUtils.calcTransitionMatrix(worldToGl, worldBounds, glBounds);
        batch.setProjectionMatrix(worldToGl);

        MatrixUtils.calcTransitionMatrix(screenToWorld, screenBounds, worldBounds);
        resize(worldBounds);


    }

    public void resize(Rect worldBounds) {
//        System.out.println("resize worldBounds width = "+ worldBounds.getWidth() + "   worldBoundsHeight = "+ worldBounds.getHeight());
    }

    @Override
    public void pause() {
        System.out.println("pause");
    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

    @Override
    public void hide() {
//            System.out.println("hide");
        dispose();
    }

    @Override
    public void dispose() {
//            System.out.println("dispose");
        batch.dispose();
    }


    @Override
    public boolean keyDown(int keycode) {
//        System.out.println("keydowm  keycode = " + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
//        System.out.println("keyup keycode = " + keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        System.out.println("charakter = " + character);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        System.out.println("touchdown = " + screenX + " " + screenY);
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDown(touch, pointer);
        return false;
    }

    public boolean touchDown(Vector2 touch, int pointer) {
        System.out.println("touchdown touch.x= " + touch.x + " " + " touch.y= " + touch.y + "  POINTER " + pointer);
        System.out.println();

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        System.out.println("touchup = "+ screenX+" "+ screenY);
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchUp(touch, pointer);
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer) {
//        System.out.println("touchup  touch.x= "+ touch.x+" touch.y = "+ touch.y);

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        System.out.println("touchdragged = " + screenX + " " + screenY);
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDragged(touch, pointer);
        return false;
    }

    public boolean touchDragged(Vector2 touch, int pointer) {
//        System.out.println("touchdragged touchdragger.x= " + touch.x + " touchdragger.y = " + touch.y);

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
//        System.out.println("keydowm  keycode = "+ keycode);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
//        System.out.println("keydowm  keycode = "+ keycode);
        return false;
    }



    //SOUND

}
