package br.cefetmg.move2play.whippingtop.desktop;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import br.cefetmg.move2play.whippingtop.WhippingTopGame;
import java.util.List;

public class DesktopLauncher implements Move2PlayGame{
    
    private WhippingTopGame gameClass;

    public static void main (String[] arg) {
        new DesktopLauncher().run();
    }

    public void run(){
        gameClass=new WhippingTopGame();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title="Whipping Top Game";
        config.width=WhippingTopGame.WIDTH;
        config.height=WhippingTopGame.HEIGHT;
        config.forceExit=true;
        new LwjglApplication(gameClass, config);
    }

    @Override
    public void startGame(List<Player> list) {
        System.out.println("start from launcher");
        gameClass.startGame(list);
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
    public void move(int i) {
        System.out.println("move from launcher");
        gameClass.move(i);
    }

    @Override
    public void initGame() {
        System.out.println("init from launcher");
        WhippingTopGame.runningOnMove2Play=true;
        run();
        System.out.println("new thread");
    }
}
