package br.cefetmg.move2play.whippingtop.screens;

import br.cefetmg.move2play.whippingtop.WhippingTopGame;
import br.cefetmg.move2play.whippingtop.util.Util;
import br.cefetmg.move2play.whippingtop.util.files.Fonts;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;

public class MainMenu implements Screen{
    
    private SpriteBatch batch;
    private WhippingTopGame game;
    
    private BitmapFont gooDFont;
    private Skin skin;
    private Stage stage;
    
    public MainMenu(WhippingTopGame game){
        this.game=game;
        gooDFont=Util.createBitMapFromTTF(Fonts.GooD(), Color.BLACK, 20);
    }

    @Override
    public void show() { 
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
 
        
        skin = new Skin();
        skin.add("default", gooDFont);
        Pixmap pixmap = new Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background",new Texture(pixmap));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
        
        TextButton btn_newGame = new TextButton("Novo Jogo", skin); 
        btn_newGame.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , Gdx.graphics.getHeight()/2);
        btn_newGame.addListener(new ClickListener() {
        @Override
            public void clicked(InputEvent event, float x, float y) {
               game.setScreen(new WaitingPlayers(game));
            }
        });
        stage.addActor(btn_newGame);
        
        TextButton btn_Exit = new TextButton("Sair", skin); 
        btn_Exit.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/8 , Gdx.graphics.getHeight()/3);
        btn_Exit.addListener(new ClickListener() {
        @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        Gdx.app.exit();
                    }
                });
            }
        });
        stage.addActor(btn_Exit);
        
        
    }

    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
 
        stage.act();
        stage.draw();
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
