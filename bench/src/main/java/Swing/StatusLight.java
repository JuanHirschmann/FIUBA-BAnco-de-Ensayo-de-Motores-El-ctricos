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
        
        this.setOpaque(true);
        this.setBackground(Color.GRAY);;
        label.setText(labelText);
        add(label);
    }
    public void setText(String text)
    {
        label.setText(text);
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
    public void setColor(Color color)
    {
        this.setBackground(color);
        savedColor=color;
    }
    
    public void red()
    {
        this.setBackground(Color.RED);
        savedColor=Color.RED;
    }
    
    public void yellow()
    {
        this.setBackground(Color.YELLOW);
        savedColor=Color.YELLOW;
    }

    
    public void green()
    {
        this.setBackground(Color.GREEN);
        savedColor=Color.GREEN;
    }
    public void off()
    {
        
        this.setBackground(Color.GRAY);
        savedColor=Color.GRAY;
    }
}
