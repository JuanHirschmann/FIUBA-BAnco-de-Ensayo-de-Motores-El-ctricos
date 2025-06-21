package Swing;
import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.JPanel;

public class BasicBackgroundPanel extends JPanel
{
    @SuppressWarnings("unused")
    private Image background;
 
    public BasicBackgroundPanel(Image background)
    {
        this.background = background;
        setLayout( new BorderLayout() );
    }
 
    
}
