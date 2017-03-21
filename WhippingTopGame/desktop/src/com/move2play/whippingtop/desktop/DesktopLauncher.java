package com.move2play.whippingtop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.move2play.whippingtop.WhippingTopGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.title="Whipping Top Game";
                config.width=WhippingTopGame.WIDTH;
                config.height=WhippingTopGame.HEIGHT;
		new LwjglApplication(new WhippingTopGame(), config);
	}
}
