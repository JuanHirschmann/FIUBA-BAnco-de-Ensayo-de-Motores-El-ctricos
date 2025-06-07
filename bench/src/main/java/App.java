import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;

import Controller.Controller;
import Model.Model;
import Views.Views;

public class App {
    //TODO: MODO DUT
    //TODO: SI EMERGENCY_STOP ENTONCES VOLVER AL ESTADO ANTERIOR, SALVO RUNNING
    //TODO: VELOCIDAD*-1
    //TODO: ENABLE DUT
    public static void main(String[] args) {
        /* try
        {
            File file = new File("SCDin.lock");
            channel= new RandomAccessFile(file,"rw").getChannel();
            FileLock lock=channel.tryLock();
             try {
            lock = channel.tryLock();
            } catch (OverlappingFileLockException e) {
                // File is already locked in this thread or virtual machine
            }
        } */
        Model model = new Model();
        try {
            UIManager.setLookAndFeel( new FlatCyanLightIJTheme() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        Views view = new Views();
        Controller controller = new Controller(model, view);
        view.setController(controller);
    }

}

