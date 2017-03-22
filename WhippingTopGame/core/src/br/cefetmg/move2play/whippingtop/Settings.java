package br.cefetmg.move2play.whippingtop;

import br.cefetmg.move2play.exception.IllegalAttributeException;
import br.cefetmg.move2play.settings.GameSettings;

public class Settings extends GameSettings{
    private boolean runningOnMove2Play=false;
    private boolean multipleScreens=false;
    private static int FPS=0;
    
    @Override
    public void loadSettings(String s){
        super.loadSettings(s);
        FPS=get("FPS");
    }
    
    @Override
    public void loadSettings(){
        super.loadSettings();
        FPS=get("FPS");
    }

    public <T> T get(String attr){
        try{
            if(     attr.equals("FPS")||
                    attr.equals("width")||
                    attr.equals("height")||
                    attr.equals("amountPlayers")||
                    attr.equals("defaultTrackSize")){
                return getSpecificSetting(attr).parse();
            }else if(attr.equals("runningOnMove2Play")){
                return (T) Boolean.class.cast(runningOnMove2Play);
            }else if(attr.equals("multipleScreens")){
                return (T) Boolean.class.cast(multipleScreens);
            }
        }catch(IllegalAttributeException e){
            System.out.println("Error parsing setting. "+e.getMessage());
        }
        
        System.out.println("unknow attr: "+attr);
        return null;
    }

    public void setRunningOnMove2Play(boolean runningOnMove2Play) {
        this.runningOnMove2Play = runningOnMove2Play;
    }

    public void setMultipleScreens(boolean multipleScreens) {
        this.multipleScreens = multipleScreens;
    }

    public static int getFPS() {
        return FPS;
    }
    
    public int getFps() {
        return FPS;
    }
}
