package Controller;

import Model.Constants.commands;
import Model.Constants.serverSideTestError;
import Model.Constants.serverSideTestStatus;
import Swing.TorqueEquationParameter;
import Model.Model;
import Views.Views;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import static Model.Constants.DUT_SAVE_TO_BUFFER;
import static Model.Constants.DUT_SPEED_TIME_VALUES;
import static Model.Constants.DUT_TIMESTAMP;
import static Model.Constants.MAX_CHUNK_SIZE;
import static Model.Constants.SAVE_TO_BUFFER;
import static Model.Constants.TIMESTAMP;
import static Model.Constants.TORQUE_TIME_BUFFER_SIZE;
import static Model.Constants.TORQUE_TIME_VALUES;
import static Model.Constants.VAR_PATH;
import static Views.Constants.CONTROL_NOT_CONNECTED_MSG;
import static Views.Constants.KEEPALIVE_FAILURE_MSG;

import java.net.ConnectException;
import java.util.concurrent.ExecutorService;
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
    private serverSideTestStatus previousTestState = serverSideTestStatus.NOT_STARTED;
    ExecutorService measurementExecutor =Executors.newFixedThreadPool(20);
    ScheduledExecutorService torqueTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService bufferTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService DUTBufferTimer = Executors.newScheduledThreadPool(10);
    ScheduledExecutorService lowPriorityTimer = Executors.newScheduledThreadPool(10);

    public Controller() {
    };

    /**
     * Sets test endtime parameter, checks for integer conversion and positive value
     * 
     * @param endtime_ms
     * @throws ConnectException
     */
    public void setTestEndTime(String endtime_ms) throws ConnectException {
        if (model.isConnected()) {
            model.setTestEndTime(endtime_ms, true);
        } else {
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
        // Este tiempo de actualización tiene que ser menor al de la actualización del
        // gráfico.
        torqueTimer.scheduleAtFixedRate(new updateMeasurements(measuredVars), 0, 100, TimeUnit.MILLISECONDS);

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
                throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
            }
        } else {

            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
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
                throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
            }
        } else {

            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
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
            lowPriorityTimer.scheduleAtFixedRate(new lowPriorityUpdate(), 20, 200, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
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
            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
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
            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);

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
            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
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
            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
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
            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
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
            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
        }
    }

    /**
     * Selects Torque vs Time test type.
     * Checks for server connectivity, CSV file length and maximum torque
     * 
     * @throws Exception
     */
    public void selectTorqueVsTime() throws ConnectException {
        if (model.isConnected()) {
            Float last_timestamp = Float
                    .valueOf(this.torqueTimeValues.getTimestamp(this.torqueTimeValues.length() - 1));
            bufferTimer.scheduleAtFixedRate(new sendTorqueCommands(), 100, 1000, TimeUnit.MILLISECONDS);
            model.selectTorqueVsTime(true);
            int last_timestamp_as_int = last_timestamp.intValue();
            System.out.print("ÚLTIMO TIMESTAMP");
            System.out.println(last_timestamp_as_int);
            model.setTestEndTime(String.valueOf(last_timestamp_as_int), true);
        } else {
            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
        }
    }

    /**
     * Selects Torque vs Speed test type.
     * Checks for server connectivity.
     * 
     * @throws Exception
     */
    public void selectTorqueVsSpeed() throws ConnectException {
        if (model.isConnected()) {
            model.selectTorqueVsSpeed(true);
        } else {
            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
        }
    }

    /**
     * Selects Torque vs Time test type.
     * Checks for server connectivity, CSV file length and maximum torque
     * 
     * @throws Exception
     */
    public void selectMixedTest() throws ConnectException {

        if (model.isConnected()) {
            this.torqueTimeValues.torqueCommandCheck();
            // Initial delay to avoid stepping on model.setTestEndTime
            Float last_timestamp = Float
                    .valueOf(this.torqueTimeValues.getTimestamp(this.torqueTimeValues.length() - 1));
            int last_timestamp_as_int = last_timestamp.intValue();
            model.setTestEndTime(String.valueOf(last_timestamp_as_int), true);
            bufferTimer.scheduleAtFixedRate(new sendTorqueCommands(), 100, 1000, TimeUnit.MILLISECONDS);
            model.selectMixedTest(true);

            System.out.print("ÚLTIMO TIMESTAMP");
            System.out.println(last_timestamp_as_int);
        } else {
            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
        }
    }

    /**
     * Selects Torque vs Time test type.
     * Checks for server connectivity, CSV file length and maximum torque
     * 
     * @throws Exception
     */
    public void selectSelfSustainedMode() throws Exception {
        boolean checkSuccess = true;
        try {

            this.speedTimeValues.speedCommandCheck();
        } catch (Exception e) {
            checkSuccess = false;
            throw e;
        }
        if (model.isConnected() && checkSuccess) {
            model.enableDUTAxis(true);
            DUTBufferTimer.scheduleAtFixedRate(new sendSpeedCommands(), 200, 1000, TimeUnit.MILLISECONDS);
        } else {
            throw new ConnectException(CONTROL_NOT_CONNECTED_MSG);
        }
    }

    /**
     * Sets torque vs speed test type parameters. Checks for server connection,
     * D parameter's maximum value and sign.
     * Checks for server connectivity.
     * 
     * @throws Exception
     */
    public void setTorqueVsSpeedParameters(Map<String, TorqueEquationParameter> parameters) throws ConnectException {

        if (model.isConnected()) {
            model.setTorqueVsSpeedParameters(parameters);
            view.updateLoadedTestStatus(true);
        } else {
            view.updateLoadedTestStatus(false);
            throw new ConnectException();
        }
    }

    public void dismissQueuedCommands() {
        this.model.clearWriteQueue();
    }

    public void executeQueuedCommands() throws ConnectException {
        this.model.executeWriteQueue();
    }
    public void setTestStatus(serverSideTestStatus status) throws ConnectException {
        this.model.setTestStatus(status);
        //this.model.executeWriteQueue();
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
        if (previousTestState==serverSideTestStatus.READY_TO_START && testStatus==serverSideTestStatus.RUNNING) {
            this.view.handleStartCommand();
        } 
        
        this.previousTestState=testStatus;

    }

    private void handleTestError(serverSideTestError testError) {

        // view.updateTestStatus(testError);
        if (testError != serverSideTestError.NO_ERROR) {
            view.alert(testError.getResponseMessage());

        }

    }

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
            measurementExecutor.submit(() -> {

                this.command.addAll(command);
            });
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
                    this.measurement = model.readVars(VAR_PATH, varNames);
                    this.delay = System.currentTimeMillis() - this.delay;
                    for (int i = 0; i < command.size(); i++) {
                        measurementsBufferedValues.addValue(command.get(i).seriesName,
                                Float.valueOf(this.measurement.get(varNames.get(i))),
                                timestamp - delay / 2);
                    }
                    // System.err.println(delay);
                } else {
                    System.out.println("Falla en medición");

                    throw new Exception("Error de medición");
                }

            } catch (Exception e) {

                System.out.println("Falla en medición");
            }
        }

    }

    /*
     * Implements the update of keepalive variable.
     */
    private class lowPriorityUpdate implements Runnable {
        /* List<String> updateVarsName= new ArrayList<String>(); */
        lowPriorityUpdate() {
            // updateVarsName.add(AXIS_ENABLED_SIGNAL);
            // updateVarsName.add(TEST_STATUS);
            // updateVarsName.add(CURRENT_ERROR);
        };

        public void run() {

            try {
                if (model.isConnected()) {
                    model.writeVar("FALSE", "SIMOTION", "glob/KEEPALIVE");

                    // model.readVars(VAR_PATH, null);
                    view.updateConnectionStatus(true);
                    view.updateALMStatus(model.ALM_is_active());
                    serverSideTestStatus testStatus = model.getTestStatus();
                    handleTestStatus(testStatus);
                    serverSideTestError testError = model.getTestError();
                    handleTestError(testError);
                } else {

                    view.updateConnectionStatus(false);
                    throw new Exception(KEEPALIVE_FAILURE_MSG);

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
