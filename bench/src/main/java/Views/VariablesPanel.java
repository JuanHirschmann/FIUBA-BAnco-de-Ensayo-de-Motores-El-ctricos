package Views;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Model.Constants.testTypes;

import static Model.Constants.BROWSE_FILE_BUTTON_LABEL;
import static Model.Constants.WRITE_CSV;

import java.awt.Dimension;
import java.awt.FlowLayout;

public class VariablesPanel extends JPanel{
    public VariablesPanel()
    {
        setLayout(new WrapLayout());
        this.setPreferredSize(new Dimension(1100,100));
    }
    
}
