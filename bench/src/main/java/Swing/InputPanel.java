package Swing;

import static Views.Constants.BROWSE_FILE_BUTTON_LABEL;
import static Views.Constants.CONNECT_BUTTON_LABEL;
import static Views.Constants.DEFAULT_SERVER_ADDRESS;
import static Views.Constants.EMERGENCY_STOP_BUTTON_LABEL;
import static Views.Constants.POWER_ON_BUTTON_LABEL;
import static Views.Constants.SHUTDOWN_BUTTON_LABEL;
import static Views.Constants.START_BUTTON_LABEL;
import static Views.Constants.SET_TEST_PARAMETERS_BUTTON_LABEL;
import static Views.Constants.WRITE_CSV;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import Model.Constants.commands;
import Model.Constants.testTypes;

public class InputPanel extends JPanel {
    public JPanel statusPanel=new JPanel();
    public ControlPanel controlPanel=new ControlPanel();
    public VariablesPanel variablesPanel=new VariablesPanel();
    public UserMessagePanel userMsgPanel=new UserMessagePanel();
    //control panel
    
    public JButton powerOnButton = new JButton(POWER_ON_BUTTON_LABEL);// Activa el modulo activo de linea y el eje
    public JButton emergencyButton = new JButton(EMERGENCY_STOP_BUTTON_LABEL); // Freno de emergencia
    public JButton buttonConnect = new JButton(CONNECT_BUTTON_LABEL);// Conecta a la IP objetivo
    public JButton startButton = new JButton(START_BUTTON_LABEL);// Arranca el ensayo TODO
    public JButton shutdownButton = new JButton(SHUTDOWN_BUTTON_LABEL);
    public LabeledInput targetIP= new LabeledInput("IP objetivo: ");
    
    //Variables panel
    public LabeledInput stopTime = new LabeledInput("Tiempo de fin [s]:");
    public JTextField filename = new JTextField(40);
    public JButton setParametersButton = new JButton(SET_TEST_PARAMETERS_BUTTON_LABEL);
    public JButton saveCSVButton = new JButton(WRITE_CSV);
    public JButton openFileButton = new JButton(BROWSE_FILE_BUTTON_LABEL);
    public JComboBox<testTypes> torqueTestModeComboBox = new JComboBox<testTypes>();
    public TorqueEquation torqueEquationText = new TorqueEquation();
    public JLabel torqueEquation = new JLabel(torqueEquationText.toString());
    public TorqueEquationParameters torqueEquationParameters = new TorqueEquationParameters();
    public JLabel testPeriodLabel = new JLabel("Cantidad de periodos a ensayar");
    public SpinnerModel numericModel = new SpinnerNumberModel(1, 1, 99, 1);
    public JSpinner testPeriodsSpinner = new JSpinner(numericModel);
    
    public StatusLight powerOnIndicator=new StatusLight("ALM");
    public StatusLight connectionIndicator=new StatusLight("CONN");
    public StatusLight semaphoreIndicator=new StatusLight("STATUS");
    public StatusLight loadedTestIndicator=new StatusLight("TEST");
    
    public onScreenMeasurements displayedMeasurements = new onScreenMeasurements();

    public JLabel userMessageLabel=new JLabel();
    public  InputPanel()
    
    {
        
        statusPanel.setLayout( new GridLayout(0,1));
        controlPanel.setBackground(Color.GREEN);
        targetIP.setText(DEFAULT_SERVER_ADDRESS);
        controlPanel.add(buttonConnect);
        controlPanel.add(powerOnButton);
        controlPanel.add(startButton);
        controlPanel.add(emergencyButton);
        controlPanel.add(shutdownButton);
        targetIP.set(controlPanel);
        
        //variablesPanel
        variablesPanel.setBackground(Color.YELLOW);
        this.torqueTestModeComboBox.addItem(testTypes.TORQUE_VS_TIME);
        this.torqueTestModeComboBox.addItem(testTypes.TORQUE_VS_SPEED);
        variablesPanel.add(torqueTestModeComboBox);
        variablesPanel.add(openFileButton);
        variablesPanel.add(filename);
        variablesPanel.add(torqueEquation);
        torqueEquationParameters.setParameters(variablesPanel);
        variablesPanel.add(testPeriodLabel);
        variablesPanel.add(testPeriodsSpinner);
        stopTime.set(variablesPanel);
        variablesPanel.add(setParametersButton);
        variablesPanel.add(saveCSVButton);
        
        displayedMeasurements.setMeasurements(variablesPanel);
        //Status panel
        statusPanel.add(powerOnIndicator);
        statusPanel.add(connectionIndicator);
        statusPanel.add(semaphoreIndicator);
        statusPanel.add(loadedTestIndicator);
        //setBackground(Color.black);
        this.displayedMeasurements.addMeasuredVariable(commands.TORQUE);
        this.displayedMeasurements.addMeasuredVariable(commands.POWER);
        this.displayedMeasurements.addMeasuredVariable(commands.VOLTAGE);
        this.displayedMeasurements.addMeasuredVariable(commands.CURRENT);
        this.displayedMeasurements.addMeasuredVariable(commands.SPEED);
        this.displayedMeasurements.addMeasuredVariable(commands.TORQUE_COMMAND);
        userMsgPanel.setBackground(Color.WHITE);
        displayedMeasurements.setMeasurements(userMsgPanel);
        

        setLayout(new GridBagLayout());//(new GridLayout(2,0));
        GridBagConstraints gbc= new GridBagConstraints();
        gbc.gridx=1;
        gbc.gridy=0;
        gbc.gridheight=3;
        gbc.gridwidth=1;
        gbc.fill=GridBagConstraints.VERTICAL;
        add(statusPanel,gbc);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridheight=1;
        gbc.gridwidth=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        add(controlPanel,gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridheight=2;
        gbc.gridwidth=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        add(variablesPanel,gbc);
        
        gbc.gridx=0;
        gbc.gridy=3;
        gbc.gridheight=1;
        gbc.gridwidth=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        add(userMsgPanel,gbc);
        
    }
    private class TorqueEquation {
        String A;
        String B;
        String C;
        String D;
        String E;

        TorqueEquation() {
            this.reset();
        };

        public String toString() {
            return "<html>" + A + "+" + B + "v+" + C + "v<sup>2</sup>+" + D
                    + "<sup>dv</sup> &frasl; <sub>dt</sub></html>";
        }

        public void reset() {
            A = "A";
            B = "B";
            C = "C";
            D = "D";
            E = "E";
        }
    }
}
