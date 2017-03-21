package com.move2play.whippingtop.game;

import com.move2play.whippingtop.WhippingTopGame;

public class Top {
    
    //TODO funcao de cair piao e passar obstaculos
    
    private double maxVelocity, acceleration,stability, precision, luck;//0-10
    private double life;//0-10
    private double speed;//0-c
    private int position;//0-sizeof(Track)
    private double positionRelative;//0-sizeof(Track[position].size)
    private double friction;//0-1
    
    public Top(double velocity, double acc, double stability, double precision, double luck, double friction){
        setAttributes(velocity,acc,stability,precision,luck);
        life=10;
        speed=0;
        position=0;
        positionRelative=0;
        this.friction=friction;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void pedal() {
        this.speed = speed+this.acceleration*(1/WhippingTopGame.FPS);
    }

    public double getStability() {
        return stability;
    }

    public void setStability(double stability) {
        this.stability = stability;
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
            //TODO quebrar piao
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setMaxVelocity(double v) {
        this.maxVelocity= v;
    }
    
    public void updateSpeed(Obstacle ground){
        this.speed=this.speed*(1-this.friction/(2*WhippingTopGame.FPS));
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
    
}
