package ru.ibelykh.sickdopeskills.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.ibelykh.sickdopeskills.math.Rect;
import ru.ibelykh.sickdopeskills.sprites.Flag;
import ru.ibelykh.sickdopeskills.sprites.Tree;

public interface GameModelMethods {

    void draw(SpriteBatch batch);
    void update(float delta);
    void resize(Rect worldBounds);
    void dispose();
    void startNewGame();
    void deleteAllDestroyed();

}
