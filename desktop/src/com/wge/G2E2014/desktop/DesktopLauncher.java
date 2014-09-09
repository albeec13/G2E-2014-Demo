package com.wge.G2E2014.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.wge.G2E2014.G2E_Demo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1600;
        config.height = 900;
        config.fullscreen = true;
		new LwjglApplication(new G2E_Demo(), config);
	}
}
