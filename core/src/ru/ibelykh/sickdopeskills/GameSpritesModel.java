package ru.ibelykh.sickdopeskills;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.ibelykh.sickdopeskills.buttons.ButtonGameSoundOffOn;
import ru.ibelykh.sickdopeskills.buttons.ButtonPause;
import ru.ibelykh.sickdopeskills.interfaces.GameModelMethods;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.math.Rnd;
import ru.ibelykh.sickdopeskills.pools.BrizgiPool;
import ru.ibelykh.sickdopeskills.pools.FlagPool;
import ru.ibelykh.sickdopeskills.pools.TreePool;

import ru.ibelykh.sickdopeskills.sprites.Brizgi;
import ru.ibelykh.sickdopeskills.sprites.Flag;
import ru.ibelykh.sickdopeskills.sprites.Rider;
import ru.ibelykh.sickdopeskills.sprites.Snow;

import ru.ibelykh.sickdopeskills.sprites.StartGates;
import ru.ibelykh.sickdopeskills.sprites.Tree;
import ru.ibelykh.sickdopeskills.utils.BrizgiEmitter;
import ru.ibelykh.sickdopeskills.utils.FlagEmitter;
import ru.ibelykh.sickdopeskills.utils.TreeEmitter;

public class GameSpritesModel implements GameModelMethods {
    private final TextureAtlas ALL_SPRITES_TEXTURE_ATLAS = new TextureAtlas("images/sprites/allSprites.atlas");
    private static final int SPLASH_COUNT = 20;
    private static final int SNOW_COUNT = Rnd.nextInt(500, 10000);
    private StartGates startGates;
    private Rider rider;
    private Snow[] snows;
    private List<Tree> treeList;
    private TreePool treePool;
    private TreeEmitter treeEmitter;
    private List<Flag> flagList;
    private FlagPool flagPool;
    private FlagEmitter flagEmitter;
    private ButtonGameSoundOffOn buttonGameSoundOffOn;
    private ButtonPause buttonPause;

    private List<Brizgi>brizgiList;
    private BrizgiPool brizgiPool;
    private BrizgiEmitter brizgiEmitter;

    public GameSpritesModel(Rect worldBounds) {
        this.rider = new Rider(ALL_SPRITES_TEXTURE_ATLAS,worldBounds);
        this.startGates = new StartGates(ALL_SPRITES_TEXTURE_ATLAS,worldBounds);

        //snow
        this.snows = new Snow[SNOW_COUNT];
        float windDirectionX = Rnd.nextFloat(-0.7f, 0.7f); //wind speed on X
        float windDirectionY = Rnd.nextFloat(-0.7f, 0.7f); //wind speed on Y
        for (int i = 0; i < snows.length; i++) {
            Vector2 snowSpeed = new Vector2(Rnd.nextFloat(windDirectionX - 0.1f, windDirectionX + 0.1f),
                    Rnd.nextFloat(windDirectionY - 0.1f, windDirectionY + 0.1f));
            snows[i] = new Snow(ALL_SPRITES_TEXTURE_ATLAS, snowSpeed);
        }
        treePool = new TreePool(worldBounds);
        treeList = treePool.getActiveObjects();
        treeEmitter = new TreeEmitter(worldBounds, treePool, ALL_SPRITES_TEXTURE_ATLAS);
        flagPool = new FlagPool(worldBounds);
        flagList = flagPool.getActiveObjects();
        flagEmitter = new FlagEmitter(worldBounds, flagPool, ALL_SPRITES_TEXTURE_ATLAS);
        buttonGameSoundOffOn = new ButtonGameSoundOffOn(ALL_SPRITES_TEXTURE_ATLAS);
        buttonPause = new ButtonPause(ALL_SPRITES_TEXTURE_ATLAS);

        brizgiPool = new BrizgiPool(worldBounds);
        brizgiList = brizgiPool.getActiveObjects();
        brizgiEmitter = new BrizgiEmitter(worldBounds,brizgiPool,ALL_SPRITES_TEXTURE_ATLAS,rider);
    }

    public Rider getRider() {
        return rider;
    }

    public StartGates getStartGates() {
        return startGates;
    }



    public Snow[] getSnows() {
        return snows;
    }

    public TreeEmitter getTreeEmitter() {
        return treeEmitter;
    }

    public List<Tree> getTreeList() {
        return treeList;
    }

    public TreePool getTreePool() {
        return treePool;
    }

    public List<Flag> getFlagList() {
        return flagList;
    }

    public FlagPool getFlagPool() {
        return flagPool;
    }

    public FlagEmitter getFlagEmitter() {
        return flagEmitter;
    }

    public ButtonGameSoundOffOn getButtonGameSoundOffOn() {
        return buttonGameSoundOffOn;
    }

    public ButtonPause getButtonPause() {
        return buttonPause;
    }

    @Override
    public void draw(SpriteBatch batch) {

        brizgiPool.drawActiveSprites(batch);
        rider.draw(batch);
        startGates.draw(batch);
        flagPool.drawActiveSprites(batch);
        treePool.drawActiveSprites(batch);
        //snows
        for (Snow singleSnowflake : snows) {
            singleSnowflake.draw(batch);
        }
        buttonPause.draw(batch);
        buttonGameSoundOffOn.draw(batch);


    }

    @Override
    public void update(float delta) {
        startGates.update(delta);
        rider.update(delta);

        //snows
        for (Snow singleSnowflake : snows) {
            singleSnowflake.update(delta);
        }
        treePool.updateActiveSprites(delta);
        treeEmitter.generateTreesTop(delta);
        treeEmitter.generateTreesBottom(delta);
        flagPool.updateActiveSprites(delta);
        flagEmitter.generateFlags(delta);
        buttonPause.update(delta);
        buttonGameSoundOffOn.update(delta);

        brizgiPool.updateActiveSprites(delta);
        brizgiEmitter.generateBrizgi(delta);
    }

    @Override
    public void resize(Rect worldBounds) {
        startGates.resize(worldBounds, worldBounds.getLeft() + 0.1f, 0f);
        rider.resize(worldBounds, worldBounds.getLeft() + 1.2f * rider.getWidth(), 0f);

        //snows
        for (Snow singleSnowflake : snows) {
            singleSnowflake.resize(worldBounds);
        }
        buttonPause.resize(worldBounds);
        buttonGameSoundOffOn.resize(worldBounds);
    }

    @Override
    public void dispose() {
        flagPool.dispose();
        treePool.dispose();
        ALL_SPRITES_TEXTURE_ATLAS.dispose();
    }

    @Override
    public void startNewGame() {
        startGates.setTheNewGame();
        rider.setTheNewGame();
    }

    @Override
    public void deleteAllDestroyed() {
        flagPool.freeAllDestroyedActiveSprites();
        treePool.freeAllDestroyedActiveSprites();
        brizgiPool.freeAllDestroyedActiveSprites();
    }
}
