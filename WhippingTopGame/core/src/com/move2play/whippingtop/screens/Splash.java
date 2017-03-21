package com.move2play.whippingtop.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.move2play.whippingtop.WhippingTopGame;
import com.move2play.whippingtop.tween.SpriteAcessor;

public class Splash implements Screen{

    private SpriteBatch batch;
    private Sprite moveLogo;
    private TweenManager tween;
    
    @Override
    public void show() {
        batch=new SpriteBatch();
        tween=new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAcessor());
        moveLogo=new Sprite(new Texture("img/Move2Play.png"));
        moveLogo.setScale(.5f, .5f);
        moveLogo.setPosition((Gdx.graphics.getWidth() - moveLogo.getRegionWidth()) / 2.0f, (Gdx.graphics.getHeight()- moveLogo.getRegionHeight()) / 2.0f);
        Tween.set(moveLogo, SpriteAcessor.ALPHA).target(0).start(tween);
        Tween.to(moveLogo, SpriteAcessor.ALPHA, 1).target(1).start(tween);
        Tween.to(moveLogo, SpriteAcessor.ALPHA, 1).target(0).delay(2).start(tween);
    }

    @Override
    public void render(float delta) {
        tween.update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        moveLogo.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        Vector2 size = Scaling.fit.apply(WhippingTopGame.WIDTH, WhippingTopGame.HEIGHT, width, height);
        int viewportX = (int)(width - size.x) / 2;
        int viewportY = (int)(height - size.y) / 2;
        int viewportWidth = (int)size.x;
        int viewportHeight = (int)size.y;
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
        //stage.setViewport(WhippingTopGame.WIDTH, WhippingTopGame.HEIGHT, true, viewportX, viewportY, viewportWidth, viewportHeight);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }    
}
