package br.cefetmg.move2play.whippingtop.game;

import com.badlogic.gdx.math.Vector3;
import br.cefetmg.move2play.whippingtop.WhippingTopGame;
import br.cefetmg.move2play.whippingtop.util.Util;
import java.util.Random;

public class Top {
    
    private static Random rand = new Random();
    public static final int MAXLIFE=10;
    
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
    private double life;//0-10
    private double speed;//0-c
    private int position;//0-sizeof(Track)
    private double positionRelative;//0-sizeof(Track[position].size)
    private double friction;//0-1
    private boolean started=false;
    private long pedals=0;
    private Track track;
    private Vector3 gamePosition;
    private Vector3 gameOrientation;
    private boolean arrived;

    public Top(int type,double velocity, double acc, double stability, double precision, double luck, double friction){
        this.type=type;
        setAttributes(velocity,acc,stability,precision,luck);
        life=MAXLIFE;
        speed=0;
        position=0;
        positionRelative=0;
        gamePosition=new Vector3(0,0,0);
        gameOrientation=new Vector3(30,0,0);
        this.friction=friction;
        arrived=false;
    }
    
    public Top(int type,double velocity, double acc, double stability, double precision, double luck, double friction,Track t){
        this.type=type;
        setAttributes(velocity,acc,stability,precision,luck);
        life=MAXLIFE;
        speed=0;
        position=0;
        positionRelative=0;
        this.friction=friction;
        gamePosition=new Vector3(0,0,0);
        gameOrientation=new Vector3(30,0,0);
        this.track=t;
        arrived=false;
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
        this.speed += acceleration/(double)WhippingTopGame.FPS;
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

    public double getLife() {
        return life;
    }

    public void applyDamage(double dmg) {
        if(started&&!arrived)
            this.life -= dmg;
        if (life<=0){
            life=0;
        }
    }

    public boolean isAlive(){
        return life!=0;
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
        this.speed=this.speed*(1-finalFric/(double)(2*WhippingTopGame.FPS));
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
    
    private void applyTrackDamage(){
        int danger=track.getCondition().getDanger();
        if(rand.nextInt(666-danger)==0&&danger!=0){//apply damage
            double dmg=1*(danger*rand.nextInt(danger)/(double)666)/(double)1000;
            applyDamage(dmg);
        }
    }
    
    private double calculateSpeed(double moveDist, double optimousSpeed, double difficulty, double pastOptimousSpeed){
        if(!Util.offsetEqual(speed, optimousSpeed)){
            if(speed>optimousSpeed){
                double moveMultiplier=1-(((difficulty/(double)5*(5-precision)/5f)+Util.compareSpeed(optimousSpeed, speed))/(double)2);
                moveMultiplier*=1-((rand.nextDouble()*luck/(double)5)*0.12f);
                if(Util.compareSpeed(optimousSpeed, speed)>0.11*(5-precision)/5f){
                    if(rand.nextInt((int)Math.ceil(luck*9))==0){
                        double dmg=rand.nextDouble()*0.5*(rand.nextDouble()*(5-luck*0.2)/5f);
                        applyDamage(dmg);
                    }
                }else if(Util.compareSpeed(optimousSpeed, speed)>0.55*(5-precision)/5f){
                    if(Util.compareSpeed(pastOptimousSpeed,optimousSpeed)>Util.compareSpeed(optimousSpeed, speed)*0.3f){
                        double dmg=rand.nextDouble()*0.7*(rand.nextDouble()*(5-luck*0.2)/5f);
                        applyDamage(dmg);
                    }else{
                        applyDamage(life);
                    }
                }
                if(moveMultiplier>1)moveMultiplier=1;
                if(moveMultiplier<=0){applyDamage(life);}
                moveDist*=moveMultiplier;
            }else{
                double moveMultiplier=1-(((difficulty/(double)5*(5-stability)/(double)5)+Util.compareSpeed(optimousSpeed, speed))/(double)2);
                moveMultiplier*=1-((rand.nextDouble()*luck/(double)5)*0.12f);
                if(Util.compareSpeed(optimousSpeed, speed)>0.856f*(5-stability)/5f){
                    if(Util.compareSpeed(optimousSpeed, pastOptimousSpeed)>Util.compareSpeed(optimousSpeed, speed)*0.3f){
                        double dmg=rand.nextDouble()*0.7*(rand.nextDouble()*(5-luck*0.2)/5f);
                        applyDamage(dmg);
                    }else{
                        applyDamage(life);
                    }
                }
                if(moveMultiplier>1)moveMultiplier=1;
                if(moveMultiplier<=0){applyDamage(life);}
                moveDist*=moveMultiplier;
            }
        }
        return moveDist;
    }
    
    private void move(double d){
        double oldPositionRelative=positionRelative;
        positionRelative+=d;
        gamePosition.z+=d;
        increaseRotation();
        if(positionRelative>=track.sizeAt(position)){
            positionRelative=track.sizeAt(position)-oldPositionRelative;
            position++;
            if(position>=Math.pow(2,31))
                position=(int) Math.pow(2,31);
            if(position>=track.getFullSize()&&track.getFullSize()!=Track.INFINITE){
                arrived=true;
            }
        }
    }
    
    public void roll(){
        applyTrackDamage();
        updateSpeed(track.frictionAt(position));
        double optimousSpeed=track.optSpeedAt(position, positionRelative);
        double pastOptimousSpeed=track.optSpeedAt(position, positionRelative);
        double difficulty=track.difficultyAt(position);
        double baseDistance=speed/(double)WhippingTopGame.FPS;
        double distance=calculateSpeed(baseDistance,optimousSpeed,difficulty,pastOptimousSpeed);
        move(distance);
    }

    public long getPedals() {
        return pedals;
    }

    @Override
    public String toString() {
        return "Top{" +"speed=" + speed + ", position=" + position + ", positionRelative=" + positionRelative +", life=" + life +", maxVelocity=" + maxVelocity + ", acceleration=" + acceleration + ", stability=" + stability + ", precision=" + precision + ", luck=" + luck + ", friction=" + friction + "}\n";
    }
    
}
