package br.cefetmg.move2play.whippingtop;

import br.cefetmg.move2play.whippingtop.screens.YouLose;
import br.cefetmg.move2play.whippingtop.screens.YouWin;
import br.cefetmg.move2play.whippingtop.util.Util;
import br.cefetmg.move2play.whippingtop.util.files.Animations;
import br.cefetmg.move2play.whippingtop.util.files.Fonts;
import br.cefetmg.move2play.whippingtop.util.files.Images;
import br.cefetmg.move2play.whippingtop.util.files.Models;
import br.cefetmg.move2play.whippingtop.util.files.Musics;
import br.cefetmg.move2play.whippingtop.util.files.Particles;
import br.cefetmg.move2play.whippingtop.util.files.Sounds;
import br.cefetmg.move2play.whippingtop.util.files.Textures;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.Model;

public class Assets {
    
    private boolean loaded;
    private AssetManager assets;

    private Music stageMusic,soundTop0,soundTop1,rainSound,snowSound,palmas;
    private Texture floorText,arrivalText,speedUpText,boostSprSheet;
    private Texture bridges[];
    private Model grassModel,topModel;
    private Model cones[];
    private Texture lifeText,velocimeterText,velocimeterPointerText,semaphoreText,skyText,gameOverText;
    private BitmapFont hudFontSmall,hudFontBig;
    private ParticleEffect rainPart,snowPart,sunnyPart;
    private YouLose loseScreen;
    private YouWin  winScreen;
    
    public Assets(){
        loaded=false;
        load();
    }
    
    public void load(){
        loaded=false;
        assets=new AssetManager();
        assets.load(Models.TOP(), Model.class);
        assets.load(Models.Grass(), Model.class);
        assets.load(Models.Cones(), Model.class);
        assets.load(Models.Cone0(), Model.class);
        assets.load(Models.Cone1(), Model.class);
        assets.load(Models.Cone2(), Model.class);
        assets.load(Models.Cone3(), Model.class);
        assets.load(Models.Cone4(), Model.class);
        assets.load(Models.Cone5(), Model.class);
        assets.load(Particles.Rain(), ParticleEffect.class);
        assets.load(Particles.Snow(), ParticleEffect.class);
        assets.load(Particles.Sunny(), ParticleEffect.class);
        assets.load(Textures.Track(), Texture.class);
        assets.load(Textures.Sky(), Texture.class);
        assets.load(Images.GameOver(),Texture.class);
        assets.load(Textures.Bridge0(), Texture.class);
        assets.load(Textures.Bridge1(), Texture.class);
        assets.load(Textures.Bridge2(), Texture.class);
        assets.load(Textures.Bridge3(), Texture.class);
        assets.load(Textures.Bridge4(), Texture.class);
        assets.load(Textures.SpeedUp(), Texture.class);
        assets.load(Animations.Boost(), Texture.class);
        assets.load(Images.Velocimeter(), Texture.class);
        assets.load(Images.VelPointer(), Texture.class);
        assets.load(Textures.Arrival(), Texture.class);
        assets.load(Images.Life(), Texture.class);
        assets.load(Images.Semaphore(), Texture.class);
        stageMusic=Gdx.audio.newMusic(Gdx.files.internal(Musics.Music1()));
        soundTop0=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Top0()));
        soundTop1=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Top1()));
        rainSound=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Rain()));
        snowSound=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Snow()));
        palmas=Gdx.audio.newMusic(Gdx.files.internal(Sounds.Palmas()));
        loseScreen=new YouLose(null);
        winScreen=new YouWin(null);
        new Thread() {
                @Override
                public void run(){
                    while(assets.update()){}
                }
        }.start();
    }

    private void doneLoading(){
        gameOverText=assets.get(Images.GameOver(), Texture.class);
        floorText=assets.get(Textures.Track(), Texture.class);
        arrivalText=assets.get(Textures.Arrival(), Texture.class);
        speedUpText=assets.get(Textures.SpeedUp(), Texture.class);
        lifeText=assets.get(Images.Life(), Texture.class);
        velocimeterText=assets.get(Images.Velocimeter(), Texture.class);
        velocimeterPointerText=assets.get(Images.VelPointer(), Texture.class);
        grassModel=assets.get(Models.Grass(), Model.class);
        topModel=assets.get(Models.TOP(), Model.class);//gisele bundchen
        hudFontSmall=Util.createBitMapFromTTF(Fonts.Amerika(), Color.BLACK, 19);
        hudFontBig=Util.createBitMapFromTTF(Fonts.Amerika(), Color.BLACK, 30);
        boostSprSheet=assets.get(Animations.Boost(), Texture.class);
        bridges=new Texture[5];
        for(int i=0;i<5;i++)
            bridges[i]=assets.get(Textures.Bridge(i), Texture.class);
        cones=new Model[6];
        for(int i=0;i<6;i++)
            cones[i]=assets.get(Models.Cone(i), Model.class);
        rainPart=((ParticleEffect) assets.get(Particles.Rain()));
        snowPart=((ParticleEffect) assets.get(Particles.Snow()));
        sunnyPart=((ParticleEffect) assets.get(Particles.Sunny()));
        semaphoreText=assets.get(Images.Semaphore(), Texture.class);
        skyText=assets.get(Textures.Sky(), Texture.class);
        loaded=true;
    }
    
    public void dispose(){
        assets.dispose();
        stageMusic.dispose();
        soundTop0.dispose();
        soundTop1.dispose();
        rainSound.dispose();
        snowSound.dispose();
        floorText.dispose();
        arrivalText.dispose();
        speedUpText.dispose();
        boostSprSheet.dispose();
        for(Texture t:bridges)
            t.dispose();
        grassModel.dispose();
        topModel.dispose();
        for(Model m:cones)
            m.dispose();
        lifeText.dispose();
        velocimeterText.dispose();
        velocimeterPointerText.dispose();
        semaphoreText.dispose();
        skyText.dispose();
        hudFontSmall.dispose();
        hudFontBig.dispose();
        rainPart.dispose();
        snowPart.dispose();
        sunnyPart.dispose();
        loseScreen.dispose();
        winScreen.dispose();
    }
    
    public boolean isLoaded() {
        if(!loaded&&assets.update())
            doneLoading();
        return loaded;
    }

    public Music getStageMusic() {
        return stageMusic;
    }

    public Music getSoundTop0() {
        return soundTop0;
    }

    public Music getSoundTop1() {
        return soundTop1;
    }

    public Music getRainSound() {
        return rainSound;
    }

    public Music getSnowSound() {
        return snowSound;
    }

    public YouLose getLoseScreen() {
        return loseScreen;
    }

    public YouWin getWinScreen() {
        return winScreen;
    }

    public Music getPalmas() {
        return palmas;
    }

    public Texture getGameOverText() {
        return gameOverText;
    }

    public Texture getFloorText() {
        return floorText;
    }

    public Texture getArrivalText() {
        return arrivalText;
    }

    public Texture getSpeedUpText() {
        return speedUpText;
    }

    public Texture getBoostSprSheet() {
        return boostSprSheet;
    }

    public Texture[] getBridges() {
        return bridges;
    }

    public Model getGrassModel() {
        return grassModel;
    }

    public Model getTopModel() {
        return topModel;
    }

    public Model[] getCones() {
        return cones;
    }

    public Texture getLifeText() {
        return lifeText;
    }

    public Texture getVelocimeterText() {
        return velocimeterText;
    }

    public Texture getVelocimeterPointerText() {
        return velocimeterPointerText;
    }

    public Texture getSemaphoreText() {
        return semaphoreText;
    }

    public Texture getSkyText() {
        return skyText;
    }

    public BitmapFont getHudFontSmall() {
        return hudFontSmall;
    }

    public BitmapFont getHudFontBig() {
        return hudFontBig;
    }

    public ParticleEffect getRainPart() {
        return rainPart;
    }

    public ParticleEffect getSnowPart() {
        return snowPart;
    }

    public ParticleEffect getSunnyPart() {
        return sunnyPart;
    }
    
    
}
