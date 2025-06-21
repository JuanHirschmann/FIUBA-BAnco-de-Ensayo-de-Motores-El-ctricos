package Swing;
import static Views.Constants.DANGER_TEXTURE_PATH;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class StatusPanel extends JPanel {

    BufferedImage background;
    
    public StatusPanel() {
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout( new GridLayout(1,4,0,0));
        try{
            background=ImageIO.read(new File(DANGER_TEXTURE_PATH));
                 
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

            g.drawImage(background, 0, 0, getWidth(), getHeight()*8, null); // image scaled
        }
    }
 
    
}