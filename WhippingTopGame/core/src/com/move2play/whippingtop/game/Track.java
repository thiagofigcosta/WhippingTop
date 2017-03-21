package com.move2play.whippingtop.game;

public class Track {
    private Obstacle[] track;
    //TODO condições climaticas
    
    public Track(int size, double difficulty){
        if(difficulty<0)difficulty=0;
        if(difficulty>10)difficulty=10;
        track= new Obstacle[size];
        track[0]=Obstacle.genRandom();
        for(int i=1;i<size; i++){
            track[i]=Obstacle.genRandom(track[i-1].getType());
            track[i].setDifficulty(track[i].getDifficulty()*(1+difficulty));
        }
    }
    
    public Track(int size){
        track= new Obstacle[size];
        track[0]=Obstacle.genRandom();
        for(int i=1;i<size; i++){
            track[i]=Obstacle.genRandom(track[i-1].getType());
        }
    }
    
    public Obstacle at(int i){
        return track[i];
    }
}
