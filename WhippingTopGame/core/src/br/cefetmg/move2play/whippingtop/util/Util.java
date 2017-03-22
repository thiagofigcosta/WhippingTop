package br.cefetmg.move2play.whippingtop.util;

import com.badlogic.gdx.math.Vector3;
import br.cefetmg.move2play.whippingtop.util.files.Models;
import br.cefetmg.move2play.whippingtop.util.files.Textures;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.stbtt.TrueTypeFontFactory;
import java.util.Random;


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
    
    public static Color getSpriteColorFromID(int id){
        switch (id){
            default:return new Color(1,1,1,1);
            case 1: return new Color(1,0.6f,0.6f,1);
            case 2: return new Color(0.6f,1,0.6f,1);
            case 3: return new Color(0.6f,0.6f,1,1);
        }
    }
    
    public static double modAngle(double angle,double increase ,double max, double min){
        if(angle+increase>max){
            angle=max;
        }else if(angle+increase<min){
            angle=min;
        }else{
            angle+=increase;
        }
        return angle;
    }
    
    public static String genCone(){
        Random rand = new Random();
        switch(rand.nextInt(5)){
            default:return Models.Cone0();
            case 1 :return Models.Cone1();
            case 2 :return Models.Cone2();
            case 3 :return Models.Cone3();
            case 4 :return Models.Cone4();
            case 5 :return Models.Cone5();
        }
    }
    
    public static String genBridge(){
        Random rand = new Random();
        switch(rand.nextInt(5)){
            default: return Textures.Bridge0();
            case 1: return Textures.Bridge1();
            case 2: return Textures.Bridge2();
            case 3: return Textures.Bridge3();
            case 4: return Textures.Bridge4();
        }
    }
    
    public static int genInt(int max){
        Random rand = new Random();
        return rand.nextInt(max);
    }
    
    public static Vector3 vec3Sub(Vector3 a,Vector3 b){
        return new Vector3(a.x-b.x,a.y-b.y,a.z-b.z);
    }
    public static Vector3 vec3Mult(Vector3 a, float b){
        return new Vector3(a.x*b,a.y*b,a.z*b);
    }
    
    public static BitmapFont createBitMapFromTTF(String ttfpath, Color color){
        BitmapFont font;
        String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
        font = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal(ttfpath), FONT_CHARACTERS, 12.5f, 7.5f, 1.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.setColor(color);
        return font;
    }
}
