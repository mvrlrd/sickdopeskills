package ru.ibelykh.sickdopeskills.utils;


import com.badlogic.gdx.Game;

import ru.ibelykh.sickdopeskills.screens.GameScreen;


public class SickDope2D extends Game {
    @Override
    public void create() { setScreen(new GameScreen(this));
    }
}
