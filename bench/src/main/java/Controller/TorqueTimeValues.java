package Controller;

import static Views.Constants.CSV_DELIMITER;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class TorqueTimeValues {
    private int length=0;
    private int onePeriodLength=0;
    private Map<String, ArrayList<String>> data = new Hashtable<>();
    public TorqueTimeValues(TorqueTimeValues copyTarget)
    {
        this.length=copyTarget.length;
        System.out.println(this.length);
        this.onePeriodLength=copyTarget.onePeriodLength;
        for (String key: copyTarget.data.keySet())
        {  
            data.put(key, new ArrayList<String>(copyTarget.data.get(key)));
            

        }
    }
    public TorqueTimeValues()
    {
        data.put("value", new ArrayList<String>());
        data.put("timestamp", new ArrayList<String>());
    }
    
    /** 
     * Returns the maximum torque value found on the torque time waveform
     * @return float
     */
    public float getMax()
    {
        float max=0;
        for(int i =0;i<data.get("value").size();i++)
        {
            if(Float.valueOf(data.get("value").get(i))>max)
            {
                max=Float.valueOf(data.get("value").get(i));
            }
        }
        return max;
    }
    /** 
     * Returns the minimum time delta in ms found on torque-time waveform
     * @return float
     */
    public float getMinTimeDelta()
    {
        float min=0;
        float delta=0;
        for(int i =0;i<this.onePeriodLength-1;i++)
        {
            delta=Float.valueOf(data.get("timestamp").get(i))-Float.valueOf(data.get("timestamp").get(i));
            if(Float.valueOf(data.get("timestamp").get(i))<min)
            {
                min=delta;
            }
        }
        return delta;
    }
    public void torqueCommandCheck() throws IllegalArgumentException
    {
        if (this.length() == 0) {
            throw new IllegalArgumentException("Seleccione un archivo en formato CSV (tiempo[ms],torque[Nm]).");
        } else if (this.getMax() > 26) {
            throw new IllegalArgumentException("El archivo elegido supera el torque máximo del sistema (26Nm).");
        }else if (this.getMinTimeDelta() < 100) {
            throw new IllegalArgumentException("El archivo elegido tiene uno o más comandos de tiempo con separación menor a 100ms");
        }
    }
    public void speedCommandCheck() throws IllegalArgumentException
    {
        if (this.length() == 0) {
            throw new IllegalArgumentException("Seleccione un archivo en formato CSV (tiempo[ms],torque[Nm]).");
        } else if (this.getMax() > 3000) {
            throw new IllegalArgumentException("Velocidad máxima del eje simulador excedida (3000RPM)");
        }else if (this.getMinTimeDelta() < 100) {
            throw new IllegalArgumentException("El archivo elegido tiene uno o más comandos de tiempo con separación menor a 100ms (speed)");
        }
    }
    /** 
     * returns torque commands value's length
     * @return int
     */
    public int length()
    {
        return length;
    }
    /* 
     * Replicates the torque time waveform for a integer number of periods
     */
    public void extend(int periods)
    {
        String aux;
        Float time_delta;
        int initialLength=onePeriodLength;
        this.dropExtentions();
        for(int i=initialLength;i<(periods*initialLength)-1;i++)
        {   
            aux=data.get("value").get(i-initialLength);
            
            data.get("value").add(aux);
            aux=data.get("timestamp").get(i-initialLength);
            if (Float.valueOf(aux)==0)
            {
                aux="30";
            }
            time_delta=Float.valueOf(aux)+Float.valueOf(data.get("timestamp").get(initialLength-1));
            data.get("timestamp").add(String.valueOf(time_delta));
            length++;
        }
        System.err.println("extendi");
        System.err.println(length);
    }
    /* 
     * Drops the torque waveform to its original size. Only changes the waveform 
     * if it was previously extended
     */
    public void dropExtentions()
    {
        int initialLength=onePeriodLength;
        int counter=0;
        for(int i=initialLength;i<this.length;i++)
        {   
            data.get("value").remove(initialLength);
            data.get("timestamp").remove(initialLength);
            counter++;
        }
        this.length-=counter;
        System.err.println("reduje");
        System.err.println(length);
    }
    /* 
     * Clears the torque time waveform
     */
    public void clear()
    {
        this.data.clear();
        this.length=0;
        this.onePeriodLength=0;
        
        data.put("value", new ArrayList<String>());
        data.put("timestamp", new ArrayList<String>());
    }
    /* 
     * Creates a torque time waveform from a CSV file (time , torque)
     */
    public void fromCSV(String filepath)
    {
        this.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(CSV_DELIMITER);
                data.get("timestamp").add(values[0]);
                data.get("value").add(values[1]);
                length++;
                onePeriodLength++;
            }
        } catch (IOException e) {
            System.out.println(e);
            throw new IllegalStateException("Cannot write dataset", e);
        }
    }
    public void zeropad(int padLength)
    {
        System.out.print("Pad: ");
        System.out.println(padLength);
        for(int i=0;i<padLength;i++)
        {
            
            data.get("timestamp").add("0");
            data.get("value").add("0");
            length++;
        }

    }
    /* 
     * gets the timestamps for the waveform
     */
    public ArrayList<String> getTimestamp()
    {
        return this.data.get("timestamp");
    }
    
    
    /* 
     * gets the values for the waveform
     */
    public ArrayList<String> getValue()
    {
        return this.data.get("value");
    }

    /*
     * Gets a certain index from timestamp    
     * @param index
     */
    public String getTimestamp( int index)
    {
        String timestamp;
        try
        { 
            timestamp=this.data.get("timestamp").get(index);
        }
        catch(IndexOutOfBoundsException e){
            timestamp=null;
        }
        return timestamp;
    }
    
    
    /*
     * Gets a certain index from torque    
     * @param index
     */
    public String getValue(int index)
    {
        String value;
        try
        {

            value=this.data.get("value").get(index);
        }
        catch(IndexOutOfBoundsException e){
            value=null;
        }
        return value;
    }
}

