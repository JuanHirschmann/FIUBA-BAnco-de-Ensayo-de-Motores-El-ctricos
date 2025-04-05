package simotion_opc_xml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.opcfoundation.webservices.XMLDA._1_0.RequestOptions;


/**
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Organisation: Siemens AG</p>
 *
 * @author RD NRH RHN A&D B18 AZ
 * @version 1.1
 */

class Subscription extends JDialog{

	// indicates if SubscriptionPolledRefreshThread is running
	boolean running;
	
	// MainPanel object
	JPanel JPanelMain;
	JPanel JPanelLeft;
	JPanel JPanelCenter;
	JPanel JPanelRight;
	JPanel JPanelBottom;

    // create graphical elements
	JLabel jLabelItem;
    JLabel jLabelValue;
    JLabel jLabelHoldtime;
    JLabel jLabelWaittime;
    JLabel jLabelStatus;
    
    JTextField jTextFieldItem;
    JTextField jTextFieldValue;
    JTextField jTextFieldHoldTime;
    JTextField jTextFieldWaitTime;
    JTextField jTextFieldStatus;
    
    JButton jButtonSubscribe;
    JButton jButtonExit;

    String sItemPath = "";
    String sItemName = "";
    
    public static Calendar holdTimeCalendar;


/////////////////////////////////////////////////////////////////////////////////////////////////////////
// Declaration of datatypes needed for subscription:
/////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // only one item subscribe
    String [] serverSubHandles = new String [1];
    String serverSubHandle = "";

    // create new web-service
    org.opcfoundation.webservices.XMLDA._1_0.ServiceStub mySimotionWebService;
    
    // create RequestOptions object for passing options to web service actions
    org.opcfoundation.webservices.XMLDA._1_0.RequestOptions options = new RequestOptions();

    // create item objects to pass to the "Subscribe"-object
    org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItem     subItem     = new org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItem();
    org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItem[]   subItems    = new org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItem[1];
    org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItemList reqItemList = new org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItemList();

    // create parameters to pass to to the "Subscribe"-, "SubscriptionPolledRefresh"-,
    // "SubscriptionCancel" - objects
    org.opcfoundation.webservices.XMLDA._1_0.Subscribe                 subParameters       = new org.opcfoundation.webservices.XMLDA._1_0.Subscribe();
    org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefresh subPolledParameters = new org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefresh();
    org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancel        subCancelParameters = new org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancel();
 
    // Returned items of subscribePolledRefresh object
    org.opcfoundation.webservices.XMLDA._1_0.SubscribePolledRefreshReplyItemList subPolledRefreshItem = new org.opcfoundation.webservices.XMLDA._1_0.SubscribePolledRefreshReplyItemList();
    org.opcfoundation.webservices.XMLDA._1_0.SubscribePolledRefreshReplyItemList[] subPolledRefreshItemList = new org.opcfoundation.webservices.XMLDA._1_0.SubscribePolledRefreshReplyItemList[1];
    
    // Create Thread to Start/Stop subscription
    SubscriptionPolledRefreshThread subPolledRefreshThread;
    
/////////////////////////////////////////////////////////////////////////////////////////////////// 
// End declaration datatypes needed for subscription
///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * constructor of Class SubscriptionPolledRefreshThread
     * 
     * @param owner relative Frame
     * @param ItemPath itemPath of the item to be subscribed
     * @param ItemName itemName of te item to be subscribed
     */
    public Subscription (JFrame owner, String ItemPath, String ItemName) {
       super (owner, "Subscription of \"" + ItemName + "\"", true);
       setSize(400, 200);
       setResizable(false);
       setLocationRelativeTo(owner);
       running = false;		// public variable to indicate if SubscriptionPolledRefreshThread is running
       
       /**
        * sets action to be executed if "X" is pressed to close the windows.
        */
       setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
       addWindowListener( new WindowAdapter() {  
           public void windowClosing( WindowEvent e ) {  
        	   closeWindow();
           }  
       }); 
       
       sItemPath = ItemPath;
       sItemName = ItemName;

/////////////////////////////////////////////////////////////////////////////////////////////////////////
//     Initialising datatypes needed for Subscription:
/////////////////////////////////////////////////////////////////////////////////////////////////////////       
       
       mySimotionWebService = null;
       
       //Calendar requestDeadline = new Calendar();
	   //requestDeadline.set(2007, 04, 17);
    	
	   options.setLocaleID("en");
	   options.setClientRequestHandle("ClientReqHandle");
	   //options.setRequestDeadline(requestDeadline);
	   options.setReturnErrorText(true);
	   options.setReturnDiagnosticInfo(true);
	   options.setReturnItemTime(true);
	   options.setReturnItemPath(true);
	   options.setReturnItemName(true);
	           
	   subItem.setClientItemHandle("ClientItemHandle");
	   //subItem.setDeadband(0);  	// Range from 0 to 100 percent. Indicates when the server is 
	   								// supposed to indicate a change of this item. Default: 0
	   subItem.setEnableBuffering(false);
	   subItem.setItemName(sItemName);
	   subItem.setItemPath(sItemPath);
	   //subItem.setReqType(reqType);
	   subItem.setRequestedSamplingRate(500);   // sampling rate at which the server should check for value changes
		
	   // only one item to subscribe
	   subItems[0] = subItem;
	   
	   // to set itemPath for entire itemList (if some items in the same itemPath are used)
	   // is is not neccassary to set for each item, but can be set like that:
	   // reqItemList.setItemPath(sItemPath);
	   
	   reqItemList.setItems(subItems);
	   reqItemList.setEnableBuffering(false);
   		
	   subParameters.setOptions(options);
	   subParameters.setItemList(reqItemList);
	   subParameters.setReturnValuesOnReply(true);
	   subParameters.setSubscriptionPingRate(20000); 	// time (in ms) the server waits before terminating subscription 
	   													// AFTER last subscriptionPolledRefresh-call
	   													// IMPORTANT: This is NOT the time setting for web server 
	   													//			  response. The web server's time-out value may
	   													//            be smaller (e.g. SIMOTION D435: app. 10 secs)
	   
	   
/////////////////////////////////////////////////////////////////////////////////////////////////////////
//     End initialising datatypes needed for Subscription
/////////////////////////////////////////////////////////////////////////////////////////////////////////  
	   

       // initialize Panel objects
	   JPanelMain = new JPanel();
       JPanelMain.setLayout(new BorderLayout());
       
       JPanelLeft   = new JPanel();
       JPanelRight  = new JPanel();
       JPanelBottom = new JPanel();
       
       JPanelLeft.setLayout(new GridLayout(5, 1, 6, 6));
       JPanelRight.setLayout(new GridLayout(5, 1, 6, 6));
       
       // initialize graphical elements
       jLabelItem = new JLabel("Item Name: ");
       jTextFieldItem = new JTextField (sItemName, 15);
       jTextFieldItem.setEditable(false);
       
       jLabelValue = new JLabel ("Item Value: ");
       jTextFieldValue = new JTextField ("", 4);
       jTextFieldValue.setEditable(false);

       jLabelHoldtime = new JLabel("Holdtime (ms): ");
       jTextFieldHoldTime = new JTextField ("2000", 4); 
     
       jLabelWaittime = new JLabel("Waittime (ms):    ");
       jTextFieldWaitTime = new JTextField ("3000", 4);
       
       jLabelStatus = new JLabel("Status:");
       jTextFieldStatus = new JTextField ("Status messages", 4);
       jTextFieldStatus.setEditable(false);
       
       jButtonSubscribe = new JButton ("Subscribe");	
       jButtonExit = new JButton("Exit");
       
       // add graphical elements to MainPanel
       JPanelLeft.add(jLabelItem);
       JPanelRight.add(jTextFieldItem);
       
       JPanelLeft.add(jLabelValue);
       JPanelRight.add(jTextFieldValue);
       
       JPanelLeft.add(jLabelHoldtime);
       JPanelRight.add(jTextFieldHoldTime);
       
       JPanelLeft.add(jLabelWaittime);
       JPanelRight.add(jTextFieldWaitTime);
       
       JPanelLeft.add(jLabelStatus);
       JPanelRight.add(jTextFieldStatus);
       
       JPanelBottom.add(jButtonSubscribe);
       JPanelBottom.add(jButtonExit);
       
       JPanelMain.add(JPanelLeft, BorderLayout.WEST);
       JPanelMain.add(JPanelRight, BorderLayout.CENTER);
       JPanelMain.add(JPanelBottom, BorderLayout.SOUTH);
       
       JPanel contentPane = (JPanel) getContentPane();
       contentPane.add(JPanelMain, "Center");
       
       /**
        * Input handler for exiting Subscription. Calls "closeWindows"-method
        */
       jButtonExit.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent actionEvent) {
        	   closeWindow();
           }
       });
       
       /**
        * Input handler for "subscribe" button. The actions executed are:
        * - change button label
        * - disable/enable TextFields and the "Exit"-Button
        * - call the "subscribeChosenItem"-method to subscribe the item
        * - start/stop the subscriptionPolledRefresh-thread to monitor the value of subscribed item
        * - sets status message in "JTextFieldStatus"
        */
       
       jButtonSubscribe.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent actionEvent) {
        	   int wt, ht; //waitTime, Holdtime
        	   wt = Integer.valueOf( jTextFieldWaitTime.getText() ).intValue();
        	   ht = Integer.valueOf( jTextFieldHoldTime.getText() ).intValue();
        	   
        	   // To avoid web server time out, WaitTime and HoldTime must not 
        	   // exceed a combined value of 10 seconds
        	   if ((wt + ht) > 10000){
        		   jTextFieldStatus.setText("Combined time has to be smaller than 10.000 ms");
        	   } else {
        	   
        	   if (jButtonSubscribe.getText().equals(new String ("Subscribe"))){
               	   
        		   if ((wt < 1000) && (ht < 1000)){
            		   Component frame = null;
            		   JOptionPane jOptPane = new JOptionPane();
            		   JOptionPane.showMessageDialog(frame,
            				   "Please note that update rates smaller than 1000 ms will not be updated in the GUI!",
            				   "Warning",
            				   JOptionPane.WARNING_MESSAGE);
            	   } 
        		   
        		   jButtonSubscribe.setText("Cancel subscription"); 
        		   jTextFieldWaitTime.setEnabled(false);
        	 	   jTextFieldHoldTime.setEnabled(false);
        		   jTextFieldStatus.setText("Subscription started");
        		   
     			   running = true;
     			   subPolledRefreshThread = new SubscriptionPolledRefreshThread();
     			   
     			   try {
     				   subscribeChosenItem();
     			   }
     			   catch (RemoteException e) {
						System.out.println("Exception in registering subscribe");
     			   }   

     			  subPolledRefreshThread.setPriority( Thread.MAX_PRIORITY );
     			  subPolledRefreshThread.start();
//     			  System.out.println("Subscription started");
        		   
        	   }else {
        	 	   
        		   jButtonSubscribe.setText("Subscribe");
        	 	   jTextFieldWaitTime.setEnabled(true);
        	 	   jTextFieldHoldTime.setEnabled(true);
        		   
        	 	   running = false;
            	   
        	 	   subCancelParameters.setServerSubHandle(serverSubHandle);
        	 	   org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancelResponse subCancelResp = null;
    	         
        	 	   try {
       	 			   subCancelResp = mySimotionWebService.subscriptionCancel(subCancelParameters);
        	 	   } catch (RemoteException e) {
        	 		   System.out.println("Subscription already cancelled from server side");
        	 	   }
    	       
        	 	   if (subCancelResp != null){
//        	 		   System.out.println ("Subscription cancelled");
        	 		   jTextFieldStatus.setText("Subscription cancelled");
        	 	   }
        	   }}
           }
       });
    }

    /**
     * Executes the needed steps to colse the Subscription window. These are:
     *  - cancelling the subscription
     *  - stopping the subscritptionPolledRefreshThread
     *  - hiding (closing) the window itself
     */
    
    public void closeWindow() {
    	subCancelParameters.setServerSubHandle(serverSubHandle);
    	org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancelResponse subCancelResp = null;
  
    	try {
 		   if (mySimotionWebService != null){
 			   subCancelResp = mySimotionWebService.subscriptionCancel(subCancelParameters);
 		   } else System.out.println("Subscription already cancelled from server");
    	} catch (RemoteException e) {}
    	
//    	System.out.println("Subscription cancelled");
 	    System.out.println("Frame closed");
    	running = false;
    	setVisible(false);
	}
    
    
    /**
     * Method which executes the "subscribe" method and thereby registers the current 
     * item for Subscription.
     * 
     * @throws RemoteException
     */

	public void subscribeChosenItem() throws RemoteException
    {
	    org.opcfoundation.webservices.XMLDA._1_0.SubscribeResponse subscribeResponse = null;
 		subscribeResponse = mySimotionWebService.subscribe(subParameters);

 		if (subscribeResponse != null) {
	
 			serverSubHandle = subscribeResponse.getServerSubHandle();
 			holdTimeCalendar = subscribeResponse.getSubscribeResult().getReplyTime();
			 			
 			serverSubHandles [0] = serverSubHandle;
	 
			if (subscribeResponse.getRItemList().getItems(0).getItemValue() != null){
				jTextFieldValue.setText(subscribeResponse.getRItemList().getItems(0).getItemValue().getValue().toString());
			}
 		}
 		else {
 			System.out.println("writeResponse == null");
 		}
    }
	
	/**
	 * Class which executes the subscriptionPolledRefresh-method. Has to be implemented in 
	 * a new thread to become aware of the break condition of the while-loop. If it wasn't 
	 * a new Thread, the application would hang-up in the while-loop because it would not 
	 * recognize the change of the break condition.
	 *
	 */
	
	public class SubscriptionPolledRefreshThread extends Thread{

		int counter, holdTime, waitTime;
				
	    public void run() {
	    	
	    	counter = 0;
	    	holdTime = Integer.parseInt(jTextFieldHoldTime.getText());
	    	waitTime = Integer.parseInt(jTextFieldWaitTime.getText());
	    	
//	    	holdTime = (int) (holdTime * 1.105);	// Offset because Server answers earlier than
//	    	waitTime = (int) (waitTime * 1.1);		// expected...
	    	
	    	subPolledParameters.setOptions(options);
	    	subPolledParameters.setServerSubHandles(serverSubHandles);
	    	subPolledParameters.setReturnAllItems(false);
	    	subPolledParameters.setWaitTime(waitTime);

	    	org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefreshResponse subPolledRefreshResponse = null;
		            
	    	while (true){
//	    		counter++;
//				System.out.println(counter); System.out.println("---------");
	    		
	    		holdTimeCalendar.add(holdTimeCalendar.MILLISECOND, holdTime);

	    		subPolledParameters.setHoldTime(holdTimeCalendar);
	    		try {
	    			subPolledRefreshResponse = mySimotionWebService.subscriptionPolledRefresh(subPolledParameters);
	    		}
	    		catch (Exception exception) {
	    			running = false;
	    		}
	    		
	    		if (subPolledRefreshResponse != null){
	    			
	     			holdTimeCalendar = subPolledRefreshResponse.getSubscriptionPolledRefreshResult().getReplyTime();
	
		    		if (subPolledRefreshResponse.getRItemList() != null){ //Antwort enthält Item, also hat sich was geändert
//		    			System.out.println("Value change at: " + holdTimeCalendar.getTime());
		     			jTextFieldStatus.setText("Value change at: " + currentTimeOf(holdTimeCalendar));
		 				jTextFieldValue.setText(subPolledRefreshResponse.getRItemList(0).getItems(0).getValue().toString());
	    			} else if (!jTextFieldStatus.getText().equals("Subscription cancelled")){
		     			jTextFieldStatus.setText("Time expired at:  " + currentTimeOf(holdTimeCalendar));		     				
//	    				System.out.println("Time expired at:  " + holdTimeCalendar.getTime());
	    			}
	    		}
	    		else {
	    			running = false;
	    			jButtonSubscribe.setText("Subscribe");
	        	 	jTextFieldWaitTime.setEnabled(true);
	        	 	jTextFieldHoldTime.setEnabled(true);
	    			jTextFieldStatus.setText("Server timeout -> Subscription cancelled");
	    		}
	    		if (!running){
	    			break;
	    		}
	    	}
	    }    
	}	
    
    /**
     * Method to convert time format from "Calendar.getTime()-format" to "hh:mm:ss a.m./p.m."
     * 
     * @param cal Calendar to convert to time format hh:mm:ss a.m./p.m.
     * @return Time in format hh:mm:ss a.m./p.m.
     */
	
	public String currentTimeOf(Calendar cal){
    	String sTime, sHour, sMin, sSec, am_pm;
    	
    	//get current values
    	int iHour = cal.getTime().getHours();
    	int iMin  = cal.getTime().getMinutes();
    	int iSec  = cal.getTime().getSeconds();
    	
    	//convert int to String
    	sHour = "" + iHour;
    	sMin = "" + iMin;
    	sSec = "" + iSec;
    	
    	// add a.m. or p.m. to output string
    	if(iHour < 13) am_pm = "a.m.";
    	else am_pm = "p.m.";    	
    	if (iHour > 12) iHour = iHour - 12;
    	
    	//insert "0" if value is smaller than 10 (e.g. 8:7:12 becomes 08:07:12)
    	
    	if (iHour < 10) sHour = "0" + iHour;
    	if (iMin < 10) sMin = "0" + iMin;    	
    	if (iSec < 10) sSec = "0" + iSec;
    	
    	sTime = sHour + ":" + sMin + ":" + sSec + " " + am_pm;
    	return sTime;
    }
  
}
