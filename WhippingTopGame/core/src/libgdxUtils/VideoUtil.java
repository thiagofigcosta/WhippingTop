package libgdxUtils;

import br.cefetmg.move2play.whippingtop.util.files.Textures;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.io.File;

/**
 *
 * @author reysguep
 */
public class VideoUtil {

    public static Animation<TextureRegion> imageSequenceToAnimation(String url, int fps, int nimages, String ext) {
        float ffps = fps;
        float frameDuration = 1 / ffps;
        AssetManager assets=new AssetManager();
        for (int i = 0; i < nimages; i++) {
            assets.load(url+"/"+i+ext, Texture.class);
        }
        TextureRegion[] frames = new TextureRegion[nimages];  
        while(!assets.update());
        for (int i = 0; i < nimages; i++) {
            frames[i]=new TextureRegion(assets.get(url+"/"+i+ext, Texture.class)); 
        }
        return new Animation<TextureRegion>(frameDuration, frames);
    }
}
