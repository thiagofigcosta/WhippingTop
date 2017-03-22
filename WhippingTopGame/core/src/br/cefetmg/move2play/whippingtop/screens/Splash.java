package br.cefetmg.move2play.whippingtop.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import br.cefetmg.move2play.whippingtop.WhippingTopGame;
import br.cefetmg.move2play.whippingtop.tween.SpriteAcessor;
import br.cefetmg.move2play.whippingtop.util.files.Images;
import com.badlogic.gdx.math.Rectangle;
import java.util.List;

public class Splash implements Screen,Move2PlayGame{

    private SpriteBatch batch;
    private Sprite moveLogo;
    private TweenManager tween;
    private WhippingTopGame game;
    
    private Rectangle viewport;
    
    public Splash(WhippingTopGame game){
        this.game=game;
    }
    
    @Override
    public void show() {
        game.eventHandler=(Move2PlayGame) this;
        batch=new SpriteBatch();
        tween=new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAcessor());
        moveLogo=new Sprite(new Texture(Images.Move2PlayLogo()));
        moveLogo.setScale(.5f, .5f);
        moveLogo.setPosition(Gdx.graphics.getWidth()/2.0f - moveLogo.getWidth()/2.0f, Gdx.graphics.getHeight()/2.0f- moveLogo.getHeight()/2.0f);
        Tween.set(moveLogo, SpriteAcessor.ALPHA).target(0).start(tween);
        Tween.to(moveLogo, SpriteAcessor.ALPHA, 1).target(1).start(tween);
        Tween.to(moveLogo, SpriteAcessor.ALPHA, 1).target(0).delay(2).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                game.setScreen(new WaitingPlayers(game));
            }
        }).start(tween);
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
        moveLogo.getTexture().dispose();
        //tween.killall()??????
    }    

    @Override
    public void startMatch() {
        System.out.println("startMatch from splash");
    }
    
    @Override
    public void finishMatch() {
        System.out.println("finishMatch from splash");
    }

    @Override
    public void addPlayer(Player player) {
        System.out.println("AddPlayer from splash");
    }

    @Override
    public void removePlayer(Player player) {
        System.out.println("RemovePlayer from splash");
    }

    @Override
    public void move(String uuid,int i) {
        System.out.println("Move from splash");
    }

    @Override
    public void initGame() {
        System.out.println("InitGame from splash");
    }
    
    @Override
    public void closeGame() {
        System.out.println("CloseGame from Waiting splash");
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        });
    }
   
}
