package Controller;

import javax.swing.JButton;
import javax.swing.JPanel;

import Model.Model;
import Views.Views;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import static Model.Constants.CONNECT_BUTTON_LABEL;
import static Model.Constants.CONTROLLER_START_BUTTON_LABEL;
import static Model.Constants.EMERGENCY_STOP_BUTTON_LABEL;
import static Model.Constants.MEASURED_SIMULATOR_SPEED;
import static Model.Constants.MEASURED_SIMULATOR_TORQUE;
import static Model.Constants.PAUSE_BUTTON_LABEL;
import static Model.Constants.POWER_ON_BUTTON_LABEL;
import static Model.Constants.READ_VARIABLE_BUTTON_LABEL;
import static Model.Constants.START_BUTTON_LABEL;
import static Model.Constants.VAR_PATH;
import static Model.Constants.WRITE_VARIABLE_BUTTON_LABEL;
import static Model.Constants.GRAPH_UPDATE_RATIO;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    private Views view;
    private Model model;
    private JButton controllerStart = new JButton(CONTROLLER_START_BUTTON_LABEL); // Pone el control en RUN
    private JButton powerOnButton = new JButton(POWER_ON_BUTTON_LABEL);// Activa el modulo activo de linea y el eje
    private JButton emergencyButton = new JButton(EMERGENCY_STOP_BUTTON_LABEL); // Freno de emergencia
    private JButton buttonConnect = new JButton(CONNECT_BUTTON_LABEL);// Conecta a la IP objetivo
    private JButton buttonRead = new JButton(READ_VARIABLE_BUTTON_LABEL); // Lee una variable
    private JButton buttonWrite = new JButton(WRITE_VARIABLE_BUTTON_LABEL);// Escribe una variable
    private JButton pauseButton = new JButton(PAUSE_BUTTON_LABEL);// Detiene el ensayo TODO
    private JButton startButton = new JButton(START_BUTTON_LABEL);// Arranca el ensayo TODO

    private ArrayList<ArrayList<String>> measurements = new ArrayList<ArrayList<String>>();
    private Timer variable_poll_timer = new Timer();
    private String[] measured_simulator_torque = new String[GRAPH_UPDATE_RATIO];
    private String[] measured_simulator_speed = new String[GRAPH_UPDATE_RATIO];
    private String[] measured_simulator_voltage = new String[GRAPH_UPDATE_RATIO];
    private String[] measured_simulator_current = new String[GRAPH_UPDATE_RATIO];
    private String[] measured_simulator_power = new String[GRAPH_UPDATE_RATIO];
    private long[] timestamp = new long[GRAPH_UPDATE_RATIO];
    private int index = 0;

    private class updateMeasurements implements Runnable {

        public void run() {
            Random rand = new Random();
            
            try {

                measured_simulator_torque[index] = model.readVar(VAR_PATH, MEASURED_SIMULATOR_TORQUE);
                measured_simulator_speed[index] = model.readVar(VAR_PATH, MEASURED_SIMULATOR_SPEED);
                measured_simulator_voltage[index] = String.valueOf(0);
                measured_simulator_current[index] = String.valueOf(0);
                measured_simulator_power[index] = String.valueOf(0);
                timestamp[index]=System.currentTimeMillis();

            } catch (Exception e) {
                /*
                    * view.updateMeasurements(String.valueOf(rand.nextFloat()),
                    * String.valueOf(rand.nextFloat()),
                    * String.valueOf(rand.nextFloat()),
                    * String.valueOf(rand.nextFloat()),
                    * String.valueOf(rand.nextFloat()));
                    */
                /* measured_simulator_torque[index] = "--";
                measured_simulator_speed[index] = "--";
                measured_simulator_voltage[index] = "--";
                measured_simulator_current[index] = "--";
                measured_simulator_power[index] = "--"; */

                measured_simulator_torque[index] = String.valueOf(rand.nextFloat()+1);
                measured_simulator_speed[index] = String.valueOf(rand.nextFloat()+2);
                measured_simulator_voltage[index] = String.valueOf(rand.nextFloat()+3);
                measured_simulator_current[index] = String.valueOf(rand.nextFloat()-1);
                measured_simulator_power[index] = String.valueOf(rand.nextFloat()-2);
                timestamp[index]=System.currentTimeMillis();
            }
            index++;
            if (index>GRAPH_UPDATE_RATIO-1) {
                view.updateGraphMeasurements(timestamp,
                    measured_simulator_torque,
                    measured_simulator_speed,
                    measured_simulator_current,
                    measured_simulator_voltage,
                    measured_simulator_power);
                index=0;
            }
            
            // model.
            // readVar(ENABLE_SIMULATOR_AXIS, OPERATION_MODE);
        }

    }

    ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);

    public Controller(Model model, Views view) {
        this.model = model;
        this.view = view;

    }

    public void start() {
        // this.view.launchMainWindow();
        // System.err.println(model);

    }

    /**
     * modifies panel to account for controller-specific
     * flow control
     * 
     * @return JPanel panel to modify
     */
    public JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buttonConnect);
        panel.add(buttonRead);
        panel.add(pauseButton);
        panel.add(powerOnButton);
        panel.add(startButton);
        panel.add(buttonWrite);
        panel.add(controllerStart);
        panel.add(emergencyButton);

        buttonConnect.addActionListener(new ButtonHandler());
        buttonRead.addActionListener(new ButtonHandler());
        buttonWrite.addActionListener(new ButtonHandler());
        pauseButton.addActionListener(new ButtonHandler());
        startButton.addActionListener(new ButtonHandler());
        controllerStart.addActionListener(new ButtonHandler());
        emergencyButton.addActionListener(new ButtonHandler());
        powerOnButton.addActionListener(new ButtonHandler());

        return panel;
    }

    /**
     * @param panel
     */
    public void setupPanel(JPanel panel) {
        // JPanel panel = new JPanel(new BorderLayout());
        panel.add(startButton);
        panel.add(pauseButton);
        panel.add(buttonRead);
        panel.add(buttonWrite);
        panel.add(buttonConnect);
        panel.add(powerOnButton);
        panel.add(controllerStart);
        panel.add(emergencyButton);

        buttonConnect.addActionListener(new ButtonHandler());
        buttonRead.addActionListener(new ButtonHandler());
        buttonWrite.addActionListener(new ButtonHandler());
        pauseButton.addActionListener(new ButtonHandler());
        startButton.addActionListener(new ButtonHandler());
        controllerStart.addActionListener(new ButtonHandler());
        emergencyButton.addActionListener(new ButtonHandler());
        powerOnButton.addActionListener(new ButtonHandler());

    }

    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String cmd = event.getActionCommand();
            System.err.println(cmd);
            if (CONNECT_BUTTON_LABEL.equals(cmd)) {
                String url = view.getTargetIP();
                System.err.println(url);
                try {
                    model.connect(url);
                    model.controllerOn();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());
                }
            } else if (READ_VARIABLE_BUTTON_LABEL.equals(cmd)) {
                String varName = view.getTargetVarName();
                String varPath = view.getTargetVarPath();
                System.err.println("estoy en leer");
                try {

                    String varVal = model.readVar(varPath, varName);
                    System.err.println(varVal);
                } catch (ConnectException e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());

                }
            } else if (WRITE_VARIABLE_BUTTON_LABEL.equals(cmd)) {
                String varValue = view.getTargetVarValue();
                System.err.println("estoy en escribir");
                String varName = view.getTargetVarName();
                String varPath = view.getTargetVarPath();
                try {

                    model.writeVar(varValue, varPath, varName);
                } catch (ConnectException e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());

                }
            } else if (PAUSE_BUTTON_LABEL.equals(cmd)) {
                System.err.println("estoy en detener");
                try {

                    model.emergencyStop();

                } catch (ConnectException e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());
                }
            } else if (EMERGENCY_STOP_BUTTON_LABEL.equals(cmd)) {
                System.err.println("estoy en EMG Stop");
                try {

                    model.emergencyStop();

                } catch (ConnectException e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());
                }
            } else if (CONTROLLER_START_BUTTON_LABEL.equals(cmd)) {
                System.err.println("estoy en iniciar");
                try {

                    model.controllerOn();

                } catch (ConnectException e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());

                }
            } else if (POWER_ON_BUTTON_LABEL.equals(cmd)) {

                // variable_poll_timer.schedule(new UpdateMeasurements(), 0, 1000);
                timer.scheduleAtFixedRate(new updateMeasurements(), 0, 50, TimeUnit.MILLISECONDS);
                System.err.println("estoy en potencia");
                try {

                    model.powerOn();

                } catch (ConnectException e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());
                }
            }

        }
    }
}