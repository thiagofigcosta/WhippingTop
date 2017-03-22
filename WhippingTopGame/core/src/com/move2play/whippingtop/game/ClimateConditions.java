package com.move2play.whippingtop.game;

import com.move2play.whippingtop.WhippingTopGame;
import java.util.Random;

public class ClimateConditions {
    
    private static Random rand = new Random();
    
    private static final int NOFPREFABS=4;
    
    public static ClimateConditions NORMAL(){
        return new ClimateConditions(0,0,0,0,0,rand.nextDouble()*6+15);
    }
    public static ClimateConditions SNOW(){
        return new ClimateConditions(1,rand.nextDouble()*0.2+0.1,-(rand.nextDouble()*0.1+0.2),rand.nextDouble()*0.2+0.1,rand.nextInt(200)+50,rand.nextDouble()*10+18);
    }
    public static ClimateConditions RAIN(){
        return new ClimateConditions(2,rand.nextDouble()*0.25,-(rand.nextDouble()*0.11),-(rand.nextDouble()*0.3+0.11),rand.nextInt(60)+10,rand.nextDouble()*15+6);
    }
    public static ClimateConditions SUNNY(){
        return new ClimateConditions(3,rand.nextDouble()*0.1,rand.nextDouble()*0.03,rand.nextDouble()*0.2+0.11,0,rand.nextDouble()*6+15);
    }
    
    private int type;
    private double difficultyMultiplicator;//0-1
    private double optimousSpeedMultiplicator;//0-1
    private double frictionMultiplicator;//0-1
    private int danger;//0-666 (aplica dano no peao aleatoriamente)
    private double duration;//real duration = duration*fps

    public ClimateConditions(int type, double difficultyMultiplicator, double optimousSpeedMultiplicator, double frictionMultiplicator, int danger, double duration) {
        this.type = type;
        this.difficultyMultiplicator = difficultyMultiplicator;
        this.optimousSpeedMultiplicator = optimousSpeedMultiplicator;
        this.frictionMultiplicator = frictionMultiplicator;
        this.danger = danger;
        this.duration = duration;
    }
    
    public void reset(ClimateConditions cc) {
        this.type = cc.type;
        this.difficultyMultiplicator = cc.difficultyMultiplicator;
        this.optimousSpeedMultiplicator = cc.optimousSpeedMultiplicator;
        this.frictionMultiplicator = cc.frictionMultiplicator;
        this.danger = cc.danger;
        this.duration = cc.duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getDifficultyMultiplicator() {
        return difficultyMultiplicator;
    }

    public void setDifficultyMultiplicator(double difficultyMultiplicator) {
        this.difficultyMultiplicator = difficultyMultiplicator;
    }

    public double getOptimousSpeedMultiplicator() {
        return optimousSpeedMultiplicator;
    }

    public void setOptimousSpeedMultiplicator(double optimousSpeedMultiplicator) {
        this.optimousSpeedMultiplicator = optimousSpeedMultiplicator;
    }

    public double getFrictionMultiplicator() {
        return frictionMultiplicator;
    }

    public void setFrictionMultiplicator(double frictionMultiplicator) {
        this.frictionMultiplicator = frictionMultiplicator;
    }

    public int getDanger() {
        return danger;
    }

    public void setDanger(int danger) {
        this.danger = danger;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
    
    public double updateDuration(){
        int realDuration=(int)Math.ceil(this.duration*WhippingTopGame.FPS);
        realDuration--;
        this.duration=realDuration/(double)WhippingTopGame.FPS;
        if(duration<=0){
            reset(genRandom(type));
        }
        return duration;
    }
    
    public static ClimateConditions genRandom(){
        return genRandom(rand.nextInt(NOFPREFABS-1)+1);
    }
    
    public static ClimateConditions genRandom(int previous){
        int next=previous;
        if(previous!=0){
            if(rand.nextInt(8)==0)
                next=rand.nextInt(NOFPREFABS);
        }else
            if(rand.nextInt(4)==0)
                next=rand.nextInt(NOFPREFABS);
            
        switch(next){
            default: return NORMAL();
            case 1: return SNOW();
            case 2: return RAIN();
            case 3: return SUNNY();
        }
    }

    @Override
    public String toString() {
        String name;
        switch(type){
            default: name="normal";break;
            case 1: name="snow";break;
            case 2: name="rain";break;
            case 3: name="snny";break;
        }
        return name+"{difficultyMultiplicator=" + difficultyMultiplicator + ", optimousSpeedMultiplicator=" + optimousSpeedMultiplicator + ", frictionMultiplicator=" + frictionMultiplicator + ", danger=" + danger + ", duration=" + duration + "}\n";
    }
    
    
}
