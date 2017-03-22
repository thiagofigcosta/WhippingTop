package br.cefetmg.move2play.whippingtop.game;

public class Track {
    private Obstacle[] track;
    private ClimateConditions condition;
    public static float TRACKWIDTH=20;
    public static final int INFINITE=-1000;
    private boolean resetedFirstHalf=false;
    
    public Track(int size, double difficulty){
        if(difficulty<0)difficulty=0;
        if(difficulty>10)difficulty=10;
        if(size==INFINITE){
            size=-INFINITE;
            track= new Obstacle[size];
        }else
            track= new Obstacle[size+1];
        track[0]=Obstacle.genRandom();
        for(int i=1;i<size; i++){
            track[i]=Obstacle.genRandom(track[i-1].getType());
            track[i].setDifficulty(track[i].getDifficulty()*(1+difficulty));
        }
        if(size!=-INFINITE)track[size]=Obstacle.ARRIVAL();
        condition=ClimateConditions.genRandom();
        resetedFirstHalf=false;
    }
    
    public Track(int size){
        if(size==INFINITE){
            size=-INFINITE;
            track= new Obstacle[size];
        }else
            track= new Obstacle[size+1];;
        track[0]=Obstacle.genRandom();
        for(int i=1;i<size; i++){
            track[i]=Obstacle.genRandom(track[i-1].getType());
        }
        if(size!=-INFINITE)track[size]=Obstacle.ARRIVAL();
        condition=ClimateConditions.genRandom();
        resetedFirstHalf=false;
    }
    
    private Obstacle at(int i){
        if(i<0)i=0;
        i=i%track.length;
        return track[i];
    }
    
    public boolean isBoostAt(int i){
        return at(i).getType()==Obstacle.SPEEDUP;
    }
    
    public double difficultyAt(int i){
        return at(i).getDifficulty()*(1+condition.getDifficultyMultiplicator());
    }
    public double optSpeedAt(int i,double pos){
        return at(i).getOptimousSpeed(pos)*(1+condition.getOptimousSpeedMultiplicator());
    }
    public double frictionAt(int i){
        return at(i).getFriction()*(1+condition.getFrictionMultiplicator());
    }
    public double sizeAt(int i){
        return at(i).getSize();
    }
    public int obstacleTypeAt(int i){
        return at(i).getType();
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
    
    public boolean regenHalfTrack(boolean firstHalf){
        if(resetedFirstHalf==firstHalf)
            return false;
        int tam=track.length/2;
        if(firstHalf){
            for(int i=0;i<tam; i++){
                track[i]=track[i+tam];
            }
        }
        track[tam]=Obstacle.genRandom();
        for(int i=tam+1;i<track.length; i++){
            track[i]=Obstacle.genRandom(track[i-1].getType());
        }
        resetedFirstHalf=!resetedFirstHalf;
        return true;
    }
    
    public double getFutureOptSpeedAt(int idx, double pos){
        idx=idx%track.length;
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
