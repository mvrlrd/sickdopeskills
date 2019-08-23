package ru.ibelykh.sickdopeskills.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.pools.TreePool;
import ru.ibelykh.sickdopeskills.screens.GameScreen;
import ru.ibelykh.sickdopeskills.sprites.Tree;


public class TreeEmitter {
private static final float MINTREESIZE = 0.1f;
    private static final float MAXTREESIZE = 0.3f;

    private static final float MINTREEINTERVAL = 0.5f;
    private static final float MAXTREEINTERVAL = 1.2f;
    private Rect worldBounds;
    private float generateInterval;
    private float generateTimer;
    private TextureRegion[] treeRegion;
    private TreePool treePool;


    public TreeEmitter(Rect worldBounds, TreePool treePool, TextureAtlas atlas) {
        this.worldBounds = worldBounds;
        this.treePool = treePool;
        TextureRegion textureRegion = atlas.findRegion("forest");
        this.treeRegion = Regions.split(textureRegion, 1, 9, 9);
    }

    public void generateTreesTop(float delta) {

        if (GameScreen.getIsPlaying()) {
            generateInterval = Rnd.nextFloat(MINTREEINTERVAL,MAXTREEINTERVAL);
            generateTimer += delta;
            if (generateTimer >= generateInterval) {
                generateTimer = 0f;
                Tree tree = treePool.obtain();
//                tree.setFrame(Rnd.nextInt(0,9));
                if (GameScreen.getCountPoints()<4){
                    tree.setFrame(2);

                } if ((GameScreen.getCountPoints()>=4)&&(GameScreen.getCountPoints()<7)) {
                    tree.setFrame(Rnd.nextIntInterval(5,6));
                }
                if ((GameScreen.getCountPoints()>=7)&&(GameScreen.getCountPoints()<10)) {
                    tree.setFrame(Rnd.nextIntInterval(0,1));
                }
                if ((GameScreen.getCountPoints()>=10)&&(GameScreen.getCountPoints()<13)) {
                    tree.setFrame(Rnd.nextIntInterval(3,4));
                }
                if ((GameScreen.getCountPoints()>=13)) {
                    tree.setFrame(Rnd.nextIntInterval(7,8));
                }
                tree.set(
                        treeRegion,
                        Rnd.nextFloat(MINTREESIZE, MAXTREESIZE)
                );
                tree.pos.y = Rnd.nextFloat(0.4f, worldBounds.getTop());
                tree.pos.x = (worldBounds.getRight()+tree.getHalfHeight());
            }
        }
    }

    public void generateTreesBottom(float delta) {

        if (GameScreen.getIsPlaying()) {
            generateInterval = Rnd.nextFloat(MINTREEINTERVAL,MAXTREEINTERVAL);
            generateTimer += delta;
            if (generateTimer >= generateInterval) {
                generateTimer = 0f;
                Tree tree = treePool.obtain();
//                tree.setFrame(Rnd.nextInt(0,2));
                if (GameScreen.getCountPoints()<4){
                    tree.setFrame(2);

                } if ((GameScreen.getCountPoints()>=4)&&(GameScreen.getCountPoints()<7)) {
                    tree.setFrame(Rnd.nextIntInterval(5,6));
                }
                if ((GameScreen.getCountPoints()>=7)&&(GameScreen.getCountPoints()<10)) {
                    tree.setFrame(Rnd.nextIntInterval(0,1));
                }
                if ((GameScreen.getCountPoints()>=10)&&(GameScreen.getCountPoints()<13)) {
                    tree.setFrame(Rnd.nextIntInterval(3,4));
                }
                if ((GameScreen.getCountPoints()>=13)) {
                    tree.setFrame(Rnd.nextIntInterval(7,8));
                }


                tree.set(

                        treeRegion,
                        Rnd.nextFloat(MINTREESIZE, MAXTREESIZE)
                );
                tree.pos.y = Rnd.nextFloat(-0.4f, worldBounds.getBottom());
                tree.pos.x = (worldBounds.getRight()+tree.getHalfHeight());
            }
        }
    }
}
