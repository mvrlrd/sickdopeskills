package ru.ibelykh.sickdopeskills.sprites;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ru.ibelykh.sickdopeskills.base.Sprite;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.screens.MenuScreen;


public class Rider extends Sprite {
    private final static int RIGHT_FRAME_RUS = 2;
    private final static int LEFT_FRAME_RUS = 3;
    private final static int RIGHT_FRAME_DARK = 0;
    private final static int LEFT_FRAME_DARK = 1;
    private final static int RIGHT_FRAME_GRAY = 4;
    private final static int LEFT_FRAME_GRAY = 5;
    private static final String REGIONNAME = "riders";
    private Rect worldBounds;

    private Vector2 v = new Vector2();
    private Vector2 velocity = new Vector2();
    private float velocityY=0.045f;  //было    0.06f
    private Vector2 lastVelocity= new Vector2(0,velocityY);
    private  int rightHandedFrame;
    private int leftHandedFrame;

    private Vector2 prePauseSpeed;
    private Vector2 preVelocity;

    private Vector2 speed;
    private Rectangle board;

   private Rect board2;
   private Vector2 boardPos;


    public Rider(TextureAtlas atlas, Rect worldBounds) {
        super(atlas.findRegion(REGIONNAME), 3, 2, 6);
        speed=new Vector2();
        framer(MenuScreen.getBtnGetRider().getFrame());
        frame = rightHandedFrame;
        float proportion = 0.1f;
        setHeightProportion(proportion);
        this.worldBounds = worldBounds;
        board = new Rectangle();

        board2 = new Rect();
        boardPos = new Vector2();
        board2.setWidth(getWidth()/2f);
        board2.setHeight(getHeight()/3f);

        prePauseSpeed = new Vector2();
        preVelocity = new Vector2();


    }

    private void framer(int frame){
        if (frame==2){
            this.rightHandedFrame=RIGHT_FRAME_RUS;
            this.leftHandedFrame=LEFT_FRAME_RUS;
        }
        else if (frame==0) {
            this.rightHandedFrame=RIGHT_FRAME_DARK;
            this.leftHandedFrame=LEFT_FRAME_DARK;
        }
        else if (frame==4) {
            this.rightHandedFrame=RIGHT_FRAME_GRAY;
            this.leftHandedFrame=LEFT_FRAME_GRAY;
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
            boardPos.set(getLeft()+getHalfWidth()*1.3f,getBottom()+getHalfHeight()/1.1f);
            board2.setPos(boardPos);


//            board2.setLeft(getLeft()+getWidth()*0.4f);
//            board2.setBottom(getBottom()+getHalfWidth()/1.5f);

//            board2.setRight(getLeft()+getWidth()*0.4f+getWidth()/1.7f);
//            board2.setTop(getBottom()+getHalfWidth()/1.5f+getHeight()/3f);


        }
        if (velocity.y>0f){
            frame = leftHandedFrame;
            this.angle = v.y*30;
            board.set(getLeft()+getWidth()*0.385f,getBottom()+getHalfWidth()/3.9f,
                    getWidth()/1.7f,getHeight()/3f);
            boardPos.set(getLeft()+getHalfWidth()*1.3f,getBottom()+getHalfHeight()/1.1f);
            board2.setPos(boardPos);
//            board2.setLeft(getLeft()+getWidth()*0.4f);
//            board2.setBottom(getBottom()+getHalfWidth()/1.5f);
//            board2.setRight(getLeft()+getWidth()*0.4f+getWidth()/1.7f);
//            board2.setTop(getBottom()+getHalfWidth()/1.5f+getHeight()/3f);
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
       return super.isDestroyed();
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

    public Rect getBoard2() {
        return board2;
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
