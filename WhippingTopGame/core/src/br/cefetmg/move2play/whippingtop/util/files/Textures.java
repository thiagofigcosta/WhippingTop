package br.cefetmg.move2play.whippingtop.util.files;

public class Textures {
    public static final String BASEFOLDER="img/tex/";
    
    private static final String Track="track/0.jpg";
    private static final String SpeedUp="track/speedup.png";
    private static final String Bridge0="track/bridges/0.png";
    private static final String Bridge1="track/bridges/1.png";
    private static final String Bridge2="track/bridges/2.png";
    private static final String Bridge3="track/bridges/3.png";
    private static final String Bridge4="track/bridges/4.png";
    private static final String SkySphere="sky.jpg";
    private static final String Arrival="track/arrival.png";
    
    
    
    public static String Track(){return BASEFOLDER+Track;}
    public static String Sky(){return BASEFOLDER+SkySphere;}
    public static String Bridge(int i){return BASEFOLDER+"track/bridges/"+i+".png";}
    public static String Bridge0(){return BASEFOLDER+Bridge0;}
    public static String Bridge1(){return BASEFOLDER+Bridge1;}
    public static String Bridge2(){return BASEFOLDER+Bridge2;}
    public static String Bridge3(){return BASEFOLDER+Bridge3;}
    public static String Bridge4(){return BASEFOLDER+Bridge4;}
    public static String SpeedUp(){return BASEFOLDER+SpeedUp;}
    public static String Arrival(){return BASEFOLDER+Arrival;}
}
