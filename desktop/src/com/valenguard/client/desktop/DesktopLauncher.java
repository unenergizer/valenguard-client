package com.valenguard.client.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.valenguard.client.Settings;
import com.valenguard.client.Valenguard;

/**
 * Created by unene on 12/20/2017.
 */

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Settings.SCREEN_WIDTH;
		config.height = Settings.SCREEN_HEIGHT;
		new LwjglApplication(new Valenguard(), config);
	}
}
