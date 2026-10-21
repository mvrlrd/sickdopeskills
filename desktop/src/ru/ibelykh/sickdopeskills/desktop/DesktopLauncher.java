package ru.ibelykh.sickdopeskills.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.ibelykh.sickdopeskills.utils.SickDope2D;

public class DesktopLauncher {
	public static void main (String[] arg) {
		if (StartupHelper.startNewJvmIfRequired()) return;

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		float aspect = 3f/4f;
		int width = 650;
		int height = (int)(width/aspect);
		config.setWindowedMode(width, height);
		config.setResizable(false);
		new Lwjgl3Application(new SickDope2D(), config);

	}
}
