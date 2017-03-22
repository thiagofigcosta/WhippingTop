package br.cefetmg.move2play.whippingtop;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import br.cefetmg.move2play.whippingtop.screens.Splash;
import com.badlogic.gdx.Gdx;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WhippingTopGame extends Game implements Move2PlayGame{
    public Move2PlayGame eventHandler;
    private Screen previousScreen=null;
    private Assets resources;
    private String settingsFilename="game-settings.json";
    private Settings gs;
    
    public WhippingTopGame(){
        eventHandler=this;
        gs=new Settings();
        String settingsPath = "";
        try{
            
            String classPath = WhippingTopGame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath = URLDecoder.decode(classPath, "UTF-8");
            File execPath=new File(decodedPath);
            settingsPath= execPath.getParent()+File.separator+settingsFilename;
            File f=new File(settingsPath);
            if(f.exists()&&!f.isDirectory()) { 
                gs.loadSettings(settingsPath);
            }else{
                System.out.println("Não existe "+settingsPath);
                gs.loadSettings(settingsFilename);
            }
        }catch(Exception e){
            System.out.println("Exceção exec...  ao achar "+settingsPath);
            try {
                File callPath=new File(".");
                String execPathStr=callPath.getAbsolutePath().substring(0, callPath.getAbsolutePath().length() - 1);;
                settingsPath= execPathStr+settingsFilename;
                File f=new File(settingsPath);
                if(f.exists()&&!f.isDirectory()) { 
                    gs.loadSettings(settingsPath);
                }else{
                    System.out.println("Não existe "+settingsPath);
                    gs.loadSettings(settingsFilename);
                }
            } catch (Exception ex) {
                System.out.println("Exceção call...  ao achar "+settingsPath);
                gs.loadSettings(settingsFilename);
            }
        }
    }
    
    public Assets getResources() {
        return resources;
    }

    public Settings getSettings() {
        return gs;
    }
       
    @Override
    public void setScreen(Screen screen){
        previousScreen=this.screen;
        super.setScreen(screen);
    }

    @Override
    public void create () {
        setScreen(new Splash(this));
        resources=new Assets();
    }

    @Override
    public void render () {
        super.render();
        if(previousScreen!=null){
            previousScreen.dispose();
            previousScreen=null;
        }
    }

    @Override
    public void dispose () {
        super.dispose();
    }

    @Override
    public void resize (int width, int height) {
        super.resize(width,height);
    }

    @Override
    public void pause () {
        super.pause();
    }

    @Override
    public void resume () {
        super.resume();
    }

    @Override
    public void startMatch() {
        if(eventHandler!=this)
            eventHandler.startMatch();
        else
            System.out.println("startMatch from Main");
    }
    
    @Override
    public void finishMatch() {
        if(eventHandler!=this)
            eventHandler.finishMatch();
        else
            System.out.println("finishMatch from Main");
    }

    @Override
    public void addPlayer(Player player) {
        if(eventHandler!=this)
            eventHandler.addPlayer(player);
        else
            System.out.println("AddPlayer from Main");
    }

    @Override
    public void removePlayer(Player player) {
        if(eventHandler!=this)
            eventHandler.removePlayer(player);
        else
            System.out.println("RemomvePlayer from Main");
    }

    @Override
    public void initGame() {
        if(eventHandler!=this)
            eventHandler.initGame();
        else
            System.out.println("InitGame from Main");
    }

    @Override
    public void closeGame() {
        if(eventHandler!=this)
            eventHandler.closeGame();
        else
            System.out.println("ExitGame from Main");
    }

    @Override
    public void move(String uuid, int i) {
        if(eventHandler!=this)
            eventHandler.move(uuid,i);
        else
            System.out.println("Move from Main");
    }
}