package Views;

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
        public void set(JPanel panel) {
            panel.add(inputLabel);
            panel.add(input);
        }
        public void setVisible(boolean visible) {
            inputLabel.setVisible(visible);
            input.setVisible(visible);
        }

        public String getValue() {
            return input.getText();
        }
    }