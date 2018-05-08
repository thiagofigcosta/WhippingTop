package br.cefetmg.move2play.whippingtop;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import br.cefetmg.move2play.whippingtop.screens.Splash;

public class WhippingTopGame extends Game implements Move2PlayGame{
    public Move2PlayGame eventHandler;
    private Screen previousScreen=null;
    private Assets resources;
    private Settings gs;
    
    public WhippingTopGame(){
        eventHandler=this;
        gs=new Settings(WhippingTopGame.class);
        gs.loadSettings();
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
    public boolean startMatch() {
        if(eventHandler!=this)
            return eventHandler.startMatch();
        else
            System.out.println("startMatch from Main");
        return false;
    }
    
    @Override
    public boolean finishMatch() {
        if(eventHandler!=this)
            return eventHandler.finishMatch();
        else
            System.out.println("finishMatch from Main");
        return false;
    }

    @Override
    public boolean addPlayer(Player player) {
        if(eventHandler!=this)
            return eventHandler.addPlayer(player);
        else
            System.out.println("AddPlayer from Main");
        return false;
    }

    @Override
    public boolean removePlayer(Player player) {
        if(eventHandler!=this)
            return eventHandler.removePlayer(player);
        else
            System.out.println("RemomvePlayer from Main");
        return false;
    }

    @Override
    public boolean initGame() {
        if(eventHandler!=this)
            eventHandler.initGame();
        else
            System.out.println("InitGame from Main");
        return true;
    }

    @Override
    public boolean closeGame() {
        if(eventHandler!=this)
            return eventHandler.closeGame();
        else
            System.out.println("ExitGame from Main");
        return false;
    }

    @Override
    public boolean move(String uuid, int i) {
        if(eventHandler!=this)
            return eventHandler.move(uuid,i);
        else
            System.out.println("Move from Main");
        return false;
    }

    @Override
    public boolean reset() {
        gs.loadSettings();
        setScreen(new Splash(this));
        resources=new Assets();
        return true;
    }

    @Override
    public GameState getState() {
        if(eventHandler!=this)
            return eventHandler.getState();
        return GameState.loading;
    }
}