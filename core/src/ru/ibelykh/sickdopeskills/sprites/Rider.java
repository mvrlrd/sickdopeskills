package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;



public class Rider extends Sprite {

    private Vector2 v = new Vector2();
    private Vector2 velocity = new Vector2();
    private Rect worldBounds;
    private Vector2 lastVelocity= new Vector2(0,0.011f);

    private int state;

    private Rectangle board;



    public Rider(TextureAtlas atlas, Rect worldBounds) {
        super(atlas.findRegion("rider"), 4, 1, 4);
        setHeightProportion(0.08f);
        this.worldBounds = worldBounds;
        board = new Rectangle();

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v.add(velocity),delta);



        if (this.getTop()>worldBounds.getTop()&&((v.add(velocity)).y>0.3f)){
            velocity.set(0.0f, -0.011f);
            lastVelocity.set(velocity);
            v.set(0,0.0f);
            pos.mulAdd(v.add(velocity), delta);
        }
        if (this.getBottom()<worldBounds.getBottom()&&(v.add(velocity)).y<-0.3f){
            velocity.set(0.0f, 0.011f);
            lastVelocity.set(velocity);
            v.set(0,0.0f);
            pos.mulAdd(v.add(velocity), delta);
        }
        if (velocity.y < 0) {
            frame=3;
            state=1;


            this.angle=v.y*70;
            board.set(getLeft()+getWidth()*0.4f,getBottom()+getHalfWidth()/1.5f,
                    getWidth()/1.7f,getHeight()/3f);




        }
        if (velocity.y>0f){
            frame=1;

            this.angle=v.y*70;
            board.set(getLeft()+getWidth()*0.385f,getBottom()+getHalfWidth()/3.9f,
                    getWidth()/1.7f,getHeight()/3f);
            state=0;

        }
    }

    public void resize(Rect worldBounds, float x, float y) {
        super.resize(worldBounds);
        pos.set(x,y);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        velocity.rotate(180);
        lastVelocity.set(velocity);
        return false;
    }


    @Override
    public boolean isDestroyed() {
        super.isDestroyed();
        return true;
    }

    public void setTheNewGame(){
        v.set(0.0f, 0.0f);
        pos.set(worldBounds.getLeft()+1.2f*this.getWidth(),0f);
        velocity.set(lastVelocity.rotate(180));
    }

    public void gameOver(){
        v.set(-0.15f,0f);
        velocity.set(0.000f,-0.0f);
    }

    public Rectangle getBoard() {
        return board;
    }

    public float getBoardLeft(){
        return board.x;
    }
    public float getBoardRight(){
        return board.x+board.getWidth();
    }
    public float getBoardTop(){
        return board.y+board.getHeight();
    }
    public float getBoardBottom(){
        return board.y;
    }

    public int getState() {
        return state;
    }
}
