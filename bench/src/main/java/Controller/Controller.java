package Controller;

import javax.swing.JButton;
import javax.swing.JPanel;

import Model.Constants.commands;
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
import static Model.Constants.MEASURED_SIMULATOR_CURRENT;
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
    ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
    ScheduledExecutorService timer1 = Executors.newScheduledThreadPool(1);
    ScheduledExecutorService timer2 = Executors.newScheduledThreadPool(1);
    ScheduledExecutorService timer3 = Executors.newScheduledThreadPool(1);
    ScheduledExecutorService timer4 = Executors.newScheduledThreadPool(1);

    // TODO: Ejecutar con un temporizador por variable, considerar un tiempo de
    // muestra de 60ms.
    // TODO: Agregar el setpoint de torque
    private class updateMeasurements implements Runnable {
        private String measurement;
        private long timestamp;
        private long measurement_start_time = 0;
        private commands command;
        updateMeasurements(commands command) {
            this.command=command;
        };

        public void run() {
            // Esto termina tienendo un lag de 200ms o mas
            Random rand = new Random();
            if (measurement_start_time == 0) {
                this.measurement_start_time = System.currentTimeMillis();
            }
            this.timestamp = System.currentTimeMillis() - measurement_start_time;
            //System.out.println(timestamp);
            try {
                this.measurement = model.readVar(command.varPath, command.varName);

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

                this.measurement = String.valueOf((timestamp / 1e4) + rand.nextFloat());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException exception) {
                    System.out.println("nit");
                } // Simula demora en leer datos

            }
            view.updateMeasurements(timestamp,this.measurement,command.seriesName);
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
                timer1.shutdown();
                timer2.shutdown();
                timer3.shutdown();
                timer4.shutdown();
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
                startButton.setText(PAUSE_BUTTON_LABEL);
                //Estos retardos tienen algo que ver con la desaparición de trazos en el gráfico en tiempo real
                timer.scheduleAtFixedRate(new updateMeasurements(commands.TORQUE), 0, 100, TimeUnit.MILLISECONDS);
                timer1.scheduleAtFixedRate(new updateMeasurements(commands.VOLTAGE), 8, 100, TimeUnit.MILLISECONDS);
                timer2.scheduleAtFixedRate(new updateMeasurements(commands.CURRENT), 16, 100, TimeUnit.MILLISECONDS);
                timer3.scheduleAtFixedRate(new updateMeasurements(commands.POWER), 32, 100, TimeUnit.MILLISECONDS);
                timer4.scheduleAtFixedRate(new updateMeasurements(commands.SPEED), 64, 100, TimeUnit.MILLISECONDS);
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