package com.move2play.whippingtop.game;

import br.cefetmg.move2play.model.Player;

public class TopPlayer{
    private Player player;
    private Top top;

    public TopPlayer(Player player) {
        this.player = player;
    }

    public TopPlayer(Player player, Top top) {
        this.player = player;
        this.top = top;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Top getTop() {
        return top;
    }

    public void setTop(Top top) {
        this.top = top;
    }
    
    
}
