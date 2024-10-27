package Views;

import java.util.Hashtable;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class onScreenMeasurements {
    Map<String, JLabel> measurements = new Hashtable<>();

    public void addMeasuredVariable(String varName) {
        measurements.put(varName, new JLabel());
    }

    public void addMeasurement(String key, float value) {
        if (measurements.get(key) == null) {
            this.addMeasuredVariable(key);
        }
        this.measurements.get(key).setText(String.valueOf(value));
    }

    public void setMeasurements(JPanel panel) {
        for (String key : this.measurements.keySet()) {
            panel.add(measurements.get(key));
        }
    }

}
