package Views;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Views {
    JFrame frame = new JFrame("Banco de ensayo - FIUBA");
    JPanel panelContainer = new JPanel();
    JPanel panelFirst = new JPanel();
    JPanel panelSecond = new JPanel();
    JButton buttonOne = new JButton("Switch to second panel/workspace");
    JButton buttonSecond = new JButton("Switch to first panel/workspace");
    JButton buttonStart = new JButton("Arranque");
    JButton buttonStop = new JButton("Frenado");
    JButton buttonPause = new JButton("Pausa");
    JLabel speedTimeFunctionLabel=new JLabel("Función de velocidad en función del tiempo:");
    JTextField speedTimeFunctionInput= new JTextField(20);
    JLabel torqueTimeFunctionLabel=new JLabel("Función de cupla en función del tiempo:");
    JTextField torqueTimeFunctionInput= new JTextField(20);
    JLabel torqueSpeedFunctionLabel=new JLabel("Función de velocidad en función de la cupla:");
    JTextField torqueSpeedFunctionInput= new JTextField(20);
    JLabel startTimeLabel=new JLabel("Tiempo de inicio [s]:");
    JTextField startTimeInput= new JTextField(10);
    JLabel stopTimeLabel=new JLabel("Tiempo de fin [s]:");
    JTextField stopTimeInput= new JTextField(10);
    CardLayout cardLayout = new CardLayout();
    private static int width=1200;
    private static int height=800;
    public void launchMainWindow() {
        this.setup();
    }
    private void setup()
    {
        panelContainer.setLayout(cardLayout);
        panelFirst.add(buttonOne);
        panelFirst.add(buttonStart);
        panelFirst.add(buttonPause);
        panelFirst.add(buttonStop);
        panelFirst.add(speedTimeFunctionLabel);
        panelFirst.add(speedTimeFunctionInput);
        panelFirst.add(torqueTimeFunctionLabel);
        panelFirst.add(torqueTimeFunctionInput);
        panelFirst.add(torqueSpeedFunctionLabel);
        panelFirst.add(torqueSpeedFunctionInput);
        panelFirst.add(startTimeLabel);
        panelFirst.add(startTimeInput);
        panelFirst.add(stopTimeLabel);
        panelFirst.add(stopTimeInput);
        panelSecond.add(buttonSecond);
        panelFirst.setBackground(Color.GRAY);
        panelSecond.setBackground(Color.GREEN);

        panelContainer.add(panelFirst, "1");
        panelContainer.add(panelSecond, "2");
        cardLayout.show(panelContainer, "1");

        buttonOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                cardLayout.show(panelContainer, "2");
            }
        });

        buttonSecond.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                cardLayout.show(panelContainer, "1");
            }
        });

        frame.add(panelContainer);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        this.setLayout();
    }
    private void setLayout()
    {
        frame.setSize(Views.width,Views.height);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
     

            public void run() {
                new Views();
            }
        });
    }
}
