package br.cefetmg.move2play.whippingtop.desktop;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import br.cefetmg.move2play.whippingtop.WhippingTopGame;

public class DesktopLauncher implements Move2PlayGame{
    
    private WhippingTopGame gameClass;
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

    public static void main (String[] arg) {
        DesktopLauncher x=new DesktopLauncher();
        x.config.forceExit=true;
        x.run();
    }

    public void run(){
        gameClass=new WhippingTopGame();
        config.title=gameClass.getSettings().getGameName();
        config.width=gameClass.getSettings().get("width");
        config.height=gameClass.getSettings().get("height");
        new LwjglApplication(gameClass, config);
    }

    @Override
    public boolean startMatch() {
        System.out.println("startMatch from launcher");
        return gameClass.startMatch();
    }
    
    @Override
    public boolean finishMatch() {
        System.out.println("finishMatch from launcher");
        return gameClass.finishMatch();
    }

    @Override
    public boolean addPlayer(Player player) {
        System.out.println("add from launcher");
        return gameClass.addPlayer(player);
    }

    @Override
    public boolean removePlayer(Player player) {
        System.out.println("rm from launcher");
        return gameClass.removePlayer(player);
    }

    @Override
    public boolean move(String uuid,int i) {
        System.out.println("move from launcher");
        return gameClass.move(uuid,i);
    }

    @Override
    public boolean initGame() {
        System.out.println("init from launcher");
        config.forceExit=true;
        run();
        gameClass.getSettings().setRunningOnMove2Play(true);
        System.out.println("new thread");
        return true;
    }

    @Override
    public boolean closeGame() {
        System.out.println("exitGame from launcher");
        return gameClass.closeGame();
    }

    @Override
    public boolean reset() {
        System.out.println("reset from launcher");
        return gameClass.reset();
    }

    @Override
    public GameState getState() {
        return gameClass.getState();
    }
}
