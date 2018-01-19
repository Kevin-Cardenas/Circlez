package com.cardenask.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cardenask.main.Main;

/** DesktopLauncher - launcher class for the desktop testing */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
		config.title = "Circle Defender";
		config.samples = 10;
		config.width = 500;
		config.height = 500 * 16 / 9;
		config.resizable = false;
		new LwjglApplication(new Main(), config);
	}
}
