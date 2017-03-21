package com.move2play.whippingtop;

import com.badlogic.gdx.Game;
import com.move2play.whippingtop.screens.Splash;

public class WhippingTopGame extends Game {
        public static final int WIDTH=800;
        public static final int HEIGHT=600;
        public static final int FPS=30;
        
	@Override
	public void create () {
            setScreen(new Splash());
	}

	@Override
	public void render () {
            super.render();
	}
	
	@Override
	public void dispose () {
            super.dispose();
	}
        
        @Override
	public void resize (int width, int height) {
            super.resize(width,height);
	}
        
        @Override
	public void pause () {
            super.pause();
	}
        
        @Override
	public void resume () {
            super.resume();
	}
}

/*
package com.move2play.whippingtop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WhippingTopGame extends Game {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("img/Move2Play.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}

*/