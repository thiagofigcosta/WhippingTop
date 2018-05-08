package br.cefetmg.move2play.whippingtop.screens;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import br.cefetmg.move2play.whippingtop.WhippingTopGame;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import libgdxUtils.AnimatedSprite;
import libgdxUtils.VideoUtil;

public class Splash implements Screen,Move2PlayGame{

    private SpriteBatch batch;
    private WhippingTopGame game;
    
    private Rectangle viewport;
    
    private AnimatedSprite animationSplash;
    private Sound sound;
    
    public Splash(WhippingTopGame game){
        this.game=game;
        animationSplash = new AnimatedSprite(VideoUtil.imageSequenceToAnimation("anim/splash",24,51,".jpeg"));
    }
    
    @Override
    public void show() {
        game.eventHandler=(Move2PlayGame) this;
        sound = Gdx.audio.newSound(Gdx.files.internal("audio/sound/splash.mp3"));
        sound.play(1.0f);
        batch=new SpriteBatch();
        animationSplash.setPosition(Gdx.graphics.getWidth()/2.0f - animationSplash.getWidth()/2.0f, Gdx.graphics.getHeight()/2.0f- animationSplash.getHeight()/2.0f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(!animationSplash.isAnimationFinished()){
           batch.begin();
            animationSplash.draw(batch);
            batch.end();
        }else{
            game.setScreen(new WaitingPlayers(game));
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
        sound.dispose();
    }    

    @Override
    public boolean startMatch() {
        System.out.println("startMatch from splash");
        return false;
    }
    
    @Override
    public boolean finishMatch() {
        System.out.println("finishMatch from splash");
        return false;
    }

    @Override
    public boolean addPlayer(Player player) {
        System.out.println("AddPlayer from splash");
        return false;
    }

    @Override
    public boolean removePlayer(Player player) {
        System.out.println("RemovePlayer from splash");
        return false;
    }

    @Override
    public boolean move(String uuid,int i) {
        System.out.println("Move from splash");
        return false;
    }

    @Override
    public boolean initGame() {
        System.out.println("InitGame from splash");
        return false;
    }
    
    @Override
    public boolean closeGame() {
        System.out.println("CloseGame from Waiting splash");
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
        return GameState.loading;
    }
   
}
