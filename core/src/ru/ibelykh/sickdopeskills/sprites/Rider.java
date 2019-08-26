package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.screens.MenuScreen;


public class Rider extends Sprite {
    private final static int RIGHTMOVINGDARKFRAME = 2;
    private final static int LEFTMOVINGDARKFRAME = 0;
    private final static int RIGHTMOVINGBLONDFRAME = 3;
    private final static int LEFTMOVINGDBLONDFRAME = 1;
    private static float proportion = 0.1f;
    private static final String REGIONNAME = "rider";
    private Vector2 v = new Vector2();
    private Vector2 velocity = new Vector2();
    private Rect worldBounds;
    private float velocityY=0.045f;
    private Vector2 lastVelocity= new Vector2(0,velocityY);
    private  int rightHandedFrame;
    private int leftHandedFrame;

    private Vector2 prePauseSpeed = new Vector2();
    private Vector2 preVelocity = new Vector2();

    private Vector2 speed;
    private Rectangle board;


    public Rider(TextureAtlas atlas, Rect worldBounds) {
        super(atlas.findRegion(REGIONNAME), 4, 1, 4);
        speed=new Vector2();
        framer(MenuScreen.getBtnGetRider().getFrame());
        frame = rightHandedFrame;
        setHeightProportion(proportion);
        this.worldBounds = worldBounds;
        board = new Rectangle();

    }

    private void framer(int frame){
        if (frame==0){
            this.rightHandedFrame=RIGHTMOVINGBLONDFRAME;
            this.leftHandedFrame=LEFTMOVINGDBLONDFRAME;
        }else if (frame==1) {
            this.rightHandedFrame=RIGHTMOVINGDARKFRAME;
            this.leftHandedFrame=LEFTMOVINGDARKFRAME;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        speed=v.add(velocity);
        pos.mulAdd(speed,delta);

        if (this.getTop()>worldBounds.getTop()&&((v.add(velocity)).y>0.3f)){
            velocity.set(velocity.x,-velocityY);
            v.set(0f,0f);
            pos.mulAdd(v.add(velocity), delta);
        }
        if (this.getBottom()<worldBounds.getBottom()&&(v.add(velocity)).y<-0.3f){
            velocity.set(velocity.x,velocityY);
            v.set(0f,0f);
            pos.mulAdd(v.add(velocity), delta);
        }
        if (velocity.y < 0f) {
            frame=rightHandedFrame;
            this.angle=v.y*30;
            board.set(getLeft()+getWidth()*0.4f,getBottom()+getHalfWidth()/1.5f,
                    getWidth()/1.7f,getHeight()/3f);


        }
        if (velocity.y>0f){
            frame = leftHandedFrame;
            this.angle = v.y*30;
            board.set(getLeft()+getWidth()*0.385f,getBottom()+getHalfWidth()/3.9f,
                    getWidth()/1.7f,getHeight()/3f);
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
        v.set(-0.0f,0f);
        velocity.set(0.000f,-0.0f);
    }

    public Rectangle getBoard() {
        return board;
    }

    public float getBoardBack(){
        return board.x;
    }
    public float getBoardNose(){
        return board.x+board.getWidth();
    }
    public float getBoardTop(){
        return board.y+board.getHeight();
    }
    public float getBoardBottom(){
        return board.y;
    }


    public void pause(){
prePauseSpeed.set(speed);
preVelocity.set(velocity);

        speed.set(0f,0f);
        velocity.set(0f,0f);
    }

    public void resume(){

        speed.set(prePauseSpeed);
        velocity.set(preVelocity);
    }


}
