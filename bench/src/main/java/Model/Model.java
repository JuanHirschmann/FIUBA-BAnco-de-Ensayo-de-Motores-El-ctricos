package Model;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.namespace.QName;
import static Model.Constants.VAR_PATH;
//import static Model.Constants.TEST_VAR;
import static Model.Constants.TORQUE_SETPOINT;
import static Model.Constants.TORQUE_TIME_BUFFER_SIZE;
import static Model.Constants.TORQUE_TIME_VALUES;
//import static Model.Constants.RUN;
import static Model.Constants.SOFTWARE_KILLSWITCH;
import static Model.Constants.SOFTWARE_START_BUTTON;
import static Model.Constants.SOFTWARE_STOP_BUTTON;
import static Model.Constants.TEST_RUNTIME;
import static Model.Constants.TIMESTAMP;
import static Model.Constants.TORQUE_VS_TIMESTAMP_SELECTED;
import static Model.Constants.TORQUE_VS_SPEED_SELECTED;
import static Model.Constants.CLEAR_TO_RECIEVE;
import static Model.Constants.DUT_CLEAR_TO_RECEIVE;
import static Model.Constants.DUT_CLEAR_TO_RECIEVE;
import static Model.Constants.ENABLE_ACTIVE_LINEMODULE;
import static Model.Constants.ENABLE_SIMULATOR_AXIS;
//import static Model.Constants.LOAD_AXIS_SPEED_SETPOINT;
//import static Model.Constants.NEW_SPEED_SETPOINT_LOAD_AXIS;
import static Model.Constants.OPERATION_MODE;
import static Model.Constants.SAVE_TO_BUFFER;
import static Model.Constants.AXIS_ENABLED_SIGNAL;
import static Model.Constants.CLEAR_TO_RECEIVE;
import static Model.Constants.TEST_STATUS;

import org.opcfoundation.webservices.XMLDA._1_0.ItemValue;

import Model.Constants.serverSideTestStatus;
import Swing.TorqueEquationParameter;
import Views.Constants.testStates;

public class Model {

    org.opcfoundation.webservices.XMLDA._1_0.ServiceStub mySimotionWebService;
    boolean isConnected = false;

    /**
     * @param args
     */
    

    public serverSideTestStatus getTestStatus() throws ConnectException {
        String serverSideTestState=this.readVar(VAR_PATH, TEST_STATUS);
        serverSideTestStatus testState=serverSideTestStatus.NOT_STARTED;
        if (serverSideTestState.equalsIgnoreCase("0")) {
            testState=serverSideTestStatus.RUNNING;
        } else if (serverSideTestState.equalsIgnoreCase("1")) {
            testState=serverSideTestStatus.STOPPED;
        } else if (serverSideTestState.equalsIgnoreCase("2")) {
            testState=serverSideTestStatus.ENDED;
        } else if (serverSideTestState.equalsIgnoreCase("3")) {
            testState=serverSideTestStatus.NOT_STARTED;
        } else if (serverSideTestState.equalsIgnoreCase("4")) {
            testState=serverSideTestStatus.EMERGENCY_STOP;
        } else {
            testState=serverSideTestStatus.READY_TO_START;
        }
        System.out.println(serverSideTestState);
        return testState;
    }

    public boolean ALM_is_active() throws ConnectException {
        boolean output = false;
        if (this.readVar(VAR_PATH, AXIS_ENABLED_SIGNAL).equalsIgnoreCase("TRUE")) {
            output = true;
        }
        return output;
    }

    /**
     * @param URL OPC XML-DA Server URL to connect to
     * @throws Exception Throws exception when not posible to connect to new server
     */
    public void connect(String URL) throws Exception {
        try {
            mySimotionWebService = new org.opcfoundation.webservices.XMLDA._1_0.ServiceStub(new java.net.URL(URL),
                    null);
        } catch (Exception exception) {
            // exception.printStackTrace();
            // mySimotionWebService=null;
            if (exception.getMessage().toString().indexOf("protocol") != -1) {
                this.isConnected = false;
                if (!isConnected) {

                    throw new ConnectException("Connection error while trying to connect to URL");
                } else {
                    throw new ConnectException(
                            "Connection error while trying to connect to new URL. Previous connection will remain active");
                    // bLockStatusLabel = true;
                }
            }
        }
        // Checks if known variable exists
        try {
            isConnected = true; // Force readVar
            this.readVar(VAR_PATH, OPERATION_MODE);
        } catch (Exception e) {
            System.out.println(URL);
            System.out.println(e);
            isConnected = false;
        }
    }

    /**
     * @return boolean
     */
    public boolean isConnected() {
        return this.isConnected;
    }

    /**
     * Reads a variable and returns value
     * 
     * @param varPath path in server where target variable is stored
     * @param varName name of target variable in server
     * @return String value of target variable
     * @throws ConnectException Exception thrown when target variable not found or
     *                          server connection wasn't established
     */
    public String readVar(String varPath, String varName) throws ConnectException {
        String returnVal = new String();
        if (!this.isConnected()) {
            throw new ConnectException("No connection established");
        }
        try {
            org.opcfoundation.webservices.XMLDA._1_0.RequestOptions options = new org.opcfoundation.webservices.XMLDA._1_0.RequestOptions();
            org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList itemList = new org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList();
            org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem[] requestItems = new org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem[1];
            org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem requestItem = new org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem();
            options.setLocaleID("en");
            requestItem.setItemPath(varPath);
            requestItem.setItemName(varName);
            requestItems[0] = requestItem;
            itemList.setItems(requestItems);
            org.opcfoundation.webservices.XMLDA._1_0.ReadResponse readResponse = null;
            readResponse = mySimotionWebService.read(options, itemList /* , readResult, RItemList, errors */);
            if (readResponse != null) {
                org.opcfoundation.webservices.XMLDA._1_0.ReplyItemList RItemList = readResponse.getRItemList();
                if (RItemList != null) {
                    if (RItemList.getItems().length > 0) {
                        org.opcfoundation.webservices.XMLDA._1_0.ItemValue item = RItemList.getItems(0);
                        String itemValueString = item.getValue().toString();
                        returnVal = itemValueString;
                        QName valueTypeQualifier = item.getValueTypeQualifier();
                        String valueTypeQualifierString = "";
                        if (valueTypeQualifier != null)
                            valueTypeQualifierString = valueTypeQualifier.getLocalPart();
                        // System.out.println("Value: " + itemValueString + " Qualifier: " +
                        // valueTypeQualifierString);
                    }
                }
            } else {
                System.err.println("readResponse == null");
                throw new ConnectException("No hubo respuesta");
                // jLabelStatus.setText("readResponse == null");
            }
        } catch (Exception exception) {

            System.out.println(exception);

            System.out.println("No se encontró la variable " + varName + " en el path: " + varPath);
            throw new ConnectException("No se encontró la variable " + varName + " en el path: " + varPath);
            // jLabelStatus.setText("Error reading item \"" + itemName + "\"");
        }
        return returnVal;
    }

    /**
     * Writes variable on OPC server
     * 
     * @param varValue Variable value as String
     * @param varPath  Variable Path on OPC Server Root
     * @param varName  Variable name
     * @throws ConnectException
     */
    public void writeVar(String varValue, String varPath, String varName) throws ConnectException {

        try {

            org.opcfoundation.webservices.XMLDA._1_0.Write parameters = new org.opcfoundation.webservices.XMLDA._1_0.Write();
            org.opcfoundation.webservices.XMLDA._1_0.RequestOptions options = new org.opcfoundation.webservices.XMLDA._1_0.RequestOptions();
            org.opcfoundation.webservices.XMLDA._1_0.WriteRequestItemList itemList = new org.opcfoundation.webservices.XMLDA._1_0.WriteRequestItemList();
            options.setLocaleID("en");
            parameters.setOptions(options);
            org.opcfoundation.webservices.XMLDA._1_0.ItemValue[] items = new ItemValue[1];
            org.opcfoundation.webservices.XMLDA._1_0.ItemValue item = new ItemValue();
            item.setItemPath(varPath);
            item.setItemName(varName);
            item.setValueTypeQualifier(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string")); // "int"));
            String str = new String("" + varValue);
            item.setValue(str);
            // item.setClientItemHandle("j");
            items[0] = item;
            itemList.setItems(items);
            parameters.setItemList(itemList);

            org.opcfoundation.webservices.XMLDA._1_0.WriteResponse writeResponse = null;
            writeResponse = mySimotionWebService.write(parameters);
            if (writeResponse == null) {
                System.err.println("readResponse == null");
                throw new ConnectException("No fue posible la escritura de la variable");
                // jLabelStatus.setText("readResponse == null");
            }
        } catch (Exception exception) {
            System.out.println("Toy aca2");
            throw new ConnectException("No se encontró la variable " + varName + " en el path: " + varPath);
            // jLabelStatus.setText("Error reading item \"" + itemName + "\"");
        }
    }

    public void writeArray(List<String> varValue, String varPath, String varName, int maxIndex, int offset)throws ConnectException {
        System.err.println("Entre a Write array");
        try {

            org.opcfoundation.webservices.XMLDA._1_0.Write parameters = new org.opcfoundation.webservices.XMLDA._1_0.Write();
            org.opcfoundation.webservices.XMLDA._1_0.RequestOptions options = new org.opcfoundation.webservices.XMLDA._1_0.RequestOptions();
            org.opcfoundation.webservices.XMLDA._1_0.WriteRequestItemList itemList = new org.opcfoundation.webservices.XMLDA._1_0.WriteRequestItemList();
            options.setLocaleID("en");
            parameters.setOptions(options);
            org.opcfoundation.webservices.XMLDA._1_0.ItemValue[] items = new ItemValue[maxIndex];
            
            
            
            System.out.print("offset: ");
            System.err.println(offset);
            for(int i=0;i<maxIndex;i++)
            {   
                org.opcfoundation.webservices.XMLDA._1_0.ItemValue item = new ItemValue();
                item.setItemPath(varPath);
                int bufferIndex=i+offset;
                item.setItemName(varName+ "[" + bufferIndex + "]");
                item.setValueTypeQualifier(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string")); // "int"));
                String str = new String("" + varValue.get(i));
                item.setItemPath(varPath);
                item.setValue(str);
                items[i] = item;

            }
            System.err.println(items);
            // item.setClientItemHandle("j");
            itemList.setItems(items);
            parameters.setItemList(itemList);

            org.opcfoundation.webservices.XMLDA._1_0.WriteResponse writeResponse = null;
            writeResponse = mySimotionWebService.write(parameters);
            if (writeResponse == null) {
                System.err.println("readResponse == null");
                throw new ConnectException("No fue posible la escritura de la variable");
                // jLabelStatus.setText("readResponse == null");
            }
        } catch (Exception exception) {
            System.out.println("Toy aca2");
            System.out.println(exception.getMessage());
            throw new ConnectException("No se encontró la variable " + varName + " en el path: " + varPath);
            // jLabelStatus.setText("Error reading item \"" + itemName + "\"");
        }
    }

    /**
     * Sends emergency stop signal to PLC.
     * 
     * @throws ConnectException
     */
    public void emergencyStop() throws ConnectException {
        writeVar("TRUE", VAR_PATH, SOFTWARE_KILLSWITCH);
    }

    /**
     * Sends emergency stop signal to PLC.
     * 
     * @throws ConnectException
     */
    public void emergencyRelease() throws ConnectException {
        writeVar("FALSE", VAR_PATH, SOFTWARE_KILLSWITCH);
    }

    /**
     * Releases emergency stop signal from PLC.
     * 
     * @throws ConnectException
     */
    public void realeaseEmergency() throws ConnectException {
        writeVar("FALSE", VAR_PATH, SOFTWARE_KILLSWITCH);
    }

    /**
     * Changes Active Line Module state.
     * 
     * @param state if true linemodule will be enabled, if false it will be
     *              disabled.
     * @throws ConnectException
     */
    private void enableLineModule(boolean state) throws ConnectException {
        if (state) {
            writeVar("TRUE", VAR_PATH, ENABLE_ACTIVE_LINEMODULE);
        } else {
            writeVar("FALSE", VAR_PATH, ENABLE_ACTIVE_LINEMODULE);
        }
    }

    /**
     * Changes simulator axis enable state
     * 
     * @param state if true axis will be enabled, if false it will be disabled.
     *              This method doesn't check current axis state or line module
     *              state.
     * @throws ConnectException
     */
    private void enableSimulatorAxis(boolean state) throws ConnectException {
        if (state) {
            writeVar("TRUE", VAR_PATH, ENABLE_SIMULATOR_AXIS);
        } else {
            writeVar("FALSE", VAR_PATH, ENABLE_SIMULATOR_AXIS);
        }
    }

    /**
     * Sets controller in RUN mode
     * 
     * @throws ConnectException
     */
    public void controllerOn() throws ConnectException {

        writeVar("_RUN", VAR_PATH, OPERATION_MODE);
    }

    /**
     * Sets controller in STOP mode
     * 
     * @throws ConnectException
     */
    public void controllerOff() throws ConnectException {

        writeVar("_STOP", VAR_PATH, OPERATION_MODE);
    }

    /**
     * Enables line module and simulator axis, expects to have a delay between both
     * events programmed on PLC side
     * 
     * @throws ConnectException
     */
    public void powerOn() throws ConnectException {
        this.enableLineModule(true);
        this.enableSimulatorAxis(true);
    }

    /**
     * Writes FALSE to SOFTWARE_START variable in PLC
     * 
     * @throws ConnectException
     */
    public void stop() throws ConnectException {

        writeVar("TRUE", VAR_PATH, SOFTWARE_STOP_BUTTON);

        writeVar("FALSE", VAR_PATH, SOFTWARE_START_BUTTON);
    }

    /**
     * Writes TRUE to SOFTWARE_START variable in PLC
     * 
     * @throws ConnectException
     */
    // TODO: esto tarda una barbaridad y se cuelga. Implementar el reset de Software
    // stop en PLC
    public void start() throws ConnectException {
        // TODO Meter un retardo de 10ms
        writeVar("FALSE", VAR_PATH, SOFTWARE_STOP_BUTTON);
        writeVar("TRUE", VAR_PATH, SOFTWARE_START_BUTTON);
    }

    /**
     * Returns communication buffer Clear to Receive status from PLC.
     * 
     * @return boolean if TRUE, PLC communication buffer is clear to receive.
     */
    public boolean bufferCTR() {
        boolean output = false;
        try {
            String CTR = readVar(VAR_PATH, CLEAR_TO_RECEIVE);
            if (CTR == "true") {
                output = true;
            }
        } catch (ConnectException e) {
            System.out.println(e.getMessage());
        }
        return output;
    }
    /**
     * Returns communication buffer Clear to Receive status from PLC.
     * 
     * @return boolean if TRUE, PLC communication buffer is clear to receive.
     */
    public boolean DUTBufferCTR() {
        boolean output = false;
        try {
            String CTR = readVar(VAR_PATH, DUT_CLEAR_TO_RECEIVE);
            if (CTR == "true") {
                output = true;
            }
        } catch (ConnectException e) {
            System.out.println(e.getMessage());
        }
        return output;
    }

    /**
     * Writes a List of values to the PLC's internal buffer. Doesn't check for
     * preconditions. Both lists are asumed to be same length, values should be
     * numeric type.
     * Timestamps intervals should be larger than 100ms.
     * 
     * @param timestamp timestamp to apply torque. Timestamp[i] should correspond to
     *                  torque[i]
     * @param torque    Torque to be applied. Timestamp[i] should correspond to
     *                  torque[i]
     * @throws ConnectException
     */
    public void writeBuffer(List<String> timestamp, List<String> torque) throws Exception {
        System.err.println("write buffer");
        System.err.println(timestamp.size());
        // TODO: si esta bien implementado, el control mide tiempo decreciente y corta
        for (int i = 0; i < timestamp.size(); i++) {
            // TODO: Ver bien como se llaman los vectores
            System.err.println(i);
            writeVar(torque.get(i), VAR_PATH, TORQUE_TIME_VALUES + "[" + i + "]");
            writeVar(timestamp.get(i), VAR_PATH, TIMESTAMP + "[" + i + "]");
            /*
             * if (timestamp.size() <= i) {
             * writeVar("0", VAR_PATH, TORQUE_TIME_VALUES + "[" + i + "]");
             * writeVar("0", VAR_PATH, TIMESTAMP + "[" + i + "]");
             * 
             * } else {
             * 
             * }
             */
        }
        this.saveToBuffer();
        System.err.println("Escribi buffer");
    }

    /**
     * @throws ConnectException
     */
    //
    public void selectTorqueVsSpeed() throws ConnectException {

        writeVar("FALSE", VAR_PATH, TORQUE_VS_TIMESTAMP_SELECTED);
        writeVar("TRUE", VAR_PATH, TORQUE_VS_SPEED_SELECTED);
    }

    /**
     * Writes Torque Equation parameters onto OPC Server
     * 
     * @param parameters Dictionary of String - TorqueEquationParameter pairs to be
     *                   written.
     * @throws ConnectException
     */
    public void setTorqueVsSpeedParameters(Map<String, TorqueEquationParameter> parameters)
            throws ConnectException {
        for (String key : parameters.keySet()) {
            System.err.println(parameters.get(key).getVarPath());

            System.err.println(parameters.get(key).getVarName());

            System.err.println(parameters.get(key).getValue());
            writeVar(parameters.get(key).getValue(), parameters.get(key).getVarPath(),
                    parameters.get(key).getVarName());
        }
    }

    /**
     * @param endtime_ms
     * @throws ConnectException
     */
    public void setTestEndTime(String endtime_ms) throws ConnectException {

        writeVar(endtime_ms, VAR_PATH, TEST_RUNTIME);
    }

    /**
     * Writes TRUE to TORQUE_FROM_TIMESTAMP_SELECTED variable on PLC.
     * 
     * @throws ConnectException
     */
    public void selectTorqueVsTime() throws ConnectException {

        writeVar("TRUE", VAR_PATH, TORQUE_VS_TIMESTAMP_SELECTED);
        writeVar("FALSE", VAR_PATH, TORQUE_VS_SPEED_SELECTED);
    }
    /**
     * Writes TRUE to TORQUE_FROM_TIMESTAMP_SELECTED variable on PLC.
     * 
     * @throws ConnectException
     */
    public void selectMixedTest() throws ConnectException {

        writeVar("TRUE", VAR_PATH, TORQUE_VS_TIMESTAMP_SELECTED);
        writeVar("TRUE", VAR_PATH, TORQUE_VS_SPEED_SELECTED);
    }
    /**
     * Disables simulator axis and shutdowns line module
     * 
     * @throws ConnectException
     */
    public void powerOff() throws ConnectException {
        this.enableSimulatorAxis(false);
        this.enableLineModule(false);
        writeVar("_STOP", VAR_PATH, OPERATION_MODE);
    }

    /**
     * Sets user defined setpoint, doesn't check for new command flag
     * 
     * @param torque_setpoint Torque setpoint in Nm
     */
    public void setTorqueSetpoint(Float torque_setpoint) throws ConnectException {
        writeVar(torque_setpoint.toString(), VAR_PATH, TORQUE_SETPOINT);
    }

    public void saveToBuffer() throws Exception {
        this.writeVar("TRUE", VAR_PATH, SAVE_TO_BUFFER);
        Thread.sleep(50);
        this.writeVar("FALSE", VAR_PATH, SAVE_TO_BUFFER);

    }
    public static void main(String[] args)
    {
        
        Model model=new Model();
       
        try
        {
            model.connect("http://169.254.11.22/soap/opcxml");
            List<String> values=new ArrayList<String>();
            for(int i=0;i<10;i++)
            {
                values.add(String.valueOf(i));
            }
            //model.writeArray(values, VAR_PATH, TIMESTAMP, 10);

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}