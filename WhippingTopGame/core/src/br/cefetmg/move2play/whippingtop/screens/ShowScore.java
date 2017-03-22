package br.cefetmg.move2play.whippingtop.screens;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import br.cefetmg.move2play.whippingtop.WhippingTopGame;
import br.cefetmg.move2play.whippingtop.game.TopPlayer;
import br.cefetmg.move2play.whippingtop.util.Util;
import br.cefetmg.move2play.whippingtop.util.files.Fonts;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import java.util.ArrayList;
import java.util.List;

public class ShowScore implements Screen,Move2PlayGame{

    private SpriteBatch batch;
    private WhippingTopGame game;
    private BitmapFont font;
    private List<TopPlayer> players;
    
    
    public ShowScore(List<TopPlayer> players,WhippingTopGame game){
        this.game=game;
        font=Util.createBitMapFromTTF(Fonts.GooD(), Color.BLUE, 30);
        batch=new SpriteBatch();
        this.players=players;
    }
    
    @Override
    public void show() {
        game.eventHandler=(Move2PlayGame) this;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();
        draw(delta);
    }
    
    public void draw(float delta){
        String text="";
        int pad=10;
        for(TopPlayer tp:players){
            text+="Nick: "+tp.getPlayer().getName();
            for(int i=tp.getPlayer().getName().length();i<=pad;i++)
                text+=" ";
            text+=" Score: "+tp.getTop().getPoints();
            for(int i=(tp.getTop().getPoints()+"").length();i<=pad;i++)
                text+=" ";
            text+=" Pedaladas: "+tp.getTop().getPedals()+"\n\n\n";
        }
        Util.drawCenteredText(text,new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2),font,batch);
    }

    @Override
    public void resize(int width, int height) {
        int w=game.getSettings().get("width");
        int h=game.getSettings().get("height");
        Vector2 size = Scaling.fit.apply(w,h, width, height);
        int viewportX = (int)(width - size.x) / 2;
        int viewportY = (int)(height - size.y) / 2;
        int viewportWidth = (int)size.x;
        int viewportHeight = (int)size.y;
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
    }
    
    public void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.I)){//start game
            game.setScreen(new WaitingPlayers(game));
        }
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
        font.dispose();
    }

    @Override
    public void initGame() {
        System.out.println("InitGame from Show Scores");
    }

    @Override
    public void startMatch() {
        System.out.println("startMatch from Show Scores");
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                game.setScreen(new WaitingPlayers(game));
            }
        });
    }
    
    @Override
    public void finishMatch() {
        System.out.println("finishMatch from Show Scores");
    }

    @Override
    public void addPlayer(Player player) {
        System.out.println("addPlayer from Show Scores");
    }

    @Override
    public void removePlayer(Player player) {
        System.out.println("rmPlayer from Show Scores");
    }

    @Override
    public void move(String uuid,int i) {
        System.out.println("Move from Show Scores");
    }

    @Override
    public void closeGame() {
        System.out.println("CloseGame from show score");
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        });
    }    
}
