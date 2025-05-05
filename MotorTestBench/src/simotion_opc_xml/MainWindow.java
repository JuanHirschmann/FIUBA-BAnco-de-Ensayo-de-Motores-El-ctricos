package simotion_opc_xml;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.xml.namespace.QName;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.opcfoundation.webservices.XMLDA._1_0.ServiceStub;
import org.opcfoundation.webservices.XMLDA._1_0.*;  //from WSDL2Java emitter

/**
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Organisation: Siemens AG</p>
 *
 * @author RD NRH RHN A&D B18 AZ
 * @version 1.1
 */

public class MainWindow extends JFrame {
	public boolean isConnected, bLockStatusLabel;
	static boolean bWindowsLookAndFeel;
	
    String textVar = new String("Value: ");

    //declaration of graphical elements
    JSplitPane jPanelWorkspace = new JSplitPane();
    JPanel jPanelTop = new JPanel();
    JPanel jPanelBottom = new JPanel();

    JButton jButtonUNamePWD = new JButton("   PWD   ");
    JLabel jLabelUrl = new JLabel(" Url:");

    //standard URL can be entered here
    JTextField jTextFieldURL = new JTextField("http://169.254.11.22/soap/opcxml/",25);
    //JTextField jTextFieldURL = new JTextField("http://192.168.214.1/soap/opcxml/",25);
    String sDefaultURL = jTextFieldURL.getText();
    
    JButton jButtonConnect = new JButton(" Connect ");
    JLabel jLabelItemPath = new JLabel(" ItemPath:");
    JTextField jTextFieldItemPath = new JTextField("", 15);
    JLabel jLabelItemName = new JLabel(" ItemName:");
    JTextField jTextFieldItemName = new JTextField("", 18);
    JButton jButtonSubscribe = new JButton ("Subscribe");
    JButton jButtonReadCyclic = new JButton("ReadCyclic");
    JLabel jLabelVar = new JLabel(textVar);
    JLabel jLabelVar2 = new JLabel(textVar);
    JTextField jTextFieldVar = new JTextField("", 15);
    JButton jButtonWrite = new JButton("   Write   ");
    JTextField JTextFieldVarValue = new JTextField("", 21);

    //status bar
    JPanel jPanelStatus = new JPanel();
    public static JLabel jLabelStatus = new JLabel("");

    //Simotion OPC XML browser
    TaggedDefaultMutableTreeNode root = new TaggedDefaultMutableTreeNode("/"); //browser root entry
    JTree jTreeBrowser = new JTree(root);
    JScrollPane jScrollPaneBrowser = new JScrollPane(jTreeBrowser);
    BrowseTreeWillExpandListener myTreeWillExpandListener = new BrowseTreeWillExpandListener();
    BrowseTreeSelectionListener myBrowseTreeListener = new BrowseTreeSelectionListener();

    //Simotion WebService
    org.opcfoundation.webservices.XMLDA._1_0.ServiceStub mySimotionWebService;
	String sUName = new String("simotion");
	String sPWD = new String("simotion");

    public MainWindow(int width, int height) {
        try {
        	isConnected = false;
    		bLockStatusLabel = false;
    		
            //windows settings
        	setSize(width,height);
        	setResizable(true);
            setLocation(10, 10);
            setTitle("Simotion OPC XML-DA Browser");
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            jTextFieldURL.setBackground(Color.orange); //orange --> no connection
            JTextFieldVarValue.setEditable(false);

            //Add Components to Panels
            jPanelTop.add(jLabelUrl);
            jPanelTop.add(jTextFieldURL);
            jPanelTop.add(jButtonConnect);
            jPanelTop.add(jLabelItemPath);
            jPanelTop.add(jTextFieldItemPath);
            jPanelTop.add(jLabelItemName);
            jPanelTop.add(jTextFieldItemName);
            jPanelBottom.add(jButtonUNamePWD);
            jPanelBottom.add(jButtonSubscribe);
            jPanelBottom.add(jButtonReadCyclic);
            jPanelBottom.add(jLabelVar);
            jPanelBottom.add(jTextFieldVar);
            jPanelBottom.add(jButtonWrite);
            jPanelBottom.add(jLabelVar2);
            jPanelBottom.add(JTextFieldVarValue);
            jPanelWorkspace.setTopComponent(jPanelTop);
            jPanelWorkspace.setBottomComponent(jPanelBottom);
            jPanelWorkspace.setOrientation(JSplitPane.VERTICAL_SPLIT);
            jPanelStatus.add(jLabelStatus);

            JPanel contentPane = (JPanel) getContentPane();
            contentPane.add(jPanelWorkspace, "North");
            contentPane.add(jScrollPaneBrowser, "Center");
            contentPane.add(jPanelStatus, "South");

            //input handler
                      
            jTextFieldURL.addActionListener(new ActionListener() {
	            public void actionPerformed(final ActionEvent actionEvent) {
	            	if (jButtonConnect.isEnabled()) jTextFieldURL_actionPerformed(actionEvent);
	            }
            });
            
            jButtonConnect.addActionListener(new ActionListener() {
	            public void actionPerformed(final ActionEvent actionEvent) {
	       	    	jTextFieldURL_actionPerformed(actionEvent);
            	}
            });
            
            jTextFieldItemPath.addActionListener(new ActionListener() {
	            public void actionPerformed(final ActionEvent actionEvent) {
	            	jItemFields_ActionPerformed(actionEvent);
	            }
            });
            
            jTextFieldItemName.addActionListener(new ActionListener() {
	            public void actionPerformed(final ActionEvent actionEvent) {
	            	jItemFields_ActionPerformed(actionEvent);
	            }
            });
            
            jButtonSubscribe.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                	jButtonSubscribe_actionPerformed(actionEvent);
                }
            });
            jTextFieldVar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    jTextFieldVar_actionPerformed(actionEvent);
                }
            });
            jButtonWrite.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    jTextFieldVar_actionPerformed(actionEvent);
                }
            });
            jButtonReadCyclic.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    jButtonReadCyclic_actionPerformed(actionEvent);
                }
            });
            jButtonUNamePWD.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    jButtonUNamePWD_actionPerformed (actionEvent);
                }
            });

            jTreeBrowser.addTreeWillExpandListener(myTreeWillExpandListener);
            jTreeBrowser.addTreeSelectionListener(myBrowseTreeListener);

            //create dummy child so root can be expanded
            TaggedDefaultMutableTreeNode dummyEntry = new TaggedDefaultMutableTreeNode("dummy");
            root.add(dummyEntry);
            
            //Set up Simotion WebService
            mySimotionWebService_SetUp ();
            setVisible(true);
        }
        catch (Exception exception) {
        	exception.printStackTrace();
        }
    }

    /**
     * sets up a new WebService 
     * initializes mySimotionWebService
     */
    private void mySimotionWebService_SetUp () {
    	try {
    		mySimotionWebService = new org.opcfoundation.webservices.XMLDA._1_0.
            	ServiceStub(new java.net.URL(jTextFieldURL.getText()), null);
            mySimotionWebService.setUsername(sUName); //credentials
            mySimotionWebService.setPassword(sPWD);
        }
        catch (Exception exception) {
            //exception.printStackTrace();
         	if (exception.getMessage().toString().indexOf("protocol") != -1){
         		if(!isConnected){
	         		jLabelStatus.setText("Connection error while trying to connect to new URL. Unkown protocol. Trying to connect to default URL \"" + sDefaultURL +  "\" instead");
	         		
	         	}else {
	         		jLabelStatus.setText("Connection error while trying to connect to new URL. Unkown protocol. Keeping last connection alive!");
	         		bLockStatusLabel = true;
	         	}
         	}
        }
    }

    /**
     * Sets up defined application state, clears all textfields
     */
    private void setupDefinedAppPoint (boolean SetUpMySimoWebService) {
    	
    	jTextFieldURL.setBackground(Color.orange); //orange --> no connection

        root.removeAllChildren(); 	//destroy browse tree
        TaggedDefaultMutableTreeNode dummyEntry = new TaggedDefaultMutableTreeNode("dummy");
        root.add(dummyEntry); 		//and create dummy child so root can be expanded
        jTreeBrowser.updateUI(); 	//make changes visible
      	
        jTextFieldItemName.setText("");
        jTextFieldItemPath.setText("");
        
        if (SetUpMySimoWebService) {
        	mySimotionWebService_SetUp();
        }
        isConnected = false;
    }

    /**
     * input handler for "Connect" button
     * Sets a safe status, then calls browse-method to get root of server's variables
     */
    private void jTextFieldURL_actionPerformed(ActionEvent actionEvent) {
    	
//    	System.out.println("Connecting to SIMOTION OPC XML-DA server...");
    	jLabelStatus.setText("Trying to connect to OPC server...");

    	jTextFieldURL.setBackground(Color.orange);
    	setupDefinedAppPoint(true);
    	
/*	   Component frame = null;
	   JOptionPane.showMessageDialog(frame,
	   "Trying to connect to OPC server...");
*/
    	new Thread() {
	  		public void run() {
   				jButtonConnect.setEnabled(false);
   				browse("", "", root);
   				jButtonConnect.setEnabled(true);
   				jTreeBrowser.updateUI();
    	    }
	    }.start();
		
    	// browse children of root node
    		// and display content !
    }

    /**
     * input handler for "write" button
     * reads out the "ItemPath" and "ItemVale" textfields to get the item to write. Then calls method "write"
     * to write this item from the server, if ItemName and ItemPath are not null
     */
    private void jTextFieldVar_actionPerformed(ActionEvent actionEvent) {
        try {
            //var1 = Integer.parseInt(jTextFieldVar.getText());
            //jLabelVar_withValue.setText(textVar + jTextFieldVar.getText());//var1);
            String itemPath = jTextFieldItemPath.getText();
            String itemName = jTextFieldItemName.getText();
            System.out.print(itemPath);
            System.out.print(itemName);

            if ((itemPath != null) && (itemPath.length() > 0) &&
                (itemName != null) && (itemName.length() > 0)){
            	if (jTextFieldVar.getText().length() > 0) {
            		write(jTextFieldItemPath.getText(), jTextFieldItemName.getText(),jTextFieldVar.getText()); //var1);
            	}
            	else jLabelStatus.setText("Please enter a value to write to OPC Server");
            } else jLabelStatus.setText("Please choose an item to write to OPC Server");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /**
     * input handler for "write" button
     * reads out the "ItemPath" and "ItemVale" textfields to get the item to write. Then calls method "write"
     * to write this item from the server, if ItemName and ItemPath are not null
     */
    private void jItemFields_ActionPerformed(ActionEvent actionEvent) {
        try {
            String itemPath = jTextFieldItemPath.getText();
            String itemName = jTextFieldItemName.getText();
            if ((itemPath != null) && (itemPath.length() > 0) &&
                (itemName != null) && (itemName.length() > 0)) {
            	JTextFieldVarValue.setText(read(itemPath, itemName));
            	//jLabelStatus.setText("Item \"" + itemName + "\" read from OPC Server");
            } else jLabelStatus.setText("Please choose an item to write to OPC Server");
        }
        catch (Exception exception) {
        	jLabelStatus.setText("Error reading specified item");
        }
    }
    
    

    /**
     * workaround for correct array output in the browsing area
     * @param sString String which format has to be corrected
     */
    private String IsArrayElemConverter (String sString) {
        StringBuffer sbBuffer = new StringBuffer(sString);
        String sTmp = new String();
        boolean bTmp;
        int i;

        for (i = sbBuffer.length(); i > 0; i--)
        {
            sTmp = sbBuffer.substring(i-1, i);
            bTmp = sTmp.startsWith("[");
            if (bTmp) {
                sbBuffer.append(" ]");
                break;
            }
            bTmp = sTmp.startsWith("]");
            if (bTmp) {
              sbBuffer = (new StringBuffer("[")).append(sbBuffer);
              break;
          }

        }
        return sbBuffer.toString();
    }

    /**
     * Button 'Subscription' has been clicked: create new object "Subscription" if ItemName and 
     * ItemPath are not null
     */
    private void jButtonSubscribe_actionPerformed(ActionEvent actionEvent){
        String itemPath = jTextFieldItemPath.getText();
        String itemName = jTextFieldItemName.getText();  
        
        if ((itemPath != null) && (itemPath.length() > 0) &&
            (itemName != null) && (itemName.length() > 0)){
        	
        	jLabelStatus.setText("Subscription of " + itemName);
        	Subscription sub = null;
            
        	sub = new Subscription(this, itemPath, itemName);
        	sub.mySimotionWebService = mySimotionWebService;
            sub.show();
            
            if (sub.mySimotionWebService == null) {
                mySimotionWebService_SetUp();
            }
        } else jLabelStatus.setText("Please choose an item to start Subscription");
    }    

    /**
     * button 'readCyclic' has been clicked: create new object "CyclicRead" if ItemName and 
     * ItemPath are not null 
     */
    private void jButtonReadCyclic_actionPerformed(ActionEvent actionEvent) {
        String itemPath = jTextFieldItemPath.getText();
        String itemName = jTextFieldItemName.getText();

        if ((itemPath != null) && (itemPath.length() > 0) &&
            (itemName != null) && (itemName.length() > 0)) {

        	jLabelStatus.setText("Cyclic Read of " + itemName);
        	CyclicRead cr = new CyclicRead(this, itemName);

            cr.mySimotionWebService = mySimotionWebService;
            cr.sItemPath = itemPath;

            cr.show();

            if (cr.mySimotionWebService == null) {
                mySimotionWebService_SetUp();
            }
        } else jLabelStatus.setText("Please choose an item to start Cyclic Read");
    }

    /**
     * change username and password
     */
    private void jButtonUNamePWD_actionPerformed (ActionEvent actionEvent) {
        
    	new PWDWindow(this);

        mySimotionWebService.setUsername(sUName);
        mySimotionWebService.setPassword(sPWD);
    }

    /**
     * tree node shall be expanded; browse children now from Simotion
     */
    class BrowseTreeWillExpandListener implements javax.swing.event.TreeWillExpandListener {
        public void treeWillExpand(TreeExpansionEvent p_event) throws ExpandVetoException {
            try {
                if ( ( (TaggedDefaultMutableTreeNode) p_event.getPath().getLastPathComponent()).equals(root)) {
                    browse("", "", root); //browse children of root node
                    //in some cases the tree is not refreshed and so the new content
                    //will not be displayed correctly
                }
                else {
                    String itemPath = ( (TaggedDefaultMutableTreeNode) p_event.getPath().getLastPathComponent()).getItemPath();
                    String itemName = ( (TaggedDefaultMutableTreeNode) p_event.getPath().getLastPathComponent()).getItemName();
                    if ((itemPath != null) && (itemPath.length() > 0) &&
                      (itemName != null) && (itemName.length() >= 0)) {
                        //browse children of this node
                        browse(itemPath, itemName, ( (TaggedDefaultMutableTreeNode) p_event.getPath().getLastPathComponent()));
                    }
                }
            }
            catch (Exception exception) {
            	System.out.println("no expand...");
            	//exception.printStackTrace();
            }
        }

        //has to be here
        public void treeWillCollapse(TreeExpansionEvent p_event) {
        }
    }

    /**
     * tree node has been selected; read data of child (items) now
     */
    class BrowseTreeSelectionListener implements javax.swing.event.TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent p_event) {
            try {
                if (((TaggedDefaultMutableTreeNode) p_event.getPath().getLastPathComponent()).equals(root)) {
                    getStatus(); //just get server status if root has been selected
                }
                else {
                    if (((TaggedDefaultMutableTreeNode) p_event.getPath().getLastPathComponent()).isLeaf()) {
                        String itemPath = ((TaggedDefaultMutableTreeNode) p_event.getPath().getLastPathComponent()).getItemPath();;
                        String itemName = ((TaggedDefaultMutableTreeNode) p_event.getPath().getLastPathComponent()).getItemName();;
                        if ((itemPath != null) && (itemPath.length() > 0) &&
                          (itemName != null) && (itemName.length() > 0)) {
                            jTextFieldItemPath.setText(itemPath);
                            jTextFieldItemName.setText(itemName);
                            //and read value of this child (item)
                            String var = read(jTextFieldItemPath.getText(), jTextFieldItemName.getText());
                            JTextFieldVarValue.setText(var);
                        }
                    }
                }
            }
            catch (Exception exception) {
            	System.out.println("no tree....");
            }
        }
    }

    /**
     * get status of Simotion WebService
     */
    private void getStatus() {
       try {
            org.opcfoundation.webservices.XMLDA._1_0.GetStatus parameters = new org.opcfoundation.webservices.XMLDA._1_0.GetStatus();
            parameters.setLocaleID("en");

            org.opcfoundation.webservices.XMLDA._1_0.GetStatusResponse getStatusResp = null;
            getStatusResp = mySimotionWebService.getStatus(parameters);
            if (getStatusResp != null) {
                ServerStatus serverStatus = getStatusResp.getStatus();
                String simotionServerStatus = serverStatus.getStatusInfo();
                System.out.println(simotionServerStatus);
                jLabelStatus.setText("ServerStatus: " + simotionServerStatus);
            }
            else {
                jLabelStatus.setText("getStatus == null");
            }
        }
        catch (Exception exception) {
            //exception.printStackTrace();
            setupDefinedAppPoint(true);
            jLabelStatus.setText("Error reading status");
        }
    }

    /**
     * Reads the item specified by "itemPath" and "itemName" from server. Therefore objects of rype
     * RequestOptions, ReadRequestItemList, ReadRequestItem[] and ReadRequestItem are created and initialized.
     * Object "readResponse" is the return value of the OPC-function read and contains te current value of the read item
     *  
     * @param itemPath specifies the item to read
     * @param itemName specifies the item to read
     * @return Vaue of read item
     */
    private String read(String itemPath, String itemName) {
        String retVal = new String("");
        jLabelStatus.setText("Reading  " + itemName + "...");
        try {
            org.opcfoundation.webservices.XMLDA._1_0.RequestOptions options = new org.opcfoundation.webservices.XMLDA._1_0.RequestOptions();
            org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList itemList = new org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList();
            org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem[] requestItems = new org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem[1];
            org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem requestItem = new org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem();
            options.setLocaleID("en");
            //options.setClientRequestHandle("i");
            //options.setReturnErrorText(true);
            //options.setReturnDiagnosticInfo(true);
            //options.setReturnItemTime(true);
            //options.setReturnItemPath(true);
            //options.setReturnItemName(true);
            //options.setRequestDeadline();
            requestItem.setItemPath(itemPath);
            requestItem.setItemName(itemName);
            //requestItem.setClientItemHandle("j");
            requestItems[0] = requestItem;
            itemList.setItems(requestItems);

            org.opcfoundation.webservices.XMLDA._1_0.ReadResponse readResponse = null;
            readResponse = mySimotionWebService.read(options, itemList /*, readResult, RItemList, errors*/);
            if (readResponse != null) {
/*
                org.opcfoundation.webservices.XMLDA._1_0.ReplyBase readResult = readResponse.getReadResult();
                ServerState serverState = readResult.getServerState();
                String simotionServerState = serverState.getValue();
                System.out.println(simotionServerState);
                jLabelStatus.setText(simotionServerState);
*/				
                org.opcfoundation.webservices.XMLDA._1_0.ReplyItemList RItemList = readResponse.getRItemList();
                if (RItemList != null) {
                    if (RItemList.getItems().length > 0) {
                        org.opcfoundation.webservices.XMLDA._1_0.ItemValue item = RItemList.getItems(0);
                        String itemValueString = item.getValue().toString();
                        retVal = itemValueString;
                        QName valueTypeQualifier = item.getValueTypeQualifier();
                        String valueTypeQualifierString = "";
                        if (valueTypeQualifier != null)
                          valueTypeQualifierString = valueTypeQualifier.getLocalPart();
                        //System.out.println("Value: " + itemValueString + "  Qualifier: " + valueTypeQualifierString);
                    }
                }
            }
            else {
                System.out.println("readResponse == null");
                jLabelStatus.setText("readResponse == null");
            }
        }
        catch (Exception exception) {
            jLabelStatus.setText("Error reading item \"" + itemName + "\"");
        }
        return retVal;
    }

    
    /**
     * Writes the item specified by "itemPath" and "itemName" to server. Therefore objects of rype
     * Write, RequestOptions and WriteRequestItemList are created and initialized.
     * Object "writeResponse" is the return value of the OPC-function write.
     * 
     * @param itemPath specifies the item to read
     * @param itemName specifies the item to read
     * @param value item value to be written to server
     */
    
    private void write(String itemPath, String itemName, String value) {
        jLabelStatus.setText("Writing  " + itemName + "...");
        try {

            org.opcfoundation.webservices.XMLDA._1_0.Write parameters = new org.opcfoundation.webservices.XMLDA._1_0.Write();
            org.opcfoundation.webservices.XMLDA._1_0.RequestOptions options = new org.opcfoundation.webservices.XMLDA._1_0.RequestOptions();
            org.opcfoundation.webservices.XMLDA._1_0.WriteRequestItemList itemList = new org.opcfoundation.webservices.XMLDA._1_0.WriteRequestItemList();
            options.setLocaleID("en");
            //options.setClientRequestHandle("i");
            //options.setReturnErrorText(true);
            //options.setReturnDiagnosticInfo(true);
            //options.setReturnItemTime(true);
            //options.setReturnItemPath(true);
            //options.setReturnItemName(true);
            //options.setRequestDeadline();
            parameters.setOptions(options);
            org.opcfoundation.webservices.XMLDA._1_0.ItemValue[] items = new ItemValue[1];
            org.opcfoundation.webservices.XMLDA._1_0.ItemValue item = new ItemValue();
            item.setItemPath(itemPath);
            item.setItemName(itemName);
            item.setValueTypeQualifier(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string")); //"int"));
            String str = new String("" + value);
            item.setValue(str);
            //item.setClientItemHandle("j");
            items[0] = item;
            itemList.setItems(items);
            parameters.setItemList(itemList);

            org.opcfoundation.webservices.XMLDA._1_0.WriteResponse writeResponse = null;
            writeResponse = mySimotionWebService.write(parameters);
            if (writeResponse != null) {
                String var = read(itemPath, itemName);
                JTextFieldVarValue.setText(var);
                jLabelStatus.setText("Item Value \"" + var + "\" written to OPC Server");
                
            }
            else {
                System.out.println("writeResponse == null");
                jLabelStatus.setText("writeResponse == null");
            }
        } 

        catch (Exception exception) {
            exception.printStackTrace();
            setupDefinedAppPoint(true);
            jLabelStatus.setText("Error writing  " + itemName);
        }
   }

    /**
     * Browses the items provided by server an creates a grahical tree-structure depending in 
     * itemPath and itemName.
     * 
     * @param itemPath 
     * @param itemName
     * @param node basic tree node
     */
    private void browse(String itemPath, String itemName, TaggedDefaultMutableTreeNode node) {
    	
        try {
            org.opcfoundation.webservices.XMLDA._1_0.Browse parameters = new org.opcfoundation.webservices.XMLDA._1_0.Browse();
            parameters.setItemPath(itemPath);
            parameters.setItemName(itemName);
            parameters.setLocaleID("en");
            //parameters.setClientRequestHandle("i");
            //parameters.setReturnAllProperties(false);
            //parameters.setReturnErrorText(true);
            //parameters.setReturnPropertyValues(false);
            //parameters.setMaxElementsReturned(10);

            //repetition necessary for more than 32 elements
            //max number submitted elements = 32
            org.opcfoundation.webservices.XMLDA._1_0.BrowseResponse browseResponse;
            boolean bFirstRepetition = true;

            String sTmp = new String();
            do {
                browseResponse = null;
                browseResponse = mySimotionWebService.browse(parameters);
                parameters.setContinuationPoint(browseResponse.getContinuationPoint());
                if (browseResponse != null) {
                    jTextFieldURL.setBackground(Color.green); //green --> connection etablished
                    //org.opcfoundation.webservices.XMLDA._1_0.ReplyBase browseResult = browseResponse.getBrowseResult();
                    org.opcfoundation.webservices.XMLDA._1_0.BrowseElement[]
                    elements = browseResponse.getElements();
                    //org.opcfoundation.webservices.XMLDA._1_0.OPCError[] errors;
                    //java.lang.String continuationPoint;  // attribute

                    if (elements != null) {
                        if (elements.length > 0 && bFirstRepetition) {
                            node.removeAllChildren(); //remove old entries or dummy
                            bFirstRepetition = false;
                        }
                        for (int i = 0; i < elements.length; i++) {
                            TaggedDefaultMutableTreeNode newNode;

                            sTmp = elements[i].getName();
                            sTmp = IsArrayElemConverter(sTmp);
                            newNode = new TaggedDefaultMutableTreeNode(sTmp);

                            //newNode = new TaggedDefaultMutableTreeNode(elements[
                              //i].getName());
                            ( (TaggedDefaultMutableTreeNode) newNode).
                              setItemPath(
                                elements[i].getItemPath());
                            ( (TaggedDefaultMutableTreeNode) newNode).
                              setItemName(
                                elements[i].getItemName());
                            node.add(newNode);
                            if (elements[i].isHasChildren()) {
                                TaggedDefaultMutableTreeNode dummyEntry = new
                                  TaggedDefaultMutableTreeNode("dummy");
                                newNode.add(dummyEntry);
                            }
                        }
                        if (isConnected) jLabelStatus.setText("Browsing  " + itemPath + " " + itemName + "..."); 
                        else if (!bLockStatusLabel) jLabelStatus.setText("Connected to SIMOTION OPC XML-DA server"); 
                        isConnected = true;
                    }
                }
                else {
                    jLabelStatus.setText("browseResponse == null");
                }
            } while ( browseResponse.isMoreElements());
        }
        catch (Exception exception) {
            setupDefinedAppPoint(true);
            jLabelStatus.setText("Error connecting to SIMOTION");
            //System.out.println("Mess : " + exception.getMessage());
            //System.out.println("Cause: " + exception.getCause());
            if (exception.getMessage().equals("(404)Not Found")) jLabelStatus.setText("Connection error. Please check URL"); 
            else if (exception.getMessage().equals("(400)Bad Request")) jLabelStatus.setText("Connection error. Please check URL"); 
            else {
	            if (exception.getCause().toString().indexOf("Bad envelope tag") != -1) 
	            	 jLabelStatus.setText("Connection error. Please check URL. It seems you did not enter \" /soap/opcxml/ \" after SIMOTION's IP address");
	            if (exception.getCause().toString().indexOf("Connection timed out") != -1) 
	            	jLabelStatus.setText("Connection error. Please check IP address");
	            if (exception.getCause().toString().indexOf("java.net.UnknownHostException") != -1) 
	            	jLabelStatus.setText("Connection error. Please check IP address");
            }
        }
        bLockStatusLabel = false;
    }
    
    //extends DefaultMutableTreeNode; new tags to hold Simotion itemPath and itemName
    class TaggedDefaultMutableTreeNode extends DefaultMutableTreeNode {
        private String m_itemPath;
        private String m_itemName;

        public TaggedDefaultMutableTreeNode(String param) {
            super(param);
        }
        public String getItemPath() {
            return m_itemPath;
        }
        public void setItemPath(String itemPath) {
            m_itemPath = itemPath;
        }
        public String getItemName() {
            return m_itemName;
        }
        public void setItemName(String itemName) {
            m_itemName = itemName;
        }
    }
    
	/**
	 * main application method to be exected wat startup
	 * @param args
	 */
    public static void main(String[] args) {
        bWindowsLookAndFeel = false;
        if (args.length == 1){
        	if (args [0].equals("1")) bWindowsLookAndFeel = true;
        } 
 
    	try {
            if (bWindowsLookAndFeel) UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception exception) {
        	exception.printStackTrace();
        	bWindowsLookAndFeel = false;
        }

        if (bWindowsLookAndFeel) new MainWindow(760, 500);  // Breite/H�he
        else new MainWindow(940, 500);
    }
}
