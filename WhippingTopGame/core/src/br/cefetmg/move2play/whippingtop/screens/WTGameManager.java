package br.cefetmg.move2play.whippingtop.screens;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import br.cefetmg.move2play.whippingtop.WhippingTopGame;
import br.cefetmg.move2play.whippingtop.game.ClimateConditions;
import br.cefetmg.move2play.whippingtop.game.Top;
import br.cefetmg.move2play.whippingtop.game.Track;
import br.cefetmg.move2play.whippingtop.util.ModelCreator;
import br.cefetmg.move2play.whippingtop.util.SpriteCreator;
import br.cefetmg.move2play.whippingtop.util.Util;
import br.cefetmg.move2play.whippingtop.util.files.Images;
import br.cefetmg.move2play.whippingtop.util.files.Models;
import br.cefetmg.move2play.whippingtop.util.files.Musics;
import br.cefetmg.move2play.whippingtop.util.files.Particles;
import br.cefetmg.move2play.whippingtop.util.files.Sounds;
import br.cefetmg.move2play.whippingtop.util.files.Textures;
import java.util.ArrayList;
import java.util.List;

public class WTGameManager implements Screen, Move2PlayGame{
    List<WTGame> screens;
    
    private static boolean MultipleScreens=true;
    
    private Camera cam;
    private CameraInputController camController;
    
    private AssetManager assets;
    private SpriteBatch batch;
    private Music stageMusic,soundTop0,soundTop1,rainSound,snowSound;
    private Sprite semaphore,semRed,semYellow,semGreen;
    private ParticleEffect rainPart,snowPart,sunnyPart;
    private boolean loading;
    
    private Texture floorText,arrivalText,speedUpText;
    private Texture bridges[];
    private Model grassModel;
    private Model cones[];
    
    private WhippingTopGame game;
    private long FPScount;
    private boolean started;
    private float MiddleOffset;
    
    public WTGameManager(int players, int trackSize, WhippingTopGame game){
//        if(players<=0)
//            throw new Exception("Erro, players deve ser>0");
        FPScount=0;
        started=false;
        this.game=game;
        Track t=new Track(trackSize);
        screens=new ArrayList();
        float base=Track.TRACKWIDTH*1.2f;
        for(int i=0;i<players;i++){
            float Xoffset=i*base;
            WTGame wttmp=new WTGame(game,t,Xoffset);
            screens.add(wttmp);
        }
        MiddleOffset=base*players/2.0f;
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
        stageMusic=Gdx.audio.newMusic(Gdx.files.internal(Musics.Music1()));
        soundTop0=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Top0()));
        soundTop1=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Top1()));
        rainSound=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Rain()));
        snowSound=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Snow()));
        loading=true;
    }
    
    private void doneLoading(){
        ModelInstance sky=ModelCreator.createSkyModel(assets.get(Textures.Sky(), Texture.class));
        floorText=assets.get(Textures.Track(), Texture.class);
        arrivalText=assets.get(Textures.Arrival(), Texture.class);
        speedUpText=assets.get(Textures.SpeedUp(), Texture.class);
        Texture lifeText=assets.get(Images.Life(), Texture.class);
        Texture velocimeterText=assets.get(Images.Velocimeter(), Texture.class);
        Texture velocimeterPointerText=assets.get(Images.VelPointer(), Texture.class);
        grassModel=assets.get(Models.Grass(), Model.class);
        Model topModel=assets.get(Models.TOP(), Model.class);//gisele bundchen
        bridges=new Texture[5];
        for(int i=0;i<5;i++)
            bridges[i]=assets.get(Textures.Bridge(i), Texture.class);
        cones=new Model[6];
        for(int i=0;i<6;i++)
            cones[i]=assets.get(Models.Cone(i), Model.class);
        int wtid=0;
        for(WTGame wt:screens){
            wt.setSkyObj(sky);
            wt.setTrackObj(ModelCreator.createTrackModel(wt.getTrack(), wt.getXoffset(), 0, floorText, arrivalText, bridges, speedUpText));
            wt.setTrackObjs(ModelCreator.createTrackObjs(wt.getTrack(), wt.getXoffset(), 0, grassModel, cones));
            ModelInstance topModelInstance=new ModelInstance(topModel);
            topModelInstance.transform.setToTranslation(wt.getXoffset(), 0, 0);
            wt.setTopObj(topModelInstance);
            Music rollingSound=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Wheel()));
            rollingSound.setLooping(true);
            rollingSound.setVolume(0);
            rollingSound.play();
            wt.setRollingSound(rollingSound);
            List<Sprite> lifeSpr=new ArrayList();
            for(int j=0;j<Top.MAXLIFE;j++){
                lifeSpr.add(new Sprite(lifeText));
                if(MultipleScreens){
                    lifeSpr.get(j).setScale(0.13f);
                    lifeSpr.get(j).setPosition(-70+19*j,Gdx.graphics.getHeight()/1.28f);
                }else{
                    lifeSpr.get(j).setScale(0.07f);
                    lifeSpr.get(j).setPosition(-70+15*j+(screens.size()-1-wtid)*200,Gdx.graphics.getHeight()/1.28f);
                    lifeSpr.get(j).setColor(Util.getSpriteColorFromID(wtid));
                }
            }
            wt.setLifeSpr(lifeSpr);
            Sprite velocimeter=new Sprite(velocimeterText);
            Sprite velocimeterPointer=new Sprite(velocimeterPointerText);
            velocimeter.setScale(.35f);
            velocimeterPointer.setOrigin(2,velocimeterPointer.getHeight()/2);
            velocimeterPointer.setScale(0.18f);
            if(MultipleScreens)
                velocimeter.setPosition(Gdx.graphics.getWidth()/1.15f-velocimeter.getWidth()/2.0f,Gdx.graphics.getHeight()/6.666f-velocimeter.getHeight()/2.0f);
            else{
                velocimeter.setColor(Util.getSpriteColorFromID(wtid));
                velocimeter.setPosition(Gdx.graphics.getWidth()/1.15f-velocimeter.getWidth()/2.0f-200*wtid,Gdx.graphics.getHeight()/6.666f-velocimeter.getHeight()/2.0f);
            }
            velocimeterPointer.setPosition(velocimeter.getX()+velocimeter.getWidth()/2.0f,velocimeter.getHeight()/6f);
            wt.setVelocimeter(velocimeter);
            wt.setVelocimeterPointer(velocimeterPointer);
            Sprite velocimeterOptSpeed=SpriteCreator.createCircle(5, new Vector2(32,32), Color.GREEN);
            float ponteirSize=velocimeterPointer.getWidth()*velocimeterPointer.getScaleX();
            velocimeterOptSpeed.setPosition(velocimeter.getX()+velocimeter.getWidth()/2f-16+ponteirSize,velocimeter.getHeight()/6f+16);
            velocimeterOptSpeed.setOrigin(-33,velocimeterOptSpeed.getHeight()/2);
            wt.setVelocimeterOptSpeed(velocimeterOptSpeed);
            wtid++;
        }
        semaphore= new Sprite(assets.get(Images.Semaphore(), Texture.class));
        semaphore.setPosition(Gdx.graphics.getWidth()/2.0f - semaphore.getWidth()/2.0f, Gdx.graphics.getHeight()/2.0f- semaphore.getHeight()/2.0f);
        semRed=SpriteCreator.createCircle(30,new Vector2(semaphore.getWidth(),semaphore.getHeight()), Color.RED);
        semYellow=SpriteCreator.createCircle(30,new Vector2(semaphore.getWidth(),semaphore.getHeight()), Color.YELLOW);
        semGreen=SpriteCreator.createCircle(30,new Vector2(semaphore.getWidth(),semaphore.getHeight()), Color.GREEN);
        semRed.setPosition(semaphore.getX(), semaphore.getY()+semaphore.getHeight()/3.7f);
        semYellow.setPosition(semaphore.getX(), semaphore.getY());
        semGreen.setPosition(semaphore.getX(), semaphore.getY()-semaphore.getHeight()/3.7f);
        rainPart=((ParticleEffect) assets.get(Particles.Rain()));
        rainPart.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        snowPart=((ParticleEffect) assets.get(Particles.Snow()));
        snowPart.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        sunnyPart=((ParticleEffect) assets.get(Particles.Sunny()));
        sunnyPart.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        stageMusic.setLooping(true);
        stageMusic.setVolume(0.07f);
        stageMusic.play();
        soundTop0.setLooping(false);
        soundTop0.setVolume(1);
        soundTop1.setLooping(false);
        soundTop1.setVolume(1);
        snowSound.setVolume(1);
        rainSound.setVolume(1);
        loading=false;
    }
    
    @Override
    public void show() {
        game.eventHandler=this;
         Gdx.gl.glClearColor(1, 1, 1, 1);
         batch=new SpriteBatch();
         Gdx.input.setInputProcessor(camController);
         loadFiles();
         configCam();
         for(int i=0;i<screens.size();i++){
            WTGame wt=screens.get(i);
            wt.configCam(i,screens.size());
            wt.show();
         }
    }

    @Override
    public void render(float delta) {
        camController.update();
        if(loading&&assets.update())
            doneLoading();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        if(!loading){
            if(MultipleScreens || screens.size()==1){
                switch(screens.size()){
                    case 1:
                        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
                        screens.get(0).render(delta);
                        break;

                    case 2:
                        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight());
                        screens.get(0).render(delta);
                        Gdx.gl.glViewport(Gdx.graphics.getWidth()/2,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
                        screens.get(1).render(delta);
                        break;

                    case 3:
                        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight());
                        screens.get(0).render(delta);
                        Gdx.gl.glViewport(Gdx.graphics.getWidth()/3,0,Gdx.graphics.getWidth()*2/3,Gdx.graphics.getHeight());
                        screens.get(1).render(delta);
                        Gdx.gl.glViewport(Gdx.graphics.getWidth()*2/3,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
                        screens.get(2).render(delta);
                        break;

                    case 4:
                        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
                        screens.get(0).render(delta);
                        Gdx.gl.glViewport(Gdx.graphics.getWidth()/2,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/2);
                        screens.get(1).render(delta);
                        Gdx.gl.glViewport(0,Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight());
                        screens.get(2).render(delta);
                        Gdx.gl.glViewport(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
                        screens.get(3).render(delta);
                        break;
                }
            }else{
                refreshCam();
                Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
                for(WTGame wt:screens){
                    wt.drawTrack(cam);
                }
                screens.get(0).drawSky(cam);
                for(WTGame wt:screens){
                    wt.drawTop(cam);
                }
            }
            if(!started){
                Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
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
                refreshTrack();
                for(WTGame wt:screens){
                    wt.handleInput();
                    wt.gameLogic();
                    wt.drawHUD();
                }
                Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
                drawClimateConditions(delta);
            }
        }
    }

    private void refreshTrack(){
        Track t = screens.get(0).getTrack();
        if(t.getFullSize()==-Track.INFINITE){
            int sum=0;
            int divider=0;
            int min=-Track.INFINITE;
            int max=0;
            boolean reseted=false;
            int newPosOffset=0;
            for(WTGame wt:screens){
                int pos=wt.getTop().getPosition()%-Track.INFINITE;
                if(min>pos)
                    min=pos;
                if(max<pos)
                    max=pos;
                sum+=pos;
                divider++;
            }
            sum/=divider;
            if(sum>=Track.INFINITE/-2){
                if(min>Track.INFINITE/-2){
                    reseted=t.regenHalfTrack(true);
                    newPosOffset=Track.INFINITE/2;
                }
            }else{
                if(max<Track.INFINITE/-2){
                    reseted=t.regenHalfTrack(false);
                }
            }
            if(reseted){
                for(WTGame wt:screens){
                    if(newPosOffset!=0)
                        wt.getTop().refreshPos(wt.getTop().getPosition()+newPosOffset);
                    wt.setTrackObj(ModelCreator.createTrackModel(wt.getTrack(), wt.getXoffset(), 0, floorText, arrivalText, bridges, speedUpText));
                    wt.setTrackObjs(ModelCreator.createTrackObjs(wt.getTrack(), wt.getXoffset(), 0, grassModel, cones));
                }
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
        switch(screens.size()){
            case 1:
                screens.get(0).getCam().viewportWidth=WhippingTopGame.HEIGHT*aspectRatio;
                screens.get(0).getCam().viewportHeight=WhippingTopGame.HEIGHT;
                screens.get(0).getCam().update();
                break;
                
            case 2:
            screens.get(0).getCam().viewportWidth=WhippingTopGame.HEIGHT*aspectRatio/2;
            screens.get(0).getCam().viewportHeight=WhippingTopGame.HEIGHT;
            screens.get(0).getCam().update();
            screens.get(1).getCam().viewportWidth=WhippingTopGame.HEIGHT*aspectRatio/2;
            screens.get(1).getCam().viewportHeight=WhippingTopGame.HEIGHT;
            screens.get(1).getCam().update();
                break;
            case 3:
            screens.get(0).getCam().viewportWidth=WhippingTopGame.HEIGHT*aspectRatio/3;
            screens.get(0).getCam().viewportHeight=WhippingTopGame.HEIGHT;
            screens.get(0).getCam().update();
            screens.get(1).getCam().viewportWidth=WhippingTopGame.HEIGHT*aspectRatio/3;
            screens.get(1).getCam().viewportHeight=WhippingTopGame.HEIGHT;
            screens.get(1).getCam().update();
            screens.get(2).getCam().viewportWidth=WhippingTopGame.HEIGHT*aspectRatio/3;
            screens.get(2).getCam().viewportHeight=WhippingTopGame.HEIGHT;
            screens.get(2).getCam().update();
                break;
            case 4:
            screens.get(0).getCam().viewportWidth=WhippingTopGame.HEIGHT*aspectRatio/2;
            screens.get(0).getCam().viewportHeight=WhippingTopGame.HEIGHT/2;
            screens.get(0).getCam().update();
            screens.get(1).getCam().viewportWidth=WhippingTopGame.HEIGHT*aspectRatio/2;
            screens.get(1).getCam().viewportHeight=WhippingTopGame.HEIGHT/2;
            screens.get(1).getCam().update();
            screens.get(2).getCam().viewportWidth=WhippingTopGame.HEIGHT*aspectRatio/2;
            screens.get(2).getCam().viewportHeight=WhippingTopGame.HEIGHT/2;
            screens.get(2).getCam().update();
            screens.get(3).getCam().viewportWidth=WhippingTopGame.HEIGHT*aspectRatio/2;
            screens.get(3).getCam().viewportHeight=WhippingTopGame.HEIGHT/2;
            screens.get(3).getCam().update();
                break;
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
    
    public void configCam(){
        float aspectRatio = ((float)Gdx.graphics.getWidth())/Gdx.graphics.getHeight();
        cam = new PerspectiveCamera(90, WhippingTopGame.HEIGHT * aspectRatio, WhippingTopGame.HEIGHT);
        cam.position.set(MiddleOffset, 30f, -3f);
        cam.lookAt(MiddleOffset, 0.35f, 10f);
        cam.near=1;
        cam.far=300;
        cam.update();
        camController = new CameraInputController(cam);
    }
    
    public void refreshCam(){
        double pos=0;
        for(WTGame wt:screens)
            pos+=wt.getTop().getGamePosition().z;
        pos/=screens.size();
        cam.position.z=(float)pos;
        screens.get(0).setSkyPos(new Vector3(MiddleOffset,0.1f, (float) pos));
        cam.update();
    }
    
    private void drawClimateConditions(float delta){
        for(int i=1;i<screens.size();i++)//sync conditions
            screens.get(i).getTrack().setCondition(screens.get(0).getTrack().getCondition());
        batch.begin();
        switch(screens.get(0).getTrack().getCondition().getType()){
            case ClimateConditions.RAIN:
                rainSound.play();
                snowSound.stop();
                rainPart.start();
                rainPart.draw(batch,delta);
                if (rainPart.isComplete())
                    rainPart.reset();
                break;
                
            case ClimateConditions.SNOW:
                snowSound.play();
                rainSound.stop();
                snowPart.start();
                snowPart.draw(batch,delta);
                if (snowPart.isComplete())
                    snowPart.reset();
                break;
                
            case ClimateConditions.SUNNY:
                snowSound.stop();
                rainSound.stop();
                sunnyPart.start();
                sunnyPart.draw(batch,delta);
                if (sunnyPart.isComplete())
                    sunnyPart.reset();
                break;
            case ClimateConditions.NORMAL:
                snowSound.stop();
                rainSound.stop();
                break;
        }
        batch.end();
    }

    @Override
    public void dispose() {
        assets.dispose();
        stageMusic.stop();
        stageMusic.dispose();
        rainPart.dispose();
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
