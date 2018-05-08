package libgdxUtils;

import com.badlogic.gdx.graphics.g2d.Animation;
import java.util.Map;

/**
 *
 * coutinho
 */
public class MultiAnimatedSprite extends AnimatedSprite {

    private final Map<String, Animation> animations;

    public MultiAnimatedSprite(Map<String, Animation> animations,
            String initialAnimationName) {
        super(animations.get(initialAnimationName));
        this.animations = animations;
    }

    public void startAnimation(String animationName) {
        if (!animations.containsKey(animationName)) {
            throw new IllegalArgumentException(
                    "Pediu-se para iniciar uma animação com o nome + '"
                    + animationName + "', mas esta MultiAnimatedSprite"
                    + "não possui uma animação com esse nome");
        }

        //Configurando o time que define o frame de ínicio da animação para zero by Bruno e Carlos 
        super.setTime(0);

        super.setAnimation(animations.get(animationName));
    }
    
    public Animation getAnimation(String nomeAnimacao){
        return animations.get(nomeAnimacao);
    }
}
