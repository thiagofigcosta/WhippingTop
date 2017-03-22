package com.move2play.whippingtop.util;


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
}
