package libgdxUtils;

/**
 *
 * @author reysguep
 */
public class AnimationCode {
    public static final String DYING = "dying";
    public static final String ATTACKING = "attacking";
    public static final String IDLE = "idle";
    
    public static String getAnimationCode(String animation){
        if(animation.equalsIgnoreCase(DYING)){
            return DYING;
        }
        else if(animation.equalsIgnoreCase(ATTACKING)){
            return ATTACKING;
        }
        else if(animation.equalsIgnoreCase(IDLE)){
            return IDLE;
        }
        else{
            System.err.println("Animação sem código!");
            Runtime.getRuntime().exit(-1);
            return "ERROR";
        }
    }
}
