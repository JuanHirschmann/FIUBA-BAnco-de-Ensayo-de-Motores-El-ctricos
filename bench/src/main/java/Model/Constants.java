package Model;

public final class Constants {

    private Constants() {

    }
    public enum commands
    {
        TORQUE(MEASURED_SIMULATOR_TORQUE,"Torque","[Nm]"),
        TORQUE_COMMAND("Torque comandado","Torque comandado","[Nm]"),
        SPEED(MEASURED_SIMULATOR_SPEED,"Velocidad ","[RPM]"),
        POWER(MEASURED_SIMULATOR_POWER,"Potencia activa","[kW]"),
        CURRENT(MEASURED_SIMULATOR_CURRENT,"Corriente absoluta ","[Arms]"),
        VOLTAGE(MEASURED_SIMULATOR_VOLTAGE,"Tensión ","[Vrms]");
        
        public final String varPath;
        public final String varName;
        public final String displayName;
        public final String displayUnit;
        public final String seriesName;
        /* commands(String path,String name,String displayName)
        {
            this.varPath=path;
            this.varName=name;
            this.displayName=displayName;
        } */
        commands(String name,String displayName,String displayUnit)
        {
            this.varPath=VAR_PATH;
            this.varName=name;
            this.displayName=displayName;
            this.displayUnit=displayUnit;
            this.seriesName=displayName+" "+displayUnit;
        }
    }
    // Variables de programa en SIMOTION
    public static final String VAR_PATH = "SIMOTION";
    public static final String ENABLE_TEST_AXIS = "glob/ENABLE_DUT_MOVEMENT";
    public static final String ENABLE_SIMULATOR_AXIS = "glob/ENABLE_SIMULATOR_AXIS";
    public static final String ENABLE_ACTIVE_LINEMODULE = "glob/ENABLE_LINEMODULE";
    public static final String SOFTWARE_KILLSWITCH = "glob/SOFTWARE_KILLSWITCH";
    public static final String TEST_RUNTIME = "glob/TEST_RUNTIME";
    public static final String SOFTWARE_STOP = "unit/ladder.SOFTWARE_STOP_BUTTON";
    public static final String SOFTWARE_START = "unit/ladder.SOFTWARE_START_BUTTON";
    public static final String TORQUE_SETPOINT = "glob/TORQUE_SETPOINT";
    public static final String CLEAR_TO_RECIEVE = "glob/CLEAR_TO_RECEIVE";
    public static final String SIMULATOR_SPEED_LIMIT = "glob/SIMULATOR_SPEED_LIMIT";
    //TODO: Ver bien como se llaman los vectores
    public static final String  TORQUE_TIME_VALUES = "glob/TORQUE_VALUES";
    public static final String TIMESTAMP = "glob/TIMESTAMP";
    public static final String TORQUE_FROM_TIMESTAMP_SELECTED = "glob/TORQUE_FROM_TIMESTAMP_SELECTED";
    public static final String TIME_DELTA = "glob/DELTA";
    public static final String OPERATION_MODE = "var/modeOfOperation"; // _RUN, _STOP
    public static final String MEASURED_SIMULATOR_SPEED = "glob/ACTUAL_SIMULATOR_SPEED_RPM";// RPM
    public static final String MEASURED_SIMULATOR_TORQUE = "glob/ACTUAL_MEASURED_TORQUE";// Nm
    public static final String MEASURED_SIMULATOR_VOLTAGE = "unit/ladder.ACTUAL_OUTPUT_VOLTAGE";// Vrms
    public static final String MEASURED_SIMULATOR_POWER = "unit/ladder.ACTUAL_ACTIVE_POWER"; // Kw
    public static final String MEASURED_SIMULATOR_CURRENT = "unit/ladder.ACTUAL_ABSOLUTE_CURRENT"; // Kw
    
    public static final int TORQUE_TIME_BUFFER_SIZE = 1024;

    public static final String TORQUE_BIAS = "unit/ladder.TORQUE_BIAS";
    public static final String QUADRATIC_COEFF = "unit/ladder.QUADRATIC_COEFF";
    public static final String LINEAR_COEFF = "unit/ladder.LINEAR_COEFF";
    //TODO: Definir si estas variables entran
    public static final String ANGULAR_VELOCITY = "unit/ladder.ANGULAR_VELOCITY";
    public static final String INERTIA_COEFF = "unit/ladder.INERTIA_COEFF";
    public static final String AMPLITUDE = "unit/ladder.AMPLITUDE";
    
    public static final String TORQUE_SETPOINT_COMMAND = "glob/TORQUE_VALUES";
    public static final String TIMESTAMP_COMMAND = "glob/TIMESTAMP";
    // Etiquetas en las vistas
    
    // Se indexa como string glob/TORQUE_VALUES[0],glob/TORQUE_VALUES[1]...
    public static final String AVAILABLE_TORQUE_MODES[]={"Cupla en función del tiempo","Cupla en función de la velocidad"};
    public static final String DEFAULT_SERVER_ADDRESS = "http://192.168.214.1/soap/opcxml/";
    public static final int GRAPH_UPDATE_RATIO = 1;
    public static final int GRAPH_BUFFER_SIZE = 10000;
    public static final String CSV_FILEPATH="C:\\Users\\juanh\\OneDrive\\Escritorio\\TPP\\";
    /*
     * simulator_axis.actualTorque.value
     * simulator_axis.actorData.actualSpeed
     */
    public enum testTypes
    {
        TORQUE_VS_SPEED("Cupla en función de la velocidad"),
        TORQUE_VS_TIME("Cupla en función del tiempo");
        public final String displayName;
        /* commands(String path,String name,String displayName)
        {
            this.varPath=path;
            this.varName=name;
            this.displayName=displayName;
        } */
        testTypes(String name)
        {
            this.displayName=name;
        }
        @Override
        public String toString() {
            return this.displayName;
        }
    }
}

