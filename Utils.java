import javax.swing.*;
import java.io.File;

public class Utils {


    private static String lastDir = ".";

    public static JFileChooser getFileChooser() {
        if(lastDir != null) {
            JFileChooser fc = new JFileChooser(lastDir);
            return fc;
        } else {
            JFileChooser fc = new JFileChooser();
            return fc;
        }
    }

    public static void setLastDir(File file) {
        lastDir = file.getParent();
    }

    public static String getLastDir() {
        return lastDir;
    }


}