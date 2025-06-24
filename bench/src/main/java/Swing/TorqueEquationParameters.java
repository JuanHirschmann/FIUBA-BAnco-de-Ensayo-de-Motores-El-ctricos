package Swing;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static Model.Constants.INERTIA_COEFF;
import static Model.Constants.LINEAR_COEFF;
import static Model.Constants.QUADRATIC_COEFF;
import static Model.Constants.TORQUE_BIAS;
import static Model.Constants.VAR_PATH;
public class TorqueEquationParameters {

    Map<String, JLabel> parameterLabels = new TreeMap<>();
    Map<String, JTextField> parameterInput = new TreeMap<>();
    Map<String, TorqueEquationParameter> parameterValue = new TreeMap<>();

    public TorqueEquationParameters() {
        this.addParameter("A",TORQUE_BIAS, VAR_PATH,"0", "[Nm]",5);
        this.addParameter("B",LINEAR_COEFF,VAR_PATH ,"0", "[Nm/RPM]",5);
        this.addParameter("C",QUADRATIC_COEFF,VAR_PATH, "0", "[Nm/RPM^2]",10);
        this.addParameter("D",INERTIA_COEFF,VAR_PATH, "0", "[Nm/(RPM*s)]",5);
    }

    
    /** 
     * @return Map<String, TorqueEquationParameter>
     */
    public Map<String, TorqueEquationParameter> getParameterValues() {
        Map<String, TorqueEquationParameter> output = new Hashtable<>();
        this.update();
        for (String key : this.parameterValue.keySet()) {
            output.put(key, parameterValue.get(key));
        }
        return output;
    }
    public void update() {
        String value;
        for (String key : this.parameterValue.keySet()) {
            value=this.parameterInput.get(key).getText();
            this.parameterValue.get(key).update(value);
        }
    }
    
    /** 
     * @param panel
     */
    public void setParameters(JPanel panel) {
        for (String key : this.parameterValue.keySet()) {
            panel.add(parameterLabels.get(key),1);
            panel.add(parameterInput.get(key),1);
        }
    }

    public void setVisible(boolean visible) {
        for (String key : this.parameterValue.keySet()) {
            parameterLabels.get(key).setVisible(visible);
            parameterInput.get(key).setVisible(visible);
        }
    }

    public void addParameter(String parameterKey, String parameterVarName, String parameterVarPath, String parameterValue, String parameterUnits,int columns){
        this.parameterValue.put(parameterKey, new TorqueEquationParameter(parameterKey,parameterVarName,parameterVarPath,parameterValue,parameterUnits));
        this.parameterLabels.put(parameterKey, new JLabel(this.parameterValue.get(parameterKey).getDisplayName()));
        this.parameterInput.put(parameterKey, new JTextField(this.parameterValue.get(parameterKey).getDisplayValue(), columns));
    }

    
}
