package Swing;

import java.awt.BorderLayout;

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
        setSize(1200,800);
        setLocation(0,0);
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
    }
    public static void main(String[] args)
    {
        
        new MainFrame();
        
    }
}
