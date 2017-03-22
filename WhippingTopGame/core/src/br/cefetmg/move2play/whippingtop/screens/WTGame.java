package br.cefetmg.move2play.whippingtop.screens;

import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import br.cefetmg.move2play.whippingtop.WhippingTopGame;
import br.cefetmg.move2play.whippingtop.game.Top;
import br.cefetmg.move2play.whippingtop.game.TopPlayer;
import br.cefetmg.move2play.whippingtop.game.Track;
import br.cefetmg.move2play.whippingtop.util.AnimationSprite;
import br.cefetmg.move2play.whippingtop.util.Util;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

public class WTGame implements Screen{
    private ModelBatch modelBatch;
    private SpriteBatch batch;
    
    private Environment environment;
    private Camera cam;
    private List<ModelInstance> trackObjs;
    private ModelInstance topObj;
    private ModelInstance skyObj,trackObj;
    
    private BitmapFont hudFontSmall;
    
    private Sprite velocimeter,velocimeterPointer,velocimeterOptSpeed;
    private Music rollingSound;
    private List<Sprite> lifeSpr;
    
    private float Xoffset;
    private float Zcamoffset=-3;
    private float camBoostDistance=2;
    private Vector3 lookAtCam;
    
    private WhippingTopGame game;
    private TopPlayer player;
    private Track track;
    
    private AnimationSprite boostAnim;
    
    private boolean drawOnScreen=true;
    
    
    public WTGame(WhippingTopGame game,float Xoffset){
        this(game,new Track(80),Xoffset,new Player());
    }
    
    public WTGame(WhippingTopGame game, Track t,float Xoffset,Player pl){
        this.game=game;
        trackObjs=new ArrayList();
        track=t;
        this.Xoffset=Xoffset;
        player=new TopPlayer(pl,Top.DEFAULT(track));
        player.getTop().getGamePosition().x=Xoffset;
    }
    
    public Track getTrack(){
        return track;
    }
    
    public void configCam(int id,float divider){
        int h=game.getSettings().get("height");
        lookAtCam=new Vector3(Xoffset,0.35f,10f);
        Vector3 posCam=new Vector3(Xoffset,8.3f,-3f);
        float angle=90;
        float aspectRatio = ((float)Gdx.graphics.getWidth())/Gdx.graphics.getHeight();
        cam = new PerspectiveCamera(angle,h/divider*aspectRatio,h/divider);
        cam.position.set(posCam);
        cam.lookAt(lookAtCam);
        cam.near=1;
        cam.far=300;
        cam.update();
    }
    
    public void modifyCameraSettings(char l){
        float increaser=0.5f;
        switch(l){
            case 'q':cam.position.x+=increaser; break; 
            case 'a':cam.position.x-=increaser; break;
            
            case 'w':cam.position.y+=increaser; break; 
            case 's':cam.position.y-=increaser; break;
        
            case 'e':Zcamoffset+=increaser; break; 
            case 'd':Zcamoffset-=increaser; break; 
            
            
            case 'r':lookAtCam.x+=increaser; cam.lookAt(lookAtCam);break; 
            case 'f':lookAtCam.x-=increaser; cam.lookAt(lookAtCam);break;
            
            case 't':lookAtCam.y+=increaser; cam.lookAt(lookAtCam);break; 
            case 'g':lookAtCam.y-=increaser; cam.lookAt(lookAtCam);break;
        
            case 'y':lookAtCam.z+=increaser; cam.lookAt(lookAtCam);break; 
            case 'h':lookAtCam.z-=increaser; cam.lookAt(lookAtCam);break; 
            
            
            case 'p': System.out.println("Position(x:"+cam.position.x+" | y:"+cam.position.y+" | z:"+cam.position.z+")");
                      System.out.println("Look At (x:"+lookAtCam.x+" | y:"+lookAtCam.y+" | z:"+lookAtCam.z+")");
        }
        cam.update();
    }
    
    @Override
    public void show() {
        modelBatch=new ModelBatch();
        batch=new SpriteBatch();
        environment=new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.4f, 0.4f, 0.4f, 1));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1, -0.8f, -0.2f));
        boolean irom2p=game.getSettings().get("runningOnMove2Play");
        if(!irom2p){
            System.out.println("No Move2Play");
            System.out.println("Single Player Local");
        }
    }

    public void setSkyObj(ModelInstance skyObj) {
        this.skyObj = skyObj;
    }

    public void setTrackObj(ModelInstance trackObj) {
        this.trackObj = trackObj;
    }

    public void setTrackObjs(List<ModelInstance> trackObjs) {
        this.trackObjs = trackObjs;
    }

    public void setTopObj(ModelInstance topObj) {
        this.topObj = topObj;
    }

    public AnimationSprite getBoostAnim() {
        return boostAnim;
    }

    public TopPlayer getPlayer() {
        return player;
    }
    
    public void setBoostAnim(AnimationSprite boostAnim) {
        this.boostAnim = boostAnim;
    }
    
    public void setVelocimeterOptSpeed(Sprite velocimeterOptSpeed) {
        this.velocimeterOptSpeed = velocimeterOptSpeed;
    }

    public void setLifeSpr(List<Sprite> lifeSpr) {
        this.lifeSpr = lifeSpr;
    }
    
    public Top getTop(){
        return player.getTop();
    }

    public void setVelocimeter(Sprite velocimeter) {
        this.velocimeter = velocimeter;
        Color velColor=Util.byteToColor(player.getPlayer().getColor());
        this.velocimeter.setColor(velColor);
    }

    public void setVelocimeterPointer(Sprite velocimeterPointer) {
        this.velocimeterPointer = velocimeterPointer;
    }

    public void setRollingSound(Music rollingSound) {
        this.rollingSound = rollingSound;
    }

    public Camera getCam() {
        return cam;
    }
    
    public void setSkyPos(Vector3 pos){
        skyObj.transform.idt();
        skyObj.transform.translate(pos);
    }

    public void setHudFont(BitmapFont hudFont) {
        this.hudFontSmall = hudFont;
    }

    public String getUUID() {
        return player.getPlayer().getUUID();
    }

    public void setUUID(String id) {
        player.getPlayer().setUUID(id);
    }
    
    public void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            boolean irom2p=game.getSettings().get("runningOnMove2Play");
            if(!irom2p){
                player.getTop().pedal();
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.C)&&player.getPlayer().getUUID().equals("0"))
            player.getTop().pedal();
        else if(Gdx.input.isKeyPressed(Input.Keys.V)&&player.getPlayer().getUUID().equals("1")) 
            player.getTop().pedal();
        else if(Gdx.input.isKeyPressed(Input.Keys.B)&&player.getPlayer().getUUID().equals("2")) 
            player.getTop().pedal();
        else if(Gdx.input.isKeyPressed(Input.Keys.N)&&player.getPlayer().getUUID().equals("3")) 
            player.getTop().pedal(); 
        else if(Gdx.input.isKeyPressed(Input.Keys.M)&&player.getPlayer().getUUID().equals("4")) 
            player.getTop().pedal();         
//        if(Gdx.input.isKeyPressed(Input.Keys.Q))
//            modifyCameraSettings('q');
//        if(Gdx.input.isKeyPressed(Input.Keys.A))
//            modifyCameraSettings('a');
//        if(Gdx.input.isKeyPressed(Input.Keys.W))
//            modifyCameraSettings('w');
//        if(Gdx.input.isKeyPressed(Input.Keys.S))
//            modifyCameraSettings('s');
//        if(Gdx.input.isKeyPressed(Input.Keys.E))
//            modifyCameraSettings('e');
//        if(Gdx.input.isKeyPressed(Input.Keys.D))
//            modifyCameraSettings('d');
//        
//        if(Gdx.input.isKeyPressed(Input.Keys.R))
//            modifyCameraSettings('r');
//        if(Gdx.input.isKeyPressed(Input.Keys.F))
//            modifyCameraSettings('f');
//        if(Gdx.input.isKeyPressed(Input.Keys.T))
//            modifyCameraSettings('t');
//        if(Gdx.input.isKeyPressed(Input.Keys.G))
//            modifyCameraSettings('g');
//        if(Gdx.input.isKeyPressed(Input.Keys.Y))
//            modifyCameraSettings('y');
//        if(Gdx.input.isKeyPressed(Input.Keys.H))
//            modifyCameraSettings('h');
//        
//        if(Gdx.input.isKeyPressed(Input.Keys.P))
//            modifyCameraSettings('p');
        
        
//        if(Gdx.input.isKeyPressed(Input.Keys.R)){
//            System.out.println("reseting...");
//            game.setScreen(new WTGame(game,Xoffset));
//        }
    }

    public float getXoffset() {
        return Xoffset;
    }
    
    
    public void gameLogic(){
        track.climaticFoward();
        Top t=player.getTop();
        ModelInstance topInstance=topObj;
        t.roll();
        rollingSound.setVolume((float)((t.getSpeed()*t.getSpeed())/(t.getMaxVelocity()*t.getMaxVelocity()))*0.8f);
        skyObj.transform.idt();
        skyObj.transform.translate(t.getGamePosition());
        cam.position.z=t.getGamePosition().z+Zcamoffset;
        if(track.isBoostAt(t.getPosition()))
            cam.position.z-=camBoostDistance;
        cam.update();
        topInstance.transform.idt();
        topInstance.transform.translate(t.getGamePosition());
        topInstance.transform.rotate(Vector3.Y, t.getGameYawOrientation());
        topInstance.transform.rotate(Vector3.X, t.getGamePitchOrientation());
    }
    
    private void drawPoints(long points){
        Util.drawCenteredText(""+points,new Vector2(velocimeter.getX()+velocimeter.getWidth()/2.0f,velocimeter.getHeight()/6f),hudFontSmall,batch);
    }
    
    public void drawHUD(){
        if(!drawOnScreen)return;
        Top t=player.getTop();
        drawVelocimeter(t.getSpeed(),t.getMaxVelocity(),track.getFutureOptSpeedAt(t.getPosition(), t.getPositionRelative()));
        //drawLife(t.getLife());
        if(track.isBoostAt(t.getPosition()))
            boostAnim.draw(batch);
        drawPoints(t.getPoints());
    }
    
    private void drawLife(double life){
        batch.begin();
        for(int j=0;j<Math.ceil(life);j++){
            float floatLife=(float)(life-(int)life);
            if((int)life==j)
                lifeSpr.get(j).setAlpha(1-floatLife);
            else
                lifeSpr.get(j).setAlpha(1);
            lifeSpr.get(j).draw(batch);
        }
        batch.end();
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
    
    public void drawTop(Camera c){
        modelBatch.begin(c);
        modelBatch.render(topObj,environment);
        modelBatch.end();
    }
    public void drawSky(Camera c){
        modelBatch.begin(c);
        modelBatch.render(skyObj, environment);
        modelBatch.end();
    }
    public void drawTrack(Camera c){
        modelBatch.begin(c);
        modelBatch.render(trackObj,environment);
        modelBatch.render(trackObjs, environment);
        modelBatch.end();
    }

    @Override
    public void render(float delta) {
        if(!drawOnScreen) return;
        modelBatch.begin(cam);
        modelBatch.render(trackObj,environment);
        modelBatch.render(trackObjs, environment);
        modelBatch.render(skyObj, environment);
        modelBatch.render(topObj,environment);
        modelBatch.end();
    }

    public boolean isAlive(){
        boolean alive = player.getTop().isAlive();
        if(!alive){
            drawOnScreen=false;
            rollingSound.stop();
        }
        return alive;
    }
    
    public boolean wonGame(){
        if(track.getFullSize()!=-Track.INFINITE){
            return player.getTop().alreadyArrived();
        }
        return false;
    }
    
    @Override
    public void resize(int width, int height) {
        int h=game.getSettings().get("height");
        float aspectRatio=(float)width/(float)height;
        cam.viewportWidth=h*aspectRatio;
        cam.viewportHeight=h;
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
        trackObjs.clear();
        rollingSound.stop();
        rollingSound.dispose();
        batch.dispose();
    }
}
