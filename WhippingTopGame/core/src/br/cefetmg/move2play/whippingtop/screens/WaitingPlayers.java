package br.cefetmg.move2play.whippingtop.screens;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import br.cefetmg.move2play.whippingtop.WhippingTopGame;
import br.cefetmg.move2play.whippingtop.util.SpriteCreator;
import br.cefetmg.move2play.whippingtop.util.Util;
import br.cefetmg.move2play.whippingtop.util.files.Fonts;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaitingPlayers implements Screen,Move2PlayGame{

    private SpriteBatch batch;
    private WhippingTopGame game;
    private BitmapFont font;
    private List<Sprite> colors;
    private List<Player> players;
    
    
    public WaitingPlayers(WhippingTopGame game){
        this.game=game;
        font=Util.createBitMapFromTTF(Fonts.Amerika(), Color.RED, 40);
        batch=new SpriteBatch();
        colors=new ArrayList();
        players=new ArrayList();
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
        Util.drawCenteredText("Waiting players...", new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/1.15f), font, batch);
        for(int i=0;i<players.size();i++){
            Util.drawCenteredText(players.get(i).getName(), new Vector2(Gdx.graphics.getWidth()/5+Gdx.graphics.getWidth()/6.5f, Gdx.graphics.getHeight()/1.5f-i*44), font, batch);
            colors.get(i).setColor(Util.byteToColor(players.get(i).getColor()));
            colors.get(i).setPosition(Gdx.graphics.getWidth()/5, Gdx.graphics.getHeight()/1.5f-i*44-15);
            batch.begin();
            colors.get(i).draw(batch);
            batch.end();
        }
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
        if(Gdx.input.isKeyPressed(Input.Keys.S)){//start game
            int defaultTrackSize=game.getSettings().get("defaultTrackSize");
            if(players.size()==0){
                int players=game.getSettings().get("amountPlayers");
                game.setScreen(new WTGameManager(players,defaultTrackSize,game));
            }else{
                game.setScreen(new WTGameManager(players,defaultTrackSize,game));
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){//add player
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color c = new Color(r, g, b, 1);
            Player pl=new Player();
            pl.setName("Jogador "+(1+players.size()));
            pl.setUUID(""+players.size());
            pl.setColor(Util.colorToByte(c));
            addPlayer(pl);
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
        System.out.println("InitGame from Waiting Players");
    }

    @Override
    public void startMatch() {
        System.out.println("startMatch from Waiting Players");
        if(players.size()>0){
            final int defaultTrackSize=game.getSettings().get("defaultTrackSize");
            final List<Player> listf=players;
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new WTGameManager(listf,defaultTrackSize,game));
                }
            });
        }
    }
    
    @Override
    public void finishMatch() {
        System.out.println("finishMatch from Waiting Players(gg)");
    }

    @Override
    public void addPlayer(Player player) {
        System.out.println("addPlayer from Waiting Players");
        players.add(player);
        colors.add(SpriteCreator.createCircle(20, new Vector2(20,20), Util.byteToColor(player.getColor())));
    }

    @Override
    public void removePlayer(Player player) {
        System.out.println("rmPlayer from Waiting Players");
        for(int i=0;i<players.size();i++)
            if(players.get(i).getUUID().equals(player.getUUID())){
                players.remove(i);
                colors.remove(i);
                break;
            }
    }

    @Override
    public void move(String uuid,int i) {
        System.out.println("Move from Waiting Players");
    }

    @Override
    public void closeGame() {
        System.out.println("CloseGame from Waiting Players");
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        });
    }    
}
