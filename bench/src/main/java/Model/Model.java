package Model;

import java.net.ConnectException;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.namespace.QName;
import static Model.Constants.VAR_PATH;
//import static Model.Constants.TEST_VAR;
import static Model.Constants.TORQUE_SETPOINT;
//import static Model.Constants.RUN;
import static Model.Constants.SOFTWARE_KILLSWITCH;
import static Model.Constants.SOFTWARE_START;
import static Model.Constants.SOFTWARE_STOP;
import static Model.Constants.ENABLE_ACTIVE_LINEMODULE;
import static Model.Constants.ENABLE_SIMULATOR_AXIS;
//import static Model.Constants.LOAD_AXIS_SPEED_SETPOINT;
//import static Model.Constants.NEW_SPEED_SETPOINT_LOAD_AXIS;
import static Model.Constants.OPERATION_MODE;

import org.opcfoundation.webservices.XMLDA._1_0.Browse;
import org.opcfoundation.webservices.XMLDA._1_0.BrowseElement;
import org.opcfoundation.webservices.XMLDA._1_0.BrowseResponse;
import org.opcfoundation.webservices.XMLDA._1_0.GetStatus;
import org.opcfoundation.webservices.XMLDA._1_0.GetStatusResponse;
import org.opcfoundation.webservices.XMLDA._1_0.ItemValue;
import org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem;
import org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList;
import org.opcfoundation.webservices.XMLDA._1_0.ReadResponse;
import org.opcfoundation.webservices.XMLDA._1_0.ReplyItemList;
import org.opcfoundation.webservices.XMLDA._1_0.RequestOptions;
import org.opcfoundation.webservices.XMLDA._1_0.ServerStatus;
import org.opcfoundation.webservices.XMLDA._1_0.ServiceStub;
import org.opcfoundation.webservices.XMLDA._1_0.Write;
import org.opcfoundation.webservices.XMLDA._1_0.WriteRequestItemList;
import org.opcfoundation.webservices.XMLDA._1_0.WriteResponse;

public class Model {

    org.opcfoundation.webservices.XMLDA._1_0.ServiceStub mySimotionWebService;

    /**
     * @param args
     */
    public static void main(String[] args) {
        new Model();
    }

    /**
     * @param URL OPC XML-DA Server URL to connect to
     * @throws Exception Throws exception when not posible to connect to new server
     */
    public void connect(String URL) throws Exception {
        try {
            mySimotionWebService = new org.opcfoundation.webservices.XMLDA._1_0.ServiceStub(new java.net.URL(URL),
                    null);
            // mySimotionWebService.setUsername(sUName); //credentials
            // mySimotionWebService.setPassword(sPWD);
        } catch (Exception exception) {
            // exception.printStackTrace();
            if (exception.getMessage().toString().indexOf("protocol") != -1) {
                boolean isConnected = true;
                System.err.println("Estoy aca");
                if (!isConnected) {

                    throw new ConnectException("Connection error while trying to connect to URL");
                } else {
                    throw new ConnectException(
                            "Connection error while trying to connect to new URL. Previous connection will remain active");
                    // bLockStatusLabel = true;
                }
            }
        }
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
        if (this.mySimotionWebService == null) {
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
            System.out.println("Toy aca2");
            throw new ConnectException("No se encontró la variable " + varName + " en el path: " + varPath);
            // jLabelStatus.setText("Error reading item \"" + itemName + "\"");
        }
        return returnVal;
    }

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

    /*
     * public String readTestVar() throws ConnectException
     * {
     * return readVar(VAR_PATH, TEST_VAR);
     * }
     * 
     * public String readRunVar() throws ConnectException
     * {
     * return readVar(VAR_PATH, TEST_VAR);
     * }
     * public void setLoadAxisSpeed(String speedSetpoint) throws ConnectException
     * {
     * writeVar(speedSetpoint,VAR_PATH, LOAD_AXIS_SPEED_SETPOINT);
     * writeVar("True",VAR_PATH, NEW_SPEED_SETPOINT_LOAD_AXIS);
     * }
     * public String readMotorLoadAxisSpeed() throws ConnectException
     * {
     * return readVar(VAR_PATH, TEST_VAR);
     * }
     */
    /*
     * public void run() throws ConnectException
     * {
     * writeVar("True",VAR_PATH, RUN);
     * }
     * public void stop() throws ConnectException
     * {
     * writeVar("False",VAR_PATH, RUN);
     * }
     */
    /**
     * Sends emergency stop signal to PLC.
     * 
     * @throws ConnectException
     */
    public void emergencyStop() throws ConnectException {
        writeVar("TRUE", VAR_PATH, SOFTWARE_KILLSWITCH);
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
     * Disables simulator axis and shutdowns line module
     * 
     * @throws ConnectException
     */
    public void stop() throws ConnectException {
        
        writeVar("TRUE", VAR_PATH, SOFTWARE_STOP);
        writeVar("FALSE", VAR_PATH, SOFTWARE_START);
    }

    /**
     * Disables simulator axis and shutdowns line module
     * 
     * @throws ConnectException
     */
    public void start() throws ConnectException {
        writeVar("FALSE", VAR_PATH, SOFTWARE_STOP);
        writeVar("TRUE", VAR_PATH, SOFTWARE_START);
    }

    /**
     * Disables simulator axis and shutdowns line module
     * 
     * @throws ConnectException
     */
    public void powerOff() throws ConnectException {
        this.enableSimulatorAxis(true);
        this.enableLineModule(true);
    }

    /**
     * Sets user defined setpoint, doesn't check for new command flag
     * 
     * @param torque_setpoint Torque setpoint in Nm
     */
    public void setTorqueSetpoint(Float torque_setpoint) throws ConnectException {
        writeVar(torque_setpoint.toString(), VAR_PATH, TORQUE_SETPOINT);
    }

}