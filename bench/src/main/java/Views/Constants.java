package Views;

public final class Constants {

    private Constants() {

    }

    public static final String CONTROLLER_START_BUTTON_LABEL = "Iniciar Control";
    public static final String POWER_ON_BUTTON_LABEL = "Activar potencia";
    public static final String EMERGENCY_STOP_BUTTON_LABEL = "EMG";
    public static final String EMERGENCY_RELEASE_BUTTON_LABEL = "Liberar EMG";
    public static final String CONNECT_BUTTON_LABEL = "Conectar";
    public static final String READ_VARIABLE_BUTTON_LABEL = "Leer variable";
    public static final String WRITE_VARIABLE_BUTTON_LABEL = "Escribir variable";
    public static final String PAUSE_BUTTON_LABEL = "Pausar ensayo";
    public static final String START_BUTTON_LABEL = "Iniciar ensayo";
    public static final String RESUME_BUTTON_LABEL = "Reanudar ensayo";
    public static final String SHUTDOWN_BUTTON_LABEL = "Apagar";
    public static final String SAVE_CSV_BUTTON_LABEL = "Exportar curvas como CSV";
    public static final String BROWSE_FILE_BUTTON_LABEL = "Importar curva torque-tiempo...";
    public static final String SET_TEST_PARAMETERS_BUTTON_LABEL= "Fijar parámetros";
    public static final String SELF_SUSTAINED_TEST_LABEL="Ensayo autocontenido";
    public static final String SELF_SUSTAINED_TEST_IMPORT_LABEL="Importar curva velocidad-tiempo...";
    public static final String CSV_DELIMITER=",";
    public static final String WRITE_CSV="Exportar como CSV";
    public static final String READ_CSV="Importar CSV";
    // Se indexa como string glob/TORQUE_VALUES[0],glob/TORQUE_VALUES[1]...
    public static final String AVAILABLE_TORQUE_MODES[]={"Cupla en función del tiempo","Cupla en función de la velocidad"};
    //public static final String DEFAULT_SERVER_ADDRESS = "http://192.168.214.1/soap/opcxml/";
    public static final String DEFAULT_SERVER_ADDRESS = "http://169.254.11.22/soap/opcxml";

    public static final String SELF_SUSTAINED_MODE_WARNING="Se seleccionó el modo de ensayo autocontenido. En este modo, ambos ejes conectados al banco están habilitados para realizar movimiento. Verifique que ningún objeto esté en la cercanía de los ejes. No desatienda el equipo, esté preparado para accionar el botón de emergencia desde el control de operador. Evite accidentes";
    public static final String PRE_START_WARNING="Al seleccionar '"+ START_BUTTON_LABEL+ "' el movimiento comenzará. Verifique que ningún objeto esté en la cercanía de los ejes. No desatienda el equipo, esté preparado para accionar el botón de emergencia desde el control de operador. Evite accidentes";
    
    public static final String CSV_FILEPATH="C:\\Users\\juanh\\OneDrive\\Escritorio\\TPP\\";
    public enum testStates {
        INITIAL(),
        PLC_CONNECTED(),
        POWER_CONNECTED(),
        TEST_PARAMETER_LOAD(),
        TEST_PARAMETER_READY(),
        TEST_RUNNING(),
        TEST_END(),
    }
    
    /*
     * simulator_axis.actualTorque.value
     * simulator_axis.actorData.actualSpeed
     */
    /*  */
}

