package Model;

import java.net.ConnectException;
import java.util.concurrent.ExecutionException;

import javax.xml.namespace.QName;

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
}