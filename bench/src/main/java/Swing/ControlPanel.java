package Swing;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;

public class ControlPanel extends JPanel {

    public ControlPanel() {
        setLayout(new FlowLayout());

        this.setPreferredSize(new Dimension(1100, 45));

    }
}
