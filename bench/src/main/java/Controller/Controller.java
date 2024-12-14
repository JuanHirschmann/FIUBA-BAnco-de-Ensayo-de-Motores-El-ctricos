package Controller;

import Model.Constants.commands;
import Swing.TorqueEquationParameter;
import Model.Model;
import Views.Views;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static Model.Constants.TORQUE_TIME_BUFFER_SIZE;

import java.lang.Math;
import java.net.ConnectException;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    // private ViewListener viewListener;
    private Views view;
    private Model model;

    private MeasurementBuffer measurementsBufferedValues = new MeasurementBuffer();
    private TorqueTimeValues torqueTimeValues = new TorqueTimeValues();

    ScheduledExecutorService torqueTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService voltageTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService currentTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService powerTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService speedTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService bufferTimer = Executors.newScheduledThreadPool(10);

    public Controller() {
    };

    public void setTestEndTime(String endtime_ms) throws ConnectException {
        model.setTestEndTime(endtime_ms);
    }

    public void createTorqueTimeFromCSV(String filepath) {
        this.torqueTimeValues.fromCSV(filepath);
        System.out.println(torqueTimeValues.length());
    }

    public void extendTorqueTimeValues(int periods) {
        this.torqueTimeValues.extend(periods);
    }

    public TorqueTimeValues getTorqueTimeValues() {
        // TorqueTimeValues output= new TorqueTimeValues(this.torqueTimeValues);
        // System.out.println(torqueTimeValues.length());

        return new TorqueTimeValues(this.torqueTimeValues);
    }

    public Controller(Model model, Views view) {
        this.model = model;
        this.view = view;

    }

    public void startMeasurements() {
        torqueTimer.scheduleAtFixedRate(new updateMeasurements(commands.TORQUE), 0, 200, TimeUnit.MILLISECONDS);
        voltageTimer.scheduleAtFixedRate(new updateMeasurements(commands.VOLTAGE), 50, 200,
                TimeUnit.MILLISECONDS);
        currentTimer.scheduleAtFixedRate(new updateMeasurements(commands.CURRENT), 100, 200,
                TimeUnit.MILLISECONDS);
        powerTimer.scheduleAtFixedRate(new updateMeasurements(commands.POWER), 150, 200, TimeUnit.MILLISECONDS);
        speedTimer.scheduleAtFixedRate(new updateMeasurements(commands.SPEED), 200, 200, TimeUnit.MILLISECONDS);
        // Meter estado de LEDs
        // Meter update de arrays
        /*
         * try {
         * // Cargar tipo de parámetro
         * testTypes test = view.getTestType();
         * // int runtime=view.getTestRuntime();
         * System.out.println(test);
         * if (test == testTypes.TORQUE_VS_SPEED) {
         * System.out.println("torquevsspeed");
         * model.selectTorqueVsSpeed();
         * model.setTorqueVsTimeParameters(view.getTorqueEquationParameters());
         * } else if (test == testTypes.TORQUE_VS_TIME) {
         * TorqueTimeValues torqueTimeValue = view.getTorqueTimeValues();
         * //model.selectTorqueVsTime();
         * bufferTimer.scheduleAtFixedRate(new sendTorqueCommands(model,
         * torqueTimeValue.getTimestamp(),
         * torqueTimeValue.getTimestamp()), 0, 500, TimeUnit.MILLISECONDS);
         * 
         * }
         */
        // model.setRuntime(runtime);
        // model.start();
        /*
         * } catch (Exception e) {
         * System.err.println("Tire error");
         * 
         * System.err.println(e.getMessage());
         * view.alert(e.getMessage());
         * }
         */
    }

    public void stopMeasurements() {
        torqueTimer.shutdown();
        voltageTimer.shutdown();
        currentTimer.shutdown();
        powerTimer.shutdown();
        speedTimer.shutdown();
    }

    public void start() throws ConnectException {
        if (model.isConnected()) {
            try {

                model.start();
            } catch (Exception e) {
                throw new ConnectException("El control no está conectado. Verifique la configuración IP");
            }
        } else {

            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    public MeasurementBuffer getMeasurementBuffer() {
        return measurementsBufferedValues;
    }

    public void clearMeasurementBuffer() {
        measurementsBufferedValues.clearBuffer();
    }

    public void connect(String targetIP) throws Exception {
        try {

            model.connect(targetIP);
        } catch (Exception e) {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }

    }

    public void PLCStart() throws Exception {
        if (model.isConnected()) {
            model.controllerOn();

        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }

    }

    public void PLCStop() throws Exception {
        model.controllerOff();
    }

    public void powerOn() throws Exception {
        if (model.isConnected()) {
            model.powerOn();
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }

    }

    public void powerOff() throws Exception {
        if (model.isConnected()) {
            model.powerOff();
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    public void emergencyStop() throws Exception {
        if (model.isConnected()) {
            model.emergencyStop();
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    public void emergencyRelease() throws Exception {
        if (model.isConnected()) {
            model.emergencyRelease();
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    public void selectTorqueVsTime() throws ConnectException {
        //TODO: SACAR EL !
        if (!model.isConnected()) {
            if (this.torqueTimeValues.length() == 0) {
                throw new IllegalArgumentException("Seleccione un archivo en formato CSV (tiempo[ms],torque[Nm]).");
            } else if (this.torqueTimeValues.getMaxTorque() > 30) {
                throw new IllegalArgumentException("Torque máximo excedido");
            }
            model.selectTorqueVsTime();
            model.setTestEndTime(null);
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    public void selectTorqueVsSpeed() throws ConnectException {
        if (model.isConnected()) {
            model.selectTorqueVsSpeed();
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    public void setTorqueVsSpeedParameters(Map<String, TorqueEquationParameter> parameters) throws Exception {
        //TODO: SACAR EL !
        if (!model.isConnected()) {
            if (Float.valueOf(parameters.get("D").getValue()) < 0) {
                throw new IllegalArgumentException("El valor del término inercial no puede ser negativo");
            } else if (Float.valueOf(parameters.get("D").getValue()) > 0.3) {
                throw new IllegalArgumentException("El valor del término inercial no puede ser mayor a 0.3");
            }
            model.setTorqueVsSpeedParameters(parameters);
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    public boolean isModelConnected() {
        return model.isConnected();
    }

    // TODO: Agregar el TORQUE_COMMAND
    private class updateMeasurements implements Runnable {
        private String measurement;
        private long timestamp;
        private long measurement_start_time = 0;
        private commands command;

        updateMeasurements(commands command) {
            this.command = command;
        };

        public void run() {
            // Esto termina tienendo un lag de 200ms o mas
            Random rand = new Random();
            if (measurement_start_time == 0) {
                this.measurement_start_time = System.currentTimeMillis();
            }
            this.timestamp = System.currentTimeMillis() - measurement_start_time;
            // System.out.println(timestamp);
            try {
                if (model.isConnected()) {

                    this.measurement = model.readVar(command.varPath, command.varName);
                } else {
                    throw new Exception("d");
                }

            } catch (Exception e) {

                // TODO: Cuando no hay un dato usar algún tipo de promedio ponderado en vez de
                // esto.
                this.measurement = String.valueOf((timestamp / 1e4) + rand.nextFloat());
                try {
                    Thread.sleep(150);
                } catch (InterruptedException exception) {
                    System.out.println("nit");
                } // Simula demora en leer datos
            }
            measurementsBufferedValues.addValue(command.seriesName, Float.valueOf(this.measurement), timestamp);
        }

    }

    private class sendTorqueCommands implements Runnable {
        private int itemsLoadedOnBuffer = 0;
        private boolean allDataLoaded = false;
        private ArrayList<String> timestamp = new ArrayList<String>();
        private ArrayList<String> torque = new ArrayList<String>();

        sendTorqueCommands(Model model, ArrayList<String> timestamp, ArrayList<String> torque) {
            this.itemsLoadedOnBuffer = 0;
            this.timestamp = timestamp;
            this.torque = torque;

        };

        public void run() {
            // Esto termina tienendo un lag de 200ms o mas
            int chunkEnd = 0;
            System.out.println(timestamp.size());
            if (model.bufferCTR() && !this.allDataLoaded) {
                if (itemsLoadedOnBuffer + TORQUE_TIME_BUFFER_SIZE > timestamp.size()) {
                    chunkEnd = timestamp.size();

                    this.allDataLoaded = true;
                } else {
                    chunkEnd = itemsLoadedOnBuffer + TORQUE_TIME_BUFFER_SIZE;
                }
                // try {

                // model.writeBuffer(timestamp.subList(itemsLoadedOnBuffer, chunkEnd),
                // torque.subList(itemsLoadedOnBuffer, chunkEnd));
                this.itemsLoadedOnBuffer = chunkEnd;
                System.err.println(this.itemsLoadedOnBuffer);
                /*
                 * } catch (ConnectException e) {
                 * System.err.println(e.getMessage());
                 * this.allDataLoaded=false;
                 * }
                 */

            }
        }

    }
}
