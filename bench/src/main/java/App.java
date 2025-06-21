import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;

import Controller.Controller;
import Model.Model;
import Views.Views;

public class App {

    public static void main(String[] args) {
        File lockFile = new File("lockfile/SCDin.lock");
        if (lockFile.exists() && !lockFile.isDirectory()) {
            JOptionPane.showMessageDialog(null,
                    "Otra instancia de SCDin se está ejecutando. Por favor, finalice el proceso para poder abrir una nueva instancia. ");
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
