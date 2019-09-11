package ru.ibelykh.sickdopeskills.utils;


import com.badlogic.gdx.Game;

import ru.ibelykh.sickdopeskills.screens.MenuScreen;


public class SickDope2D extends Game {
    @Override
    public void create() { setScreen(new MenuScreen(this));
    }
}
