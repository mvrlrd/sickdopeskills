package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.ibelykh.sickdopeskills.base.ScaledButton;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.screens.GameScreen;

public class ButtonNewGame extends ScaledButton {
    private Game game;

    public ButtonNewGame(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("dope"));
        setHeightProportion(0.3f);
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
//        this.setAngle(90f);
        setTop(worldBounds.getTop());
        setRight(worldBounds.getRight()-0.04f);
    }

    @Override
    public void actionPerformed() {
        game.setScreen(new GameScreen(game));
    }
}