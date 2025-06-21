package Common;

import java.util.Hashtable;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static Model.Constants.VAR_PATH;

public class TorqueEquationParameters {

    Map<String, JLabel> parameterLabels = new Hashtable<>();
    Map<String, JTextField> parameterInput = new Hashtable<>();
    Map<String, TorqueEquationParameter> parameterValue = new Hashtable<>();

    public TorqueEquationParameters() {
        this.addParameter("A",VAR_PATH,"TORQUE_BIAS", "0", "[Nm]");
        this.addParameter("B",VAR_PATH,"LINEAR_TERM", "0", "[Nm/RPM]");
        this.addParameter("C",VAR_PATH,"CUADRATIC_TERM", "0", "[Nm/RPM^2]");
        this.addParameter("D",VAR_PATH,"INTERTIAL_TERM", "0", "[Nm/(RPM*s)]");
    }

    
    /** 
     * @return Map<String, String>
     */
    public Map<String, String> getParameterValues() {
        Map<String, String> output = new Hashtable<>();
        for (String key : this.parameterValue.keySet()) {
            output.put(key, parameterValue.get(key).parameterValue);
        }
        return output;
    }

    public void setParameters(JPanel panel) {
        for (String key : this.parameterLabels.keySet()) {
            panel.add(parameterLabels.get(key));
            panel.add(parameterInput.get(key));
        }
    }

    public void setVisible(boolean visible) {
        for (String key : this.parameterValue.keySet()) {
            parameterLabels.get(key).setVisible(visible);
            parameterInput.get(key).setVisible(visible);
        }
    }

    public void addParameter(String parameterKey, String parameterVarName, String parameterVarPath, String parameterValue, String parameterUnits){
        this.parameterValue.put(parameterKey, new TorqueEquationParameter(parameterKey,parameterVarName,parameterVarPath,parameterValue,parameterUnits));
        this.parameterLabels.put(parameterKey, new JLabel(this.parameterValue.get(parameterKey).getDisplayName()));
        this.parameterInput.put(parameterKey, new JTextField(this.parameterValue.get(parameterKey).getDisplayValue(), 5));
    }

    private class TorqueEquationParameter {
        private String parameterValue;
        private String parameterUnit;
        private String parameterKey;
        private String parameterVarName;
        private String parameterVarPath;

        TorqueEquationParameter(String parameterKey, String parameterVarName, String parameterVarPath,String parameterValue, String parameterUnit) {
            this.parameterVarName = parameterVarName;
            this.parameterVarPath = parameterVarPath;
            this.parameterKey = parameterKey;
            this.parameterValue = parameterValue;
            this.parameterUnit = parameterUnit;
        }

        /* public void update(String newValue) {
            this.parameterValue = newValue;
        } */

        public String getDisplayName() {
            return this.parameterKey + " " + this.parameterUnit + ": ";
        }

        public String getDisplayValue() {
            return this.parameterValue;
        }

        @SuppressWarnings("unused")
        public String getVarName() {
            return this.parameterVarName;
        }

        @SuppressWarnings("unused")
        public String getVarPath() {
            return this.parameterVarPath;
        }
    }
}

