package com.move2play.whippingtop;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.move2play.whippingtop.screens.Splash;
import com.move2play.whippingtop.screens.WTGame;
import com.move2play.whippingtop.screens.WTGameManager;
import java.util.List;

public class WhippingTopGame extends Game implements Move2PlayGame{
    public static final int WIDTH=800;
    public static final int HEIGHT=600;
    public static int FPS=30;
    public static boolean runningOnMove2Play=false;

    public Move2PlayGame eventHandler;
    private Screen previousScreen=null;
    
    @Override
    public void setScreen(Screen screen){
        previousScreen=this.screen;
        super.setScreen(screen);
    }

    @Override
    public void create () {
        eventHandler=this;
        setScreen(new WTGameManager(4,80,this));//Splash
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
    public void startGame(List<Player> list) {
        if(eventHandler!=this)
            eventHandler.startGame(list);
        else
            System.out.println("StartGame from Main");
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
    public void move(int i) {
        if(eventHandler!=this)
            eventHandler.move(i);
        else
            System.out.println("Move from Main");
    }

    @Override
    public void initGame() {
        if(eventHandler!=this)
            eventHandler.initGame();
        else
            System.out.println("InitGame from Main");
    }
}