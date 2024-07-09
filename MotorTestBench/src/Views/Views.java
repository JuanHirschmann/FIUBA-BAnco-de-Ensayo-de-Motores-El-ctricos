package Views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Views extends JPanel {

    JFrame frame = new JFrame("Banco de ensayo - FIUBA");
    JPanel panelContainer = new JPanel();
    JPanel panelFirst = new JPanel();
    JPanel panelSecond = new JPanel();
    //JButton buttonOne = new JButton("Switch to second panel/workspace");
    //JButton buttonSecond = new JButton("Switch to first panel/workspace");
    JButton buttonStart = new JButton("Arranque");
    JButton buttonStop = new JButton("Frenado");
    JButton buttonPause = new JButton("Pausa");
    // JButton buttonConnect = new JButton("Conectar");
    JLabel targetIPLabel = new JLabel("IP objetivo:");
    JTextField targetIPInput = new JTextField(20);
    JLabel speedTimeFunctionLabel = new JLabel("Función de velocidad en función del tiempo:");
    JTextField speedTimeFunctionInput = new JTextField(20);
    JLabel torqueTimeFunctionLabel = new JLabel("Función de cupla en función del tiempo:");
    JTextField torqueTimeFunctionInput = new JTextField(20);
    JLabel torqueSpeedFunctionLabel = new JLabel("Función de velocidad en función de la cupla:");
    JTextField torqueSpeedFunctionInput = new JTextField(20);
    JLabel startTimeLabel = new JLabel("Tiempo de inicio [s]:");
    JTextField startTimeInput = new JTextField(10);
    JLabel stopTimeLabel = new JLabel("Tiempo de fin [s]:");
    JTextField stopTimeInput = new JTextField(10);
    JLabel errorMsgLabel = new JLabel("");
    JLabel varValueLabel= new JLabel("");
    JTextField targetVarNameInput = new JTextField(10);

    JTextField targetVarPathInput = new JTextField(10);
    // CardLayout cardLayout = new CardLayout();
    

    public Views() {
        super(new BorderLayout());

    }

    public JPanel setup(JPanel panel) {
        // panelContainer.setLayout(cardLayout);

        //panel.add(buttonOne);
        panel.add(buttonStart);
        panel.add(buttonPause);
        panel.add(buttonStop);
        panel.add(speedTimeFunctionLabel);
        panel.add(speedTimeFunctionInput);
        panel.add(torqueTimeFunctionLabel);
        panel.add(torqueTimeFunctionInput);
        panel.add(torqueSpeedFunctionLabel);
        panel.add(torqueSpeedFunctionInput);
        panel.add(startTimeLabel);
        panel.add(startTimeInput);
        panel.add(stopTimeLabel);
        panel.add(stopTimeInput);
        panel.add(targetIPLabel);
        panel.add(targetIPInput);
        panel.add(targetVarNameInput);
        panel.add(targetVarPathInput);
        panel.add(errorMsgLabel);
        panel.add(varValueLabel);
        panel.setBackground(Color.GRAY);
        // panelSecond.add(buttonSecond);
        // panelSecond.setBackground(Color.GREEN);

        // panelContainer.add(panelFirst, "1");
        // panelContainer.add(panelSecond, "2");
        // cardLayout.show(panelContainer, "1");

        /*
         * buttonOne.addActionListener(new ActionListener() {
         * 
         * @Override
         * public void actionPerformed(ActionEvent arg0) {
         * //cardLayout.show(panelContainer, "2");
         * }
         * });
         */

        /*
         * buttonSecond.addActionListener(new ActionListener() {
         * 
         * @Override
         * public void actionPerformed(ActionEvent arg0) {
         * cardLayout.show(panelContainer, "1");
         * }
         * });
         */

        // frame.add(panelContainer);
        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // frame.pack();
        // frame.setVisible(true);
        // this.setLayout();
        return panel;
    }

    

    public String getTargetIP() {
        System.err.println(this.targetIPInput.getText());
        return this.targetIPInput.getText();
    }

    public String getTargetVarName() {
        System.err.println(this.targetIPInput.getText());
        return this.targetVarPathInput.getText();
    }

    public String getTargetVarPath() {
        System.err.println(this.targetIPInput.getText());
        return this.targetVarNameInput.getText();
    }
    public void setVarValue(String value)
    {
        this.varValueLabel.setText(value);
    }
    public void alert(String message) {
        errorMsgLabel.setText(message);
    }
}
