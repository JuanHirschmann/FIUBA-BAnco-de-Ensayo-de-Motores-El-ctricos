package Controller;

import Model.Constants.commands;
import Model.Constants.serverSideTestError;
import Model.Constants.serverSideTestStatus;
import Model.Constants.testStatus;
import Model.Constants.testTypes;
import Swing.TorqueEquationParameter;
import Model.Model;
import Views.Views;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static Model.Constants.DUT_SAVE_TO_BUFFER;
import static Model.Constants.DUT_SPEED_TIME_VALUES;
import static Model.Constants.DUT_TIMESTAMP;
import static Model.Constants.MAX_CHUNK_SIZE;
import static Model.Constants.SAVE_TO_BUFFER;
import static Model.Constants.TIMESTAMP;
import static Model.Constants.TORQUE_TIME_BUFFER_SIZE;
import static Model.Constants.TORQUE_TIME_VALUES;
import static Model.Constants.VAR_PATH;

import java.lang.Math;
import java.net.ConnectException;
import java.text.NumberFormat;
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
    private TorqueTimeValues speedTimeValues = new TorqueTimeValues();

    ScheduledExecutorService torqueTimer = Executors.newScheduledThreadPool(10);
    // ScheduledExecutorService voltageTimer = Executors.newScheduledThreadPool(10);
    // ScheduledExecutorService currentTimer = Executors.newScheduledThreadPool(10);
    // ScheduledExecutorService powerTimer = Executors.newScheduledThreadPool(10);
    // ScheduledExecutorService speedTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService bufferTimer = Executors.newScheduledThreadPool(10);

    // ScheduledExecutorService DUTTorqueTimer =
    // Executors.newScheduledThreadPool(10);
    // ScheduledExecutorService DUTVoltageTimer =
    // Executors.newScheduledThreadPool(10);
    // ScheduledExecutorService DUTCurrentTimer =
    // Executors.newScheduledThreadPool(10);
    // ScheduledExecutorService DUTPowerTimer =
    // Executors.newScheduledThreadPool(10);
    ScheduledExecutorService DUTBufferTimer = Executors.newScheduledThreadPool(10);
    //
    ScheduledExecutorService lowPriorityTimer = Executors.newScheduledThreadPool(10);

    public Controller() {
    };

    /**
     * Sets test endtime parameter, checks for integer conversion and positive value
     * 
     * @param endtime_ms
     * @throws ConnectException
     */
    public void setTestEndTime(String endtime_ms) throws Exception {
        try {
            Integer endtime = Integer.valueOf(endtime_ms);
        } catch (NumberFormatException e) {
            System.err.println(e);
            throw new IllegalArgumentException(
                    "El formato del tiempo de finalización no puede convertirse a un número entero");
        }
        if (Integer.valueOf(endtime_ms) <= 0) {
            throw new IllegalArgumentException(
                    "El tiempo de finalización no puede convertirse ser igual o menor a cero");

        }
        if (model.isConnected()) {
            model.setTestEndTime(endtime_ms);
        }else
        {
            throw new ConnectException();
        }
    }

    /**
     * Fills TorqueTimeValues from a csv file with format Time[ms],Torque
     * command[Nm]. Doesn't accept Headers
     * 
     * @param filepath
     */
    public void createTorqueTimeFromCSV(String filepath) {
        this.torqueTimeValues.fromCSV(filepath);
        System.out.println(torqueTimeValues.length());
    }

    /**
     * Fills TorqueTimeValues from a csv file with format Time[ms],Torque
     * command[Nm]. Doesn't accept Headers
     * 
     * @param filepath
     */
    public void createSpeedTimeFromCSV(String filepath) {
        this.speedTimeValues.fromCSV(filepath);
        System.out.println(speedTimeValues.length());
    }

    /**
     * Extends the selected torque time waveform by an integer number of periods.
     * 
     * @param periods
     */
    public void extendTorqueTimeValues(int periods) {
        this.torqueTimeValues.extend(periods);
    }

    /**
     * Gets the loaded torque time values.
     * 
     * @return TorqueTimeValues
     */
    public TorqueTimeValues getTorqueTimeValues() {
        // TorqueTimeValues output= new TorqueTimeValues(this.torqueTimeValues);
        // System.out.println(torqueTimeValues.length());

        return new TorqueTimeValues(this.torqueTimeValues);
    }

    /**
     * Gets the loaded torque time values.
     * 
     * @return TorqueTimeValues
     */
    public TorqueTimeValues getSpeedTimeValues() {
        // TorqueTimeValues output= new TorqueTimeValues(this.torqueTimeValues);
        // System.out.println(torqueTimeValues.length());

        return new TorqueTimeValues(this.speedTimeValues);
    }

    public Controller(Model model, Views view) {
        this.model = model;
        this.view = view;

    }

    /*
     * starts the measurements for variables at a fixed rate
     */
    public void startMeasurements() {
        List<commands> measuredVars = new ArrayList<commands>();
        measuredVars.add(commands.TORQUE);
        // if()
        // measuredVars.add(commands.TORQUE_COMMAND);//TODO: Solo con torque-time
        measuredVars.add(commands.VOLTAGE);
        measuredVars.add(commands.CURRENT);
        measuredVars.add(commands.POWER);
        measuredVars.add(commands.SPEED);
        if (view.DUTModeSelected()) {

            measuredVars.add(commands.DUT_TORQUE);
            measuredVars.add(commands.DUT_VOLTAGE);
            measuredVars.add(commands.DUT_CURRENT);
            measuredVars.add(commands.DUT_POWER);
            // measuredVars.add(commands.DUT_SPEED);
        }
        // TODO: Asegurar que cada medición corra en un hilo por separado
        System.err.println("empece a medir");
        torqueTimer.scheduleAtFixedRate(new updateMeasurements(measuredVars), 0, 200, TimeUnit.MILLISECONDS);
        /*
         * voltageTimer.scheduleAtFixedRate(new updateMeasurements(commands.VOLTAGE),
         * 100, 800,
         * TimeUnit.MILLISECONDS);
         * currentTimer.scheduleAtFixedRate(new updateMeasurements(commands.CURRENT),
         * 200, 800,
         * TimeUnit.MILLISECONDS);
         * powerTimer.scheduleAtFixedRate(new updateMeasurements(commands.POWER), 300,
         * 800, TimeUnit.MILLISECONDS);
         * speedTimer.scheduleAtFixedRate(new updateMeasurements(commands.SPEED), 400,
         * 800, TimeUnit.MILLISECONDS);
         * if (view.DUTModeSelected()) {
         * DUTTorqueTimer.scheduleAtFixedRate(new
         * updateMeasurements(commands.DUT_TORQUE), 500, 800,
         * TimeUnit.MILLISECONDS);
         * DUTVoltageTimer.scheduleAtFixedRate(new
         * updateMeasurements(commands.DUT_VOLTAGE), 600, 800,
         * TimeUnit.MILLISECONDS);
         * DUTCurrentTimer.scheduleAtFixedRate(new
         * updateMeasurements(commands.DUT_CURRENT), 700, 800,
         * TimeUnit.MILLISECONDS);
         * DUTPowerTimer.scheduleAtFixedRate(new updateMeasurements(commands.DUT_POWER),
         * 750, 800,
         * TimeUnit.MILLISECONDS);
         * 
         * }
         */
    }

    /*
     * Stops measurment update
     */
    public void stopMeasurements() {
        torqueTimer.shutdown();
        // voltageTimer.shutdown();
        // currentTimer.shutdown();
        // powerTimer.shutdown();
        // speedTimer.shutdown();
    }

    /**
     * Sets the PLC state to RUN. Checks for server conection.
     * 
     * @throws ConnectException
     */
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

    /**
     * Sets the PLC state to RUN. Checks for server conection.
     * 
     * @throws ConnectException
     */
    public void stop() throws ConnectException {
        if (model.isConnected()) {
            try {

                model.stop();
            } catch (Exception e) {
                throw new ConnectException("El control no está conectado. Verifique la configuración IP");
            }
        } else {

            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    /**
     * Gets buffered variables meaurements.
     * 
     * @return MeasurementBuffer
     */
    public MeasurementBuffer getMeasurementBuffer() {
        return measurementsBufferedValues;
    }

    /*
     * Clears the buffered measured variables
     */
    public void clearMeasurementBuffer() {
        measurementsBufferedValues.clearBuffer();
    }

    /**
     * connects to server.
     * 
     * @param targetIP OPC XML-DA server IP
     * @throws Exception Throws ConnectException when connection isn't possible
     */
    public void connect(String targetIP) throws Exception {
        try {

            model.connect(targetIP);
            System.out.println(targetIP);
            lowPriorityTimer.scheduleAtFixedRate(new lowPriorityUpdate(), 20, 1000, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }

    }

    /**
     * Sets the PLC state to RUN. Checks for server conection.
     * 
     * @throws ConnectException
     */
    public void PLCStart() throws Exception {
        if (model.isConnected()) {
            model.controllerOn();

        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }

    }

    /**
     * Sets the PLC state to STOP. Checks for server conection.
     * 
     * @throws ConnectException
     */
    public void PLCStop() throws Exception {
        if (model.isConnected()) {
            model.controllerOff();
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");

        }
    }

    /**
     * Turns on power for the Line Module and axis. Checks for server connectivity
     * 
     * @throws Exception
     */
    public void powerOn() throws Exception {
        if (model.isConnected()) {
            model.powerOn();
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }

    }

    /**
     * Turns off power for the Line Module and axis. Checks for server connectivity
     * 
     * @throws Exception
     */
    public void powerOff() throws Exception {
        if (model.isConnected()) {
            model.powerOff();
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    /**
     * Sets emergency stop command. Checks for server connectivity
     * 
     * @throws Exception
     */
    public void emergencyStop() throws Exception {
        if (model.isConnected()) {
            model.emergencyStop();
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    /**
     * Sets emergency stop release command. Checks for server connectivity
     * 
     * @throws Exception
     */
    public void emergencyRelease() throws Exception {
        if (model.isConnected()) {
            model.emergencyRelease();
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    /**
     * Selects Torque vs Time test type.
     * Checks for server connectivity, CSV file length and maximum torque
     * 
     * @throws Exception
     */
    public void selectTorqueVsTime() throws Exception {
        if (model.isConnected()) {
            this.torqueTimeValues.torqueCommandCheck();
            Float last_timestamp = Float
                    .valueOf(this.torqueTimeValues.getTimestamp(this.torqueTimeValues.length() - 1));
            bufferTimer.scheduleAtFixedRate(new sendTorqueCommands(), 1000, 1000, TimeUnit.MILLISECONDS);
            model.selectTorqueVsTime();

            int last_timestamp_as_int = last_timestamp.intValue();
            System.out.print("ÚLTIMO TIMESTAMP");
            System.out.println(last_timestamp_as_int);
            model.setTestEndTime(String.valueOf(last_timestamp_as_int));
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    /**
     * Selects Torque vs Speed test type.
     * Checks for server connectivity.
     * 
     * @throws Exception
     */
    public void selectTorqueVsSpeed() throws Exception {
        if (model.isConnected()) {
            model.selectTorqueVsSpeed();
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    /**
     * Selects Torque vs Time test type.
     * Checks for server connectivity, CSV file length and maximum torque
     * 
     * @throws Exception
     */
    public void selectMixedTest() throws ConnectException {
        // TODO: SACAR EL !
        if (model.isConnected()) {
            this.torqueTimeValues.torqueCommandCheck();
            // Initial delay to avoid stepping on model.setTestEndTime
            Float last_timestamp = Float
                    .valueOf(this.torqueTimeValues.getTimestamp(this.torqueTimeValues.length() - 1));
            int last_timestamp_as_int = last_timestamp.intValue();
            model.setTestEndTime(String.valueOf(last_timestamp_as_int));
            bufferTimer.scheduleAtFixedRate(new sendTorqueCommands(), 1000, 1000, TimeUnit.MILLISECONDS);
            model.selectMixedTest();

            System.out.print("ÚLTIMO TIMESTAMP");
            System.out.println(last_timestamp_as_int);
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    /**
     * Selects Torque vs Time test type.
     * Checks for server connectivity, CSV file length and maximum torque
     * 
     * @throws Exception
     */
    public void selectSelfSustainedMode() throws ConnectException {
        // TODO: SACAR EL !
        if (model.isConnected()) {
            this.speedTimeValues.speedCommandCheck();

            // Initial delay to avoid stepping on model.setTestEndTime
            DUTBufferTimer.scheduleAtFixedRate(new sendSpeedCommands(), 1000, 1000, TimeUnit.MILLISECONDS);
            // TODO: SELECT SELF_SUSTAINED
            // model.selectTorqueVsTime();
        } else {
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    /**
     * Sets torque vs speed test type parameters. Checks for server connection,
     * D parameter's maximum value and sign.
     * Checks for server connectivity.
     * 
     * @throws Exception
     */
    public void setTorqueVsSpeedParameters(Map<String, TorqueEquationParameter> parameters) throws Exception {
        // TODO: SACAR EL !
        if (model.isConnected()) {
            Float value;
            try {
                value = Float.valueOf(parameters.get("A").getValue());
                value = Float.valueOf(parameters.get("B").getValue());
                value = Float.valueOf(parameters.get("C").getValue());
                value = Float.valueOf(parameters.get("D").getValue());
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "Los coeficientes de la ecuación cupla-velocida deben especificarse con separador decimal punto (.). ");
            }
            if (Float.valueOf(parameters.get("D").getValue()) < 0) {

                view.updateLoadedTestStatus(false);
                throw new IllegalArgumentException("El valor del término inercial no puede ser negativo");
            } else if (Float.valueOf(parameters.get("D").getValue()) > 0.3) {

                view.updateLoadedTestStatus(false);
                throw new IllegalArgumentException("El valor del término inercial no puede ser mayor a 0.3");
            }
            model.setTorqueVsSpeedParameters(parameters);
            view.updateLoadedTestStatus(true);
        } else {
            view.updateLoadedTestStatus(false);
            throw new ConnectException("El control no está conectado. Verifique la configuración IP");
        }
    }

    /*
     * Checks if model is connected
     */
    public boolean isModelConnected() {
        return model.isConnected();
    }

    private void handleTestStatus(serverSideTestStatus testStatus) {

        view.updateTestStatus(testStatus);
        if (testStatus == serverSideTestStatus.ENDED) {
            this.stopMeasurements();
        }

    }

    private void handleTestError(serverSideTestError testError) {

       // view.updateTestStatus(testError);
        if (testError != serverSideTestError.NO_ERROR) {
            view.alert(testError.getResponseMessage());

        }

    }

    // TODO: Agregar el TORQUE_COMMAND
    /*
     * Implements the update of measurments on to a measurement
     * buffer.
     */
    private class updateMeasurements implements Runnable {
        private Map<String, String> measurement = new Hashtable<String, String>();
        private long timestamp;
        private long measurement_start_time = 0;
        private List<commands> command = new ArrayList<commands>();
        private long delay;

        updateMeasurements(List<commands> command) {

            this.command.addAll(command);
        };

        public void run() {
            // Esto termina tienendo un lag de 200ms o mas

            // Random rand = new Random();
            if (measurement_start_time == 0) {
                this.measurement_start_time = System.currentTimeMillis();
            }

            this.timestamp = System.currentTimeMillis() - measurement_start_time;
            try {

                if (model.isConnected()) {

                    /*
                     * this.delay = System.currentTimeMillis();
                     * this.measurement = model.readVar(command.varPath, command.varName);
                     * this.delay = System.currentTimeMillis() - this.delay;
                     * // System.out.print("Delay: ");
                     * // System.out.println(this.delay);
                     * 
                     * measurementsBufferedValues.addValue(command.seriesName,
                     * Float.valueOf(this.measurement),
                     * timestamp - delay / 2);
                     */
                    List<String> varNames = new ArrayList<String>();
                    for (int i = 0; i < this.command.size(); i++) {
                        varNames.add(this.command.get(i).varName);
                    }

                    this.delay = System.currentTimeMillis();
                    // System.out.println(varNames);
                    this.measurement = model.readVars(VAR_PATH, varNames);
                    this.delay = System.currentTimeMillis() - this.delay;
                    // System.err.println(this.measurement);
                    for (int i = 0; i < command.size(); i++) {
                        measurementsBufferedValues.addValue(command.get(i).seriesName,
                                Float.valueOf(this.measurement.get(varNames.get(i))),
                                timestamp - delay / 2);
                    }
                } else {
                    System.out.println("Falla en medición");

                    throw new Exception("Error de medición");
                }

            } catch (Exception e) {

                // TODO: Cuando no hay un dato usar algún tipo de promedio ponderado en vez de
                // esto.
                // TODO: Probablemente lo mejor sea no sumar el punto de medicion
                // measurementsBufferedValues.getBufferedData(command.seriesName).
                // this.measurement = String.valueOf((timestamp / 1e4) + rand.nextFloat());
                System.out.println("Falla en medición");

                // measurementsBufferedValues.addValue(command.g, Float.NaN, timestamp);
                // measurementsBufferedValues.addValue(command.seriesName, Float.NaN,
                // timestamp);
                /*
                 * try {
                 * Thread.sleep(150);
                 * } catch (InterruptedException exception) {
                 * System.out.println("nit");
                 * }
                 */// Simula demora en leer datos
            }
        }

    }

    /*
     * Implements the update of keepalive variable.
     */
    private class lowPriorityUpdate implements Runnable {

        lowPriorityUpdate() {
        };

        public void run() {

            try {
                if (model.isConnected()) {
                    model.writeVar("FALSE", "SIMOTION", "glob/KEEPALIVE");
                    view.updateConnectionStatus(true);
                    view.updateALMStatus(model.ALM_is_active());
                    serverSideTestStatus testStatus = model.getTestStatus();
                    handleTestStatus(testStatus);
                    serverSideTestError testError = model.getTestError();
                    handleTestError(testError);
                } else {
                    view.updateConnectionStatus(false);
                    throw new Exception("Keepalive failed");

                }

            } catch (Exception e) {

                System.err.println(e.getMessage());
            }

        };

    }

    private class sendTorqueCommands implements Runnable {
        private int totalItemsLoadedOnBuffer = 0;
        private boolean allDataLoaded = false;
        private ArrayList<String> timestamp = new ArrayList<String>();
        private ArrayList<String> torque = new ArrayList<String>();
        private int currentItemsLoadedOnBuffer = 0;
        private int remainingItemsToLoad = 0;

        sendTorqueCommands() {
            // PADEA el vector torque-tiempo para que tenga un largo divisible por el tamaño
            // del buffer externo.
            this.totalItemsLoadedOnBuffer = 0;
            int padLength = TORQUE_TIME_BUFFER_SIZE - torqueTimeValues.length() % TORQUE_TIME_BUFFER_SIZE;
            torqueTimeValues.zeropad(padLength);
            this.timestamp = torqueTimeValues.getTimestamp();
            this.torque = torqueTimeValues.getValue();
            this.currentItemsLoadedOnBuffer = 0;
            this.remainingItemsToLoad = 0;

        };

        public void run() {
            // Esto termina tienendo un lag de 200ms o mas

            int chunkEnd = 0;
            view.updateTorqueTimeLoad(totalItemsLoadedOnBuffer, timestamp.size());
            if (this.totalItemsLoadedOnBuffer == timestamp.size()) {

                view.updateLoadedTestStatus(allDataLoaded);
                this.allDataLoaded = true;
                currentItemsLoadedOnBuffer = TORQUE_TIME_BUFFER_SIZE;

            } else {
                System.err.print("Remaining items to buffer: ");
                System.out.println(this.remainingItemsToLoad);
                this.allDataLoaded = false;
                view.updateLoadedTestStatus(allDataLoaded);
            }
            if (model.bufferCTR() && !this.allDataLoaded) {

                System.err.println("Cargando comandos de torque-tiempo");
                if (totalItemsLoadedOnBuffer + MAX_CHUNK_SIZE > timestamp.size()) {
                    chunkEnd = timestamp.size();

                    // this.allDataLoaded = true;
                } else {
                    chunkEnd = totalItemsLoadedOnBuffer + MAX_CHUNK_SIZE;
                }
                try {
                    System.err.println("escribiendo buffer");
                    List<String> timestampChunk = timestamp.subList(totalItemsLoadedOnBuffer, chunkEnd);
                    List<String> torqueChunk = torque.subList(totalItemsLoadedOnBuffer, chunkEnd);

                    int arraySize = timestamp.subList(totalItemsLoadedOnBuffer, chunkEnd).size();
                    int bufferIndex = this.totalItemsLoadedOnBuffer % TORQUE_TIME_BUFFER_SIZE;
                    model.writeArray(timestampChunk, VAR_PATH, TIMESTAMP, arraySize, bufferIndex);
                    model.writeArray(torqueChunk, VAR_PATH, TORQUE_TIME_VALUES, arraySize, bufferIndex);
                    this.totalItemsLoadedOnBuffer = chunkEnd;
                    this.currentItemsLoadedOnBuffer += arraySize;
                    this.remainingItemsToLoad = timestamp.size() - this.totalItemsLoadedOnBuffer;
                    if (this.currentItemsLoadedOnBuffer >= TORQUE_TIME_BUFFER_SIZE) {
                        this.currentItemsLoadedOnBuffer = 0;
                        System.err.print("Carga adicional");
                        model.writeVar("TRUE", VAR_PATH, SAVE_TO_BUFFER);
                    }
                    System.err.print("Total items on buffer:");
                    System.err.println(this.totalItemsLoadedOnBuffer);

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    // this.allDataLoaded = false;
                }

            }
        }

    }

    // TODO: ACTUALIZAR CARGA DE COMANDOS DE VELOCIDAD
    private class sendSpeedCommands implements Runnable {
        private int totalItemsLoadedOnBuffer = 0;
        private boolean allDataLoaded = false;
        private ArrayList<String> timestamp = new ArrayList<String>();
        private ArrayList<String> speed = new ArrayList<String>();
        private int currentItemsLoadedOnBuffer = 0;
        private int remainingItemsToLoad = 0;

        sendSpeedCommands() {
            // PADEA el vector torque-tiempo para que tenga un largo divisible por el tamaño
            // del buffer externo.
            this.totalItemsLoadedOnBuffer = 0;
            int padLength = TORQUE_TIME_BUFFER_SIZE - speedTimeValues.length() % TORQUE_TIME_BUFFER_SIZE;
            speedTimeValues.zeropad(padLength);
            this.timestamp = speedTimeValues.getTimestamp();
            this.speed = speedTimeValues.getValue();
            this.currentItemsLoadedOnBuffer = 0;
            this.remainingItemsToLoad = 0;

        };

        public void run() {
            // Esto termina tienendo un lag de 200ms o mas

            int chunkEnd = 0;
            // view.updateTorqueTimeLoad(totalItemsLoadedOnBuffer, timestamp.size());
            if (this.totalItemsLoadedOnBuffer == timestamp.size()) {

                view.updateLoadedTestStatus(allDataLoaded);
                this.allDataLoaded = true;
                currentItemsLoadedOnBuffer = TORQUE_TIME_BUFFER_SIZE;

            } else {
                System.err.print("Remaining items to buffer: ");
                System.out.println(this.remainingItemsToLoad);
                this.allDataLoaded = false;
                view.updateLoadedTestStatus(allDataLoaded);
            }
            if (model.DUTBufferCTR() && !this.allDataLoaded) {

                System.err.println("Cargando comandos de velocidad-tiempo");
                if (totalItemsLoadedOnBuffer + MAX_CHUNK_SIZE > timestamp.size()) {
                    chunkEnd = timestamp.size();

                    // this.allDataLoaded = true;
                } else {
                    chunkEnd = totalItemsLoadedOnBuffer + MAX_CHUNK_SIZE;
                }
                try {
                    System.err.println("escribiendo buffer");
                    List<String> timestampChunk = timestamp.subList(totalItemsLoadedOnBuffer, chunkEnd);
                    List<String> speedChunk = speed.subList(totalItemsLoadedOnBuffer, chunkEnd);

                    int arraySize = timestamp.subList(totalItemsLoadedOnBuffer, chunkEnd).size();
                    int bufferIndex = this.totalItemsLoadedOnBuffer % TORQUE_TIME_BUFFER_SIZE;
                    model.writeArray(timestampChunk, VAR_PATH, DUT_TIMESTAMP, arraySize, bufferIndex);
                    model.writeArray(speedChunk, VAR_PATH, DUT_SPEED_TIME_VALUES, arraySize, bufferIndex);
                    this.totalItemsLoadedOnBuffer = chunkEnd;
                    this.currentItemsLoadedOnBuffer += arraySize;
                    this.remainingItemsToLoad = timestamp.size() - this.totalItemsLoadedOnBuffer;
                    if (this.currentItemsLoadedOnBuffer >= TORQUE_TIME_BUFFER_SIZE) {
                        this.currentItemsLoadedOnBuffer = 0;
                        System.err.print("Carga adicional");
                        model.writeVar("TRUE", VAR_PATH, DUT_SAVE_TO_BUFFER);
                    }
                    System.err.print("Total items on buffer:");
                    System.err.println(this.totalItemsLoadedOnBuffer);

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    // this.allDataLoaded = false;
                }

            }
        }

    }
}
