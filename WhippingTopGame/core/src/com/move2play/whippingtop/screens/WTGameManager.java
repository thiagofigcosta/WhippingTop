package com.move2play.whippingtop.screens;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.Screen;
import com.move2play.whippingtop.WhippingTopGame;
import java.util.ArrayList;
import java.util.List;

public class WTGameManager implements Screen, Move2PlayGame{
    
    List<WTGame> screens;
    
    private WhippingTopGame game;
    
    public WTGameManager(int players, WhippingTopGame game) throws Exception{
        if(players<=0)
            throw new Exception("Erro, players deve ser>0");
    }
    
    @Override
    public void show() {
        screens=new ArrayList();
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {
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
