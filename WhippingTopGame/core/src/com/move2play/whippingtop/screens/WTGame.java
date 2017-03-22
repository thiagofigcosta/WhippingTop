package com.move2play.whippingtop.screens;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.move2play.whippingtop.WhippingTopGame;
import java.util.List;

public class WTGame implements Screen, InputProcessor, Move2PlayGame{
    private WhippingTopGame game;
    
    public WTGame(WhippingTopGame game){
        this.game=game;
    }
    
    @Override
    public void show() {
        game.eventHandler=this;
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
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Keys.SPACE:
                //pedal
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
       return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void startGame(List<Player> list) {
        System.out.println("StartGame from WTGame");
    }

    @Override
    public void addPlayer(Player player) {
        System.out.println("AddPlayer from WTGame");
    }

    @Override
    public void removePlayer(Player player) {
        System.out.println("RmPlayer from WTGame");
    }

    @Override
    public void move(int i) {
        System.out.println("Move from WTGame");
    }

    @Override
    public void initGame() {
        System.out.println("InitGame from WTGame");
    }
    
}
