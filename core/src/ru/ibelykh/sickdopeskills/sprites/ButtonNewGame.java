package ru.ibelykh.sickdopeskills.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.ibelykh.sickdopeskills.base.ScaledButton;
import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.screens.GameScreen;

public class ButtonNewGame extends ScaledButton {
    private Game game;

    public ButtonNewGame(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btplay"));
        setHeightProportion(0.2f);
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom()+0.07f);
        setRight(worldBounds.getRight()-0.07f);
    }

    @Override
    public void actionPerformed() {
        game.setScreen(new GameScreen(game));
    }
}