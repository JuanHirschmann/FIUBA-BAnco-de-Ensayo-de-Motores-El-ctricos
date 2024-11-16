package Views;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static Model.Constants.INERTIA_COEFF;
import static Model.Constants.LINEAR_COEFF;
import static Model.Constants.QUADRATIC_COEFF;
import static Model.Constants.TORQUE_BIAS;
import static Model.Constants.VAR_PATH;
public class TorqueEquationParameters {

    Map<String, JLabel> parameterLabels = new Hashtable<>();
    Map<String, JTextField> parameterInput = new Hashtable<>();
    Map<String, TorqueEquationParameter> parameterValue = new Hashtable<>();

    public TorqueEquationParameters() {
        this.addParameter("A",VAR_PATH,TORQUE_BIAS, "0", "[Nm]");
        this.addParameter("B",VAR_PATH,LINEAR_COEFF, "0", "[Nm/RPM]");
        this.addParameter("C",VAR_PATH,QUADRATIC_COEFF, "0", "[Nm/RPM^2]");
        this.addParameter("D",VAR_PATH,INERTIA_COEFF, "0", "[Nm/(RPM*s)]");
    }

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

    
}
