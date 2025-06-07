package Swing;
import javax.swing.JPanel;

import java.awt.Component;
import java.awt.Dimension;

public class VariablesPanel extends JPanel{
    public VariablesPanel()
    {
        setLayout(new WrapLayout());
        this.setPreferredSize(new Dimension(1100,100));
    }
    
    /** 
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        for(Component component: this.getComponents())
        {
            component.setEnabled(enabled);
        }
    }
    
}
