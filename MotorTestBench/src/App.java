import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.View;

import Controller.Controller;
import Model.Model;
import Views.Views;

public class App {
   
    public static void main(String[] args) {

        EventQueue.invokeLater(new App()::display);
    }

    public void display() {
        JFrame frame = new JFrame("Banco de ensayo - FIUBA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MainPanel());
        frame.pack();
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}

class MainPanel extends JPanel {
    public MainPanel() {
        super(new BorderLayout());
        Model model = new Model();
        Views view = new Views();
        Controller control = new Controller(model, view);
        JPanel panel = control.createPanel();
        this.add(panel);
        this.add(view.setup(panel));

    }
}