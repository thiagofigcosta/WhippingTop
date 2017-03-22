package br.cefetmg.move2play.whippingtop.screens;

import br.cefetmg.move2play.whippingtop.WhippingTopGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;

public class GameModeSelect implements Screen{

    
    private WhippingTopGame game;
    
    public GameModeSelect(WhippingTopGame game){
        this.game=game;
    }
    
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {
        Vector2 size = Scaling.fit.apply(WhippingTopGame.WIDTH, WhippingTopGame.HEIGHT, width, height);
        int viewportX = (int)(width - size.x) / 2;
        int viewportY = (int)(height - size.y) / 2;
        int viewportWidth = (int)size.x;
        int viewportHeight = (int)size.y;
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
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
    }
    
}
