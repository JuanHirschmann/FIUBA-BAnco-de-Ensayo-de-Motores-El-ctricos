package Swing;

import static Views.Constants.APP_NAME;
import static Views.Constants.ICON_IMAGE_PATH;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class MainFrame extends JFrame{
    
    public JPanel userMessagePanel =new JPanel();
    public InputPanel inputPanel = new InputPanel();
    
    ChartPanel chartPanel = new ChartPanel(null);
    public MainFrame()
    {
        
        try
        {
            BufferedImage img=ImageIO.read(new File(ICON_IMAGE_PATH));
            Image dimg=img.getScaledInstance(40, 40,Image.SCALE_SMOOTH);
            setIconImage(dimg);
        }catch (Exception e)
        {
            System.out.println("no econtre");
        }
        setSize(1200,800);
        setLocation(0,0);
        setTitle(APP_NAME);
        setVisible(true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc= new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridheight=1;
        gbc.gridwidth=0;
        gbc.weightx=1;
        gbc.fill=GridBagConstraints.BOTH;
        add(inputPanel,gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridheight=2;
        gbc.gridwidth=0;
        gbc.weightx=1;
        gbc.weighty=0.5;
        
        gbc.fill=GridBagConstraints.BOTH;
        
        add(chartPanel,gbc);
    }
    
    /** 
     * @return InputPanel
     */
    public InputPanel getInputPanel()
    {
        return inputPanel;
    }
    
    /** 
     * @param chart
     */
    public void setChart(JFreeChart chart)
    {
        this.chartPanel.setChart(chart);
        this.chartPanel.setDomainZoomable(true);
        this.chartPanel.setRangeZoomable(true);
    }
    public static void main(String[] args)
    {
        
        new MainFrame();
        
    }
}
