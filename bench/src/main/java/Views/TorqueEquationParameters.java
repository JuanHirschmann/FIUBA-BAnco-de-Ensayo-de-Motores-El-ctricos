package Views;

import java.util.Hashtable;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TorqueEquationParameters {
    
    Map<String, JLabel> parameterLabels = new Hashtable<>();
    Map<String, JTextField> parameterInput = new Hashtable<>();
    Map<String, TorqueEquationParameter> parameter = new Hashtable<>();
    TorqueEquationParameters()
    {
        this.addParameter("A", "0", "[Nm]");
        this.addParameter("B", "0", "[Nm/RPM]");
        this.addParameter("C", "0", "[Nm/RPM^2]");
        this.addParameter("D", "0", "[Nm/(RPM*s)]");
    }
    public void setParameters(JPanel panel) {
        for (String key : this.parameter.keySet()) {
            panel.add(parameterLabels.get(key));
            panel.add(parameterInput.get(key));
        }
    }
    public void setVisible(boolean visible) {
        for (String key : this.parameter.keySet()) {
            parameterLabels.get(key).setVisible(visible);;
            parameterInput.get(key).setVisible(visible);;
        }
    }
    public void addParameter(String parameterName,String parameterValue,String parameterUnits)
    {
        parameter.put(parameterName, new TorqueEquationParameter(parameterName, parameterValue, parameterUnits));
        parameterLabels.put(parameterName, new JLabel(parameter.get(parameterName).getDisplayName()));
        parameterInput.put(parameterName, new JTextField(parameter.get(parameterName).getDisplayValue(),5));
    }
    private class TorqueEquationParameter
    {
        private String parameterValue;
        private String parameterUnit;
        private String parameterName;
        TorqueEquationParameter(String parameterName,String parameterValue,String parameterUnit)
        {
            this.parameterName=parameterName;
            this.parameterValue=parameterValue;
            this.parameterUnit=parameterUnit;
        }
        public void update(String newValue) {
            this.parameterValue = newValue;
        }
        public String getDisplayName()
        {
            return this.parameterName+" "+this.parameterUnit+": ";
        }
        public String getDisplayValue()
        {
            return this.parameterValue;
        }
    }
}
