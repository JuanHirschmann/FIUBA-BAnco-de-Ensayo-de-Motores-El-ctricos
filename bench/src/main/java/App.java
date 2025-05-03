import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;

import Controller.Controller;
import Model.Model;
import Views.Views;

public class App {
 
    //TODO: MODO DUT
    //TODO: AGREGAR ALARMAS 
    //TODO: SI EMERGENCY_STOP ENTONCES VOLVER AL ESTADO ANTERIOR, SALVO RUNNING
    //TODO: VELOCIDAD*-1
    //TODO: ALERTA ANTES DE INICIAR
    /** 
     * @param args
     */
    public static void main(String[] args) {
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

