package com.move2play.whippingtop.screens;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.move2play.whippingtop.WhippingTopGame;
import com.move2play.whippingtop.game.Top;
import com.move2play.whippingtop.game.TopPlayer;
import com.move2play.whippingtop.game.Track;
import com.move2play.whippingtop.util.files.Models;
import com.move2play.whippingtop.util.files.Musics;
import com.move2play.whippingtop.util.files.Sounds;
import com.move2play.whippingtop.util.files.Textures;
import java.util.ArrayList;
import java.util.List;

public class WTGame implements Screen, Move2PlayGame{
    private ModelBatch modelBatch;
    private AssetManager assets;
    private Environment environment;
    private Camera cam;
    private CameraInputController camController;
    private List<ModelInstance> topObjs;
    private List<ModelInstance> trackObjs;
    private ModelInstance skyObj;
    private ModelInstance trackObj;
    private Music rollingSound;
    private Music stageMusic;
    private boolean loading;
    
    private WhippingTopGame game;
    private List<TopPlayer> players;
    private Track track;
    
    public WTGame(WhippingTopGame game){
        this.game=game;
        players=new ArrayList();
        topObjs=new ArrayList();
        trackObjs=new ArrayList();
        track=new Track(50);
             
    }
    
    @Override
    public void show() {
        game.eventHandler=this;
        modelBatch=new ModelBatch();
        environment=new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.4f, 0.4f, 0.4f, 1));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1, -0.8f, -0.2f));
        float aspectRatio = ((float)Gdx.graphics.getWidth())/Gdx.graphics.getHeight();
        cam = new PerspectiveCamera(90, WhippingTopGame.HEIGHT * aspectRatio, WhippingTopGame.HEIGHT);
        cam.position.set(0f, 8.3f, -3f);
        cam.lookAt(0, 0.35f, 10f);
        cam.near=1;
        cam.far=300;
        cam.update();
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
        loadFiles();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        if(!WhippingTopGame.runningOnMove2Play){
            System.out.println("No Move2Play");
            System.out.println("Single Player Local");
            TopPlayer tp=new TopPlayer(new Player(),Top.DEFAULT(track));
            players.add(tp);
        }
    }
    
    
    private void createTrackModel(){
        Texture trackTexture = assets.get(Textures.Track(), Texture.class);
        Material trackMaterial = new Material(TextureAttribute.createDiffuse(trackTexture));
        double trackRealSize=0;
        for(int i=0;i<track.getFullSize();i++){
            trackRealSize+=track.sizeAt(i);
        }  
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "floor";
        MeshPartBuilder partBuilder = modelBuilder.part("floor",GL20.GL_TRIANGLES,Usage.Position|Usage.Normal|Usage.TextureCoordinates, trackMaterial);
        partBuilder.setColor(Color.GRAY);
        partBuilder.rect(-Track.TRACKWIDTH/2,0,0, 
                         -Track.TRACKWIDTH/2,0,(float)trackRealSize, 
                          Track.TRACKWIDTH/2,0,(float)trackRealSize, 
                          Track.TRACKWIDTH/2,0,0,               0, 1, 0);
        trackObj=new ModelInstance(modelBuilder.end());
    }
    
    private void createSkyModel(){
        Texture skyTex = assets.get(Textures.Sky(), Texture.class);
        Material skyMaterial = new Material(TextureAttribute.createDiffuse(skyTex));
        ModelBuilder modelBuilder = new ModelBuilder();
        skyObj=new ModelInstance(modelBuilder.createSphere(-300, -300, -300, 20, 20, skyMaterial, Usage.Position|Usage.Normal|Usage.TextureCoordinates));
    }
    
    private void loadFiles(){
        assets=new AssetManager();
        assets.load(Models.TOP(), Model.class);
        assets.load(Textures.Track(), Texture.class);
        assets.load(Textures.Sky(), Texture.class);
        rollingSound=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Wheel()));
        stageMusic=Gdx.audio.newMusic(Gdx.files.internal(Musics.Music1()));
        loading=true;
    }
    
    private void doneLoading(){
        createTrackModel();
        createSkyModel();
        Model topModel=assets.get(Models.TOP(), Model.class);//gisele bundchen
        for(TopPlayer tp:players){
            ModelInstance tmp=new ModelInstance(topModel);
            tmp.transform.setToTranslation(0, 0, 0);
            topObjs.add(tmp);
        }
        rollingSound.setLooping(true);
        rollingSound.setVolume(0);
        rollingSound.play();
        stageMusic.setLooping(true);
        stageMusic.setVolume(0.1f);
        stageMusic.play();
        
        loading=false;
    }
    
    private void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if(!WhippingTopGame.runningOnMove2Play){
                System.out.println("single player pedal");
                players.get(0).getTop().pedal();
            }
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
            ModelInstance topInstance=topObjs.get(i);
            t.roll();
            if(!WhippingTopGame.runningOnMove2Play){
                rollingSound.setVolume((float)((t.getSpeed()*t.getSpeed())/(t.getMaxVelocity()*t.getMaxVelocity())));
                skyObj.transform.idt();
                skyObj.transform.translate(t.getGamePosition());
                cam.position.z=t.getGamePosition().z-3f;
                cam.update();
            }
            topInstance.transform.idt();
            topInstance.transform.translate(t.getGamePosition());
            topInstance.transform.rotate(Vector3.Y, t.getGameYawOrientation());
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
            modelBatch.render(trackObj,environment);
            modelBatch.render(topObjs,environment);
            modelBatch.render(trackObjs, environment);
            modelBatch.render(skyObj, environment);
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
        topObjs.clear();
        trackObjs.clear();
        stageMusic.stop();
        rollingSound.stop();
        stageMusic.dispose();
        rollingSound.dispose();
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
