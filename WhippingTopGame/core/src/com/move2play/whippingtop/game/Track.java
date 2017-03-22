package com.move2play.whippingtop.game;

import com.move2play.whippingtop.util.files.Textures;
import java.util.Random;

public class Track {
    private Obstacle[] track;
    private ClimateConditions condition;
    public static float TRACKWIDTH=20;
    
    public Track(int size, double difficulty){
        if(difficulty<0)difficulty=0;
        if(difficulty>10)difficulty=10;
        track= new Obstacle[size+1];
        track[0]=Obstacle.genRandom();
        for(int i=1;i<size; i++){
            track[i]=Obstacle.genRandom(track[i-1].getType());
            track[i].setDifficulty(track[i].getDifficulty()*(1+difficulty));
        }
        track[size]=Obstacle.ARRIVAL();
        condition=ClimateConditions.genRandom();
    }
    
    public Track(int size){
        track= new Obstacle[size+1];
        track[0]=Obstacle.genRandom();
        for(int i=1;i<size; i++){
            track[i]=Obstacle.genRandom(track[i-1].getType());
        }
        track[size]=Obstacle.ARRIVAL();
        condition=ClimateConditions.genRandom();
    }
    
    private Obstacle at(int i){
        if(i>=track.length)
            i=track.length-1;
        return track[i];
    }
    
    public double difficultyAt(int i){
        if(i>=track.length)
            i=track.length-1;
        return track[i].getDifficulty()*(1+condition.getDifficultyMultiplicator());
    }
    public double optSpeedAt(int i,double pos){
        if(i>=track.length)
            i=track.length-1;
        return track[i].getOptimousSpeed(pos)*(1+condition.getOptimousSpeedMultiplicator());
    }
    public double frictionAt(int i){
        if(i>=track.length)
            i=track.length-1;
        return track[i].getFriction()*(1+condition.getFrictionMultiplicator());
    }
    public double sizeAt(int i){
        if(i>=track.length)
            i=track.length-1;
        return track[i].getSize();
    }
    public int obstacleTypeAt(int i){
        if(i>=track.length)
            i=track.length-1;
        return track[i].getType();
    }
    
    public int getFullSize(){
        return track.length;
    }

    public ClimateConditions getCondition() {
        return condition;
    }

    public void setCondition(ClimateConditions condition) {
        this.condition = condition;
    }
    
    public void climaticFoward(){
        condition.updateDuration();
    }
    
    public double getFutureOptSpeedAt(int idx, double pos){
        double optPrevision=0;
        double currentOptSum=0;
        double nextOptSum=0;
        double steep=sizeAt(idx)-pos;
        int nOfSteeps=(int)sizeAt(idx)*2;
        if(steep<0)steep*=-1;
        steep/=nOfSteeps;
        double tempPos=pos;
        int extraEmphasis=0;
        for(int i=0;i<nOfSteeps;i++){
            if(i<Math.ceil(nOfSteeps*0.17)){
                currentOptSum+=optSpeedAt(idx,tempPos)*Math.round((nOfSteeps-i)/4);
                extraEmphasis+=Math.round((nOfSteeps-i)/4);
            }else
                currentOptSum+=optSpeedAt(idx,tempPos);
            tempPos+=steep;
        }
        currentOptSum/=extraEmphasis+nOfSteeps;
        if(idx+1<track.length){
            nOfSteeps=(int)sizeAt(idx+1)*2;
            steep=sizeAt(idx+1);
            steep/=nOfSteeps;
            tempPos=0;
            for(int i=0;i<nOfSteeps;i++){
                if(nextOptSum!=0){
                    nextOptSum+=optSpeedAt(idx+1,tempPos);
                    nextOptSum/=2;
                }else{
                    nextOptSum=optSpeedAt(idx+1,tempPos);
                }
                tempPos+=steep;
            }
            if(idx+2<track.length){
                nOfSteeps=(int)sizeAt(idx+2)*2;
                steep=sizeAt(idx+2)/2;
                steep/=nOfSteeps;
                tempPos=0;
                for(int i=0;i<nOfSteeps;i++){
                    if(nextOptSum!=0){
                        nextOptSum+=optSpeedAt(idx+2,tempPos);
                        nextOptSum/=2;
                    }else{
                        nextOptSum=optSpeedAt(idx+2,tempPos);
                    }
                    tempPos+=steep;
                }
            }
        }
        if(nextOptSum==0){
            optPrevision=currentOptSum;
        }else{
            optPrevision=(currentOptSum*2+nextOptSum)/3;
        }
        
        return optPrevision;
    }

    @Override
    public String toString() {
        String out="Track:\n";
        for(int i=0;i<track.length;i++)
            out+="      "+track[i]+"\n";
        return out + condition + '\n';
    }
    
    
    
}
