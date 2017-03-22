package br.cefetmg.move2play.whippingtop.util.files;

public class Models {
    public static final String BASEFOLDER="obj/";
    
    private static final String TOP0="beyblade.obj";
    private static final String Grass="grass.obj";
    private static final String Cones="Cones/conesTodos.obj";
    private static final String Cone0="Cones/cone0.obj";
    private static final String Cone1="Cones/cone1.obj";
    private static final String Cone2="Cones/cone2.obj";
    private static final String Cone3="Cones/cone3.obj";
    private static final String Cone4="Cones/cone4.obj";
    private static final String Cone5="Cones/cone5.obj";
    
    public static String TOP(){return BASEFOLDER+TOP0;}
    public static String Grass(){return BASEFOLDER+Grass;}
    public static String Cones(){return BASEFOLDER+Cones;}
    public static String Cone(int i){return BASEFOLDER+"Cones/cone"+i+".obj";}
    public static String Cone0(){return BASEFOLDER+Cone0;}
    public static String Cone1(){return BASEFOLDER+Cone1;}
    public static String Cone2(){return BASEFOLDER+Cone2;}
    public static String Cone3(){return BASEFOLDER+Cone3;}
    public static String Cone4(){return BASEFOLDER+Cone4;}
    public static String Cone5(){return BASEFOLDER+Cone5;}
}
