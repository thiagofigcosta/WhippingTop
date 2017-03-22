package com.move2play.whippingtop.game;

public class Track {
    private Obstacle[] track;
    private ClimateConditions condition;
    public static float TRACKWIDTH=20;
    
    public Track(int size, double difficulty){
        if(difficulty<0)difficulty=0;
        if(difficulty>10)difficulty=10;
        track= new Obstacle[size];
        track[0]=Obstacle.genRandom();
        for(int i=1;i<size; i++){
            track[i]=Obstacle.genRandom(track[i-1].getType());
            track[i].setDifficulty(track[i].getDifficulty()*(1+difficulty));
        }
        condition=ClimateConditions.genRandom();
    }
    
    public Track(int size){
        track= new Obstacle[size];
        track[0]=Obstacle.genRandom();
        for(int i=1;i<size; i++){
            track[i]=Obstacle.genRandom(track[i-1].getType());
        }
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
    public double obstacleTypeAt(int i){
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

    @Override
    public String toString() {
        String out="Track:\n";
        for(int i=0;i<track.length;i++)
            out+="      "+track[i]+"\n";
        return out + condition + '\n';
    }
    
    
    
}
