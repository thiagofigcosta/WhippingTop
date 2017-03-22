package br.cefetmg.move2play.whippingtop.test;

import br.cefetmg.move2play.model.Player;
import br.cefetmg.move2play.whippingtop.game.Top;
import br.cefetmg.move2play.whippingtop.game.TopPlayer;
import br.cefetmg.move2play.whippingtop.game.Track;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameTest{
    List<TopPlayer> players=new ArrayList();
    
    public static void main(String[] args){
        List<TopPlayer> players=new ArrayList();
        TopPlayer tp=new TopPlayer(new Player(),Top.DEFAULT());
        players.add(tp);
        new GameTest().run(players);
    }

    public void run(List<TopPlayer> players){
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
}
