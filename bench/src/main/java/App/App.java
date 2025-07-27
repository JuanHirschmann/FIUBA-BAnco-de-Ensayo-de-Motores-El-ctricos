package App;
import static Views.Constants.LOCKFILE_PATHNAME;
import static Views.Constants.SEVERAL_INSTANCE_DETECTED_MSG;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;

import Controller.Controller;
import Model.Model;
import Views.Views;

public class App {

    public static void main(String[] args) {
        File lockFile = new File(System.getenv("APPDATA")+LOCKFILE_PATHNAME);
        if (lockFile.exists() && !lockFile.isDirectory()) {
            JOptionPane.showMessageDialog(null,
                    SEVERAL_INSTANCE_DETECTED_MSG);
        } else {
            lockFile.deleteOnExit();
            try {
                lockFile.createNewFile();
            } catch (Exception e) {
                lockFile.getParentFile().mkdirs();
            }
            Model model = new Model();
            try {
                UIManager.setLookAndFeel(new FlatCyanLightIJTheme());
            } catch (Exception ex) {
                System.err.println("Failed to initialize LaF");
            }
            Views view = new Views();
            Controller controller = new Controller(model, view);
            view.setController(controller);
        }

    }

}
