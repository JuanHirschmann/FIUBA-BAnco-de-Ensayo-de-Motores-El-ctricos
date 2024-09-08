package Model;

public final class Constants {
    
    private Constants()
    {

    }
    //Variables de programa en SIMOTION
    public static final String VAR_PATH="SIMOTION";
    public static final String ENABLE_TEST_AXIS="glob/ENABLE_DUT_MOVEMENT";
    public static final String ENABLE_SIMULATOR_AXIS="glob/ENABLE_SIMULATOR_AXIS";
    public static final String ENABLE_ACTIVE_LINEMODULE="glob/ENABLE_LINEMODULE";
    public static final String SOFTWARE_KILLSWITCH="glob/SOFTWARE_KILLSWITCH";
    public static final String TORQUE_SETPOINT="glob/TORQUE_SETPOINT";
    public static final String SIMULATOR_SPEED_LIMIT="glob/glob/SIMULATOR_SPEED_LIMIT";
    public static final String TIME_DELTA="glob/DELTA";
    public static final String OPERATION_MODE="var/modeOfOperation"; //_RUN, _STOP
    public static final String MEASURED_SIMULATOR_SPEED="simulator_axis.actorData.actualSpeed";
    public static final String NEW_TORQUE_COMMAND_FLAG="glob/NEW_TORQUE_COMMAND";

    //Etiquetas en las vistas
    public static final String CONTROLLER_START_BUTTON_LABEL="Iniciar Control";
    public static final String POWER_ON_BUTTON_LABEL="Activar potencia";
    public static final String EMERGENCY_STOP_BUTTON_LABEL="EMG";
    public static final String CONNECT_BUTTON_LABEL="Conectar";
    public static final String READ_VARIABLE_BUTTON_LABEL="Leer variable";
    public static final String WRITE_VARIABLE_BUTTON_LABEL="Escribir variable";
    public static final String PAUSE_BUTTON_LABEL="Pausar ensayo";
    public static final String START_BUTTON_LABEL="Iniciar ensayo";
    //public static final String CONTROLLER_START_BUTTON_LABEL="Iniciar Control";
    //public static final String CONTROLLER_START_BUTTON_LABEL="Iniciar Control";
    //public static final String CONTROLLER_START_BUTTON_LABEL="Iniciar Control";
    //public static final String CONTROLLER_START_BUTTON_LABEL="Iniciar Control";
    

    /* simulator_axis.actualTorque.value
    simulator_axis.actorData.actualSpeed */
}
