package ru.ibelykh.sickdopeskills;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import ru.ibelykh.sickdopeskills.utils.SickDope2D;


public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		// Спрайты привязаны к краям мира, поэтому игра должна владеть всем экраном:
		// иначе принудительный edge-to-edge из targetSdk 35 прячет подписи под бары.
		// В libGDX 1.13 это уже дефолт, но дефолт здесь уже менялся — фиксируем явно.
		config.useImmersiveMode = true;
		initialize(new SickDope2D(), config);
	}
}
