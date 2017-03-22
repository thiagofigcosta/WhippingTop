package com.move2play.whippingtop.game;

import com.badlogic.gdx.math.Vector3;
import com.move2play.whippingtop.WhippingTopGame;
import com.move2play.whippingtop.util.Util;
import java.util.Random;

public class Top {
    
    private static Random rand = new Random();
    
    public static Top DEFAULT(Track t){
        return new Top(0,30,3,2,3,2,0.3,t);
    }
    public static Top RANDOM(Track t){
        return new Top(0,(rand.nextInt(11)+8)*1.2,rand.nextDouble()*2.2+1,rand.nextDouble()*2.3+1.7,rand.nextDouble()*2.3+1.7,rand.nextDouble()*2.3+1.7,rand.nextDouble()*0.2+0.1,t);
    }
    public static Top DEFAULT(){
        return new Top(0,30,3,2,3,2,0.3);
    }
    public static Top RANDOM(){
        return new Top(0,(rand.nextInt(11)+8)*1.2,rand.nextDouble()*2.2+1,rand.nextDouble()*2.3+1.7,rand.nextDouble()*2.3+1.7,rand.nextDouble()*2.3+1.7,rand.nextDouble()*0.2+0.1);
    }
    
    private int type;
    private double maxVelocity, acceleration,stability, precision, luck;//0-5
    private double life;//0-10
    private double speed;//0-c
    private int position;//0-sizeof(Track)
    private double positionRelative;//0-sizeof(Track[position].size)
    private double friction;//0-1
    private Track track;
    private Vector3 gamePosition;

    public Top(int type,double velocity, double acc, double stability, double precision, double luck, double friction){
        this.type=type;
        setAttributes(velocity,acc,stability,precision,luck);
        life=10;
        speed=0;
        position=0;
        positionRelative=0;
        gamePosition=Vector3.Zero;
        this.friction=friction;
    }
    
    public Top(int type,double velocity, double acc, double stability, double precision, double luck, double friction,Track t){
        this.type=type;
        setAttributes(velocity,acc,stability,precision,luck);
        life=10;
        speed=0;
        position=0;
        positionRelative=0;
        this.friction=friction;
        gamePosition=Vector3.Zero;
        this.track=t;
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
        this.speed += acceleration/(double)WhippingTopGame.FPS;
        if(speed>maxVelocity)speed=maxVelocity;
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

    public void setLuck(double luck) {
        this.luck = luck;
    }

    public double getLife() {
        return life;
    }

    public void applyDamage(double dmg) {
        this.life -= dmg;
        if (life<=0){
            //TODO GGEZ quebrar piao
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setMaxVelocity(double v) {
        this.maxVelocity= v;
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
        this.speed=this.speed*(1-finalFric/(double)(2*WhippingTopGame.FPS));
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
    
    public void roll(){//TODO converter moveDist q passou do tamanho do obstacle em velocidade e reaplicar o mesmo no prox obstacle para nao inicar sempre na posicao 0
        int danger=track.getCondition().getDanger();
        if(rand.nextInt(666-danger)==0&&danger!=0){//apply damage
            applyDamage(1*(danger*rand.nextInt(danger)/(double)666)/(double)1000);
        }
        updateSpeed(track.frictionAt(position));
        double optimousSpeed=track.optSpeedAt(position, positionRelative);
        double difficulty=track.difficultyAt(position);
        double moveDist=speed/(double)WhippingTopGame.FPS;
        if(!Util.offsetEqual(speed, optimousSpeed)){
            if(speed>optimousSpeed){
                double moveMultiplier=1-(((difficulty/(double)5*(5-precision)/(double)5)+Util.compareSpeed(optimousSpeed, speed))/(double)2);
                moveMultiplier*=1-((rand.nextDouble()*luck/(double)5)*0.12f);
                if(Util.compareSpeed(optimousSpeed, speed)>0.16*(5-precision)/(double)5){
                    if(rand.nextInt((int)Math.ceil(luck*9))==0)
                        applyDamage(rand.nextDouble()*0.5*(rand.nextDouble()*(5-luck*0.2)/(double)5));
                }else if(Util.compareSpeed(optimousSpeed, speed)>0.55*(5-precision)/(double)5){
                    applyDamage(life);
                }
                if(moveMultiplier>1)moveMultiplier=1;
                if(moveMultiplier<=0)applyDamage(life);
                moveDist*=moveMultiplier;
            }else{
                double moveMultiplier=1-(((difficulty/(double)5*(5-stability)/(double)5)+Util.compareSpeed(optimousSpeed, speed))/(double)2);
                moveMultiplier*=1-((rand.nextDouble()*luck/(double)5)*0.12f);
                if(Util.compareSpeed(optimousSpeed, speed)>0.28*(5-stability)/(double)5){
                    applyDamage(life);
                }
                if(moveMultiplier>1)moveMultiplier=1;
                if(moveMultiplier<=0)applyDamage(life);
                moveDist*=moveMultiplier;
            }
            positionRelative+=moveDist;
            gamePosition.z+=moveDist;
            if(positionRelative>=track.sizeAt(position)){
                positionRelative=0;
                position++;
                if(position>=track.getFullSize()){
                    //TODO GGWP
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Top{" +"speed=" + speed + ", position=" + position + ", positionRelative=" + positionRelative +", life=" + life +", maxVelocity=" + maxVelocity + ", acceleration=" + acceleration + ", stability=" + stability + ", precision=" + precision + ", luck=" + luck + ", friction=" + friction + "}\n";
    }
    
}
