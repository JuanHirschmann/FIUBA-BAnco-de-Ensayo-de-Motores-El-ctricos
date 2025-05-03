package Swing;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class StatusPanel extends JPanel {

    BufferedImage background;
    
    public StatusPanel() {
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout( new GridLayout(4,1,10,0));
        try{
            background=ImageIO.read(new File("C:\\Users\\juanh\\OneDrive\\Escritorio\\TPP\\Codigo\\Pruebas OPC V\\bench\\src\\main\\resources\\danger_texture.jpg"));
                
            }catch (Exception e)
            {
                System.out.println("no encontre");
            }
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        //g.drawImage(background , 0, 0, null); // image full size
        if(background!=null)
        {

            g.drawImage(background, 0, 0, getWidth(), getHeight(), null); // image scaled
        }
    }
 
    
}