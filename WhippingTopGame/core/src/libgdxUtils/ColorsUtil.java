package libgdxUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author reysguep
 */
public class ColorsUtil {
    public static final Texture RED = new Texture(Gdx.files.internal("Colors/red.png"));
    public static final Texture BLUE = new Texture(Gdx.files.internal("Colors/blue.png"));
    //public static final Texture BLACK = new Texture(Gdx.files.internal("Colors/black.png"));
    public static final Texture GREEN = new Texture(Gdx.files.internal("Colors/green.png"));
    
    public static void dispose(){
        RED.dispose();
        BLUE.dispose();
        GREEN.dispose();
       //BLACK.dispose();
    }
}
