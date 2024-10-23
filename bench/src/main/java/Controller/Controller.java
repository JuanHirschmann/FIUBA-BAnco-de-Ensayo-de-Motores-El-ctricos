package Controller;

import javax.swing.JButton;
import javax.swing.JPanel;

import Model.Model;
import Views.Views;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.net.ConnectException;
import java.util.Random;

import static Model.Constants.CONNECT_BUTTON_LABEL;
import static Model.Constants.EMERGENCY_STOP_BUTTON_LABEL;
import static Model.Constants.MEASURED_SIMULATOR_SPEED;
import static Model.Constants.MEASURED_SIMULATOR_TORQUE;
import static Model.Constants.MEASURED_SIMULATOR_VOLTAGE;
import static Model.Constants.PAUSE_BUTTON_LABEL;
import static Model.Constants.POWER_ON_BUTTON_LABEL;
import static Model.Constants.SHUTDOWN_BUTTON_LABEL;
import static Model.Constants.START_BUTTON_LABEL;
import static Model.Constants.VAR_PATH;
import static Model.Constants.MEASURED_SIMULATOR_POWER;

import java.lang.Math;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    private Views view;
    private Model model;
    private JButton powerOnButton = new JButton(POWER_ON_BUTTON_LABEL);// Activa el modulo activo de linea y el eje
    private JButton emergencyButton = new JButton(EMERGENCY_STOP_BUTTON_LABEL); // Freno de emergencia
    private JButton buttonConnect = new JButton(CONNECT_BUTTON_LABEL);// Conecta a la IP objetivo
    private JButton startButton = new JButton(START_BUTTON_LABEL);// Arranca el ensayo TODO
    private JButton shutdownButton = new JButton(SHUTDOWN_BUTTON_LABEL);

    String torqueMeasurement;
    String speedMeasurement;
    String voltageMeasurement;
    String currentMeasurement;
    String powerMeasurement;
    long timestamp;
    private long measurement_start_time = 0;
    ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);

    private class updateMeasurements implements Runnable {

        public void run() {
            Random rand = new Random();
            if (measurement_start_time == 0) {
                measurement_start_time = System.currentTimeMillis();
            }
            timestamp = System.currentTimeMillis() - measurement_start_time;
            try {
                // TODO Buffer interno en PLC, PLC procesa buffer externo, avisa cuando termino
                // y le tiro los datos frescos.
                // TODO implementar un keepalive timer y handles para desconexión del lado del
                // plc.
                // TODO implementar readcyclic ver algo más rápido.
                // TODO un thread por medición?.
                System.err.println("act");
                torqueMeasurement = model.readVar(VAR_PATH, MEASURED_SIMULATOR_TORQUE);
                speedMeasurement = model.readVar(VAR_PATH, MEASURED_SIMULATOR_SPEED);
                voltageMeasurement = model.readVar(VAR_PATH, MEASURED_SIMULATOR_VOLTAGE);
                speedMeasurement = model.readVar(VAR_PATH, MEASURED_SIMULATOR_SPEED);
                powerMeasurement = model.readVar(VAR_PATH, MEASURED_SIMULATOR_POWER);

            } catch (Exception e) {
                /*
                 * view.updateMeasurements(String.valueOf(rand.nextFloat()),
                 * String.valueOf(rand.nextFloat()),
                 * String.valueOf(rand.nextFloat()),
                 * String.valueOf(rand.nextFloat()),
                 * String.valueOf(rand.nextFloat()));
                 */
                /*
                 * measured_simulator_torque[index] = "--";
                 * measured_simulator_speed[index] = "--";
                 * measured_simulator_voltage[index] = "--";
                 * measured_simulator_current[index] = "--";
                 * measured_simulator_power[index] = "--";
                 */

                torqueMeasurement = String.valueOf(timestamp / 1e4 + rand.nextFloat());
                speedMeasurement = String.valueOf(20 * Math.cos(50.0 * timestamp / 1e3) + rand.nextFloat());
                voltageMeasurement = String.valueOf(10 - 10 * Math.exp(-timestamp / 1e3 + rand.nextFloat()));
                speedMeasurement = String
                        .valueOf(5 * Math.cos(10.0 * timestamp / 1e4) - 20 * Math.exp(-timestamp / 1e3));
                powerMeasurement = String.valueOf(timestamp / 1e4);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException exception) {
                    System.out.println("got interrupted!");
                } // Simula demora en leer datos

            }
            view.updateMeasurements(timestamp,
                    torqueMeasurement,
                    speedMeasurement,
                    voltageMeasurement,
                    speedMeasurement,
                    powerMeasurement);
            // model.
            // readVar(ENABLE_SIMULATOR_AXIS, OPERATION_MODE);
        }

    }

    public Controller(Model model, Views view) {
        this.model = model;
        this.view = view;

    }

    /**
     * modifies panel to account for controller-specific
     * flow control
     * 
     * @return JPanel panel to modify
     */
    public void setupPanel(JPanel panel) {
        // JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(0, 3));
        topPanel.add(buttonConnect);
        topPanel.add(powerOnButton);
        topPanel.add(startButton);
        topPanel.add(emergencyButton);
        topPanel.add(shutdownButton);
        // TODO AGREGAR LOGICA DE EMG RELEASE
        // TODO AGREGAR RESET
        // TODO UNIFICAR EL ENCENDIDO
        //
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 10;
        constraints.gridheight = 10;
        panel.add(topPanel, constraints);

        buttonConnect.addActionListener(new ButtonHandler());
        startButton.addActionListener(new ButtonHandler());
        emergencyButton.addActionListener(new ButtonHandler());
        powerOnButton.addActionListener(new ButtonHandler());
        shutdownButton.addActionListener(new ButtonHandler());
        startButton.addActionListener(new ButtonHandler());

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
                    Thread.sleep(100);
                    model.controllerOn();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());
                }
            } else if (SHUTDOWN_BUTTON_LABEL.equals(cmd)) {
                timer.shutdown();
                System.err.println("estoy en apagar");
                try {

                    model.powerOff();
                    // TODO: Necesita un retardo?
                    model.controllerOff();
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
            } else if (POWER_ON_BUTTON_LABEL.equals(cmd)) {

                System.err.println("estoy en potencia");
                try {

                    model.powerOn();

                } catch (ConnectException e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());
                }
            } else if (START_BUTTON_LABEL.equals(cmd)) {
                System.err.println("estoy en iniciar");
                timer.scheduleAtFixedRate(new updateMeasurements(), 0, 100, TimeUnit.MILLISECONDS);
                startButton.setText(PAUSE_BUTTON_LABEL);
                try {
                    model.start();
                } catch (Exception e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());
                }
                // TODO Agregar lógica de inicio de ensayo
            } else if (PAUSE_BUTTON_LABEL.equals(cmd)) {
                startButton.setText(START_BUTTON_LABEL);
                System.err.println("estoy en pausa");
                try {
                    model.stop();
                } catch (Exception e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    view.alert(e.getMessage());
                }
                // TODO Agregar lógica de inicio de ensayo

            }

        }
    }
}