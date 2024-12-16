package Swing;

import java.util.Hashtable;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Model.Constants.commands;

public class onScreenMeasurements {
    Map<String, JLabel> measurementLabels = new Hashtable<>();
    Map<String, Measurement> measurement = new Hashtable<>();

    private class Measurement {
        String displayName;
        String displayUnit;
        float value;

        Measurement(String displayName, String displayUnit, float value) {
            this.displayName = displayName;
            this.displayUnit = displayUnit;
            this.value = value;
        };

        public void update(float newValue) {
            this.value = newValue;
        }
        public String getDisplayName()
        {
            return displayName+": "+String.format("%.2f", value)+" "+displayUnit;
        }
    }

    
    /** 
     * @param varName
     * @param displayName
     * @param displayUnit
     */
    public void addMeasuredVariable(String varName, String displayName, String displayUnit) {
        this.measurementLabels.put(varName, new JLabel());
        this.measurement.put(varName, new Measurement(displayName, displayUnit, 0));
    }
    
    /** 
     * @param variable
     */
    public void addMeasuredVariable(commands variable) {
        this.measurementLabels.put(variable.seriesName, new JLabel());
        this.measurement.put(variable.seriesName, new Measurement(variable.displayName, variable.displayUnit, 0));
    }
    public void addMeasurement(String key, float value) {
        this.measurement.get(key).update(value);
        this.measurementLabels.get(key).setText(this.measurement.get(key).getDisplayName());
    }

    public void setMeasurements(JPanel panel) {
        for (String key : this.measurementLabels.keySet()) {
            panel.add(measurementLabels.get(key));
        }
    }

}
