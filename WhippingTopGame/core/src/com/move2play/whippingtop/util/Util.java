package com.move2play.whippingtop.util;

import com.badlogic.gdx.math.Vector3;


public class Util {
    public static final double OFFSET=0.00001;
    
    public static boolean offsetEqual(double a, double b, double offset){
        offset=Math.abs(offset);
        return a+offset>=b&&a-offset<=b;
    }
    
    public static boolean offsetEqual(double a, double b){
        return offsetEqual(a,b,OFFSET);
    }
    
    public static double compareSpeed(double expected,double achieved){
        double percent=Math.abs(expected-achieved)/expected;
        return percent;
    }
    
    public static Vector3 vec3Sub(Vector3 a,Vector3 b){
        return new Vector3(a.x-b.x,a.y-b.y,a.z-b.z);
    }
    public static Vector3 vec3Mult(Vector3 a, float b){
        return new Vector3(a.x*b,a.y*b,a.z*b);
    }
}
