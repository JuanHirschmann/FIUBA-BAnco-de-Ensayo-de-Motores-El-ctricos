package Controller;

import javax.swing.JButton;
import javax.swing.JPanel;

import Common.TorqueEquationParameters;
import Model.Constants.commands;
import Model.Constants.testTypes;
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
    ScheduledExecutorService torqueTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService voltageTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService currentTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService powerTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService speedTimer = Executors.newScheduledThreadPool(10);

    // TODO: Agregar el TORQUE_COMMAND
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
                
                //TODO: Cuando no hay un dato usar algún tipo de promedio ponderado en vez de esto.
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
                torqueTimer.shutdown();
                voltageTimer.shutdown();
                currentTimer.shutdown();
                powerTimer.shutdown();
                speedTimer.shutdown();
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
                torqueTimer.scheduleAtFixedRate(new updateMeasurements(commands.TORQUE), 0, 200, TimeUnit.MILLISECONDS);
                voltageTimer.scheduleAtFixedRate(new updateMeasurements(commands.VOLTAGE), 0, 200, TimeUnit.MILLISECONDS);
                currentTimer.scheduleAtFixedRate(new updateMeasurements(commands.CURRENT), 0, 200, TimeUnit.MILLISECONDS);
                powerTimer.scheduleAtFixedRate(new updateMeasurements(commands.POWER), 0, 200, TimeUnit.MILLISECONDS);
                speedTimer.scheduleAtFixedRate(new updateMeasurements(commands.SPEED), 0, 200, TimeUnit.MILLISECONDS);
                //Meter estado de LEDs
                //Meter update de arrays
                try {
                    //Cargar tipo de parámetro
                    testTypes test=view.getTestType();
                    //int runtime=view.getTestRuntime();
                    if(test==testTypes.TORQUE_VS_SPEED)
                    {
                        model.selectTorqueVsSpeed();
                        model.setTorqueVsTimeParameters(view.getTorqueEquationParameters());
                    }else if(test==testTypes.TORQUE_VS_TIME)
                    {
                        model.selectTorqueVsTime();
                        //torqueTimer.scheduleAtFixedRate(new bufferCommands(model,timestamp,torque),0,500);
                        
                    }
                    //model.setRuntime(runtime);
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