package br.cefetmg.move2play.whippingtop.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class SpriteCreator {
    
    public static Sprite createCircle(int radius,Vector2 size, Color c){
        Sprite out;
        Pixmap pixmap = new Pixmap((int)size.x, (int)size.y, Pixmap.Format.RGBA8888);
        pixmap.setColor(c);
        pixmap.fillCircle((int)size.x/2, (int)size.y/2, radius);
        out=new Sprite(new Texture(pixmap));
        pixmap.dispose();
        return out;
    }
}
