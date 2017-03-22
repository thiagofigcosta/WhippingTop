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
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.move2play.whippingtop.WhippingTopGame;
import com.move2play.whippingtop.game.ClimateConditions;
import com.move2play.whippingtop.game.Obstacle;
import com.move2play.whippingtop.game.Top;
import com.move2play.whippingtop.game.TopPlayer;
import com.move2play.whippingtop.game.Track;
import com.move2play.whippingtop.util.Util;
import com.move2play.whippingtop.util.files.Images;
import com.move2play.whippingtop.util.files.Models;
import com.move2play.whippingtop.util.files.Musics;
import com.move2play.whippingtop.util.files.Particles;
import com.move2play.whippingtop.util.files.Sounds;
import com.move2play.whippingtop.util.files.Textures;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WTGame implements Screen, Move2PlayGame{
    private ModelBatch modelBatch;
    private SpriteBatch batch;
    private AssetManager assets;
    private Environment environment;
    private Camera cam;
    private CameraInputController camController;
    private List<ModelInstance> trackObjs;
    private ModelInstance topObj;
    private ModelInstance skyObj,trackObj;
    private Music rollingSound,stageMusic,soundTop0,soundTop1;
    private Sprite velocimeter,velocimeterPointer,velocimeterOptSpeed,semaphore,semRed,semYellow,semGreen;
    private List<Sprite> lifeSpr;
    private ParticleEffect rainPart,snowPart,sunnyPart;
    private boolean loading;
    
    private long FPScount;
    private boolean started;
    private WhippingTopGame game;
    private TopPlayer player;
    private Track track;
    
    public WTGame(WhippingTopGame game){
        this.game=game;
        trackObjs=new ArrayList();
        track=new Track(80);
        FPScount=0;
        started=false;
    }
    
    public WTGame(WhippingTopGame game, Track t){
        this.game=game;
        trackObjs=new ArrayList();
        track=t;
        FPScount=0;
        started=false;     
    }
    
    public Track getTrack(){
        return track;
    }
    
    @Override
    public void show() {
        game.eventHandler=this;
        modelBatch=new ModelBatch();
        batch=new SpriteBatch();
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
            player=tp;
        }
    }
    
    private double[] createTrackModel_createFloor(double size,double pos,int widthReducer,float depth,boolean defaultUVrange,Material mat,ModelBuilder modelBuilder){
        if(size>0){
            MeshPartBuilder base = modelBuilder.part("floor",GL20.GL_TRIANGLES,Usage.Position|Usage.Normal|Usage.TextureCoordinates, mat);
            if(!defaultUVrange)
                base.setUVRange(0, 0, (float) (size/23), 1);
            base.rect(-Track.TRACKWIDTH/widthReducer,depth,(float)pos, 
                        -Track.TRACKWIDTH/widthReducer,depth,(float)(size+pos), 
                         Track.TRACKWIDTH/widthReducer,depth,(float)(size+pos), 
                         Track.TRACKWIDTH/widthReducer,depth,(float)pos,               0, 1, 0);
        }
        pos+=size;
        size=0;
        double[] out=new double[2];
        out[0]=size;
        out[1]=pos;
        return out;
    }
    private void createTrackModel(){
        Texture floorText = assets.get(Textures.Track(), Texture.class);
        floorText.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        Texture arrivalText = assets.get(Textures.Arrival(), Texture.class);
        Material trackMaterial = new Material(TextureAttribute.createDiffuse(floorText));
        Texture bridgeText = assets.get(Track.genBridge(), Texture.class);
        Material obstacleMaterial = new Material(TextureAttribute.createDiffuse(bridgeText));
        obstacleMaterial.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
        Texture speedUpText = assets.get(Textures.SpeedUp(), Texture.class);
        double trackRealSize=0;
        double movedPosZ=0;
        int lastBridge=0;
        double[] size;
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "track";
        MeshPartBuilder obstacles;
        for(int i=0;i<track.getFullSize();i++){           
            if(track.obstacleTypeAt(i)==Obstacle.BRIDGE){
                if(i-lastBridge>1){
                    bridgeText = assets.get(Track.genBridge(), Texture.class);
                    obstacleMaterial = new Material(TextureAttribute.createDiffuse(bridgeText));
                    obstacleMaterial.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
                }
                lastBridge=i;
                size=createTrackModel_createFloor(trackRealSize,movedPosZ,2,0,false,trackMaterial,modelBuilder);
                trackRealSize=size[0];
                movedPosZ=size[1];
                size=createTrackModel_createFloor(track.sizeAt(i),movedPosZ,8,0,true,obstacleMaterial,modelBuilder);
                trackRealSize=size[0];
                movedPosZ=size[1];
            }else{
                if(track.obstacleTypeAt(i)==Obstacle.SPEEDUP){
                    obstacleMaterial = new Material(TextureAttribute.createDiffuse(speedUpText));
                    createTrackModel_createFloor(track.sizeAt(i),movedPosZ+trackRealSize,2,0.1f,true,obstacleMaterial,modelBuilder);
                }else if(track.obstacleTypeAt(i)==Obstacle.ARRIVAL){
                    obstacleMaterial = new Material(TextureAttribute.createDiffuse(arrivalText));
                    createTrackModel_createFloor(track.sizeAt(i),movedPosZ+trackRealSize,2,0.1f,true,obstacleMaterial,modelBuilder);
                }                
                trackRealSize+=track.sizeAt(i);
            }
        }  
        createTrackModel_createFloor(trackRealSize,movedPosZ,2,0,false,trackMaterial,modelBuilder);
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
        assets.load(Models.Grass(), Model.class);
        assets.load(Models.Cones(), Model.class);
        assets.load(Models.Cone0(), Model.class);
        assets.load(Models.Cone1(), Model.class);
        assets.load(Models.Cone2(), Model.class);
        assets.load(Models.Cone3(), Model.class);
        assets.load(Models.Cone4(), Model.class);
        assets.load(Models.Cone5(), Model.class);
        assets.load(Particles.Rain(), ParticleEffect.class);
        assets.load(Particles.Snow(), ParticleEffect.class);
        assets.load(Particles.Sunny(), ParticleEffect.class);
        assets.load(Textures.Track(), Texture.class);
        assets.load(Textures.Sky(), Texture.class);
        assets.load(Textures.Bridge0(), Texture.class);
        assets.load(Textures.Bridge1(), Texture.class);
        assets.load(Textures.Bridge2(), Texture.class);
        assets.load(Textures.Bridge3(), Texture.class);
        assets.load(Textures.Bridge4(), Texture.class);
        assets.load(Textures.SpeedUp(), Texture.class);
        assets.load(Images.Velocimeter(), Texture.class);
        assets.load(Images.VelPointer(), Texture.class);
        assets.load(Textures.Arrival(), Texture.class);
        assets.load(Images.Life(), Texture.class);
        assets.load(Images.Semaphore(), Texture.class);
        rollingSound=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Wheel()));
        stageMusic=Gdx.audio.newMusic(Gdx.files.internal(Musics.Music1()));
        soundTop0=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Top0()));
        soundTop1=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Top1()));
        loading=true;
    }
    
    private void doneLoading(){
        createTrackModel();
        createSkyModel();
        Model topModel=assets.get(Models.TOP(), Model.class);//gisele bundchen
        ModelInstance topModelInstance=new ModelInstance(topModel);
        topModelInstance.transform.setToTranslation(0, 0, 0);
        topObj= topModelInstance;
        Model grassModel=assets.get(Models.Grass(), Model.class);
        float currentSize=0;
        for(int i=0;i<track.getFullSize();i++){
            if(track.obstacleTypeAt(i)==Obstacle.GRASS){
                ModelInstance tmp=new ModelInstance(grassModel);
                tmp.transform.setToTranslation(0, 0, currentSize);
                trackObjs.add(tmp);
            }else if(track.obstacleTypeAt(i)==Obstacle.CONE){
                int nCones=(int)Math.round(track.difficultyAt(i))+2;
                float base=13.5f;
                Random rand = new Random();
                for(int n=0;n<nCones;n++){
                    Model coneModel=assets.get(Util.genCone(), Model.class);
                    ModelInstance tmp=new ModelInstance(coneModel);
                    float randX=rand.nextFloat()*base*0.71f+(1.3f*(rand.nextFloat()+0.13f));
                    if(n%2==0){
                        tmp.transform.setToTranslation(-randX, 0, currentSize+10+(rand.nextFloat()+0.3f)*randX*n);
                    }else{
                        tmp.transform.setToTranslation(+randX, 0, currentSize+10+(rand.nextFloat()+0.3f)*randX*n);
                    }
                    trackObjs.add(tmp);
                }  
            }
            currentSize+=track.sizeAt(i);
        }
        rainPart=((ParticleEffect) assets.get(Particles.Rain()));
        rainPart.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        snowPart=((ParticleEffect) assets.get(Particles.Snow()));
        snowPart.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        sunnyPart=((ParticleEffect) assets.get(Particles.Sunny()));
        sunnyPart.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        rollingSound.setLooping(true);
        rollingSound.setVolume(0);
        rollingSound.play();
        stageMusic.setLooping(true);
        stageMusic.setVolume(0.1f);
        stageMusic.play();
        soundTop0.setLooping(false);
        soundTop0.setVolume(1);
        soundTop1.setLooping(false);
        soundTop1.setVolume(1);
        semaphore= new Sprite(assets.get(Images.Semaphore(), Texture.class));
        semaphore.setPosition(Gdx.graphics.getWidth()/2.0f - semaphore.getWidth()/2.0f, Gdx.graphics.getHeight()/2.0f- semaphore.getHeight()/2.0f);
        Pixmap pixmap = new Pixmap((int)semaphore.getWidth(), (int)semaphore.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillCircle((int)semaphore.getWidth()/2, (int)semaphore.getHeight()/2, 30);
        semRed=new Sprite(new Texture(pixmap));
        pixmap.dispose();
        pixmap = new Pixmap((int)semaphore.getWidth(), (int)semaphore.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillCircle((int)semaphore.getWidth()/2, (int)semaphore.getHeight()/2, 30);
        semYellow=new Sprite(new Texture(pixmap));
        pixmap.dispose();
        pixmap = new Pixmap((int)semaphore.getWidth(), (int)semaphore.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fillCircle((int)semaphore.getWidth()/2, (int)semaphore.getHeight()/2, 30);
        semGreen=new Sprite(new Texture(pixmap));
        pixmap.dispose();
        semRed.setPosition(semaphore.getX(), semaphore.getY()+semaphore.getHeight()/3.7f);
        semYellow.setPosition(semaphore.getX(), semaphore.getY());
        semGreen.setPosition(semaphore.getX(), semaphore.getY()-semaphore.getHeight()/3.7f);
        velocimeter= new Sprite(assets.get(Images.Velocimeter(), Texture.class));
        velocimeter.setScale(.35f);
        velocimeterPointer=new Sprite(assets.get(Images.VelPointer(), Texture.class));
        velocimeterPointer.setOrigin(2,velocimeterPointer.getHeight()/2);
        velocimeterPointer.setScale(0.18f);
        velocimeter.setPosition(Gdx.graphics.getWidth()/1.15f-velocimeter.getWidth()/2.0f,Gdx.graphics.getHeight()/6.666f-velocimeter.getHeight()/2.0f);
        velocimeterPointer.setPosition(velocimeter.getX()+velocimeter.getWidth()/2.0f,velocimeter.getHeight()/6f);
        pixmap = new Pixmap(33, 33, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fillCircle(16, 16, 5);
        velocimeterOptSpeed=new Sprite(new Texture(pixmap));
        pixmap.dispose();
        float ponteirSize=velocimeterPointer.getWidth()*velocimeterPointer.getScaleX();
        velocimeterOptSpeed.setPosition(velocimeter.getX()+velocimeter.getWidth()/2f-16+ponteirSize,velocimeter.getHeight()/6f+16);
        velocimeterOptSpeed.setOrigin(-33,velocimeterOptSpeed.getHeight()/2);
        lifeSpr=new ArrayList();
        for(int i=0;i<Top.MAXLIFE;i++){
            lifeSpr.add(new Sprite(assets.get(Images.Life(), Texture.class)));
            lifeSpr.get(i).setScale(0.13f);
            lifeSpr.get(i).setPosition(-70+19*i,Gdx.graphics.getHeight()/1.28f);
        }
        loading=false;
    }
    
    private void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if(!WhippingTopGame.runningOnMove2Play){
                //System.out.println("single player pedal");
                if(started)player.getTop().pedal();
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            System.out.println("reseting...");
            game.setScreen(new WTGame(game));
        }
    }
    
    private void gameLogic(){
        track.climaticFoward();
        Top t=player.getTop();
        ModelInstance topInstance=topObj;
        t.roll();
        if(!WhippingTopGame.runningOnMove2Play){
            rollingSound.setVolume((float)((t.getSpeed()*t.getSpeed())/(t.getMaxVelocity()*t.getMaxVelocity())));
            skyObj.transform.idt();
            skyObj.transform.translate(t.getGamePosition());
            drawVelocimeter(t.getSpeed(),t.getMaxVelocity(),track.getFutureOptSpeedAt(t.getPosition(), t.getPositionRelative()));
            batch.begin();
            for(int j=0;j<Math.ceil(t.getLife());j++){
                float floatLife=(float)(t.getLife()-(int)t.getLife());
                if((int)t.getLife()==j)
                    lifeSpr.get(j).setAlpha(1-floatLife);
                else
                    lifeSpr.get(j).setAlpha(1);
                lifeSpr.get(j).draw(batch);
            }
            batch.end();
            cam.position.z=t.getGamePosition().z-3f;
            cam.update();
        }
        topInstance.transform.idt();
        topInstance.transform.translate(t.getGamePosition());
        topInstance.transform.rotate(Vector3.Y, t.getGameYawOrientation());
        topInstance.transform.rotate(Vector3.X, t.getGamePitchOrientation());
    }
    
    private void drawVelocimeter(double speed, double MaxSpeed, double optimousSpeed){
        float minAngle=-35;
        float maxAngle=345-minAngle;
        float velAngle=(float) (180-(((maxAngle-minAngle)*speed/MaxSpeed)+minAngle));
        float optAngle=(float) (180-(((maxAngle-minAngle)*optimousSpeed/MaxSpeed)+minAngle));
        if(velAngle<minAngle)velAngle=minAngle;
        if(optAngle<minAngle)optAngle=minAngle;
        batch.begin();
        velocimeter.draw(batch);
        velocimeterPointer.setRotation(velAngle);
        velocimeterOptSpeed.setRotation(optAngle);
        velocimeterOptSpeed.draw(batch);
        velocimeterPointer.draw(batch);
        batch.end();
    }
    
    private void drawClimateConditions(float delta){
        batch.begin();
        switch(track.getCondition().getType()){
            case ClimateConditions.RAIN:
                rainPart.start();
                rainPart.draw(batch,delta);
                if (rainPart.isComplete())
                    rainPart.reset();
                break;
                
            case ClimateConditions.SNOW:
                snowPart.start();
                snowPart.draw(batch,delta);
                if (snowPart.isComplete())
                    snowPart.reset();
                break;
                
            case ClimateConditions.SUNNY:
                sunnyPart.start();
                sunnyPart.draw(batch,delta);
                if (sunnyPart.isComplete())
                    sunnyPart.reset();
                break;
        }
        batch.end();
    }

    @Override
    public void render(float delta) {
        if(loading&&assets.update())
            doneLoading();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        if(!loading){
            handleInput();
            camController.update();
            modelBatch.begin(cam);
            modelBatch.render(trackObj,environment);
            modelBatch.render(trackObjs, environment);
            modelBatch.render(skyObj, environment);
            modelBatch.render(topObj,environment);
            modelBatch.end();
            drawClimateConditions(delta);
            if(!started){
                FPScount++;
                double count=FPScount/WhippingTopGame.FPS;
                batch.begin();
                semaphore.draw(batch);
                if(count>=1){
                    if(count==1)
                        soundTop0.play();
                    semRed.draw(batch);
                }
                if(count>=3){
                    if(count==3)
                        soundTop0.play();
                    semYellow.draw(batch);
                }
                if(count>=5){
                    if(count==5)
                        soundTop1.play();
                    semGreen.draw(batch);
                }
                if(count>=5.1f){
                    started=true;
                }
                batch.end();
            }else{
                gameLogic();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        float aspectRatio=(float)width/(float)height;
        cam.viewportWidth=WhippingTopGame.HEIGHT*aspectRatio;
        cam.viewportHeight=WhippingTopGame.HEIGHT;
        cam.update();
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
        trackObjs.clear();
        stageMusic.stop();
        rollingSound.stop();
        stageMusic.dispose();
        rainPart.dispose();
        rollingSound.dispose();
        batch.dispose();
        lifeSpr.clear();
        velocimeter.getTexture().dispose();
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
