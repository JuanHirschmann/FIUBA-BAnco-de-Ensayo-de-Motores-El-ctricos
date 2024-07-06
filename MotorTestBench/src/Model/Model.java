package Model;

import java.net.ConnectException;

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

    /* public Model() {
        new Model();
    } */
    
    org.opcfoundation.webservices.XMLDA._1_0.ServiceStub mySimotionWebService;
    public static void main(String[] args) {
        new Model();
    }

    public void connect(String URL) throws Exception
    {
        try {
    		mySimotionWebService = new org.opcfoundation.webservices.XMLDA._1_0.
            	ServiceStub(new java.net.URL(URL), null);
            //mySimotionWebService.setUsername(sUName); //credentials
            //mySimotionWebService.setPassword(sPWD);
        }
        catch (Exception exception) {
            //exception.printStackTrace();
         	if (exception.getMessage().toString().indexOf("protocol") != -1){
                boolean isConnected=true;
                System.err.println("Estoy aca");
         		if(!isConnected){
                    
	         		throw new ConnectException("Connection error while trying to connect to URL");
	         	}else {
                    throw new ConnectException("Connection error while trying to connect to new URL. Previous connection will remain active");
	         		//bLockStatusLabel = true;
	         	}
         	}
        }
    }
}