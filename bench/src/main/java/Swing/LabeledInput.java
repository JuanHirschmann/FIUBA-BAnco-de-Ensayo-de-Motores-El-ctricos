package Swing;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LabeledInput extends JTextField {
        JLabel inputLabel = new JLabel();
        JTextField input = new JTextField(20);

        LabeledInput(String label) {
            inputLabel.setText(label);
        }
        LabeledInput(String label, int columns) {
            inputLabel.setText(label);
            input.setColumns(columns);
        }
        
        /** 
         * @param panel
         */
        public void set(JPanel panel) {
            panel.add(inputLabel);
            panel.add(input);
        }
        
        /** 
         * @param visible
         */
        public void setVisible(boolean visible) {
            inputLabel.setVisible(visible);
            input.setVisible(visible);
        }
        @Override
        public void setText(String text) {
            input.setText(text);
        }
        public String getValue() {
            return input.getText();
        }
    }