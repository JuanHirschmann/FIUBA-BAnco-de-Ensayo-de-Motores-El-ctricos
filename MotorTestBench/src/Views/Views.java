package Views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Views {
    JFrame frame = new JFrame("Banco de ensayo - FIUBA");
    JPanel mainPanel= new JPanel(new BorderLayout());
    JPanel controlPanel = new JPanel(new GridLayout());
    JPanel inputPanel = new JPanel(new FlowLayout());
    JButton buttonPause = new JButton("Pausa");
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
    JLabel varValueLabel = new JLabel("");
    JLabel targetVarNameLabel = new JLabel("VarName");
    JTextField targetVarNameInput = new JTextField(10);
    JLabel targetVarPathLabel = new JLabel("VarPath");
    JTextField targetVarPathInput = new JTextField(10);
    JLabel targetVarValueLabel = new JLabel("VarValue");
    JTextField targetVarValueInput = new JTextField(10);
    // CardLayout cardLayout = new CardLayout();

    public void display() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setup();;
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        mainPanel.add(inputPanel,BorderLayout.CENTER);
        frame.add(mainPanel);
    }

    public Views() {
        // super(new GridLayout(3,4));
        

    }
    public JPanel getControlPanel()
    {
        return controlPanel;
    }
    public void setupControlPanel() {
        mainPanel.add(controlPanel,BorderLayout.NORTH);
        controlPanel.add(buttonPause);
        controlPanel.add(targetIPLabel);
        controlPanel.add(targetIPInput);
        controlPanel.add(errorMsgLabel);
        frame.pack();
    }

    private void setup() {
        inputPanel.add(speedTimeFunctionLabel);
        inputPanel.add(speedTimeFunctionInput);
        inputPanel.add(torqueTimeFunctionLabel);
        inputPanel.add(torqueTimeFunctionInput);
        inputPanel.add(torqueSpeedFunctionLabel);
        inputPanel.add(torqueSpeedFunctionInput);
        inputPanel.add(startTimeLabel);
        inputPanel.add(startTimeInput);
        inputPanel.add(stopTimeLabel);
        inputPanel.add(stopTimeInput);
        inputPanel.add(targetVarNameLabel);
        inputPanel.add(targetVarNameInput);
        inputPanel.add(targetVarPathLabel);
        inputPanel.add(targetVarPathInput);
        inputPanel.add(targetVarValueLabel);
        inputPanel.add(varValueLabel);
        inputPanel.add(targetVarValueInput);
        inputPanel.setBackground(Color.GRAY);
        targetIPInput.setText("http://192.168.214.1/soap/opcxml/");

    }

    public String getTargetIP() {
        System.err.println(this.targetIPInput.getText());
        return this.targetIPInput.getText();
    }

    public String getTargetVarName() {
        System.err.println(this.targetVarPathInput.getText());
        return this.targetVarPathInput.getText();
    }

    public String getTargetVarPath() {
        System.err.println(this.targetVarNameInput.getText());
        return this.targetVarNameInput.getText();
    }
    public String getTargetVarValue() {
        System.err.println(this.targetVarValueInput.getText());
        return this.targetVarValueInput.getText();
    }

    public void setVarValue(String value) {
        this.varValueLabel.setText(value);
    }

    public void alert(String message) {
        errorMsgLabel.setText(message);
    }
}
