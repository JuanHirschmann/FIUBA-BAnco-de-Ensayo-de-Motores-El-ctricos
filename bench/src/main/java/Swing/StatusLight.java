package Swing;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusLight extends JPanel{
    boolean on=false;
    JLabel label= new JLabel();
    Color savedColor=new Color(1,1,1);
    
    /** 
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.fillOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        super.paintComponent(g);
        g.setColor(savedColor);
    }
    public StatusLight(String labelText)
    {
        this.setBackground(Color.RED);;
        label.setText(labelText);
        add(label);
    }
    
    /** 
     * @param new_state
     */
    void setState(boolean new_state)
    {   
        on=new_state;
        if(!on)
        {
            savedColor=Color.RED;
        }
    }
    void setColor(Color color)
    {
        this.setBackground(color);
    }
}
