package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

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
    private static final String REGION_NAME = "riders";
    private final float VELOCITY_Y = 0.01f;  //0.045f  было    0.06f
    private final float PROPORTION = 0.3f; //0.1f

    private Rect worldBounds;
    private Vector2 v;
    private Vector2 velocity;

    private Vector2 lastVelocity;
    private int rightHandedFrame;
    private int leftHandedFrame;

    private Vector2 prePauseSpeed;
    private Vector2 preVelocity;

    private Vector2 speed;
    private Rectangle board;

    private Rect board2;
    private Vector2 boardPos;

    Polygon polygon;
    float[] vertexArrayRightHand;
    float[] vertexArrayLeftHand;

    float[] entireVertex;

    Rectangle dot;

Vector2 pos2;



    Polygon entirePic;

    public Rider(TextureAtlas atlas, Rect worldBounds) {
        super(atlas.findRegion(REGION_NAME), 3, 2, 6);
        this.worldBounds = worldBounds;
        v = new Vector2();
        velocity = new Vector2();
        lastVelocity = new Vector2(0,VELOCITY_Y);
        speed = new Vector2();
        framer(MenuScreen.getBtnGetRider().getFrame());
        frame = rightHandedFrame;
        setHeightProportion(PROPORTION);
        board = new Rectangle();
        board2 = new Rect();
        boardPos = new Vector2();
        board2.setWidth(getWidth()/2f);
        board2.setHeight(getHeight()/3f);
        prePauseSpeed = new Vector2();
        preVelocity = new Vector2();

        dot = new Rectangle();

        pos2 = new Vector2();



        entireVertex = new float[]{
                getLeft(),getBottom(),
                getLeft(),getTop(),
                getRight(),getTop(),
                getRight(),getBottom()};
        entirePic = new Polygon(entireVertex);


        vertexArrayRightHand =  new float[]{
                getLeft() + getWidth() * 0.6f, getBottom() + getHalfWidth() / 1.1f,
                getLeft() + getWidth() * 0.6f, getBottom() + getHalfWidth() / 1.1f - getHeight()/3.1f,
                getLeft() + getWidth() * 0.6f + getHalfWidth() / 1.2f, getBottom() + getHalfWidth() / 1.1f - getHeight()/3f,
                getLeft() + getWidth() * 0.6f + getHalfWidth() / 1.2f, getBottom() + getHalfWidth() / 1.1f

        };
        //      left
        //  1______________4                <       <
        //  | snowboard    |    ---->
        //  2______________3                    >       >
        //      right
        vertexArrayLeftHand =  new float[]{
                getLeft() + getWidth() * 0.4f, getBottom() + getHalfWidth() / 1.5f,
                getLeft() + getWidth() * 0.4f, getBottom() + getHalfWidth() / 1.5f - getHeight()/3.1f,
                getLeft() + getWidth() * 0.4f + getHalfWidth() / 1.2f, getBottom() + getHalfWidth() / 1.5f - getHeight()/3f,
                getLeft() + getWidth() * 0.4f + getHalfWidth() / 1.2f, getBottom() + getHalfWidth() / 1.5f
        };

        polygon = new Polygon();

        polygon.setVertices(vertexArrayRightHand);
//        polygon.setOrigin(getLeft() + getWidth() * 0.4f, getBottom() + getHalfWidth() / 1.5f);

    }

    private void framer(int frame){
        if (frame == RIGHT_FRAME_RUS) {
            this.rightHandedFrame = RIGHT_FRAME_RUS;
            this.leftHandedFrame = LEFT_FRAME_RUS;
        } else if (frame == RIGHT_FRAME_DARK) {
            this.rightHandedFrame = RIGHT_FRAME_DARK;
            this.leftHandedFrame = LEFT_FRAME_DARK;
        } else if (frame == RIGHT_FRAME_GRAY) {
            this.rightHandedFrame = RIGHT_FRAME_GRAY;
            this.leftHandedFrame = LEFT_FRAME_GRAY;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        speed = v.add(velocity);
        pos.mulAdd(speed, delta);
        pos2.mulAdd(speed,delta);





        if (this.getTop() > worldBounds.getTop() && ((v.add(velocity)).y > 0.3f)) {
            velocity.set(0, -VELOCITY_Y);
            v.set(0f, 0f);
            pos.mulAdd(v.add(velocity), delta);
            pos2.mulAdd(v.add(velocity), delta);
        }
        if (this.getBottom() < worldBounds.getBottom() && (v.add(velocity)).y < -0.3f) {
            velocity.set(0, VELOCITY_Y);
            v.set(0f, 0f);
            pos.mulAdd(v.add(velocity), delta);
            pos2.mulAdd(v.add(velocity), delta);
        }
        if (velocity.y < 0f) {

            frame = rightHandedFrame;
            this.angle = v.y * 111;

            dot.set(getLeft() , getBottom(),
                    getWidth()/17f, getHeight()/17f );
            dot.setPosition(pos.x,pos.y);



            board.set(getLeft() + getWidth() * 0.4f, getBottom() + getHalfWidth() / 1.5f,
                    getWidth() / 1.7f, getHeight() / 3f);
            boardPos.set(getLeft() + getHalfWidth() * 1.3f, getBottom() + getHeight()/1.2f );
            board2.setPos(boardPos);

            polygon.setVertices(vertexArrayRightHand);


            polygon.setPosition(pos2.x ,pos2.y);
            polygon.setRotation(angle);
            entirePic.setPosition(pos.x,pos.y);

            entirePic.setRotation(angle);

            System.out.println("pos.y - pos2.y = rrrrrrrrrrrrrrrrrrrrrrr  "+(Math.abs(pos.y)-Math.abs(pos2.y)));

        }
        if (velocity.y > 0f) {

            frame = leftHandedFrame;
            this.angle = v.y * 111;


            dot.set(getLeft() , getBottom(),
                    getWidth()/17f, getHeight()/17f );
            dot.setPosition(pos.x,pos.y);

            polygon.setVertices(vertexArrayLeftHand);


            board.set(getLeft() + getWidth() * 0.385f, getBottom() + getHalfWidth() / 3.9f,
                    getWidth() / 1.7f, getHeight() / 3f);
            boardPos.set(getLeft() + getHalfWidth() * 1.3f, getBottom() + getHalfHeight() / 1.1f);
            board2.setPos(boardPos);


            polygon.setPosition(pos.x,pos.y);
            polygon.setRotation(angle);
            entirePic.setPosition(pos.x,pos.y);

            entirePic.setRotation(angle);
            System.out.println("pos.y - pos2.y = lllllllllllllllllll "+(Math.abs(pos.y)-Math.abs(pos2.y)));

        }
    }

    public void resize(Rect worldBounds, float x, float y) {
        super.resize(worldBounds);
        pos.set(x, y);
        float a = y+0.02f;
        pos2.set(x,a);

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
        pos.set(worldBounds.getLeft() + 1.2f * this.getWidth(), 0f);
        pos2.set(worldBounds.getLeft() + 1.2f * this.getWidth(), 0.032f);

        velocity.set(lastVelocity.rotate(180));
    }

    public void gameOver(){
        v.set(-0.0f, 0.0f);
        velocity.set(0.0f, -0.0f);
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


    public void pause() {
        prePauseSpeed.set(speed);
        preVelocity.set(velocity);
        speed.set(0f, 0f);
        velocity.set(0f, 0f);
    }

    public void resume(){
        speed.set(prePauseSpeed);
        velocity.set(preVelocity);
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public Polygon getEntirePic() {
        return entirePic;
    }

    public Rectangle getDot() {
        return dot;
    }
}
