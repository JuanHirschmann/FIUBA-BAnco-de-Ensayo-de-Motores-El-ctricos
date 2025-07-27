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
    public static final String DEFAULT_SERVER_ADDRESS = "http://192.168.214.1/soap/opcxml/";
    //public static final String DEFAULT_SERVER_ADDRESS = "http://169.254.11.22/soap/opcxml";
    
    public static final String LOCKFILE_PATHNAME = "/SCDIN/lockfile/SCDin.lock";
    public static final String SEVERAL_INSTANCE_DETECTED_MSG = "\"Otra instancia de SCDin se está ejecutando. Por favor, finalice el proceso para poder abrir una nueva instancia. \"";
    public static final String CONTROL_NOT_CONNECTED_MSG = "El control no está conectado. Verifique la configuración IP";
    public static final String KEEPALIVE_FAILURE_MSG = "Se detectó la desconexión del control. Verifique la conexión Ethernet.";
    public static final String NO_FILE_SELECTED_MSG="Seleccione un archivo en formato CSV (tiempo[ms],torque[Nm])";
    public static final String FILE_EXCEEDS_MAX_TORQUE_MSG="El archivo elegido supera el torque máximo del sistema (26Nm).";
    public static final String FILE_EXCEEDS_MIN_COMMAND_DELTA_MSG="El archivo elegido tiene uno o más comandos de tiempo con separación menor a 100ms";
    public static final String UNKNOWN_FILE_FORMAT_MSG="La última fila del archivo CSV tiene un formato desconocido.";
    public static final String UNKNOWN_PARAMETER_FORMAT_MSG="Los coeficientes de la ecuación cupla-velocidad deben especificarse con separador decimal punto (.).";
    public static final String NEGATIVE_INERTIA_MSG="El valor del término inercial no puede ser negativo";
    public static final String LARGE_INERTIA_MSG="El valor del término inercial no puede ser mayor a "+"0.2";
    public static final String UNKNOWN_ENDTIME="Ingrese el tiempo de finalización del ensayo como un número entero en milisegundos.";
    public static final String LARGE_BIAS_MSG="El valor del término constante no puede exceder los "+"23 Nm";
    public static final String SELF_SUSTAINED_NO_FILE_SELECTED_MSG="Seleccione un archivo en formato CSV (tiempo[ms],velocidad[RPM]).";
    public static final String SELF_SUSTAINED_FILE_EXCEEDS_MAX_SPEED_MSG="El archivo elegido supera la máxima del eje (3000RPM).";
    public static final String SELF_SUSTAINED_FILE_EXCEEDS_MIN_COMMAND_DELTA_MSG="El archivo elegido tiene uno o más comandos de tiempo con separación menor a 100ms (Velocidad)";
    
    public static final String TARGET_IP_LABEL="Path objetivo: ";
    public static final String STOP_TIME_LABEL="Tiempo de fin [ms]: ";
    public static final String TEST_PERIODS_LABEL="Cantidad de periodos a ensayar: ";
    public static final String POWER_INDICATOR_LABEL="POTENCIA";
    public static final String CONNECTION_INDICATOR_LABEL="CONEXIÓN";
    public static final String STATUS_INDICATOR_LABEL="NO INICIADO";
    public static final String LOADED_INDICATOR_LABEL="ENSAYO LISTO";
    public static final String SAVEFILE_NAME_DATE_FORMAT="yyyyMMdd HHmmss";
    public static final String APP_NAME="SCDin - Simulador de Carga Dinámica";
    public static final String EQUATION_IMAGE_PATH="equation.png";//src\\main\\resources\\
    public static final String ICON_IMAGE_PATH="icon.png";//src\\main\\resources\\
    public static final String DANGER_TEXTURE_PATH="danger_texture.jpg";//src\\main\\resources\\
    public static final int MIN_COMMAND_DELTA=100;//[MS]
    public static final int MAX_SPEED=3000;//RPM
    public static final int MAX_TORQUE=23;//
    
    public static final String SELF_SUSTAINED_MODE_WARNING="Se seleccionó el modo de ensayo autocontenido. En este modo, ambos ejes conectados al banco están habilitados para realizar movimiento. Verifique que ningún objeto esté en la cercanía de los ejes. No desatienda el equipo, esté preparado para accionar el botón de emergencia desde el control de operador. Evite accidentes";
    //TODO: IMPLEMENTAR ESTO
    public static final String PRE_START_WARNING="Al seleccionar '"+ START_BUTTON_LABEL+ "' el movimiento comenzará. Verifique que ningún objeto esté en la cercanía de los ejes. No desatienda el equipo, esté preparado para accionar el botón de emergencia desde el control de operador. Evite accidentes";
    
    public static final String CSV_FILEPATH="/SCDIN/Test output/";
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

