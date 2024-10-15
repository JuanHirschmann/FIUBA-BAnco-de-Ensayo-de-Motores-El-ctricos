

import javax.swing.JPanel;

import Controller.Controller;
import Model.Model;
import Views.Views;
public class App {

    public static void main(String[] args) {
        Model model = new Model();
        Views view = new Views();
        Controller controller=new Controller(model, view);
        
        JPanel controlPanel = view.getControlPanel();
        controller.setupPanel(controlPanel);
        view.setupControlPanel();
        view.display();
    }

}

class MainPanel {
    public MainPanel() {
        // super(new GridLayout(3,1));

        /*
         * Controller control = new Controller(model, view);
         * JPanel controlPanel = control.createPanel();
         * view.setupControlPanel(controlPanel);
         * this.add(controlPanel);
         * this.add(view);
         */

    }
}