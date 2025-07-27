package Swing;

import static Views.Constants.APP_NAME;
import static Views.Constants.ICON_IMAGE_PATH;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class SelfSustainedTestFrame extends JFrame{
    
    
    ChartPanel chartPanel = new ChartPanel(null);
    public SelfSustainedTestFrame()
    {
        this.setVisible(false);
        
        try
        {
            BufferedImage img=ImageIO.read(getClass().getClassLoader().getResourceAsStream(ICON_IMAGE_PATH));
            Image dimg=img.getScaledInstance(40, 40,Image.SCALE_SMOOTH);
            setIconImage(dimg);
        }catch (Exception e)
        {
            System.out.println("no econtre");
        }
        setSize(1200,800);
        setLocation(0,0);
        setTitle(APP_NAME);
        setLayout(new BorderLayout(0,0));
        setVisible(false);
        
        add(chartPanel,BorderLayout.CENTER);
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
        
        new SelfSustainedTestFrame();
        
    }
}
