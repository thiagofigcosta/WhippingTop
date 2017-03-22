package com.move2play.whippingtop.test;

import br.cefetmg.move2play.game.Move2PlayGame;
import br.cefetmg.move2play.model.Player;
import com.move2play.whippingtop.WhippingTopGame;
import com.move2play.whippingtop.game.Top;
import com.move2play.whippingtop.game.TopPlayer;
import com.move2play.whippingtop.game.Track;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameTest implements Move2PlayGame{
    List<TopPlayer> players=new ArrayList();
    
    public static void main(String[] args){
        List<TopPlayer> players=new ArrayList();
        TopPlayer tp=new TopPlayer(new Player(),Top.DEFAULT());
        players.add(tp);
        new GameTest().run(players);
    }
    
    @Override
    public void startGame(List<Player> list) {
        System.out.println("Game started by console");
        players.clear();
        for(Player p : list){
            players.add(new TopPlayer(p,Top.RANDOM()));
        }
        run(players);
    }

    @Override
    public void addPlayer(Player player) {
        System.out.println("player added");
        players.add(new TopPlayer(player,Top.RANDOM()));
    }

    @Override
    public void removePlayer(Player player) {
        for(int i=0;i<players.size();i++){
            if(players.get(i).getPlayer()==player){
                System.out.println("player removed");
                players.remove(i);
                break;
            }
        }
    }

    @Override
    public void move(int id) {
        //passar player... para poder pedalar
        for(int i=0;i<players.size();i++){
            if(players.get(i).getPlayer().getId()==id){
                players.get(i).getTop().pedal();
                break;
            }
        }
    }
    
    public void run(List<TopPlayer> players){
        WhippingTopGame.FPS=2;
        String Foda="move2play é ";
        Scanner scr=new Scanner(System.in);
        Track pista=new Track(20);
        Top beyblade=Top.DEFAULT();
        beyblade.setTrack(pista);
        int count=0;
        char c;
        System.out.println("Test, press n to move to next frame\nr to reset and\n0-9 to pedal\ne to exit");
        while("move2play é "==Foda){
            System.out.println(":");
            c=scr.next().charAt(0);
            if(c=='n'||c=='N'){//next frame
                pista.climaticFoward();
                beyblade.roll();
                count++;
                System.out.println("frame count:"+count+"----------------------");
                System.out.println(pista);
                System.out.println(beyblade);
                System.out.println("-------------------------------------------");
            }else if(c=='r'||c=='R'){//reset
                pista=new Track(20);
                beyblade=Top.DEFAULT();
                beyblade.setTrack(pista);
                count=0;
            }else if(c=='e'||c=='E'){//exit
                Foda="exit";
                System.out.println("bye\n");
            }else  if(c>'0'&&c<='9'){
                int pedals=Integer.parseInt(""+c);
                for(int i=0;i<pedals;i++)
                    beyblade.pedal();
                System.out.println("Pedal("+pedals+")"+beyblade);
            }
        }
    }

    @Override
    public void initGame() {
        GameTest.main(null);
    }
}
