package com.move2play.whippingtop.screens;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.move2play.whippingtop.WhippingTopGame;
import com.move2play.whippingtop.game.Top;
import com.move2play.whippingtop.game.TopPlayer;
import com.move2play.whippingtop.game.Track;
import java.util.ArrayList;
import java.util.List;

public class WTGame implements Screen, Move2PlayGame{
    private ModelBatch modelBatch;
    private AssetManager assets;
    private Environment environment;
    private Camera cam;
    private CameraInputController camController;
    private List<ModelInstance> tops;
    private List<ModelInstance> trackObjs;
    private boolean loading;
    
    private WhippingTopGame game;
    private List<TopPlayer> players;
    private Track track;
    
    public WTGame(WhippingTopGame game){
        this.game=game;
        players=new ArrayList();
        tops=new ArrayList();
        trackObjs=new ArrayList();
        track=new Track(20);
    }
    
    @Override
    public void show() {
        game.eventHandler=this;
        
        modelBatch=new ModelBatch();
        environment=new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.4f, 0.4f, 0.4f, 1));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1, -0.8f, -0.2f));
        float aspectRatio = ((float)Gdx.graphics.getWidth())/Gdx.graphics.getHeight();
        cam = new PerspectiveCamera(67, WhippingTopGame.HEIGHT * aspectRatio, WhippingTopGame.HEIGHT);
        cam.position.set(1f, 1.75f, 3f);
        cam.lookAt(0, 0.35f, 0);
        cam.near=1;
        cam.far=300;
        cam.update();
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
        assets=new AssetManager();
        assets.load("obj/test.obj", Model.class);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        if(!WhippingTopGame.runningOnMove2Play){
            System.out.println("No Move2Play");
            System.out.println("Single Player Local");
            TopPlayer tp=new TopPlayer(new Player(),Top.DEFAULT(track));
            players.add(tp);
        }
        loading=true;
    }
    
    private void doneLoading(){
        Model topModel=assets.get("obj/test.obj", Model.class);//gisele bundchen
        for(TopPlayer tp:players){
            ModelInstance tmp=new ModelInstance(topModel);
            tmp.transform.setToTranslation(0, 0, 0);
            tops.add(tmp);
        }
        loading=false;
    }
    
    private void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if(!WhippingTopGame.runningOnMove2Play)
                System.out.println("single player pedal");
                players.get(0).getTop().pedal();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            System.out.println("reseting...");
            this.dispose();
            game.setScreen(new WTGame(game));
        }
    }
    
    private void gameLogic(){
        track.climaticFoward();
        for(int i=0;i<players.size();i++){
            Top t=players.get(i).getTop();
            ModelInstance topInstance=tops.get(i);
            //topInstance.transform.translate(Vector3.Zero.sub(t.getGamePosition()));
            t.roll();
            topInstance.transform.setToTranslation(0, 0, 0);
            topInstance.transform.rotate(Vector3.Y, (float)t.getSpeed());
            topInstance.transform.translate(t.getGamePosition());
        }
    }

    @Override
    public void render(float delta) {
        if(loading&&assets.update())
            doneLoading();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        if(!loading){
            handleInput();
            gameLogic();
            camController.update();
            modelBatch.begin(cam);
            modelBatch.render(tops,environment);
            modelBatch.render(trackObjs, environment);
            modelBatch.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        float aspectRatio=(float)width/(float)height;
        cam.viewportWidth=WhippingTopGame.HEIGHT*aspectRatio;
        cam.viewportHeight=WhippingTopGame.HEIGHT;
        cam.update();
//        Vector2 size = Scaling.fit.apply(WhippingTopGame.WIDTH, WhippingTopGame.HEIGHT, width, height);
//        int viewportX = (int)(width - size.x) / 2;
//        int viewportY = (int)(height - size.y) / 2;
//        int viewportWidth = (int)size.x;
//        int viewportHeight = (int)size.y;
//        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
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
        modelBatch.dispose();
        assets.dispose();
        tops.clear();
        trackObjs.clear();
    }

    @Override
    public void startGame(List<Player> list) {
        System.out.println("StartGame from WTGame");
    }

    @Override
    public void addPlayer(Player player) {
        System.out.println("AddPlayer from WTGame");
    }

    @Override
    public void removePlayer(Player player) {
        System.out.println("RmPlayer from WTGame");
    }

    @Override
    public void move(int i) {
        System.out.println("Move from WTGame");
    }

    @Override
    public void initGame() {
        System.out.println("InitGame from WTGame");
    }
    
}
