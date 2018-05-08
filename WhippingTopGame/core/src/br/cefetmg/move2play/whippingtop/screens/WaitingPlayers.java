package br.cefetmg.move2play.whippingtop.screens;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import br.cefetmg.move2play.whippingtop.Assets;
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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaitingPlayers implements Screen,Move2PlayGame{

    private SpriteBatch batch;
    private WhippingTopGame game;
    private BitmapFont font;
    private List<Sprite> colors;
    private List<Player> players;
    
    private Rectangle viewport;
    
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
        float w=viewport.width;
        float h=viewport.height;
        Util.drawCenteredText("Waiting players...", new Vector2(w/2, h/2+h/4), font, batch);
        for(int i=0;i<players.size();i++){
            Util.drawCenteredText(players.get(i).getName(), new Vector2(w/5+w/6.5f, h/1.5f-i*44), font, batch);
            colors.get(i).setColor(Util.byteToColor(players.get(i).getColor()));
            colors.get(i).setPosition(w/5, h/1.5f-i*44-15);
            batch.begin();
            colors.get(i).draw(batch);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height){
        int W=game.getSettings().get("width");
        int H=game.getSettings().get("height");
        float ASPECT_RATIO=(float)W/(float)H;
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        if(aspectRatio > ASPECT_RATIO){
            scale = (float)height/(float)H;
            crop.x = (width - W*scale)/2f;
        }else if(aspectRatio < ASPECT_RATIO){
            scale = (float)width/(float)W;
            crop.y = (height - H*scale)/2f;
        }else{
            scale = (float)width/(float)W;
        }
        float w = (float)W*scale;
        float h = (float)H*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,(int) viewport.width, (int) viewport.height);
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
    public boolean initGame() {
        System.out.println("InitGame from Waiting Players");
        return true;
    }

    @Override
    public boolean startMatch() {
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
            return true;
        }else{
            System.out.println("err - 0 active players");
            return false;
        }
    }
    
    @Override
    public boolean finishMatch() {
        System.out.println("finishMatch from Waiting Players(gg)");
        return false;
    }

    @Override
    public boolean addPlayer(Player player) {
        System.out.println("addPlayer from Waiting Players");
        final Player pl=player;
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                players.add(pl);
                colors.add(SpriteCreator.createCircle(20, new Vector2(20,20), Util.byteToColor(pl.getColor())));
            }
        });
        return true;
    }

    @Override
    public boolean removePlayer(Player player) {
        System.out.println("rmPlayer from Waiting Players");
        for(int i=0;i<players.size();i++)
            if(players.get(i).getUUID().equals(player.getUUID())){
                players.remove(i);
                colors.remove(i);
                return true;
            }
        return false;
    }

    @Override
    public boolean move(String uuid,int i) {
        System.out.println("Move from Waiting Players");
        return false;
    }

    @Override
    public boolean closeGame() {
        System.out.println("CloseGame from Waiting Players");
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        });
        return true;
    }    

    @Override
    public boolean reset() {
        return game.reset();
    }

    @Override
    public GameState getState() {
        return GameState.waitingPlayers;
    }
}
