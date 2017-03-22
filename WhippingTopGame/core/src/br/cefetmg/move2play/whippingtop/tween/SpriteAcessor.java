package br.cefetmg.move2play.whippingtop.tween;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteAcessor implements TweenAccessor<Sprite>{

    public static final int ALPHA=666;
    
    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues) {
        switch(tweenType){
            case ALPHA:
                returnValues[0]=target.getColor().a;
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues) {
        switch(tweenType){
            case ALPHA:
                target.setAlpha(newValues[0]);
                break;
            default:
                assert false;
        }
    }
    
}
