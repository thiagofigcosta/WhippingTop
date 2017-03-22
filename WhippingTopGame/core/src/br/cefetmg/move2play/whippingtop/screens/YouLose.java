package br.cefetmg.move2play.whippingtop.screens;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import br.cefetmg.move2play.whippingtop.WhippingTopGame;
import br.cefetmg.move2play.whippingtop.util.Util;
import br.cefetmg.move2play.whippingtop.util.files.Fonts;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import java.util.List;

public class YouLose implements Screen,Move2PlayGame{

    private SpriteBatch batch;
    private WhippingTopGame game;
    private BitmapFont bloodyFont;
    
    public YouLose(WhippingTopGame game){
        this.game=game;
        bloodyFont=Util.createBitMapFromTTF(Fonts.Bloody(), Color.RED, 40);
        batch=new SpriteBatch();
    }
    
    @Override
    public void show() {
        game.eventHandler=(Move2PlayGame) this;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        draw(delta);
    }
    
    public void draw(float delta){
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bloodyFont, "You Lose, Loser");
        float textSizeX=glyphLayout.width/2;
        float textSizeY=glyphLayout.height/2;
        batch.begin();
        bloodyFont.draw(batch, "You Lose, Loser", Gdx.graphics.getWidth()/2-textSizeX, Gdx.graphics.getHeight()/2+textSizeY);
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
        bloodyFont.dispose();
    }

    @Override
    public void initGame() {
    }

    @Override
    public void startGame(List<Player> list) {
    }

    @Override
    public void addPlayer(Player player) {
    }

    @Override
    public void removePlayer(Player player) {
    }

    @Override
    public void move(int i) {
    }
    
}
