package br.cefetmg.move2play.whippingtop.screens;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import br.cefetmg.move2play.whippingtop.WhippingTopGame;
import br.cefetmg.move2play.whippingtop.game.ClimateConditions;
import br.cefetmg.move2play.whippingtop.game.TopPlayer;
import br.cefetmg.move2play.whippingtop.game.Track;
import br.cefetmg.move2play.whippingtop.util.AnimationSprite;
import br.cefetmg.move2play.whippingtop.util.ModelCreator;
import br.cefetmg.move2play.whippingtop.util.SpriteCreator;
import br.cefetmg.move2play.whippingtop.util.Util;
import br.cefetmg.move2play.whippingtop.util.files.Fonts;
import br.cefetmg.move2play.whippingtop.util.files.Sounds;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Scaling;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WTGameManager implements Screen, Move2PlayGame{
    List<WTGame> screens;
    
    private Camera cam;
    private CameraInputController camController;
    private SpriteBatch batch;
    private Music stageMusic,soundTop0,soundTop1,rainSound,snowSound,palmasSound;
    private Sprite semaphore,semRed,semYellow,semGreen,gameOver;
    private ParticleEffect rainPart,snowPart,sunnyPart;
    private float spaceBaseBetweenTracks;
    private WhippingTopGame game;
    private long FPSEventCounter;
    private boolean started, assignedResources, finished;
    private float MiddleOffset;
    private long startedGameTime;
    private Track track;
    private BitmapFont hudFontBig, loadingFont;
    private AnimationSprite boostAnim;   
    
    private Rectangle viewport;
       
    public WTGameManager(int players, int trackSize, WhippingTopGame game){
        this(genRaisingIdPlayerList(players),trackSize,game);
    }
    
    public WTGameManager(List<Player> players, int trackSize,WhippingTopGame game){
//        if(players.size()<=0)
//            throw new Move2PlayGameException("Erro, players deve ser>0");
        if(players.size()<=4)
            game.getSettings().setMultipleScreens(true);
        else 
            game.getSettings().setMultipleScreens(false);
        FPSEventCounter=0;
        started=false;
        finished=false;
        assignedResources=false;
        this.game=game;
        track=new Track(trackSize);
        screens=new ArrayList();
        spaceBaseBetweenTracks=Track.TRACKWIDTH+Track.TRACKWIDTH/3;
        for(int i=0;i<players.size();i++){
            float Xoffset=i*spaceBaseBetweenTracks;
            WTGame wttmp=new WTGame(game,track,Xoffset,players.get(i));
            screens.add(wttmp);
        }
        MiddleOffset=spaceBaseBetweenTracks*(players.size()-1)/2.0f;
        
    }
    
    private static List<Player> genRaisingIdPlayerList(int size){
        List<Player> players=new ArrayList();
        Random rand = new Random();
        for(int i=0;i<size;i++){
            Player pl=new Player();
            pl.setName("Whipping Top Developer");
            pl.setColor(new byte[] {(byte)rand.nextInt(266),(byte)rand.nextInt(266),(byte)rand.nextInt(266)});
            pl.setUUID(""+i);
            players.add(pl);
        }
        return players;
    }
    
    private void assignResources(){
        if(!game.getResources().isLoaded())
            return;
        float w=viewport.width;
        float h=viewport.height;
        float skySize;
        if(screens.size()<=7)
            skySize=300;
        else
            skySize=700;
        boostAnim=new AnimationSprite(game.getResources().getBoostSprSheet(),w/1.15f-game.getResources().getBoostSprSheet().getWidth()*0.5f/4.0f,h/6.666f-game.getResources().getBoostSprSheet().getHeight()*0.5f/2.0f,0.5f,0.5f,0.025f,1,2);
        hudFontBig=game.getResources().getHudFontBig();
        rainPart=game.getResources().getRainPart();
        snowPart=game.getResources().getSnowPart();
        sunnyPart=game.getResources().getSunnyPart();
        stageMusic=game.getResources().getStageMusic();
        soundTop0=game.getResources().getSoundTop0();
        soundTop1=game.getResources().getSoundTop1();
        snowSound=game.getResources().getSnowSound();
        rainSound=game.getResources().getRainSound();
        palmasSound=game.getResources().getPalmas();
        int wtid=0;
        for(WTGame wt:screens){
            wt.setHudFont(game.getResources().getHudFontSmall());
            wt.setBoostAnim(new AnimationSprite(game.getResources().getBoostSprSheet(),boostAnim.getPosX(),boostAnim.getPosY(),boostAnim.getScaleX(),boostAnim.getScaleY(),boostAnim.getInterval(),boostAnim.getSpriteSheetRows(),boostAnim.getSpriteSheetCols()));
            wt.setSkyObj(ModelCreator.createSkyModel(game.getResources().getSkyText(),skySize));
            wt.setTrackObj(ModelCreator.createTrackModel(wt.getTrack(), wt.getXoffset(), 0, game.getResources().getFloorText(), game.getResources().getArrivalText(), game.getResources().getBridges(), game.getResources().getSpeedUpText()));
            wt.setTrackObjs(ModelCreator.createTrackObjs(wt.getTrack(), wt.getXoffset(), 0, game.getResources().getGrassModel(), game.getResources().getCones()));
            ModelInstance topModelInstance=new ModelInstance(game.getResources().getTopModel());
            topModelInstance.transform.setToTranslation(wt.getXoffset(), 0, 0);
            wt.setTopObj(topModelInstance);
            Music rollingSound=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Wheel()));
            rollingSound.setLooping(true);
            rollingSound.setVolume(0);
            rollingSound.play();
            wt.setRollingSound(rollingSound);
//            List<Sprite> lifeSpr=new ArrayList();
//            for(int j=0;j<10;j++){//Top.MAXLIFE amount of thunders
//                lifeSpr.add(new Sprite(game.getResources().getLifeText()));
//                if(GameSettings.isMultipleScreens()){
//                    lifeSpr.get(j).setScale(0.13f);
//                    lifeSpr.get(j).setPosition(-70+19*j,h/1.28f);
//                }else{
//                    lifeSpr.get(j).setScale(0.07f);
//                    lifeSpr.get(j).setPosition(-70+15*j+(screens.size()-1-wtid)*200,h/1.28f);
//                    lifeSpr.get(j).setColor(Util.getSpriteColorFromID(wtid));
//                }
//            }
//            wt.setLifeSpr(lifeSpr);
            Sprite velocimeter=new Sprite(game.getResources().getVelocimeterText());
            Sprite velocimeterPointer=new Sprite(game.getResources().getVelocimeterPointerText());
            velocimeter.setScale(.35f);
            velocimeterPointer.setOrigin(2,velocimeterPointer.getHeight()/2);
            velocimeterPointer.setScale(0.18f);
            boolean isms=game.getSettings().get("multipleScreens");
            if(isms){
                velocimeter.setPosition(w/1.15f-velocimeter.getWidth()/2.0f,h/6.666f-velocimeter.getHeight()/2.0f);
            }else{
                wt.getBoostAnim().setPosX(w/1.15f-game.getResources().getBoostSprSheet().getWidth()*0.5f/4.0f-200*wtid);
                velocimeter.setPosition(w/1.15f-velocimeter.getWidth()/2.0f-200*wtid,h/6.666f-velocimeter.getHeight()/2.0f);
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
        semaphore=new Sprite(game.getResources().getSemaphoreText());
        semaphore.setPosition(w/2.0f - semaphore.getWidth()/2.0f, h/2.0f- semaphore.getHeight()/2.0f);
        gameOver=new Sprite(game.getResources().getGameOverText());
        gameOver.setPosition(w/2.0f - gameOver.getWidth()/2.0f, h/2.0f- gameOver.getHeight()/2.0f);
        semRed=SpriteCreator.createCircle(30,new Vector2(semaphore.getWidth(),semaphore.getHeight()), Color.RED);
        semYellow=SpriteCreator.createCircle(30,new Vector2(semaphore.getWidth(),semaphore.getHeight()), Color.YELLOW);
        semGreen=SpriteCreator.createCircle(30,new Vector2(semaphore.getWidth(),semaphore.getHeight()), Color.GREEN);
        semRed.setPosition(semaphore.getX(), semaphore.getY()+semaphore.getHeight()/3.7f);
        semYellow.setPosition(semaphore.getX(), semaphore.getY());
        semGreen.setPosition(semaphore.getX(), semaphore.getY()-semaphore.getHeight()/3.7f);
        rainPart.setPosition(w/2,h/2);
        snowPart.setPosition(w/2,h/2);
        sunnyPart.setPosition(w/2,h/2);
        stageMusic.setLooping(true);
        stageMusic.setVolume(0.07f);
        stageMusic.play();
        soundTop0.setLooping(false);
        soundTop0.setVolume(1);
        soundTop1.setLooping(false);
        soundTop1.setVolume(1);
        snowSound.setVolume(1);
        rainSound.setVolume(1);
        palmasSound.setLooping(true);
        palmasSound.setVolume(1);
        assignedResources=true;
    }
    
    @Override
    public void show() {
        game.eventHandler=this;
        loadingFont=Util.createBitMapFromTTF(Fonts.GooD(), Color.FIREBRICK, 111);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        batch=new SpriteBatch();
        Gdx.input.setInputProcessor(camController);
        configCam();
        for(int i=0;i<screens.size();i++){
            WTGame wt=screens.get(i);
            wt.configCam(i,screens.size());
            wt.show();
        }
    }
    
    public void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.R)){//Reset Game
            game.setScreen(new WaitingPlayers(game));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.E)){//End Game
            finished=true;
        }
    }
    
    private void renderScreenById(int id, float delta){
        if(screens.get(id).isAlive()){
            if(screens.get(id).wonGame())
                game.getResources().getWinScreen().draw(delta);
            else{
                screens.get(id).render(delta);
                if(started) screens.get(id).drawHUD();
            }
        }else
            game.getResources().getLoseScreen().draw(delta);
    }

    @Override
    public void render(float delta) {
        float W=viewport.width;
        float H=viewport.height;
        camController.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        if(!assignedResources){   
            Util.drawCenteredText("Loading...",new Vector2(W/2,H/2),loadingFont,batch);
            assignResources();
        }else{
            boolean isms=game.getSettings().get("multipleScreens");
            if(isms || screens.size()==1){
                int sizex,sizey;
                switch(screens.size()){
                    case 1:
                        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,(int) viewport.width, (int) viewport.height);
                        renderScreenById(0,delta);
                        break;

                    case 2:
                        sizex=(int)Math.ceil(W/2);
                        sizey=(int)H;
                        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,sizex,sizey);
                        renderScreenById(0,delta);
                        Gdx.gl.glViewport((int) (Math.floor(W/2)+viewport.x),(int) viewport.y,sizex,sizey);
                        renderScreenById(1,delta);
                        break;
                    case 3:
                        sizex=(int)Math.ceil(W/3)+1;
                        sizey=(int)H;
                        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,sizex,sizey);
                        renderScreenById(0,delta);
                        Gdx.gl.glViewport((int) (Math.floor(W/3)+viewport.x),(int) viewport.y,sizex,sizey);
                        renderScreenById(1,delta);
                        Gdx.gl.glViewport((int) (Math.floor(W*2/3)+viewport.x),(int) viewport.y,sizex,sizey);
                        renderScreenById(2,delta);
                        break;
                    case 4:
                        sizex=(int)Math.ceil(W/2);
                        sizey=(int)Math.ceil(H/2);
                        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,sizex,sizey);
                        renderScreenById(0,delta);
                        Gdx.gl.glViewport((int) (Math.floor(W/2)+viewport.x),(int) viewport.y,sizex,sizey);
                        renderScreenById(1,delta);
                        Gdx.gl.glViewport((int)viewport.x,(int)(Math.floor(H/2)+viewport.y),sizex,sizey);
                        renderScreenById(2,delta);
                        Gdx.gl.glViewport((int)(Math.floor(W/2)+viewport.x),(int)(Math.floor(H/2)+viewport.y),sizex,sizey);
                        renderScreenById(3,delta);
                        break;
                }
            }else{
                refreshCam();
                Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,(int) viewport.width, (int) viewport.height);
                for(WTGame wt:screens){
                    if(wt.isAlive())
                    wt.drawTrack(cam);
                }
                screens.get(0).drawSky(cam);
                for(int i=0;i<screens.size();i++){
                    WTGame wt=screens.get(i);
                    Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,(int) viewport.width, (int) viewport.height);
                    if(wt.isAlive()&&!wt.wonGame())
                        wt.drawTop(cam);
                    else{
                         Gdx.gl.glViewport((int)(Math.floor(W/screens.size()*i)+viewport.x),(int)(Math.floor(H/4)+viewport.y),(int)Math.ceil(W/screens.size()),(int)Math.ceil(H/2));
                        if(wt.wonGame())
                            game.getResources().getWinScreen().draw(delta);
                        else
                            game.getResources().getLoseScreen().draw(delta);
                    }
                }
            }
            if(!started){
                Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,(int) viewport.width, (int) viewport.height);
                FPSEventCounter++;
                batch.begin();
                semaphore.draw(batch);
                if(FPSEventCounter>=game.getSettings().getFps()){
                    if(FPSEventCounter==game.getSettings().getFps())
                        soundTop0.play();
                    semRed.draw(batch);
                }
                if(FPSEventCounter>=2*game.getSettings().getFps()){
                    if(FPSEventCounter==2*game.getSettings().getFps())
                        soundTop0.play();
                    semYellow.draw(batch);
                }
                if(FPSEventCounter>=3*game.getSettings().getFps()){
                    if(FPSEventCounter==3*game.getSettings().getFps())
                        soundTop1.play();
                    semGreen.draw(batch);
                }
                if(FPSEventCounter>=3.1f*game.getSettings().getFps()){
                    started=true;
                    startedGameTime=System.currentTimeMillis();
                    FPSEventCounter=0;
                }
                batch.end();
            }else if(finished){
                palmasSound.play();
                Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,(int) viewport.width, (int) viewport.height);
                if(FPSEventCounter==0){
                    for(WTGame wt:screens)
                        wt.getTop().setAlive(false);
                }
                FPSEventCounter++;
                batch.begin();
                gameOver.draw(batch);
                batch.end();
                List<TopPlayer> pls=new ArrayList();
                for(WTGame wt:screens)
                    pls.add(wt.getPlayer());
                if(FPSEventCounter>=2.1f*game.getSettings().getFps()){
                    palmasSound.stop();
                    game.setScreen(new ShowScore(pls,game));
                }
            }else{
                refreshTrack();
                Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,(int) viewport.width, (int) viewport.height);
                for(WTGame wt:screens){
                    wt.handleInput();
                    wt.gameLogic();
                    if(!isms)wt.drawHUD();
                }
                drawClimateConditions(delta);
                drawHUDTime();
                handleInput();
            }
        }
    }
    
    private void drawHUDTime(){
        float w=viewport.width;
        float h=viewport.height;
        long time_ms=System.currentTimeMillis()-startedGameTime;
        long time_s=time_ms/1000;
        int censecs=(int) ((time_ms/10)%100);
        int seconds=(int) (time_s%60);
        int minutes=(int) (time_s/60);
        Vector2 pos=new Vector2(w/2,h/2+h/2.9f);
        String time= String.format("%02d:%02d:%03d",minutes,seconds,censecs);
        Util.drawCenteredText(time,new Vector2(pos.x,pos.y),hudFontBig,batch);
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
                    wt.setTrackObj(ModelCreator.createTrackModel(wt.getTrack(), wt.getXoffset(), 0, game.getResources().getFloorText(), game.getResources().getArrivalText(), game.getResources().getBridges(), game.getResources().getSpeedUpText()));
                    wt.setTrackObjs(ModelCreator.createTrackObjs(wt.getTrack(), wt.getXoffset(), 0, game.getResources().getGrassModel(), game.getResources().getCones()));
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
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
        cam.viewportWidth=viewport.width;
        cam.viewportHeight=viewport.height;
        cam.update();
        
        w=viewport.width;
        h=viewport.height;
        
        switch(screens.size()){
            case 1:
                screens.get(0).getCam().viewportWidth=w;
                screens.get(0).getCam().viewportHeight=h;
                screens.get(0).getCam().update();
                break;
                
            case 2:
            screens.get(0).getCam().viewportWidth=w/2;
            screens.get(0).getCam().viewportHeight=h;
            screens.get(0).getCam().update();
            screens.get(1).getCam().viewportWidth=w/2;
            screens.get(1).getCam().viewportHeight=h;
            screens.get(1).getCam().update();
                break;
            case 3:
            screens.get(0).getCam().viewportWidth=w/3;
            screens.get(0).getCam().viewportHeight=h;
            screens.get(0).getCam().update();
            screens.get(1).getCam().viewportWidth=w/3;
            screens.get(1).getCam().viewportHeight=h;
            screens.get(1).getCam().update();
            screens.get(2).getCam().viewportWidth=w/3;
            screens.get(2).getCam().viewportHeight=h;
            screens.get(2).getCam().update();
                break;
            case 4:
            screens.get(0).getCam().viewportWidth=w/2;
            screens.get(0).getCam().viewportHeight=h/2;
            screens.get(0).getCam().update();
            screens.get(1).getCam().viewportWidth=w/2;
            screens.get(1).getCam().viewportHeight=h/2;
            screens.get(1).getCam().update();
            screens.get(2).getCam().viewportWidth=w/2;
            screens.get(2).getCam().viewportHeight=h/2;
            screens.get(2).getCam().update();
            screens.get(3).getCam().viewportWidth=w/2;
            screens.get(3).getCam().viewportHeight=h/2;
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
        int w=game.getSettings().get("width");
        int h=game.getSettings().get("height");
        float aspectRatio = ((float)w)/h;
        cam = new PerspectiveCamera(90,h* aspectRatio,h);
        cam.position.set(MiddleOffset, 10f*screens.size(), -3f);
        cam.lookAt(MiddleOffset, 0.35f, 5f+1.5f*screens.size());
        cam.near=1;
        cam.far=60*screens.size();
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
        batch.dispose();
        for(WTGame wt:screens)
            wt.dispose();
    }

    @Override
    public void initGame() {
        System.out.println("Init from Game Manager");
    }

    @Override
    public void startMatch() {
        System.out.println("startMatch from Game Manager");
    }
    
    @Override
    public void finishMatch() {
        System.out.println("finishMatch from Game Manager");
        this.finished=true;
    }

    @Override
    public void addPlayer(Player player) {
        System.out.println("AddPlayer from Game Manager");
        float Xoffset=screens.size()*spaceBaseBetweenTracks;
        WTGame wttmp=new WTGame(game,track,Xoffset,player);
        screens.add(wttmp);
        MiddleOffset=spaceBaseBetweenTracks*(screens.size()-1)/2.0f;
        if(screens.size()<=4)
            game.getSettings().setMultipleScreens(true);
        else 
            game.getSettings().setMultipleScreens(false);
    }

    @Override
    public void removePlayer(Player player) {
        System.out.println("RmPlayer from Game Manager");
        for(int i=0;i<screens.size();i++){
            if(screens.get(i).getUUID().equals(player.getUUID()))
                screens.remove(i);
        }
        MiddleOffset=spaceBaseBetweenTracks*(screens.size()-1)/2.0f;
        if(screens.size()<=4)
            game.getSettings().setMultipleScreens(true);
        else 
            game.getSettings().setMultipleScreens(false);
    }

    @Override
    public void move(String uuid,int i) {
        System.out.println("Move from Game Manager");
        for(WTGame wtg:screens)
            if(wtg.getUUID().equals(uuid))
                for(int m=0;m<i;m++)
                    wtg.getTop().pedal();
    }
    
    @Override
    public void closeGame() {
        System.out.println("CloseGame from Game Manager");
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        });
    }
    
}
