package Swing;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

public class MainFrame extends JFrame{
    
    public JPanel userMessagePanel =new JPanel();
    public InputPanel inputPanel = new InputPanel();
    
    ChartPanel chartPanel = new ChartPanel(null);
    public MainFrame()
    {
        
        try
        {
            BufferedImage img=ImageIO.read(new File("src/main/resources/icon.png"));
            Image dimg=img.getScaledInstance(40, 40,Image.SCALE_SMOOTH);
            setIconImage(dimg);
        }catch (Exception e)
        {
            System.out.println("no econtre");
        }
        setSize(1200,800);
        setLocation(0,0);
        setTitle("Banco de ensayo - FIUBA");
        setLayout(new BorderLayout(0,0));
        setVisible(true);
        add(inputPanel,BorderLayout.NORTH);
        
        add(chartPanel,BorderLayout.CENTER);
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
        /* XYItemRenderer renderer=chart.getXYPlot().getRenderer();
        renderer.setDefaultStroke(new BasicStroke(10.0f));
        ((AbstractRenderer) renderer).setAutoPopulateSeriesStroke(false); */
        this.chartPanel.setDomainZoomable(true);
        this.chartPanel.setRangeZoomable(true);
    }
    public static void main(String[] args)
    {
        
        new MainFrame();
        
    }
}
