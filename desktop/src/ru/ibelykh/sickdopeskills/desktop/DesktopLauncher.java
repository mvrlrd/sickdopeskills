package ru.ibelykh.sickdopeskills.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.ibelykh.sickdopeskills.utils.SickDope2D;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		float aspect = 3f/4f;
		config.width=650;
		config.height=(int)(config.width/aspect);
		config.resizable=false;
		new LwjglApplication(new SickDope2D(), config);

	}
}





