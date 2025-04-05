package simotion_opc_xml;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem;

import simotion_opc_xml.CyclicRead.ReadCyclicTimer.RemindTask;


/**
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Organisation: Siemens AG</p>
 *
 * @author RD NRH RHN A&D B18 AZ
 * @version 1.1
 */

class CyclicRead extends JDialog {
    JPanel JPanelMain;
    JPanel JPanelLeft;
    JPanel JPanelRight;
    JPanel JPanelBottom;
    
    JLabel jLabelInterval;
    JTextField jTextFieldInterval;
    JLabel jLabelCount;
    JTextField jTextFieldCount;
    JLabel jLabelVar;
    JTextField jTextFieldLabelVar;
    JLabel jLabelVal;
    JTextField jTextFieldVar;
    JButton jButtonStartStop;
    JButton jButtonCancel;

    String sItemPath = "";
    String sItemName = "";

    ReadCyclicTimer myTimer; //for cyclic read

    org.opcfoundation.webservices.XMLDA._1_0.ServiceStub mySimotionWebService;

    public CyclicRead(JFrame owner, String ItemName) {
       super (owner, "Cyclic Read - " + ItemName, true);
       setSize(320, 170);
       setResizable(false);
       setLocationRelativeTo(owner);
       setDefaultCloseOperation(HIDE_ON_CLOSE);

       JPanelMain = new JPanel();
       JPanelLeft = new JPanel();
       JPanelRight = new JPanel();
       JPanelBottom = new JPanel();

       mySimotionWebService = null;
       sItemName = ItemName;

       //create graphical elements
       jLabelInterval = new JLabel(" Interval ms: ");
       jTextFieldInterval = new JTextField("500",4);
      
       jLabelCount = new JLabel   (" Read Count: ");
       jTextFieldCount = new JTextField("0",4);
       jTextFieldCount.setEditable(false);
       
       jLabelVar = new JLabel (" Item name: ");
       jTextFieldLabelVar = new JTextField (sItemName, 13);
       jTextFieldLabelVar.setEditable(false);
       
       jLabelVal = new JLabel (" Item value: ");
       jTextFieldVar = new JTextField("", 13);
       jTextFieldVar.setEditable(false);
       
       jButtonStartStop = new JButton("Start");
       jButtonCancel = new JButton("Exit ");

       
       JPanelLeft.setLayout(new GridLayout(4, 1, 5, 6));
       JPanelRight.setLayout(new GridLayout(4, 1, 5, 6));  
       
       JPanelLeft.add(jLabelInterval);
       JPanelLeft.add(jLabelCount);
       JPanelLeft.add(jLabelVar);
       JPanelLeft.add(jLabelVal);
      
       JPanelRight.add(jTextFieldInterval);
       JPanelRight.add(jTextFieldCount);
       JPanelRight.add(jTextFieldLabelVar);
       JPanelRight.add(jTextFieldVar);
       
       JPanelBottom.add(jButtonStartStop);
       JPanelBottom.add(jButtonCancel);
       

       JPanelMain.setLayout(new BorderLayout());
       JPanelMain.add(JPanelLeft, BorderLayout.WEST);
       JPanelMain.add(JPanelRight, BorderLayout.CENTER);
       JPanelMain.add(JPanelBottom, BorderLayout.SOUTH);

       JPanel contentPane = (JPanel) getContentPane();
       contentPane.add(JPanelMain, "Center");

       //Input Handler
       jButtonStartStop.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent actionEvent) {
               JButtonStartStop_actionPerformed(actionEvent);
           }
       });
       jButtonCancel.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent actionEvent) {
               JButtonCancel_actionPerformed(actionEvent);
           }
       });

       addWindowListener(new MyWindowListener());

   }

   //start / stop cyclic read
   
    /**
     * Starts/stops cyclic reading. If started a new timer object (ReadCyclicTimer) is created.
     * If stopped timer is stopped
     */
    
    private void JButtonStartStop_actionPerformed(ActionEvent actionEvent) {
     try {
          if (jButtonStartStop.getText().equals(new String("Start"))) {
              //start timer
              if (null != mySimotionWebService) {
                  myTimer = new ReadCyclicTimer(Integer.parseInt(jTextFieldInterval.getText()));
                  jButtonStartStop.setText("Stop");
              }
          }
          else {
              //stop timer
              myTimer.StopTimer();
              myTimer = null;
              jButtonStartStop.setText("Start");
          }
      }
      catch (Exception exception) {
          jTextFieldInterval.setText("100");
          jTextFieldVar.setText("Exception occured");
          exception.printStackTrace();
      }
   }

   /**
    * Action executed when windows is closed
    */
    
    private void JButtonCancel_actionPerformed(ActionEvent actionEvent) {
       setVisible(false);
   }

   //WindowListener for close operation
   /**
    * Default closing operation of windows is specified
    */
    class MyWindowListener extends WindowAdapter {
       public void windowDeactivated (WindowEvent e)  {
         //stop timer if running
         if (myTimer != null) {
             myTimer.StopTimer();
         }
       }
   }

   //timer for cyclic read
    public class ReadCyclicTimer {
       int counter;
       Timer timer;
       org.opcfoundation.webservices.XMLDA._1_0.RequestOptions options = new org.opcfoundation.webservices.XMLDA._1_0.RequestOptions();
       org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList itemList = new org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList();
       ReadRequestItem[] requestItems = new ReadRequestItem[1];
       ReadRequestItem requestItem = new ReadRequestItem();
       org.opcfoundation.webservices.XMLDA._1_0.ReadResponse readResponse = null;
       org.opcfoundation.webservices.XMLDA._1_0.ReplyItemList RItemList;
       org.opcfoundation.webservices.XMLDA._1_0.ItemValue item;

       public ReadCyclicTimer(int milliSeconds) {
           counter = 0;
           options.setLocaleID("en");
           //options.setClientRequestHandle("i");
           //options.setReturnErrorText(true);
           //options.setReturnDiagnosticInfo(true);
           //options.setReturnItemTime(true);
           //options.setReturnItemPath(true);
           //options.setReturnItemName(true);
           //options.setRequestDeadline();
           requestItem.setItemPath(sItemPath);
           requestItem.setItemName(sItemName);
           //requestItem.setClientItemHandle("j");
           requestItems[0] = requestItem;
           itemList.setItems(requestItems);
           timer = new Timer();
           //timer.schedule(new RemindTask(), milliSeconds);
           timer.scheduleAtFixedRate(new RemindTask(), 0, milliSeconds);

       }

       public void StopTimer() {
    	   // Terminate timer thread
    	   timer.cancel(); 
       }

       /**
        * Cyclic repeated Task to read the current value of chosen item.
        *
        */
       class RemindTask extends TimerTask {
           public void run() {
               try {
                   readResponse = mySimotionWebService.read(options, itemList );   //, readResult, RItemList, errors);
                   if (readResponse != null) {
                       RItemList = readResponse.getRItemList();
                       if (RItemList != null) {
                           if (RItemList.getItems().length > 0) {
                               item = RItemList.getItems(0);
                               String itemValueString = item.getValue().toString();
                               jTextFieldVar.setText(itemValueString);
                               counter = counter + 1;
                               jTextFieldCount.setText(Integer.toString(counter));
                           }
                       }
                   }
                   else {
                       System.out.println("readResponse == null");
                  }
               }
               catch (Exception exception) {
                   timer.cancel();
                   mySimotionWebService = null;
                   jTextFieldVar.setText("Exception occured");
                   exception.printStackTrace();
               }
            }
       }
    }
}