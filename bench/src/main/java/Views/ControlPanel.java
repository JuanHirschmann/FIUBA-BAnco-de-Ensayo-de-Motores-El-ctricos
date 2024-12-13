package Views;

import static Model.Constants.CONNECT_BUTTON_LABEL;
import static Model.Constants.DEFAULT_SERVER_ADDRESS;
import static Model.Constants.EMERGENCY_STOP_BUTTON_LABEL;
import static Model.Constants.POWER_ON_BUTTON_LABEL;
import static Model.Constants.SHUTDOWN_BUTTON_LABEL;
import static Model.Constants.START_BUTTON_LABEL;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel{
    

    public ControlPanel()
    {
        setLayout(new FlowLayout());
        
        this.setPreferredSize(new Dimension(1100,45));
        
    }
}
