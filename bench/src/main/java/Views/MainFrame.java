package Views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import scala.annotation.meta.companionClass;

public class MainFrame extends JFrame{
    
    public JPanel userMessagePanel =new JPanel();
    public InputPanel inputPanel = new InputPanel();
    
    ChartPanel chartPanel = new ChartPanel(null);
    public MainFrame()
    {
        setSize(1200,800);
        setLocation(1920,0);
        setTitle("Banco de ensayo - FIUBA");
        setLayout(new BorderLayout(10,0));
        setVisible(true);
        add(inputPanel,BorderLayout.NORTH);
        //inputPanel.setPreferredSize(new Dimension(1000, 400));
        //chartPanel.setSize(800, 1200);
        add(chartPanel,BorderLayout.CENTER);
        userMessagePanel.add(inputPanel.userMessageLabel);
        add(userMessagePanel,BorderLayout.SOUTH);
        //pack();
    }
    public InputPanel getInputPanel()
    {
        return inputPanel;
    }
    public void setChart(JFreeChart chart)
    {
        this.chartPanel.setChart(chart);
    }
    public static void main(String[] args)
    {
        
        new MainFrame();
        
    }
}