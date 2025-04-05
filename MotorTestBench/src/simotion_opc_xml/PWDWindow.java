package simotion_opc_xml;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


/**
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Organisation: Siemens AG</p>
 *
 * @author RD NRH RHN A&D B18 AZ
 * @version 1.1
 */


class PWDWindow
extends JDialog {
  JPanel JPanelMain;
  JPanel JPanelLeft;
  JPanel JPanelRight;
  JPanel JPanelBottom;
  JTextField JTextFieldUName;
  JPasswordField JTextFieldPWD;
  JLabel JLabelUName;
  JLabel JLabelPWD;
  JButton JButtonOK;
  JButton JButtonCancel;

  JFrame JFrameOwner;

  String sUName;
  String sPWD;
  
  
/**
 * Initializes the pasword dialog window
 * @param owner LocationRelative
 */
public PWDWindow(JFrame owner) {
  super (owner, "username / password dialog", true);
  JFrameOwner = owner;
  if (MainWindow.bWindowsLookAndFeel) setSize(200, 117);
  else setSize(270, 120);
  setResizable (false);
  setLocationRelativeTo(owner);
  
  /**
   * sets action to be executed if "X" is pressed to close the windows.
   */
  setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
  addWindowListener( new WindowAdapter() {  
      public void windowClosing( WindowEvent e ) {  
    	    setVisible(false);
    	    MainWindow.jLabelStatus.setText("Password Dialog aborted");
      }  
  }); 

  //create graphical elements
  JPanelMain = new JPanel();
  JPanelLeft = new JPanel();
  JPanelRight = new JPanel();
  JPanelBottom = new JPanel();
  JTextFieldUName = new JTextField(((MainWindow)JFrameOwner).sUName, 12);
  JTextFieldPWD = new JPasswordField(((MainWindow)JFrameOwner).sPWD, 12);
  JLabelUName = new JLabel("  Username:  ");
  JLabelPWD = new JLabel  ("  Password:  ");
  JButtonOK = new JButton    ("   OK   ");
  JButtonCancel = new JButton("Cancel");
  
  JPanelLeft.setLayout(new GridLayout(2, 1, 5, 6));
  JPanelRight.setLayout(new GridLayout(2, 1, 5, 6));

  JPanelLeft.add(JLabelUName);
  JPanelLeft.add(JLabelPWD);
  JPanelLeft.add(JButtonOK);
  
  JPanelRight.add(JTextFieldUName);
  JPanelRight.add(JTextFieldPWD);
  JPanelRight.add(JButtonCancel);
  
  JPanelBottom.add(JButtonOK);
  JPanelBottom.add(JButtonCancel);
  
  JPanelMain.setLayout(new BorderLayout());
  JPanelMain.add(JPanelLeft, BorderLayout.WEST);
  JPanelMain.add(JPanelRight, BorderLayout.CENTER);
  JPanelMain.add(JPanelBottom, BorderLayout.SOUTH);
  
  JPanel contentPane = (JPanel) getContentPane();
  contentPane.add(JPanelMain);

  //Input Handler
  JButtonOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
          JButtonOK_actionPerformed(actionEvent);
      }
  });
  JButtonCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
          JButtonCancel_actionPerformed(actionEvent);
      }
  });
  JTextFieldPWD.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
          jTextFieldPWD_actionPerformed(actionEvent);
      }
  });

  //start Password Dialog
  MainWindow.jLabelStatus.setText("Password Dialog started");
  setVisible(true);
}

//get values for password and username
/**
 * reads usename/password from the input mask and set it to sUName and sPWD
 */
private void ReadOutUNamePWD () {
	
    sUName = JTextFieldUName.getText();

    //get the password from the JPasswordField
    char[] cTmp = JTextFieldPWD.getPassword();
    sPWD = "";
    for (int i = 0; i < cTmp.length; i++) {
        sPWD += cTmp[i];
    }

    System.out.println("Username entered: " + sUName);
    System.out.println("PWD entered: " + sPWD);

    ((MainWindow)JFrameOwner).sUName = sUName;
    ((MainWindow)JFrameOwner).sPWD = sPWD;
}


//finish password dialog and take over changes
private void JButtonOK_actionPerformed(ActionEvent actionEvent){
    setVisible(false);
    MainWindow.jLabelStatus.setText("Password Dialog finished");

    ReadOutUNamePWD();
}

//abort password dialog
private void JButtonCancel_actionPerformed(ActionEvent actionEvent){
    setVisible(false);
    MainWindow.jLabelStatus.setText("Password Dialog aborted");
}

//finish password dialog and take over changes
private void jTextFieldPWD_actionPerformed(ActionEvent actionEvent) {
    setVisible(false);
    MainWindow.jLabelStatus.setText("Password Dialog finished");

    ReadOutUNamePWD();
}

}