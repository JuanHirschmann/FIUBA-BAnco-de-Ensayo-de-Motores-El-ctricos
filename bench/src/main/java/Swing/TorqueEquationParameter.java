package Swing;

public class TorqueEquationParameter {
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

    
    /** 
     * @param newvalue
     */
    public void update(String newvalue)
    {
        this.parameterValue=newvalue;
    }
    
    /** 
     * @return String
     */
    public String getValue()
    {
        return this.parameterValue;
    }
    public String getDisplayName() {
        return this.parameterKey + " " + this.parameterUnit + ": ";
    }

    public String getDisplayValue() {
        return this.parameterValue;
    }

    public String getVarName() {
        return this.parameterVarName;
    }

    public String getVarPath() {
        return this.parameterVarPath;
    }
}
