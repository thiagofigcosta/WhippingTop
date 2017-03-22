package br.cefetmg.move2play.whippingtop.util.files;

public class Sounds {
    public static final String BASEFOLDER="audio/sound/";
    
    private static final String Wheel1="wheel1.mp3";    
    private static final String Top0="topGrave.mp3";
    private static final String Top1="topAgudo.mp3";
    private static final String Snow="climatic/snow.mp3";
    private static final String Rain="climatic/rain.mp3";

    public static String Wheel(){return BASEFOLDER+Wheel1;} 
    public static String Top0(){return BASEFOLDER+Top0;} 
    public static String Top1(){return BASEFOLDER+Top1;} 
    public static String Snow(){return BASEFOLDER+Snow;} 
    public static String Rain(){return BASEFOLDER+Rain;} 
}
