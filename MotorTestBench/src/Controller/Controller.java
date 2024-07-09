package Controller;

import javax.swing.JButton;
import javax.swing.JPanel;

import Model.Model;
import Views.Views;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;

public class Controller {
    private Views view;
    private Model model;
    private JButton buttonConnect = new JButton("Conectar");
    private JButton buttonRead = new JButton("Leer Variable");
    private JButton buttonWrite = new JButton("Escribir Variable");

    public Controller(Model model, Views view) {
        this.model = model;
        this.view = view;

    }

    public void start() {
        // this.view.launchMainWindow();
        // System.err.println(model);

    }

    
    /** modifies panel to account for controller-specific 
     * flow control 
     * @return JPanel panel to modify
     */
    public JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.add(buttonConnect);
        panel.add(buttonRead);
        buttonConnect.addActionListener(new ButtonHandler());
        
        panel.add(buttonRead);
        buttonRead.addActionListener(new ButtonHandler());
        
        panel.add(buttonWrite);
        buttonWrite.addActionListener(new ButtonHandler());

        return panel;
    }

    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String cmd = event.getActionCommand();
            System.err.println(cmd);
            if ("Conectar".equals(cmd)) {
                String url = view.getTargetIP();
                System.err.println(url);
                try {
                    model.connect(url);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            else if("Leer Variable".equals(cmd))
            {
                String varName =view.getTargetVarName();
                String varPath=view.getTargetVarPath();
                System.err.println("estoy en leer");
                try{

                    String varVal=model.readVar(varPath,varName);
                    System.err.println(varVal);
                } catch (ConnectException e)
                {
                    System.err.println("Tire error");
                    
                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());

                }
            }
            else if("Escribir Variable".equals(cmd))
            {
                String varValue="1";
                System.err.println("estoy en escribir");
                String varName =view.getTargetVarName();
                String varPath=view.getTargetVarPath();
                try{

                    model.writeVar(varValue,varPath,varName);
                } catch (ConnectException e)
                {
                    System.err.println("Tire error");
                    
                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());

                }
            }
        }
    }
}