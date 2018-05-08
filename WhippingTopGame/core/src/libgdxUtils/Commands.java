package libgdxUtils;

/**
 *
 * @author reysguep
 */
public class Commands {
    public static void executeCommand(String command){
        String[] parts = command.split(" ");
        
        
    }
    
    private static void summon(String[] parts){
        if(parts.length != 4) {
            System.err.println("O comando está incompleto!");
        }
    }
}
