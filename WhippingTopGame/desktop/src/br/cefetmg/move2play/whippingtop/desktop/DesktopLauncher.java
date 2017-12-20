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
    public void startMatch() {
        System.out.println("startMatch from launcher");
        gameClass.startMatch();
    }
    
    @Override
    public void finishMatch() {
        System.out.println("finishMatch from launcher");
        gameClass.finishMatch();
    }

    @Override
    public void addPlayer(Player player) {
        System.out.println("add from launcher");
        gameClass.addPlayer(player);
    }

    @Override
    public void removePlayer(Player player) {
        System.out.println("rm from launcher");
        gameClass.removePlayer(player);
    }

    @Override
    public void move(String uuid,int i) {
        System.out.println("move from launcher");
        gameClass.move(uuid,i);
    }

    @Override
    public void initGame() {
        System.out.println("init from launcher");
        config.forceExit=true;
        run();
        gameClass.getSettings().setRunningOnMove2Play(true);
        System.out.println("new thread");
    }

    @Override
    public void closeGame() {
        System.out.println("exitGame from launcher");
        gameClass.closeGame();
    }
}
