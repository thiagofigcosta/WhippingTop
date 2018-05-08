package libgdxUtils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author reysguep
 */
public class FileUtil {

    public static void writeString(String str) {
        PrintWriter out;
        try {
            out = new PrintWriter("skeleton.json");
            out.print(str);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
