package com.move2play.whippingtop.game;

import java.util.Random;

public class Obstacle {
    
    private static Random rand = new Random();
    
    private static final int NOFPREFABS=5;
    
    public static Obstacle STONE(){
        return new Obstacle(0,rand.nextDouble()*10+10,rand.nextDouble()*10+30,rand.nextDouble()*2,rand.nextDouble()*0.2+0.1);
    }
    public static Obstacle SPEEDUP(){
        return new Obstacle(1,rand.nextDouble()*5+8,rand.nextDouble()*15+60,rand.nextDouble()*2+1,rand.nextDouble()*0.1+0.01);
    }
    public static Obstacle GRASS(){
        return new Obstacle(2,rand.nextDouble()*10+10,rand.nextDouble()*5+16,rand.nextDouble()*2+1,rand.nextDouble()*0.2+0.2);
    }
    public static Obstacle CONE(){
        return new Obstacle(3,rand.nextDouble()*10+2,rand.nextDouble()*5+16,rand.nextDouble()*1.45+2,rand.nextDouble()*0.2+0.1);
    }
    public static Obstacle BRIDGE(){
        return new Obstacle(4,rand.nextDouble()*3+12,rand.nextDouble()*10+40,rand.nextDouble()*1.45+2,rand.nextDouble()*0.2+0.1);
    }
    
    private int type;
    private double size;//0-maxValueOfInt
    private double difficulty;//0-5
    private double optimousSpeed;//0-c
    private double friction;//0-1

    public Obstacle(int type,double size, double optimousSpeed, double difficulty, double friction) {
        this.type=type;
        this.size = size;
        this.optimousSpeed = optimousSpeed;
        this.difficulty=difficulty;
        this.friction=friction;
    }

    public double getSize() {
        return size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public void setSize(double size) {
        this.size = size;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public double getOptimousSpeed(double pos) {
        if(type==1)
            return optimousSpeed*((size+pos)/size);
        else
            return optimousSpeed;
    }

    public void setOptimousSpeed(double optimousSpeed) {
        this.optimousSpeed = optimousSpeed;
    }
    
    public static Obstacle genRandom(){
        return genRandom(rand.nextInt(NOFPREFABS));
    }
    
    public static Obstacle genRandom(int previous){
        int next=previous;
        if(rand.nextInt(4)==0)
            next=rand.nextInt(NOFPREFABS*2);
        switch(next){
            default: return STONE();
            case 1: return SPEEDUP();
            case 2: return GRASS();
            case 3: return CONE();
            case 4: return BRIDGE();
        }
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    @Override
    public String toString() {
        String name;
        switch(type){
            default: name="stone";break;
            case 1: name="speedup";break;
            case 2: name="grass";break;
            case 3: name="cone";break;
            case 4: name="bridge";break;
        }
        return name+"{" + "size=" + size + ", difficulty=" + difficulty + ", optimousSpeed=" + optimousSpeed + ", friction=" + friction + "}\n";
    }
    
}
