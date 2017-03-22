package br.cefetmg.move2play.whippingtop.game;

import br.cefetmg.move2play.whippingtop.Settings;
import com.badlogic.gdx.math.Vector3;
import br.cefetmg.move2play.whippingtop.util.Util;
import java.util.Random;

public class Top {
    
    private static Random rand = new Random();
    
    public static Top DEFAULT(Track t){
        return new Top(0,40,5,3,3,2,0.3,t);
    }
    public static Top RANDOM(Track t){
        return new Top(0,(rand.nextInt(20)+18)*1.2,rand.nextDouble()*3.3+1.1,rand.nextDouble()*2.3+1.7,rand.nextDouble()*2.3+1.7,rand.nextDouble()*2.3+1.7,rand.nextDouble()*0.2+0.1,t);
    }
    public static Top DEFAULT(){
        return new Top(0,40,5,2,3,2,0.3);
    }
    public static Top RANDOM(){
        return new Top(0,(rand.nextInt(20)+18)*1.2,rand.nextDouble()*3.3+1.1,rand.nextDouble()*2.3+1.7,rand.nextDouble()*2.3+1.7,rand.nextDouble()*2.3+1.7,rand.nextDouble()*0.2+0.1);
    }
    
    private int type;
    private double maxVelocity, acceleration,stability, precision, luck;//0-5
    private double speed;//0-c
    private int position;//0-sizeof(Track)
    private double positionRelative;//0-sizeof(Track[position].size)
    private double friction;//0-1
    private boolean started=false;
    private boolean alive=true;
    private long pedals=0;
    private long pedalsBkp=0;//used to boost calcs
    private Track track;
    private Vector3 gamePosition;
    private Vector3 gameOrientation;
    private boolean arrived;
    private double points=0;

    public Top(int type,double velocity, double acc, double stability, double precision, double luck, double friction){
        this.type=type;
        setAttributes(velocity,acc,stability,precision,luck);
        points=0;
        speed=0;
        position=0;
        positionRelative=0;
        gamePosition=new Vector3(0,0,0);
        gameOrientation=new Vector3(30,0,0);
        this.friction=friction;
        arrived=false;
        alive=true;
    }
    
    public Top(int type,double velocity, double acc, double stability, double precision, double luck, double friction,Track t){
        this.type=type;
        setAttributes(velocity,acc,stability,precision,luck);
        points=0;
        speed=0;
        position=0;
        positionRelative=0;
        this.friction=friction;
        gamePosition=new Vector3(0,0,0);
        gameOrientation=new Vector3(30,0,0);
        this.track=t;
        arrived=false;
        alive=true;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public void pedal() {
        pedals++;
        this.speed += acceleration/(double)Settings.getFPS();
        if(speed>maxVelocity)speed=maxVelocity;
    }
    
    public void increaseRotation(){
        gameOrientation.y=(gameOrientation.y+(float)(speed*0.66f))%360f;
        if(speed<maxVelocity/3.7f){
            gameOrientation.x=(float)Util.modAngle(gameOrientation.x,speed*0.018f,33,0);
        }else{
            gameOrientation.x=(float)Util.modAngle(gameOrientation.x,-speed*0.025f,33,0);
        }
    }

    public double getStability() {
        return stability;
    }

    public void setStability(double stability) {
        this.stability = stability;
    }

    public Vector3 getGamePosition() {
        return gamePosition;
    }
    
    public float getGameYawOrientation(){
        float y=gameOrientation.y;
        return y;
    }
    public float getGamePitchOrientation(){
        float x=gameOrientation.x;
        return x;
    }

    public void setGamePosition(Vector3 gamePosition) {
        this.gamePosition = gamePosition;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getLuck() {
        return luck;
    }
    
    public boolean alreadyArrived(){
        return arrived;
    }

    public void setLuck(double luck) {
        this.luck = luck;
    }

    public double getSpeed() {
        return speed;
    }

    public void setMaxVelocity(double v) {
        this.maxVelocity= v;
    }
    
    public float refreshPos(int i){
        i=i%track.getFullSize();
        position=i;
        gamePosition.z=(float) positionRelative;
        for(int x=0;x<i;x++)
            gamePosition.z+=track.sizeAt(x);
        return gamePosition.z;
    }
    
    public void updateSpeed(double groundFriction){
        double finalFric;
        if(groundFriction==0){
            finalFric=friction;
        }else{
            if(groundFriction>friction){
                finalFric=((groundFriction*1.666)+(friction*0.999))/2.665;
            }else{
                finalFric=((friction*1.666)+(groundFriction*0.999))/2.665;
            }
        }
        this.speed=this.speed*(1-finalFric/(double)(2*Settings.getFPS()));
        if(this.speed*0.8f>track.optSpeedAt(position, positionRelative)&&pedals>50)started=true;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getPositionRelative() {
        return positionRelative;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public void setPositionRelative(double positionRelative) {
        this.positionRelative = positionRelative;
    }
    
    public void setAttributes(double velocity, double acc,double stability, double precision, double luck){
        this.maxVelocity=velocity;
        this.acceleration=acc;
        this.stability=stability;
        this.precision=precision;
        this.luck=luck;
    }
    
    private void applyTrackPenality(){
        int danger=track.getCondition().getDanger();
        if(rand.nextInt(666-danger)==0&&danger!=0){//apply damage
            double dmg=1*(danger*rand.nextInt(danger)/(double)666)/(double)1000;
            points-=dmg/2;
            if(points<0)points=0;
        }
    }
    
    public boolean isAlive(){
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    public long getPoints() {
        return Math.round(points)/10;
    }
    
    private void increasePoints(double newPoints){
        if(Double.isNaN(newPoints))
            newPoints=1;
        if(Double.isNaN(points))
            points=newPoints;
        else
            points+=newPoints;
    }
    
    
    private double calculateDist(double moveDist, double optimousSpeed, double difficulty, double pastOptimousSpeed){
        final double optSpeedMultiplier=3;
        final double stabilityInvInfluence=(5-stability)/5f;
        final double precisionInvInfluence=(5-precision)/5f;
        final double luckInvInfluence=rand.nextDouble()*(5-luck*0.2)/5f;
        final double luckInfluence=1-((rand.nextDouble()*(Math.pow(6.45f, 2)-Math.pow(luck, 2))/5/(double)5)*0.12f);
        double newPoints=(Math.pow(speed, 2.5f)/Math.pow(speed, 1.9f))/3;
        double normalCompareSpeed=Util.compareSpeed(optimousSpeed, speed);
        double pastCompareSpeed=Util.compareSpeed(pastOptimousSpeed,optimousSpeed);
        double currentInfluence=0.4f;
        double goodErr=1;
        
        if(speed>optimousSpeed){ //faster then opt
            if(track.isBoostAt(position))
                optimousSpeed=speed;
            currentInfluence=precisionInvInfluence;
            goodErr=1.2f;
        }else{//slower than opt
            currentInfluence=stabilityInvInfluence;
            goodErr=1;
        }
        
        //calc dist
        double moveMultiplier=1-(((difficulty/(double)5*currentInfluence)+Util.compareSpeed(optimousSpeed, speed))/(double)2);
        moveMultiplier*=luckInfluence;
        if(moveMultiplier>1)moveMultiplier=1;
        if(moveMultiplier<=0.1f)moveMultiplier=0.2f;
        moveDist*=moveMultiplier;
      
        //calc points
        if(!Util.offsetEqual(speed, optimousSpeed)){//not in opt speed
            if(normalCompareSpeed>0.55*currentInfluence){//very very faster/slower
                if(pastCompareSpeed>normalCompareSpeed*0.3f){//hard track
                    newPoints*=0.5*goodErr;
                }else{//ez track
                    newPoints*=0.9*goodErr;
                }
            }else if(normalCompareSpeed>0.11*currentInfluence){//very faster/slower
                if(rand.nextInt((int)Math.ceil((5-luck)*9))!=0){//not soo lucky
                   newPoints*=optSpeedMultiplier*0.18f*goodErr;
                }else{//lucky splayer
                    newPoints+=optSpeedMultiplier*goodErr;
                    newPoints*=optSpeedMultiplier*0.26f*goodErr;
                }
            }else{//faster/slower
                newPoints+=optSpeedMultiplier*goodErr;
                newPoints*=optSpeedMultiplier*0.5f*goodErr;
            }
        }else{//perfect
            moveMultiplier=1.2f*goodErr;
            newPoints+=optSpeedMultiplier*goodErr;
            newPoints*=optSpeedMultiplier*goodErr;
        }
        if(speed>6)//minimum
            increasePoints(newPoints);
        return moveDist;
    }
    
    private void move(double d){
        double oldPositionRelative=positionRelative;
        positionRelative+=d;
        gamePosition.z+=d;
        increaseRotation();
        if(positionRelative>=track.sizeAt(position)){
            positionRelative=track.sizeAt(position)-oldPositionRelative;
            if(track.isBoostAt(position)){//past is speedboost
                final double pointsPerPedal=3;
                final double pointsPerSquarePedal=2;
                increasePoints((pedals-pedalsBkp)*pointsPerPedal);
                increasePoints(Math.sqrt(pedals*pedals-pedalsBkp*pedalsBkp)*pointsPerSquarePedal);
                pedalsBkp=pedals;
            }
            position++;
            if(position>=Math.pow(2,31))
                position=(int) Math.pow(2,31);
            if(position>=track.getFullSize()&&track.getFullSize()!=Track.INFINITE){
                arrived=true;
            }
            if(track.isBoostAt(position)){//now is speedboost
                pedalsBkp=pedals;
            }
        }
    }
    
    public void roll(){
        applyTrackPenality();
        updateSpeed(track.frictionAt(position));
        double optimousSpeed=track.optSpeedAt(position, positionRelative);
        double pastOptimousSpeed=track.optSpeedAt(position-1, positionRelative);
        double difficulty=track.difficultyAt(position);
        double baseDistance=speed/(double)Settings.getFPS();
        double distance=calculateDist(baseDistance,optimousSpeed,difficulty,pastOptimousSpeed);
        move(distance);
    }

    public long getPedals() {
        return pedals;
    }

    @Override
    public String toString() {
        return "Top{" +"speed=" + speed + ", position=" + position + ", positionRelative=" + positionRelative +", points=" + points +", maxVelocity=" + maxVelocity + ", acceleration=" + acceleration + ", stability=" + stability + ", precision=" + precision + ", luck=" + luck + ", friction=" + friction + "}\n";
    }
    
}
