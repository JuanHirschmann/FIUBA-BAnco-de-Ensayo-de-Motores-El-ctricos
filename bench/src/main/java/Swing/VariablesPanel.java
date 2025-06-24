package Swing;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class VariablesPanel extends JPanel {
    JPanel topPanel = new JPanel();
    JPanel middlePanel = new JPanel();
    JPanel bottomPanel = new JPanel();

    public VariablesPanel() {
        topPanel.setLayout(new WrapLayout());
        middlePanel.setLayout(new WrapLayout());
        bottomPanel.setLayout(new WrapLayout());/*
                                                 * topPanel.setBackground(Color.PINK);
                                                 * middlePanel.setBackground(Color.BLUE);
                                                 * bottomPanel.setBackground(Color.GRAY);
                                                 */

        // setLayout(new WrapLayout());
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 0;
        gbc.weightx = 1;
        // gbc.weighty=1;
        gbc.fill = GridBagConstraints.BOTH;
        add(topPanel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 0;
        // gbc.weighty=0.5;
        gbc.fill = GridBagConstraints.BOTH;

        add(middlePanel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = 0;
        // gbc.weighty=0.5;
        gbc.fill = GridBagConstraints.BOTH;
        add(bottomPanel, gbc);
        setBackground(Color.GREEN);
        // this.setPreferredSize(new Dimension(1100,100));
    }

    /**
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        for (Component component : this.topPanel.getComponents()) {
            component.setEnabled(enabled);
        }
        for (Component component : this.bottomPanel.getComponents()) {
            component.setEnabled(enabled);
        }
        for (Component component : this.middlePanel.getComponents()) {
            component.setEnabled(enabled);
        }
    }

    public Component add(Component comp, int position) {

        if (position == 0) {
            this.topPanel.add(comp);
        } else if (position == 1) {

            this.middlePanel.add(comp);
        } else {
            this.bottomPanel.add(comp);
        }
        return comp;
    }

}
